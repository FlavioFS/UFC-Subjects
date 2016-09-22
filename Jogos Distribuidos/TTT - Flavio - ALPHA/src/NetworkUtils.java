

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @brief Provê métodos utilitários para tarefas relacionadas à rede 
 * 
 */
public class NetworkUtils {

	/** @brief Método utilitário para obter o endereço da máquina local.
	 *  
	 *  
	 *  
	 *  @return Endereço de rede deste ponto
	 */
	public static InetAddress getNetworkAddress()
		throws RuntimeException
	{
		try
		{	
			// lista todas as interfaces de rede
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    
		    while (interfaces.hasMoreElements())
		    {
		        NetworkInterface iface = interfaces.nextElement();
		        // desconsidera endereço de loopback 127.0.0.1, assim como interfaces inativas
		        if (iface.isLoopback() || !iface.isUp() )
		            continue;

		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        

		        while(addresses.hasMoreElements())
		        {
		            InetAddress addr = addresses.nextElement();
		            // limita a busca aos endereços IPv4
		            if( addr instanceof java.net.Inet4Address )
		            {
		            	System.out.println("Using local address:" + addr);
		            	return addr;
		            }
		            
		            System.out.println("Skipping address: " + addr );
		        }
		        
		        return InetAddress.getLocalHost();		        
		    }
		} catch (Exception e) {
			
			throw new RuntimeException("Could not obtain network address");
		}
		
		return null;
	}
	
	/**
	 * Lista as interfaces de rede em busca do endereço de broadcast:
	 *  - as interfaces de rede são inspecionadas com base no endereço usado por essa máquina;
	 *  - cada interface pode possuir vários endereços. Dessa forma, o método retorna o primeiro endereço de broadcast válido.
	 *
	 * http://docs.oracle.com/javase/7/docs/api/java/net/InterfaceAddress.html#getBroadcast()
	 *
	 */
	public static InetAddress getBroadcastAddress()
			throws RuntimeException
	{
		try
		{
			//if( true ) return InetAddress.getByName("255.255.255.255");
					
			// obtém o endereço local
			InetAddress myAdd = getNetworkAddress();
			// busca a interface de rede à qual o endereço está vinculado
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress( myAdd );			
			
			// lista os endereços da interface
			for( InterfaceAddress address : networkInterface.getInterfaceAddresses() )
			{				
				// retorna o primeiro 
				InetAddress add = address.getBroadcast();
				
				if( add != null )
				{
					System.out.println("Broadcast address: " + add);
					return add;
				}
			}
			
			throw new RuntimeException("Could not find the broadcast address");
		}catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Verifica se o broadcast comum está ligado na rede local.
	 *   
	 * @return True, se e somente se o broadcast está habilitado na rede local.
	 */	
	public static boolean isBroadcastEnabledxx()
	{
		try
		{
			DatagramSocket ds = new DatagramSocket( ProtocolConfig.PORT );			
						
			for( int tries=3 ; tries>0; --tries )
			{
				try
				{
					DatagramPacket pack = PacketFactory.newPing( null );
					//pack.setPort( Protocol.PORT );
					//pack.setAddress( Protocol.CLIENT_ADDRESS );
					
					ds.send(pack );
					
					// waits for incoming packet
					Thread.sleep(50);
					DatagramPacket incoming = PacketFactory.newPacket();
					ds.receive(incoming);
							
					if( incoming != null )
					{
						//System.out.println("got: " + incoming.getAddress() + ":" + incoming.getPort() + " @ " +  Protocol.getPacketType(incoming)  );
						
						if(    incoming.getLength() == ProtocolConfig.PACKET_SIZE 
							&& incoming.getAddress().equals(Protocol.CLIENT_ADDRESS) 
							&& PacketUtils.getMessageType(incoming) == Protocol.PT_PING )
						{
							ds.close();
							return true;
						}
					}
					
				}catch(Exception shut)
				{
				}
			}
			
			ds.close();
			
		}catch(	Exception sockex )
		{
			//sockex.printStackTrace();;
			return false;
		}
		
		return false;
	}
	
}
