import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Avatar
{
	public static final int DEFAULT_RADIUS = 20;
	public static final int DEFAULT_SPEED  = 200;
	public static final int DEFAULT_SPEED_DIAG = (int) (DEFAULT_SPEED / Math.sqrt(2));
	
	String playerId;
	int radius = DEFAULT_RADIUS;
	Vector2D pos = new Vector2D ();
	Vector2D speed = new Vector2D();
	boolean alive = true;
	
	long lastMsgReceived;
	long rtt;
	long rttAdds = 1;
	
	boolean bombAvailable = true;
	private ArrayList<Bomb> bombList = new ArrayList<Bomb>();
	private int nextJoke = 0;
	String [] jokes = {
		"Wanna meet my friend PAUL?",
		"This game is going to be a BOOM in the pre-release!",
		"This game blows me up!",
		"My pocket is filled like a cluster!",
		"Wanna drink a Molotov cocktail?",
		"Let's have a BLAST tonight!"
	};
	

	
	double bulletX;
	double bulletY;
	double bulletSpeedX;
	double bulletSpeedY;
	boolean bulletAvailable = true;
	
	// Getters
	public String 	getPlayerId() 			{ return playerId; }
	public int	getRadius()					{ return radius; }
	public Vector2D getSpeed()				{ return speed; }
	public boolean isAlive()				{ return alive; }
	public Vector2D getPos()				{ return pos; }
	public double	getX()					{ return pos.getX(); }
	public double	getY()					{ return pos.getY(); }
	
	// Setters
	public void	setPlayerId(String pid) 	{ this.playerId = pid; }
	public void setRadius(int r)			{ radius = r; }
	public void setSpeed(double x, double y){ speed.setXY(x, y); }
	public void setAlive(boolean v)			{ alive = v; }
	public void setPos(Vector2D v)			{ pos = v; }
	public void	setX(double x)				{ pos.setX(x); }
	public void	setY(double y)				{ pos.setY(y); }
	public void	setXY(double x, double y)	{ pos.setXY(x, y); }
	public void translateX(double dx)		{ setX(pos.getX() + dx); }
	public void translateY(double dy)		{ setY(pos.getY() + dy); }
	public void translate (Vector2D dv)		{ setPos(Vector2D.plus(pos, dv)); }
	public void translateXY(double dx , double dy)
	{
		translateX(dx);
		translateY(dy);
	}
	public void moveToXY(double dx, double dy) {
		translateXY(dx, dy);
		
		if (pos.getX() < 0)			translateX(800);
		else if (pos.getX() > 800)	translateX(-800);
		
		if (pos.getY() < 0)			translateY(600);
		else if (pos.getY() > 600)	translateY(-600);
	};
	
	
	// Bombs
	public boolean isBombAvailable() 		{ return bombAvailable; }
	public ArrayList<Bomb> getBombList ()	{ return bombList; }
	public void dropBomb( double x, double y, String playerId )
	{
		getBombList().add(new Bomb(x, y, playerId));
		bombAvailable = false;
		
		
		
		
		new Thread() {
			public void run () {
				try		{Thread.sleep((long) Bomb.BOMB_COOLDOWN_DEFAULT); }
				catch	(InterruptedException e) { e.printStackTrace(); }
				
				bombAvailable = true;
			}
		}.start();
	}
	
	public boolean isBeingHit (Bomb b)
	{
		// Bomb belongs to player
		if (b.getPlayerId().equals(playerId)) return false;
		
		Vector2D diff = Vector2D.minus(pos, b.getPos());
		return (Vector2D.dot(diff, diff) <= b.getRadius2()) ? true : false;
	}
	public boolean isBeingHit (ArrayList<Bomb> bl)
	{
		boolean rv = false;
		
		for (Iterator<Bomb> iterator = bl.iterator(); iterator.hasNext();)
		{
			Bomb b = (Bomb) iterator.next();
			
			if (isBeingHit(b))
			{
				rv = true;
				break;
			}
		}
		
		return rv;
	}
	
	public void bombJoke () {
		Random rpun = new Random();
		int shift = rpun.nextInt(60);
		
		if (shift > 5) return;			// Adds chance of keeping silent
		
		nextJoke = (nextJoke + 1 + shift % 2) % jokes.length;
		
		System.out.println("[" + playerId + "]: " + jokes[nextJoke]);
	}
	
	
	// Bullets
	public double getBulletX()			{ return bulletX; }
	public double getBulletY()			{ return bulletY; }
	public double getBulletSpeedX()		{ return bulletSpeedX; }
	public double getBulletSpeedY()		{ return bulletSpeedY; }
	public boolean isBulletAvailable()	{ return bulletAvailable; }
	public void shoot( double x, double y, double vx, double vy )
	{
		bulletX = x;
		bulletY = y;
		bulletSpeedX = vx;
		bulletSpeedY = vy;
		bulletAvailable = false;
	}
	




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
			/*
			this.rtt += ping;
			this.rttAdds += 1;
			
			if( rttAdds > 5 )
			{
				this.rtt /= rttAdds;
				this.rttAdds = 1;
			}*/
			
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
	
	void processInputs(GameInput lgi, long inputLag )
	{
		if (!isAlive()) return;
		
		if	( lgi.isKeyDown (GameInput.KEY_LEFT) )	{
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		speed.setXY(-DEFAULT_SPEED_DIAG, -DEFAULT_SPEED_DIAG);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) speed.setXY(-DEFAULT_SPEED_DIAG,  DEFAULT_SPEED_DIAG);
			else                                        speed.setXY(-DEFAULT_SPEED, 0);
		}

		else if	( lgi.isKeyDown (GameInput.KEY_RIGHT))
		{
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		speed.setXY(DEFAULT_SPEED_DIAG, -DEFAULT_SPEED_DIAG);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) speed.setXY(DEFAULT_SPEED_DIAG,  DEFAULT_SPEED_DIAG);
			else                                        speed.setXY(DEFAULT_SPEED, 0);
		}
		
		else {
			if (lgi.isKeyDown(GameInput.KEY_UP)) 		speed.setXY(0, -DEFAULT_SPEED);
			else if (lgi.isKeyDown(GameInput.KEY_DOWN)) speed.setXY(0,  DEFAULT_SPEED);
		}
	}
	
}
