package dataTypes;

public class DVector2D {
	public double x;
	public double y;
	
	public DVector2D( double x , double y ) {
		this( x , y , false );
	}
	
	public DVector2D( double x , double y , boolean normalized ) {
		this.x = x;
		this.y = y;
		
		if( normalized == true ) {
			this.normalize();
		}
	}
	
	public double getModulus() {
		return DVector2D.getModulus( this );
	}
	
	public static double getModulus( DVector2D vect ) {
		return Math.sqrt( ( vect.x * vect.x ) + ( vect.y * vect.y ) );
	}
	
	public double getDistance( DVector2D anotherVector ) {		
		return DVector2D.getDistance( this , anotherVector );
	}
	
	public static double getDistance( DVector2D vector1 , DVector2D vector2 ) {
		DVector2D distance = DVector2D.getDistanceVector( vector1 , vector2 );	
		return distance.getModulus();
	}
	
	public static DVector2D getDistanceVector( DVector2D vector1 , DVector2D vector2 ) {
		return new DVector2D( vector2.x - vector1.x , vector2.y - vector1.y );
	}
	
	public void normalize() {
		double mod = this.getModulus();
		
		if( mod > 0 ) {
			this.x = this.x / mod;
			this.y = this.y / mod;
		}
	}
	
	public double getDirectionRAD() {
		double direction = 0;

		if( this.x == 0 ) {
			if( this.y > 0 ) {
				direction = Math.PI * 0.5;
			} else {
				direction = Math.PI * 1.5;
			}		
		} else if ( this.y == 0 ) {
			if( this.x > 0 ) {
				direction = 0;
			} else {
				direction = Math.PI;
			}
		} else {
			direction = Math.atan2( this.y , this.x );
		}
		
		return direction;
	}
	
	
}
