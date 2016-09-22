public class Bomb {
	
	// Attributes
	private Vector2D pos = new Vector2D ();
	private String playerId;
	private double radius;
	private long timeout;
	
	public static double BOMB_RADIUS_DEFAULT = 10;
	public static double BOMB_COOLDOWN_DEFAULT = 500;
	public static long BOMB_LIFETIME_DEFAULT = 20000;
	
	//////////////////////////////////////////////////////////////////////
	
	// Getters
	public Vector2D getPos()		{ return pos; }
	public String	getPlayerId()	{ return playerId; }
	public double getRadius()		{ return radius;	}
	public double getRadius2()		{ return radius*radius;	}

	// Setters
	public void setPos(Vector2D pos)		{ this.pos = pos; }
	public void setPos(double x, double y)	{ pos.setXY(x, y); }
	public void setRadius(double radius)	{ this.radius = radius; }
	public void setPlayerId(String playerId){ this.playerId = playerId; }
	
	// Constructors
	public Bomb (double x, double y, double radius, String playerId)
	{
		timeout = System.currentTimeMillis() + BOMB_LIFETIME_DEFAULT;
		setPos(x, y); setRadius(radius); setPlayerId(playerId);
	}
	
	public Bomb (double x, double y, String playerId)		{ this(x, y, BOMB_RADIUS_DEFAULT, playerId); }
	public Bomb (Vector2D v, double radius, String playerId){ this(v.getX(), v.getY(), radius, playerId); }
	public Bomb (Vector2D v, String playerId)				{ this(v.getX(), v.getY(), BOMB_RADIUS_DEFAULT, playerId); }
	
	
	//////////////////////////////////////////////////////////////////////
	
	// Methods
	public boolean isHitting (Avatar player)
	{
		// Bomb belongs to player
		if (player.getPlayerId().equals(playerId)) return false;
		
		Vector2D diff = Vector2D.minus(player.getPos(), pos);
		return (Vector2D.dot(diff, diff) <= getRadius2()) ? true : false;
	}
	
	public boolean expired () {	return System.currentTimeMillis() >= timeout; }

	//////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString () {
		return playerId + " => (" + pos.getX() + ", " + pos.getY() + ")~~~~~* " + radius;
	}
}
