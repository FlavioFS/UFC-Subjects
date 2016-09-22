import java.net.DatagramPacket;

/**
 * @brief Interface para tratamento de pacotes que chegam ao protocolo 
 *
 */
public abstract class PacketHandler
{
	/**
	 * 
	 * @param pack Datagram packet to be processed
	 * @param proto Protocol instance for communication
	 * 
	 * @return True, if the packet type is supported and was successfully processed 
	 */
	public abstract boolean handlePacket( DatagramPacket pack, Protocol proto );
	
	/**
	 * 
	 * @param pack
	 * @return True, if and only if the packet has a high priority
	 */
	public abstract boolean isPriorityPacket(DatagramPacket pack);
	
	/**
	 * Esse método deve ser sobrescrito caso o tratamento de pacotes prioritários divirja do tratamento dado a pacote comuns. 
	 * 
	 * @return
	 */
	public boolean handlePriorityPacket(DatagramPacket pack, Protocol proto )
	{
		return handlePacket(pack, proto);
	}
}
