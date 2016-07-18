

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @brief Principal classe de rede, encapsula o protocolo de comunicação entre os pontos da rede.
 * 
 * A transmissão é composta por três threads:
 *  - uma thread para envio das pacotes que são previamente inseridas na fila de saída usando o método send();
 *  - uma thread para recepção de pacotes, que uma vez recebidos são inseridos numa fila de mensagens de entrada;
 *  - uma thread para tratamento das mensagens recebidas pela thread anterior.
 *    Esta ocasiona respostas que podem ser enviadas imediatamente, no caso de mensagens de alta prioridade,
 *    ou através da fila de saída.
 * 
 */
public class Protocol 
{
	/// Encapsula o endereço do próprio cliente
	public static InetAddress CLIENT_ADDRESS    = NetworkUtils.getNetworkAddress();
	/// Encapsula o endereço de broadcast da rede do cliente
	public static InetAddress BROADCAST_ADDRESS = NetworkUtils.getBroadcastAddress();
	
	// -----------------------------------------------------------------------------
	// TODO substituir o byte identificador da mensagem por uma enum 
	// -----------------------------------------------------------------------------
	
	public static final byte PT_PING    	 = 50;
	public static final byte PT_PONG    	 = 51;
	public static final byte PT_HEARTBEAT    = 52;
	public static final byte PT_GAME_VERSION = 53;
	
	public static final byte PT_KEY_DOWN     = 54;
	public static final byte PT_KEY_UP       = 56;
	public static final byte PT_POSITION     = 57;
	
	public static final byte PT_SHOOT    = 59;
	
	
	
	
	public static final int MAX_QUEUE_SIZE = 1024;
	
	
	private long startingTimestamp = System.currentTimeMillis();
	
	/**
	 * Guarda o momento do envio do último pacote
	 */
	private long lastSentPacketTimestamp = 0;
	
	/**
	 * Flag indicando se o protocolo está sendo executado. Definido como true enquanto estiver funcionando
	 */
	private boolean serviceRunning = false;
	/**
	 * Thread responsável pelo envio de mensagens.
	 */
	private Thread sendPacketsThread;
	/**
	 * Thread responsável pela recepção de mensagens.
	 */
	private Thread receivePacketsThread;
	/**
	 * Thread responsável pelo tratamento de mensagens.
	 */
	private Thread processIncomingPacketsThread;
	/**
	 * Socket UDP usado para a comunidação.
	 */
	private DatagramSocket datagramSocket;
	
	/**
	 * Objeto responsável por tratar os pacotes que chegam.
	 */
	private PacketHandler packetHandler = new ClientPacketHandler();  
	
	/**
	 * Fila contendo as mensagens que chegam a este ponto de rede.
	 */
	private ArrayBlockingQueue<DatagramPacket> incomingMessagesQueue = new ArrayBlockingQueue<DatagramPacket>(MAX_QUEUE_SIZE);
	
	/**
	 * Fila de pacotes a serem enviados a partir deste ponto da rede
	 */
	private ArrayBlockingQueue<DatagramPacket> outQueue = new ArrayBlockingQueue<DatagramPacket>(MAX_QUEUE_SIZE);	
	
	
	public Protocol()
	{
		super();
		
		startingTimestamp = System.currentTimeMillis();
	}
	
	public long getTimestamp()
	{
		return System.currentTimeMillis() - startingTimestamp;
	}
	

	/**
	 * Escalona um pacote para envio. Note-se que pacotes de alta prioridade devem invocar o método sendPacket()
	 * 
	 * @param pack
	 */
	public void send( DatagramPacket pack )
	{
		try
		{
			outQueue.put( pack );
		}catch(InterruptedException iex)
		{
			iex.printStackTrace();
		}
	}
	
	/**
	 * Provê acesso à fila de mensagens recebidas da rede.
	 * 
	 * @return
	 */
	public ArrayBlockingQueue<DatagramPacket> getIncomingMessagesQueue()
	{
		return incomingMessagesQueue;
	}
	
	/**
	 * Provê acesso ao socket usado internamente.
	 * @return
	 */
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	/**
	 * Inicia os serviços relacionados ao protocolo. Todos executam em background.
	 * 
	 * @param ds
	 */
	public void startService( DatagramSocket ds )
	{
		synchronized(this)
		{
			// evita iniciar o serviço duas vezes seguidas
			if(     serviceRunning
				|| (sendPacketsThread != null)
				|| (receivePacketsThread != null)
				|| (processIncomingPacketsThread != null)
			  ) 
			{
				System.err.println("Cannot start the protocol twice!");
				return;
			}
			
			// configura os atributos
			serviceRunning = true;
			datagramSocket = ds;
			
			
			
			
			// inicia cada thread
			createSendPacketsThread();
			createReceivePacketsThread();
			createProcessIncomingPacketsThread();
			
			// 
			System.out.println("Protocol started!");
		}
	}
	
	protected void createSendPacketsThread()
	{
		Protocol me = this;
		
		// thread: simply consumes the queue. When inactive, sends hearbeats		
		sendPacketsThread = new Thread()
		{
			public void run()
			{
				// last ping sent
				long lastPing = getTimestamp();
				
				try
				{
					// TODO balancear envio e checagem de ping
					while (serviceRunning)
			        {
						// verifica se é preciso enviar um PING
						long now = getTimestamp();
						long delta = now - lastPing;
						if( delta >= ProtocolConfig.PING_INTERVAL )
						{
							/*
							System.out.println("ping " + now + " " + lastPing);
							*/		
							
							// hints that interval
							lastPing = now;
												
							DatagramPacket ping = PacketFactory.newPing(me);
							//sendPacket(ping);					
							send(ping);
						}
						
						// aguarda até haver o que enviar
						for( int i=0; i<10; ++i )
						{
							DatagramPacket pack = outQueue.poll();
							if( pack != null )
							{
								sendPacket(pack);			    		   
							}else
							{
								checkHeartBeat();
							}
						}
			        }
				} catch (Exception ex)
				{					
				}
			}
		};
		
		sendPacketsThread.start();
	}
	
