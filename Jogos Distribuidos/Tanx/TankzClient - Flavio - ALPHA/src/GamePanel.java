

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A principal classe do jogo
 * 
 * @author gilvan
 *
 */
public class GamePanel extends JPanel {

	/**
	 * Serial ID. Usado para fins de serialização.
	 */
	private static final long serialVersionUID = -569299395853144603L;
	
	/**
	 * Instância única desse GamePanel	 
	 */
	private static GamePanel singleton;
	
	/**
	 * Largura da janela, em pixels.	
	 */
	public static final int SCREEN_WIDTH  = 800;
	/**
	 * Largura da janela, em pixels.	
	 */
	public static final int SCREEN_HEIGHT = 600;
	
	protected long lastFrame = 0;
	protected double deltaTime = 0.001; 
	
	protected Image IMG_BKG = loadImage("bkg.png");
	
	protected Vector<Avatar> avatars = new Vector<Avatar>();
	
	protected Protocol protocol;
	
	
	
	protected Image backBuffer;
	boolean running = true;;
	protected String playerId = null;
		
	/**
	 * Construtor padrão
	 */
	private GamePanel()
	{
		// habilita bufferização dupla nessa janela
		super(true);
		
		// configuração gráfica: resolução, visibilidade etc
		setPreferredSize( new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT) );		
		setBackground( Color.BLACK );
		setVisible(true);
		
