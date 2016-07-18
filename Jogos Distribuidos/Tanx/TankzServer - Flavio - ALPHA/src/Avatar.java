import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Avatar
{
	public static final int DEFAULT_RADIUS = 20;
	public static final int DEFAULT_SPEED  = 5;
	public static final int DEFAULT_SPEED_DIAG = (int) (DEFAULT_SPEED / Math.sqrt(2));
	
	String playerId;
	int radius = DEFAULT_RADIUS;
	Vector2D pos = new Vector2D ();
	Vector2D front = new Vector2D (1, 0);
	boolean alive = true;
	
	long lastMsgReceived;
	long rtt;
	long rttAdds = 1;
	
	boolean bulletAvailable = true;
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	
//	private int nextJoke = 0;
//	String [] jokes = {
//		"Wanna meet my friend PAUL?",
//		"This game is going to be a BOOM in the pre-release!",
//		"This game blows me up!",
//		"My pocket is filled like a cluster!",
//		"Wanna drink a Molotov cocktail?",
//		"Let's have a BLAST tonight!"
//	};
	
	// Getters
	public String 	playerId()			{ return playerId; }
	public int		radius()			{ return radius; }
	public Vector2D front()				{ return front; }
	public boolean 	isAlive()			{ return alive; }
	public Vector2D pos()				{ return pos; }
	public double	x()					{ return pos.x(); }
	public double	y()					{ return pos.y(); }
	public int		bulletListSize()	{ return bulletList.size(); }
	
	// Setters
	public void	setPlayerId(String pid) 	{ this.playerId = pid; }
	public void setRadius(int r)			{ radius = r; }
	public void setFront(double x, double y){ front.setXY(x, y); }
	public void setAlive(boolean v)			{ alive = v; }
	public void setPos(Vector2D v)			{ pos = v; }
	public void	setX(double x)				{ pos.setX(x); }
	public void	setY(double y)				{ pos.setY(y); }
	public void	setXY(double x, double y)	{ pos.setXY(x, y); }
	public void	setXY(Vector2D v)			{ pos.setXY(v.x(), v.y()); }

	// Movement
	public void translateX(double dx) {
		setX(pos.x() + dx);
		
		if (pos.x() < 0)		setX(0);
		else if (pos.x() > 800)	setX(800);
		
		front.setXY(dx / Math.abs(dx), 0);
	};
	
	public void translateY(double dy) {
		setX(pos.y() + dy);
		
		if (pos.y() < 0)		setY(0);
		else if (pos.y() > 600)	setY(600);
		
		front.setXY(0, dy / Math.abs(dy));
	};
	
	public void translateXY(double dx, double dy) {
		setXY(pos.x() + dx, pos.y() + dy);
		
		if (pos.x() < 0)		setX(0);
		else if (pos.x() > 800)	setX(800);
		
		if (pos.y() < 0)		setY(0);
		else if (pos.y() > 600)	setY(600);

		Vector2D f = new Vector2D (dx, dy).normalized();
		front.setXY(f);
	};
	
	public void move () {
		translateXY(front.x(), front.y());
	}
	
	
	// Bullets
	public boolean isBulletAvailable() 		{ return bulletAvailable; }
	public ArrayList<Bullet> getBulletList ()	{ return bulletList; }
	
//	public void shoot( )
//	{
//		
//		bulletX = x;
//		bulletY = y;
//		bulletSpeedX = vx;
//		bulletSpeedY = vy;
//		bulletAvailable = false;
//	}
	
	public void shoot (double vx, double vy)
	{
		getBulletList().add(new Bullet(x(), y(), vx, vy, playerId));
		bulletAvailable = false;
		
		new Thread() {
			public void run () {
				try		{Thread.sleep((long) Bullet.BULLET_COOLDOWN_DEFAULT); }
				catch	(InterruptedException e) { e.printStackTrace(); }
				
				bulletAvailable = true;
			}
		}.start();
	}
	public void shoot ()
	{
		shoot(Bullet.BULLET_SPEED_DIAG, Bullet.BULLET_SPEED_DIAG);
	}
	
	public boolean isBeingHit (Bullet b)
	{
		// Bomb belongs to player
		if (b.playerId().equals(playerId)) return false;
		
		Vector2D diff = Vector2D.minus(pos, b.pos());
		return (Vector2D.dot(diff, diff) <= b.radius2()) ? true : false;
	}
	public boolean isBeingHit (ArrayList<Bullet> bl)
	{
		boolean rv = false;
		
		for (Iterator<Bullet> iterator = bl.iterator(); iterator.hasNext();)
		{
			Bullet b = (Bullet) iterator.next();
			
			if (isBeingHit(b))
			{
				rv = true;
				break;
			}
		}
		
		return rv;
	}
	
//	public void bombJoke () {
//		Random rpun = new Random();
//		int shift = rpun.nextInt(60);
//		
//		if (shift > 5) return;			// Adds chance of keeping silent
//		
//		nextJoke = (nextJoke + 1 + shift % 2) % jokes.length;
//		
//		System.out.println("[" + playerId + "]: " + jokes[nextJoke]);
//	}


	public Avatar(String pid, double x, double y) {
		super();
		this.playerId = pid;
		this.pos.setXY(x, y);
		this.rtt = 0;
		
		touch();
	}
	
	public double getRtt()
	{
		synchronized(this)
		{
			return (double)rtt/(double)rttAdds;
		}
	}
	
	public void computeRtt(long ping)
	{
		synchronized(this)
		{
			this.rtt = ping;
		}
	}
	
	public void touch()
	{
		lastMsgReceived = System.currentTimeMillis();
	}
	
	public boolean isConnected()
	{
		return (System.currentTimeMillis() - lastMsgReceived) <= ProtocolConfig.PLAYER_TIMEOUT;
	}

	
	public void processInputs()
	{
		GameInput lgi = GameInput.getInputForPlayer(playerId);
		if( lgi != null )
		{
			long inputLag = GameInput.DEFAULT_INPUT_LAG;
			// NOTA: corrige o input lag artificial usando o PING do jogador remoto
			if( lgi instanceof RemoteGameInput )
			{
				inputLag = inputLag - (long)Math.ceil(this.getRtt()/2.0);
				if( inputLag < 0 ) inputLag = 0;
			}
			
			processInputs( lgi, inputLag );
		}
		
//		double speedX = bulletSpeedX*GamePanel.getInstance().getDeltaTime();
//		double speedY = bulletSpeedY*GamePanel.getInstance().getDeltaTime();
//		bulletX += speedX;
//		bulletY += speedY;
//		
//		if( bulletX < 0 || bulletY <0 ||
//			bulletX > GamePanel.SCREEN_WIDTH
//			||
//			bulletY > GamePanel.SCREEN_HEIGHT
//				)
//		{
//			bulletAvailable = true;
//		}
	}
	
	void processInputs( GameInput lgi, long inputLag )
	{
		if (!isAlive()) return;
		
		if	( lgi.isKeyDown (GameInput.KEY_LEFT) )	{
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		translateXY(-DEFAULT_SPEED_DIAG, -DEFAULT_SPEED_DIAG);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) translateXY(-DEFAULT_SPEED_DIAG,  DEFAULT_SPEED_DIAG);
			else                                        translateXY(-DEFAULT_SPEED, 0);
		}

		else if	( lgi.isKeyDown (GameInput.KEY_RIGHT))
		{
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		translateXY(DEFAULT_SPEED_DIAG, -DEFAULT_SPEED_DIAG);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) translateXY(DEFAULT_SPEED_DIAG,  DEFAULT_SPEED_DIAG);
			else                                        translateXY(DEFAULT_SPEED, 0);
		}
		
		else {
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		translateXY(0, -DEFAULT_SPEED);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) translateXY(0,  DEFAULT_SPEED);
		}
		
		if ( bulletAvailable && lgi.isKeyDown(GameInput.KEY_SPACE) ) {
			Vector2D bulSpd = Vector2D.prod(Bullet.BULLET_SPEED, front().normalized());
			shoot(bulSpd.x(), bulSpd.y());
		}
	}
	
}
