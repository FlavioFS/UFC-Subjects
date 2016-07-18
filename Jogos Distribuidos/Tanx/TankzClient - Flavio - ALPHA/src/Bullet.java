public class Bullet {
	public static double BULLET_RADIUS_DEFAULT   = 10;
	public static double BULLET_SPEED_DEFAULT    = 10;
	public static final int BULLET_SPEED_DIAG = (int) (BULLET_SPEED_DEFAULT / Math.sqrt(2));
	public static double BULLET_COOLDOWN_DEFAULT = 500;
	
	// Attributes
	private Vector2D pos = new Vector2D ();
	private Vector2D speed = new Vector2D ();
	private String playerId;
	private double radius;
	
	//////////////////////////////////////////////////////////////////////
	
	// Getters
	public Vector2D pos()		{ return pos; }
	public Vector2D speed()		{ return speed;	}
	public String	playerId()	{ return playerId; }
	public double 	radius()	{ return radius;	}
	public double 	radius2()	{ return radius*radius;	}

	// Setters
	public void setPos(Vector2D pos)		{ this.pos = pos; }
	public void setPos(double x, double y)	{ pos.setXY(x, y); }
	public void setSpeed(Vector2D speed)	{ this.speed = speed; }
	public void setSpeed(double x, double y){ speed.setXY(x, y); }
	public void setRadius(double radius)	{ this.radius = radius; }
	public void setPlayerId(String playerId){ this.playerId = playerId; }
	
	// Constructors
	public Bullet (double x, double y, double vx, double vy, double radius, String playerId)
	{
		setPos(x, y);
		setSpeed(vx, vy);
		setRadius(radius);
		setPlayerId(playerId);
	}
	
	public Bullet (double x, double y, double vx, double vy, String playerId) {
		this(x, y, vx, vy, BULLET_RADIUS_DEFAULT, playerId);
	}
	
	
//	public Bullet (Vector2D v, double radius, String playerId)
//		{ this(v.x(), v.y(), BULLET_SPEED_DEFAULT, BULLET_SPEED_DEFAULT, radius, playerId); }
//	
//	public Bullet (Vector2D v, String playerId)
//		{ this(v.x(), v.y(), BULLET_SPEED_DEFAULT, BULLET_SPEED_DEFAULT, BULLET_RADIUS_DEFAULT, playerId); }
	
	
	//////////////////////////////////////////////////////////////////////
	
	// Methods
	public void move (double dx, double dy) { pos.setXY(pos.x() + dx, pos.y() + dy); }
	public void move () 					{ move(speed.x(), speed.y()); }
	
	public boolean isHitting (Avatar player)
	{
		// Bomb belongs to player
		if (player.playerId().equals(playerId)) return false;
		
		Vector2D diff = Vector2D.minus(player.pos(), pos);
		return (Vector2D.dot(diff, diff) <= radius2()) ? true : false;
	}
	

	//////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString () {
		return playerId + " => (" + pos.x() + ", " + pos.y() + ")~~~~~* " + radius;
	}
}
