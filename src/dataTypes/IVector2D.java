package dataTypes;

public class IVector2D {
	public int x;
	public int y;
	
	public IVector2D( int x , int y ) {
		this.x = x;
		this.y = y;
	}
	
	public int getModulus( ) {
		int mod;
		mod = (int)Math.sqrt( ( this.x * this.x ) + ( this.y * this.y ) );
		return mod;
	}
}
