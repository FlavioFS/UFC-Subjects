

import java.awt.event.KeyEvent;
import java.util.Hashtable;

/**
 * @brief Abstrai uma fonte de entrada para efeitos de interação com o jogador.
 * 
 * Realizações dessa interface são usadas para efetuar o jogo.  
 *
 */
public abstract class GameInput {
	
	/// Identifica a ação "UP"
	public static final String KEY_UP = "VK_UP";
	/// Identifica a ação "DOWN"
	public static final String KEY_DOWN= "VK_DOWN";
	/// Identifica a ação "LEFT"
	public static final String KEY_LEFT= "VK_LEFT";
	/// Identifica a ação "RIGHT"
	public static final String KEY_RIGHT = "VK_RIGHT";
	/// Identifica a ação "ENTER"
	public static final String KEY_ENTER = "VK_ENTER";
	/// Identifica a ação "SHOOT"
	public static final String KEY_SPACE = "VK_SPACE";
	/// Identifica a ação "ESCAPE"
	public static final String KEY_ESCAPE = "VK_ESCAPE";
	
		
	/// Input lag induzido pela rede, em milissegundos
	public static final long DEFAULT_INPUT_LAG = 50;

	/**
	 * Tabela associando um identificador de jogador a um input de jogo.
	 * Usado principalmente para associar inputs remotos aos jogadores conectados à partida.
	 */
	protected static Hashtable<String,GameInput> playerInputs = new Hashtable<String, GameInput>();
	
	/**
	 * Estruturas de dados mais eficientes podem ser criadas.
	 */
	protected Hashtable<String,Boolean> keyState = new Hashtable<String,Boolean>();
	protected boolean locked = false;
	
	/**
	 * Método utilitário usado pelo protocolo para notificar o estado de uma ação remota.
	 * 
	 * @param pid Identificador do jogador remoto.
	 * @param keyCode Identificador da ação
	 * @param state Booleano especificando se a ação está ativa
	 */
	public static void setPlayerKey( String pid, String keyCode, boolean state )
	{
		GameInput gi = getInputForPlayer( pid );
		gi.setState(state, keyCode);
	}
	
	public static void setLocked(String playerId, boolean state) {
		GameInput gi = getInputForPlayer(playerId);
		gi.locked = state;
	}
	
	
	/**
	 * Provê acesso ao input para um determinado jogador.
	 * 
	 * @param pid Identificador do jogador
	 * 
	 * @return Instância de GameInput associado àquela jogador
	 */
	public static GameInput getInputForPlayer( String pid )
	{
		synchronized(playerInputs)
		{
			GameInput gi = playerInputs.get( ""+pid);
			if( gi == null )
			{
				String local = "";
				if( pid == Game.getLocalPlayerId() )
				{
					gi = LocalGameInput.getInstance();
					local = " (local)";
				}
				else
				{
					gi = new RemoteGameInput( pid );
				}
				
				playerInputs.put(""+pid, gi);
				
				System.out.println("input created for Player " + pid + local );
			}
			
			return gi;
		}
	}
	
	/**
	 * Remove todas as fontes de inputs associados ao jogo.
	 * Útil para começar novas partidas.
	 */
	public static void clearGameInputs()
	{
		synchronized(playerInputs)
		{
			playerInputs.clear();
		}
	}
	
	
	/**
	 * Construtor padrão. Cria as teclas/ações convencionais.
	 */
	public GameInput() {
		// insere os valores padrões de teclas
		keyState.put( GameInput.KEY_UP, Boolean.valueOf(false) );
		keyState.put( GameInput.KEY_DOWN, Boolean.valueOf(false)  );
		keyState.put( GameInput.KEY_LEFT, Boolean.valueOf(false)  );
		keyState.put( GameInput.KEY_RIGHT, Boolean.valueOf(false)  );
		// botões de ação
		keyState.put( GameInput.KEY_SPACE, Boolean.valueOf(false)  );
		keyState.put( GameInput.KEY_ENTER, Boolean.valueOf(false)  );
		// botões de saída
		keyState.put( GameInput.KEY_ESCAPE, Boolean.valueOf(false)  );
	}
	
	
	/**
	 * Acessa o valor de uma tecla.
	 *  
	 * @param keyName Nome da tecla virtual. Ex: GameInput.KEY_ESCAPE
	 * 
	 * @return True, se a tecla está pressionada.
	 */
	public boolean isKeyDown( String keyName )
	{		
		synchronized(this)
		{
			if( keyState.get(keyName) != null )
			{
				boolean bok = keyState.get(keyName).booleanValue(); 
				return bok;
			}
		}
		
		return false;
	}
	
	/**
	 * Converte um código de tecla em uma String
	 * 
	 * @param keyCode Código identificando a tecla a ser convertida.
	 * @return String correspondendo ao código de tecla
	 */
	public static String keyCodeToString(int keyCode)
	{
		switch( keyCode )
		{
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			return GameInput.KEY_UP;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			return GameInput.KEY_DOWN;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			return GameInput.KEY_LEFT;			
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			return GameInput.KEY_RIGHT;
		//case KeyEvent.VK_SPACE: return GameInput.KEY_SPACE;
		case KeyEvent.VK_ENTER:
			return GameInput.KEY_ENTER;
		}
		
		return "";
	}
	
	/**
	 * Define o estado de uma tecla.
	 * 
	 * @param value Valor booleano do estado
	 * @param keyCode String identificando a tecla
	 */
	protected void setState( boolean value, int keyCode )
	{
		if (locked) return;
		
		synchronized( this )
		{
			switch( keyCode )
			{
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				keyState.put(GameInput.KEY_UP, Boolean.valueOf(value) );
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				keyState.put(GameInput.KEY_DOWN, Boolean.valueOf(value) );
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				keyState.put(GameInput.KEY_LEFT, Boolean.valueOf(value) );
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				keyState.put(GameInput.KEY_RIGHT, Boolean.valueOf(value) );
				break;
			case KeyEvent.VK_SPACE:
				keyState.put(GameInput.KEY_SPACE, Boolean.valueOf(value) );
				break;
			case KeyEvent.VK_ENTER:
				keyState.put(GameInput.KEY_ENTER, Boolean.valueOf(value) );
				break;
			}
		}
	}
	
	/**
	 * Define o estado de uma tecla virtual
	 * 
	 * @param value Valor da tecla. True significa "pressionado"
	 * @param keyCode Código virtual da tecla.
	 */
	protected void setState( boolean value, String keyCode )
	{
		if (locked) return;
		
		keyState.put( keyCode, Boolean.valueOf(value) );
	}
	 
}