	protected void createReceivePacketsThread()
	{
		// thread de recepção: capta a mensagem; valida protcolo e versão; processa pacotes de prioridade e enfileira pacotes comuns
		Protocol me = this;
		receivePacketsThread = new Thread()
		{
			public void run()
			{
				try
				{
					while (serviceRunning)
					{
						// cria um pacote "cru" para a recepção
						byte data[] = new byte[ ProtocolConfig.PACKET_SIZE ];
						DatagramPacket pack = new DatagramPacket(data, data.length);
						datagramSocket.receive(pack);
						
						if( ProtocolConfig.VERBOSE_ON )
						{
							PacketUtils.debug(pack,"INCOMING PACKET");
						}
						
						// discards invalid packets
						if( isValidPacket(pack) )
						{							
							if(  packetHandler.isPriorityPacket(pack) )
							{
								// high-priority packet: immediate processing/response
								packetHandler.handlePriorityPacket(pack,me);
							}
							else
							{
								// common packet: goes to the queue
								incomingMessagesQueue.put( pack );
							}
						}
				   }
				} catch (InterruptedException ex)
				{					
				} catch (IOException e) {
					// TODO lidar com a exceção de IO
					e.printStackTrace();
				}
			}
		};
		
		receivePacketsThread.start();
	}
	
	protected void createProcessIncomingPacketsThread()
	{
		// processamento de pacotes: basta consumir os itens da fila
		Protocol me = this;
		processIncomingPacketsThread = new Thread()
		{
			public void run()
			{
				while(serviceRunning)
				{
					try
					{
						DatagramPacket pack = incomingMessagesQueue.poll();
						if( pack != null )
						{
							if( ProtocolConfig.VERBOSE_ON )
							{
								PacketUtils.debug(pack,"PROCESSING PACKET");
							}
							
							packetHandler.handlePacket(pack,me);
						}
					}catch( Exception shutDown )
					{						
					}
				}
			}
		};
		
		processIncomingPacketsThread.start();
	}
	
	public void stopService()
	{
		synchronized(this)
		{
			serviceRunning = false;
			try
			{
				if( sendPacketsThread != null )
					sendPacketsThread.join();
				
				if( receivePacketsThread != null )
					receivePacketsThread.join();
				
				if( processIncomingPacketsThread != null )
					processIncomingPacketsThread.join();
				
				// zera as threads
				receivePacketsThread = null;
				sendPacketsThread = null;
				processIncomingPacketsThread = null;
				
				// limpa as filas de saída e de entrada
				incomingMessagesQueue.clear();
				outQueue.clear();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Verifica se o pacote é válido para recepção.
	 * Pacotes inválido são descartados prematuramente, portanto sequer entram na fila de processamento.
	 * 
	 * @param pack
	 * @return
	 */
	public static boolean isValidPacket( DatagramPacket pack)
	{
		byte[] data = pack.getData();
		byte proto = data[0];
		
		// checks protocol ID 
		if( ( proto != ProtocolConfig.PROTOCOL_BYTE_ID) )
		{
			if( ProtocolConfig.VERBOSE_ON )
				System.err.println("Invalid protocol: " + proto + " instead of " + ProtocolConfig.PROTOCOL_BYTE_ID);
			return false;
		}
		
		// checks protocol VERSION
		byte version = data[1];
		if(    version < ProtocolConfig.PROTOCOL_BYTE_MIN_VERSION
			|| version > ProtocolConfig.PROTOCOL_BYTE_MAX_VERSION
		  )
		{
			if( ProtocolConfig.VERBOSE_ON )
				System.err.println("Unsupported protocol version: " + version + " out of [" + ProtocolConfig.PROTOCOL_BYTE_MIN_VERSION + " to " + ProtocolConfig.PROTOCOL_BYTE_MAX_VERSION +"] range");
			return false;
		}
		
		// handles LOOPBACK messages
		if( ProtocolConfig.IGNORE_SELF_MESSAGES )
		{
			if( pack.getAddress().equals(Protocol.CLIENT_ADDRESS) && pack.getPort() == ProtocolConfig.PORT )
			{
				if( ProtocolConfig.VERBOSE_ON )
				{
					// 	NOTA: essa é a única mensagem que não precisaria ser logada
					System.err.println("Protocol does not accept loopback messages");
				}
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Verifica se é necessário enviar um pacote do tipo heartbeat. 
	 * Isso ocorre quando o protocolo está inativo durante ProtocolConfig.HEARTBEAT_INTERVAL milissegundos. 
	 * 
	 * @throws IOException
	 */
	protected void checkHeartBeat() throws IOException
	{
		long deltaT = System.currentTimeMillis() - lastSentPacketTimestamp;
	    if( deltaT >= ProtocolConfig.HEARTBEAT_INTERVAL )
	    {
	    	DatagramPacket posPack = PacketFactory.newHeartbeat();
	    	sendPacket( posPack );
	    }
	}
	
	/**
	 * Envia efetivamente o pacote e realiza contabilização de estatísticas, se necessário.
	 * 
	 * @param pack Pacote a ser enviado
	 * @throws IOException
	 */
	protected void sendPacket( DatagramPacket pack ) throws IOException
	{
		datagramSocket.send( pack );
		lastSentPacketTimestamp = System.currentTimeMillis(); 
		
		if( ProtocolConfig.VERBOSE_ON )
		{
			PacketUtils.debug(pack,"TRANSMITTING PACKET");
		}
		
	}	
	
}
