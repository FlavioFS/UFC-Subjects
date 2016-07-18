import java.net.DatagramPacket;

/**
 * @brief Contém métodos utilitários para lidar com os pacotes (datagramas)
 *  
 * Os pacotes do protocolo possuem a seguinte estrutura:
 * 	- 1 byte identificando o protocolo. Isso ajuda a evitar que mensagens aleatórias concorrendo com a mesma porta do protocolo sejam recebidas;
 *  - 1 byte identificando a versão do protocolo. O jogo pode suportar uma faixa de versões de acordo com os tipos de mensagens contidos em cada versão;
 *  - 1 byte para identificar o tipo da mensagem, como, por exemplo, ping, movimento, eventos do jogo, etc; 
 *  - 32 bytes (por padrão) para o identificador do jogador. Esse tamanho pode variar. Vide ProtocolConfig.MAX_PLAYER_ID_LENGTH para maiores detalhes.
 *    Note-se que em outra situação poderia ser mais interessante usar 4 bytes para um ID único dos jogadores, porém isso iria requerer um mecanismo
 *    de controle centralizado em um servidor ou mais sofisticado no caso de arquiteturas P2P;    
 *  - os demais bytes contém a mensagem em formato textual. Ressalte-se que esse design das mensagens reflete o uso didático/acadêmico, pois esse não
 *    é o formato mais eficiente ou conveniente para representação do conteúdo das mensagens. 
 */
public class PacketUtils
{
	/**
	 * Insere o player ID no pacote
	 * 
	 * @param data
	 * @param playerId
	 */
	public static final void injectPlayerId( byte[] data, String playerId )
	{
		// trunca o tamanho para no máximo 32 caracteres
		if( playerId.length() > ProtocolConfig.MAX_PLAYER_ID_LENGTH )
		{
			playerId = playerId.substring(0, ProtocolConfig.MAX_PLAYER_ID_LENGTH );
		}else
		{
			do
			{
				playerId += " ";
			}while( playerId.length() < ProtocolConfig.MAX_PLAYER_ID_LENGTH );
		}
		
		byte[] byteStr = playerId.getBytes();
		int strSize = Math.min( byteStr.length, ProtocolConfig.MAX_PLAYER_ID_LENGTH );
		int offset = 3;	
		
		//System.arraycopy(byteStr, 0, data, offset, ProtocolConfig.MAX_PLAYER_ID_LENGTH);
		System.arraycopy(byteStr, 0, data, offset, strSize );
	}
	
	/**
	 * Insere a string contendo dados no pacote. 
	 * 
	 * @param data
	 * @param dataStr
	 */
	public static final void injectMessageData( byte[] data, String dataStr )
	{		
		injectMessageData( data, dataStr.getBytes());
	}
	
	/**
	 * Insere bytes de dados dados no pacote. 
	 * 
	 * @param data
	 * @param dataStr
	 */
	public static final void injectMessageData( byte[] data, byte[] byteData )
	{		
		int offset = 3 + ProtocolConfig.MAX_PLAYER_ID_LENGTH;
		int theEnd = Math.min( byteData.length, ProtocolConfig.PACKET_SIZE-offset);
		
		System.arraycopy(byteData, 0, data, offset, theEnd);
	}
	
	public static final byte getProtocolId( DatagramPacket pack )
	{
		return pack.getData()[0];
	}
	public static final void setProtocolId( DatagramPacket pack, byte pid )
	{
		pack.getData()[0] = pid;
	}	
	
	public static final byte getProtocolVersion( DatagramPacket pack )
	{
		return pack.getData()[1];
	}
	
	public static final void setProtocolVersion( DatagramPacket pack, byte pv )
	{
		pack.getData()[1] = pv;
	}
	
	public static final byte getMessageType( DatagramPacket pack )
	{
		return pack.getData()[2];
	}
	
	public static final String getMessageTypeString( DatagramPacket pack )
	{
		switch( pack.getData()[2] )
		{
		case Protocol.PT_HEARTBEAT:
			return "HEARTBEAT";
		case Protocol.PT_PING:
			return "PING";
		case Protocol.PT_PONG:
			return "PONG";
		case Protocol.PT_KEY_DOWN:
			return "DOWN";
		case Protocol.PT_KEY_UP:
			return "UP";
		case Protocol.PT_POSITION:
			return "POSITION";
		default:
			return "UNKNOWN";
		}
	}
	
	public static final void setMessageType( DatagramPacket pack, byte tipo )
	{
		pack.getData()[2] = tipo;
	}
	
	public static final String getPlayerId( DatagramPacket pack )
	{
		String ret = new String( pack.getData(), 3, ProtocolConfig.MAX_PLAYER_ID_LENGTH );
		
		// trunca a String, pois a mesma termina em vários espaços em branco
		ret = ret.trim();
		return ret;
	}
	
	public static final String getPacketMessage( DatagramPacket pack )
	{		
		int offset = 3 + ProtocolConfig.MAX_PLAYER_ID_LENGTH;
		String ret = new String( pack.getData(), offset, pack.getData().length-offset );
		
		// trunca a String, pois a mesma termina em vários espaços em branco
		ret = ret.trim();
		return ret;
	}

	/**
	 * Imprime o conteúdo do pacote. 
	 * 
	 * @param pack
	 */
	public static void debug(DatagramPacket pack)
	{
		System.out.println( pack);
		System.out.println("\tprotocol: " + (int) PacketUtils.getProtocolId(pack));
		System.out.println("\tversion: " + (int)PacketUtils.getProtocolVersion(pack));
		System.out.println("\ttype: " + (int)PacketUtils.getMessageType(pack));
		System.out.println("\tplayer: \'" + PacketUtils.getPlayerId(pack) + "\'" );
		System.out.println("\tdata: \'" + PacketUtils.getPacketMessage(pack)+ "\'" );
	}
	public static synchronized void debug(DatagramPacket pack, String header)
	{
		System.out.println("=================================================================================");
		System.out.println("= " + header);
		System.out.println("=================================================================================");
		debug( pack );
		
	}	
	
	
}
