package prolog;


public class PrologCellDecidions {
	public int x = 0;
	public int y = 0;
	public int o = 0;
	public Commands cmd;
	
	public PrologCellDecidions( int x , int y , int o , String tipo ) {
		this.x = x;
		this.y = y;
		this.o = o;
		
		switch( tipo ) {
			case "mover":
				this.cmd = Commands.MOVE_TO;
				break;
				
			case "sair":
				this.cmd = Commands.EXIT;
				break;		
				
			case "atirar":
				this.cmd = Commands.FIRE;
				break;		
				
			case "repetir":
				this.cmd = Commands.REPEATLAST;
				break;					
		}
	}
	
	// Compare two Manhattan Distances
	public int compareManhattan( PrologCellDecidions another ) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	

	    if( this.o == another.o ) {
	    		return EQUAL;
	    }
	    else  if( this.o > another.o ) {
	    	return AFTER;
	    }
	    else {
	    	return BEFORE;
	    }
	}
	
}
