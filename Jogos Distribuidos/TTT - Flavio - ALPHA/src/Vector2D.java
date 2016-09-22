
public class Vector2D {
	
	// Attributes
	private double x, y;
	
	//////////////////////////////////////////////////////////////////////
	
	// Getters
	public double	getX()					{ return x; }
	public double	getY()					{ return y; }
	
	// Setters
	public void setX(double x)				{ this.x = x; }
	public void setY(double y) 				{ this.y = y; }
	public void setXY(double x, double y)	{ setX(x); setY(y); }
	
	// Constructors
	public Vector2D (double x, double y) 	{ setX(x); setY(y); }
	public Vector2D ()						{ this(0,0); }
	
	//////////////////////////////////////////////////////////////////////
	
	// Methods
	/** Cross product in the plane, with z-axis as 0
	 * @param Vector2D v
	 * @return z value as double (z axis does not actually exist! x and y are always zero, it is a pure z-vector.)
	 */
	public static double cross		(Vector2D v1, Vector2D v2)	{ return v1.x * v2.y - v2.x * v1.y; }	
	public static double dot		(Vector2D v1, Vector2D v2)	{ return v1.x * v2.x + v1.y * v2.y; }
	public static Vector2D prod	(double    k, Vector2D  v)	{ return new Vector2D (k * v.x, k * v.y); }
	public static boolean isNormal	(Vector2D v1, Vector2D v2)	{ return Vector2D.dot(v1, v2) == 0; }
	public static boolean parallel	(Vector2D v1, Vector2D v2)	{ return v1.x * v2.y == v1.y * v2.x; }
	public static Vector2D plus		(Vector2D v1, Vector2D v2)	{ return new Vector2D(v1.x + v2.x, v1.y + v2.y); }
	public static Vector2D minus	(Vector2D v1, Vector2D v2)	{ return new Vector2D(v1.x - v2.x, v1.y - v2.y); }
	
	public void normalize () {
		double abs = Math.sqrt(x*x + y*y);
		setXY(x/abs, y/abs);
	}
	public Vector2D normalized () {
		double abs = Math.sqrt(x*x + y*y);
		return new Vector2D(x/abs, y/abs);
	}

	@Override
	public String toString () {
		return "(" + x + ", " + y + ")";
	}
}