		// adiciona o player local		
		playerId  = Game.PLAYER;
		avatars.add( new Avatar( playerId, 25, 150) );
		
		
		try
		{
			DatagramSocket ds = new DatagramSocket( ProtocolConfig.PORT );
			ds.setBroadcast(true);
			
			protocol = new Protocol();
			protocol.startService( ds );
		}catch(Exception x)
		{			
			x.printStackTrace();
		}
		
		
		// inicia a thread de pintura da cena
		new Thread()
		{
			public void run()
			{
				while(running )
				{
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					repaint();
				}
			}
		}.start();
	}
	
	public Avatar getAvatar( String pid )
	{
		synchronized( avatars )
		{
			for( Avatar t : avatars )
			{
				// IMPORTANTE: string é objeto, que se compara com método. Ou seja, == não funciona!
				if( t.playerId().equalsIgnoreCase( pid ) )
					return t;
			}
			
			Avatar tx = new Avatar( pid, 25, 150 );
			avatars.add(tx);
			return tx;			 
		}		
	}
	
	public Protocol getProtocol() {
		return protocol;
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public void killAvatar( String pid )
	{
//		Avatar deadman = null;
		for (Avatar a: avatars) {
			if (a.playerId().equals(pid)) {
				GameInput.setLocked(pid, true);
				a.setAlive(false);
//				a.setSpeed(0, 0);
				a.setPos(new Vector2D(8008, 8008));
			}
		}
		
//		if (deadman != null) avatars.remove(deadman);

		//		for( int i = 0; i < avatars.size(); i++ )
//		{
//			if( avatars.get(i).getPlayerId().equals(pid) ) avatars.remove(i);
//		}
	}
	
	public void updateBulletPositions () {
		double dt = GamePanel.getInstance().getDeltaTime();
		
		for (Avatar player : avatars) {
//			player.translateXY( player.speed.x() * dt, player.speed.y() * dt );
			
			for (Bullet bullet : player.getBulletList()) {
				bullet.move();
			}
		}
	}


	/**
	 * Método singleton. Retorna a instância única de GamePanel.
	 * 
	 * @return
	 */
	public static synchronized GamePanel getInstance()
	{
		if( singleton == null )
			singleton = new GamePanel();
		
		return singleton;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		running = false;
	}
	
	/**
	 * 
	 * @param b
	 * 
	 * @return Id do tanque atingido. -1 caso não haja colisão.
	 */
	public int checkCollision( Object b )
	{
		/*
		double dx, dy;
		for( Avatar t : avatars )
		{
			// evita que o tanque atirando seja atingido pela própria bala
			if( t.getPid() == b.getPid() )
				continue;
			
			dx = t.getX() - b.getX();
			dy = t.getY() - b.getY();
			
			if( dx*dx + dy*dy <= 22*22)
				return t.getPid();
		}
		*/
		return -1;
	}
	
	/**
	 * 
	 */
	public void repaint() {
		LocalGameInput.getInstance();
		paint( getGraphics() );
	}
	

	/**
	 * Carrega uma imagem a partir de um arquivo.
	 * 
	 * @param filename Path do arquivo
	 * @return Nova instância de Image, ou null, caso algum erro ocorra.
	 */
	public static Image loadImage( String filename )
	{
		BufferedImage img = null;
		
		try {
	           img = ImageIO.read(new File(filename));
	       } catch (IOException e) {
	    	   e.printStackTrace();
	       }
		
		return img;
	}
	
	public double getDeltaTime()
	{
		return deltaTime;
	}
	
	protected void paintBackground(Graphics2D g2d)
	{
		/*if( IMG_BKG != null )
			g2d.drawImage(IMG_BKG, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
		else
			*/
		{
			g2d.setColor( Color.WHITE );
			g2d.fillRect(0, 0,  SCREEN_WIDTH, SCREEN_HEIGHT);
			
			//
			g2d.setColor( Color.LIGHT_GRAY );
			int cellSize = 30;
			for( int i=0 ; i<SCREEN_WIDTH ; i+=cellSize )
			{
				g2d.drawLine(i, 0, i,  SCREEN_HEIGHT);			
				g2d.drawLine(0, i, SCREEN_WIDTH, i);
			}
			
			g2d.setColor( Color.BLACK );
		}
	}
	
	/**
	 * 
	 */
	public void paint(Graphics g)
	{
		Graphics2D g2d = null;
		
		if( backBuffer == null )
		{
			backBuffer = createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
			if( backBuffer == null ) return;
			
			g2d = (Graphics2D) backBuffer.getGraphics();
			g2d.setFont( new Font(Font.SANS_SERIF, Font.BOLD, 32));
			
			// define configurações de qualidade
			g2d.setRenderingHint( RenderingHints.KEY_RENDERING , RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
			g2d = (Graphics2D) backBuffer.getGraphics();		
			g2d.setFont( new Font(Font.SANS_SERIF, Font.BOLD, 12));
		}
		
		g2d = (Graphics2D) backBuffer.getGraphics();
		paintBackground(g2d);
		
		
		
		long now = System.currentTimeMillis();
		if( (now - lastFrame) > 100 )
		{
			// truncando o frame fora do esperado
			lastFrame = now;
			deltaTime = 0.1;
		}else
		{
			deltaTime = (now - lastFrame)*0.001;
			lastFrame = now;
		}
		
		// NOTA: dispara todas as ações antes do frame
		//TaskTimer.getInstance().triggerTasks();
		
		// background: tiles com elementos colidíveis
		//Background.getInstance().draw(g2d);
		
		// pinta, move e anima cada tanque
		synchronized( avatars )
		{
			removeDisconnectedAvatars();
			updateBulletPositions();
			
			
			final Color[] colors = {  Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.DARK_GRAY, Color.ORANGE, Color.MAGENTA };			
			
			//for( Avatar a : avatars )
			for( int i=0; i<avatars.size(); i++ )
			{
				Avatar a =  avatars.get(i);
				a.processInputs();
				
				int radius = a.radius();
				int px = (int) a.x();
				int py = (int) a.y();
				
				
				// Removes bombs out of screen and renders the others
				int bombRadius = (int) Bullet.BULLET_RADIUS_DEFAULT;
				ArrayList<Bullet> bulletList = a.getBulletList();
				
				for (int j = 0; j < bulletList.size(); j++) {
					Bullet b = bulletList.get(j);
					double x = b.pos().x();
					double y = b.pos().y();
					
					boolean outofScreen =	(x < 0)	  || 
											(y < 0)   || 
											(x > 800) || 
					                   		(y > 600);
					if (outofScreen)
					{
						bulletList.remove(j);
						continue;
					}
					
					// The others are rendered
					int ipx = (int) x;
					int ipy = (int) y;
					
					g2d.setColor( colors[ i % colors.length] );					
					g2d.fillOval(ipx-bombRadius, ipy-bombRadius, 2*bombRadius, 2*bombRadius);
					g2d.setColor( Color.lightGray );
					g2d.drawOval(ipx-bombRadius, ipy-bombRadius, 2*bombRadius, 2*bombRadius);
					
				}
				
				// Renders players
				g2d.setColor( colors[ i % colors.length] );
				g2d.fillOval(px-radius, py-radius, 2*radius, 2*radius);
				
				g2d.setColor( Color.BLACK );
				g2d.drawOval(px-radius, py-radius, 2*radius, 2*radius);
				//a.draw(g2d);
			}
			
			
			
			// pinta os nomes dos jogadores
			 
			for( int i=0; i<avatars.size(); i++ )
			{
				Avatar avatar = avatars.get(i);
				String playerName =  avatar.playerId();
				double tw = g2d.getFontMetrics().stringWidth( playerName );
				double th = g2d.getFontMetrics().getHeight();
				double px = avatar.x();
				double py = avatar.y();
				
				
				//g2d.setFont( new Font(Font.SANS_SERIF, Font.BOLD, 32));
				
				g2d.setColor( new Color(128,128,128,128) );			
				//g2d.fillRect( (int) (px-tw*0.5-5), (int) (py-th*0.5 -28-5), (int)tw + 10, (int)th);
				g2d.fillRoundRect( (int) (px-tw*0.5-9), (int) (py-th*0.5 -28-9), (int)tw + 18, (int)th+ 8, 5, 5);
				/*g2d.drawString( playerName, (float) (px -tw*0.5-1), (float) (py-28));
				g2d.drawString( playerName, (float) (px -tw*0.5  ), (float) (py-29));
				g2d.drawString( playerName, (float) (px -tw*0.5  ), (float) (py-27));*/
				
				g2d.setColor( colors[ i % colors.length] );			
				g2d.drawString( playerName, (float) (px -tw*0.5), (float) (py-28));
				
			}
		}
		
		
		////////////////////////////////////////////////////////////////////////////////		!!!!!!
		
		// Bullet collision
		Avatar player = avatars.get(0);
		if (avatars.size() > 1) {	// Is there anyone else?
			
			// Search in all bomb lists
			for (int i = 0; i < avatars.size(); i++) {
				ArrayList<Bullet> iBombList = avatars.get(i).getBulletList();
				
				if (player.isBeingHit(iBombList))
				{
					DatagramPacket deathPacket = PacketFactory.newShoot(player.x(), player.y(), Bullet.BULLET_SPEED_DEFAULT, 0);
					PacketUtils.injectPlayerId(deathPacket.getData(), player.playerId());
					protocol.send(deathPacket);
					killAvatar(player.playerId());
				}
			}
			
		}
		
		////////////////////////////////////////////////////////////////////////////////
		
		// Bomb drop
//		if( LocalGameInput.getInstance().getState(KeyEvent.VK_SPACE) && player.isBulletAvailable() && player.isAlive() )
//		{
//			double sx = player.x();
//			double sy = player.y();
//			
//			player.shoot();
//			DatagramPacket bombPacket = PacketFactory.newBomb(sx, sy);
//			PacketUtils.injectPlayerId(bombPacket.getData(), player.playerId());
//			protocol.send(bombPacket);
//			player.bombJoke();
//		}
		
		
		// passa o conteúdo para a tela.
		g.drawImage(backBuffer,0,0,this);
	}
	
	protected void removeDisconnectedAvatars()
	{
		// 1) toca o jogador local
		String localPid = Game.getLocalPlayerId();
		GamePanel.getInstance().getAvatar(localPid).touch();
		
		// 2) cria uma lista negra
		ArrayList<Avatar> graveyard = new ArrayList<Avatar>();		
		for( Avatar a : avatars )
		{
			if( !a.isConnected() )
			{
				System.out.println("[DISCONNECTED] " + a.playerId() );
				graveyard.add( a );
			}
		}
		
		// 3) mata todos na lisa negra
		avatars.removeAll(graveyard);
	}
	
	
}
