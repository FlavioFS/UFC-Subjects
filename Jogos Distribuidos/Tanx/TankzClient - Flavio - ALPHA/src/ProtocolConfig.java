/**
 * Principal classe de rede, encapsula o protocolo de comunicação entre os pontos da rede. 
 * 
 * @brief 
 * 
 */
public class ProtocolConfig 
{
	/// Indica se mensagens devem ser impressas na saída do programa.
	public static final boolean VERBOSE_ON = false;
	
	/** Constante do programa: tamanho do pacote
	 *  Geralmente os protocolos construídos sobre o UDP usam pacotes de tamanho fixo.
	 *  Essa prática é questionável para alguns propósitos, quando a mudança do tamanho do
	 *  pacote faz sentido.
	 *  Por exemplo:
	 *   - durante a descoberta de serviço, o pacote é de 128 bytes;
	 *   - durante o jogo o pacote recebe maior volume de dados, podendo passar a 512 bytes.
	 */
	public static final int PACKET_SIZE = 512; 
	
	/// Número da porta utilizada pelo protocolo
	public static final int PORT  = 16888;	
	
	public static final int MAX_PLAYER_ID_LENGTH = 32;
	
	/** Deve-se ignorar mensagens do próprio host? Apenas mesmo endereço e porta.
	 *  Desse modo é possível jogar com outro cliente/servidor no mesmo endereço mas em porta distinta 
	 */
	public static final boolean IGNORE_SELF_MESSAGES = true;
	
	public static final long PLAYER_TIMEOUT = 3000;
	/// Intervalo de tempo para o envio de pacotes do tipo PING
	public static final long PING_INTERVAL = 250;
	
	/// Intervalo de tempo para envio de "heartbeat", indicando que o player ainda está ativo
	public static final long HEARTBEAT_INTERVAL = 2000;	
	
	public static final byte PROTOCOL_BYTE_ID = 0x7f; 
	public static final byte PROTOCOL_BYTE_CURR_VERSION = 10;
	public static final byte PROTOCOL_BYTE_MIN_VERSION = 10;
	public static final byte PROTOCOL_BYTE_MAX_VERSION = 18;
	
	
}
