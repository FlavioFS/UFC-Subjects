

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.DatagramPacket;
import java.util.HashMap;

/**
 * Especialização de {@link GameInput} que é responsável por ler as entradas locais.
 * 
 */
public class LocalGameInput extends GameInput
	implements KeyListener
{
	
	
	/**
	 * Instâcia singleton
	 */
	private static LocalGameInput singleton;
	/**
	 * Flag interna dizendo se este componente está habilitado.
	 * Caso contrário não haverá teclas sendo pressionadas.
	 */
	private boolean enabled = true;
	
	private HashMap<Integer, Boolean> keyStateCache = new HashMap<Integer, Boolean>();
	
	/**
	 * Construtor padrão. Inicializa as teclas padrões
	 */
	private LocalGameInput()
	{		
	}
	
	/**
	 * Método singleton
	 * 
	 * @return Instância única de GameInput
	 */
	public static LocalGameInput getInstance()
	{
		if( singleton == null )
			singleton = new LocalGameInput();
		
		return singleton;
	}
	
	public boolean getState( int keyCode )
	{
		if( keyStateCache.containsKey(Integer.valueOf( keyCode) ) )
		{
			return keyStateCache.get(Integer.valueOf( keyCode) ).booleanValue();
		}
			
		return false;
	}
	
	@Override
	protected void setState( boolean value, int keyCode )
	{
		keyStateCache.put( Integer.valueOf( keyCode), Boolean.valueOf(value) ); 
		
		// playerID
		Protocol proto = GamePanel.getInstance().getProtocol();
		String localPid = Game.getLocalPlayerId();
		//String localPid = Game.getLocalPlayerId();
		
		Avatar a = GamePanel.getInstance().getAvatar(localPid);
		
		// ajusta o pacote para KEY UP se necessário 
		String code = GameInput.keyCodeToString(keyCode);
		if( code.length() > 0)
		{
			DatagramPacket keyPack =  value ? PacketFactory.newKeyDown( localPid, code ) : PacketFactory.newKeyUp( localPid, code );	
			
			// envia o pacote primeiro
			proto.send(keyPack);
			if( value == false )
			{
				DatagramPacket posPack = PacketFactory.newPosition( a.getX(), a.getY(), 0);
				proto.send(posPack);
			}
		}
		
		super.setState(value, keyCode);
	}
	
	protected void handleEvent( KeyEvent e, boolean expectedValue )
	{
		if( enabled )
		{
			Integer key = Integer.valueOf( e.getKeyCode() );
			if(    !keyStateCache.containsKey(key) 
				|| (keyStateCache.get(key).booleanValue() != expectedValue)
				)
			{				
				setState( expectedValue, e.getKeyCode() );
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		handleEvent(e,true);
	}

	@Override
	public void keyReleased(KeyEvent e) {		
		handleEvent(e,false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
}
