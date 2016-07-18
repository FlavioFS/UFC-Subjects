

import java.net.DatagramPacket;

/**
 * @brief Classe utilitária responsável por criar pacotes
 * 
 * @author gilvan
 *
 */
public class PacketFactory
{

	/**
	 * @brief Cria um novo pacote para uso genérico. Note-se que configurações como conteúdo, endereço e porta podem ser mudados a posteriori.
	 * 
	 * @return
	 */
	public static final DatagramPacket newPacket( byte[] data )
	{			
		data[0] = ProtocolConfig.PROTOCOL_BYTE_ID;
		data[1] = ProtocolConfig.PROTOCOL_BYTE_CURR_VERSION;
		
		// pega o Player ID e insere no local certo
		String playerId = ""+GamePanel.getInstance().getPlayerId();
		PacketUtils.injectPlayerId(data, playerId);		
		
		return new DatagramPacket( data, ProtocolConfig.PACKET_SIZE, Protocol.BROADCAST_ADDRESS, ProtocolConfig.PORT );
	}
	
	/**
	 * Cria um novo pacote de um dado tipo. Ideal para "keep alive" ou montar uma mensagem mais complexa.
	 * 
	 * @param packType
	 * @return
	 */
	public static final DatagramPacket newPacket( byte packType )
	{
		DatagramPacket pack = newPacket();
		// define o tipo de pacote
		pack.getData()[2] = packType;
		return pack; 
	}
	
	/**
	 * Instancia um novo pacote com o tipo escolhido pelo invocador.
	 * 
	 * @param packType Byte identificando o tipo de pacote
	 * @param dataStr String contendo os dados transmitidos para aquele tipo de pacote.
	 *             Mensagens maiores do que Protocol.PACK_SIZE serão truncadas.      
	 * 
	 * @return Nova instância de DatagramPacket, formatada corretamente.
	 */
	public static final DatagramPacket newPacket( byte packType, String dataStr )
	{
		DatagramPacket pack = newPacket( packType );
		
		PacketUtils.injectMessageData(pack.getData(), dataStr);				
		return pack;
	}
		
	
	/**
	 * @brief Cria um novo pacote para uso genérico. Útil para a recepção de mensagens
	 *  
	 * @return
	 */
	public static DatagramPacket newPacket()
	{
		return newPacket( new byte[ProtocolConfig.PACKET_SIZE] );
	}
	

	/**
	 * @brief Instancia um novo pacote para transmissão de heartbeat.
	 * 
	 * @return
	 */
	public static DatagramPacket newHeartbeat()
	{
		return newPacket( Protocol.PT_HEARTBEAT );
	}
	
	/**
	 * Cria um novo pacote do tipo ping para ser enviado pela rede.
	 * 
	 * @return
	 */
	public static DatagramPacket newPing( Protocol proto )
	{
		if( proto != null )
			return newPacket( Protocol.PT_PING, Long.toString( proto.getTimestamp() ) );
		else
			return newPacket( Protocol.PT_PING, Long.toString(System.currentTimeMillis()) );
		
	}
	
	/**
	 * Cria um novo pacote do tipo key down
	 * 
	 * @param pid
	 * @return
	 */
	public static DatagramPacket newKeyDown( String pid, String key )
	{
		return newPacket( Protocol.PT_KEY_DOWN, key );
	}
	
	public static DatagramPacket newKeyUp( String pid, String key )
	{
		return newPacket( Protocol.PT_KEY_UP, key );
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param angle
	 * @return
	 */
	public static DatagramPacket newPosition( double x, double y, double angle )
	{
		String msg = x + " " + y;		
		
		return newPacket( Protocol.PT_POSITION, msg );
	}
	
//	public static DatagramPacket newDied( double x, double y )
//	{
//		String msg = x + " " + y;		
//		
//		return newPacket( Protocol.PT_DIED, msg );
//	}
//	
//	public static DatagramPacket newBomb( double x, double y )
//	{
//		String msg = x + " " + y;				
//		
//		return newPacket( Protocol.PT_BOMB, msg );
//	}
	
	public static DatagramPacket newShoot( double x, double y, double vx, double vy )
	{
		String msg = x + " " + y + " " + vx + " " + vy;				
		
		return newPacket( Protocol.PT_SHOOT, msg );
	}
	
}







