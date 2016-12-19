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
				this.cmd = Commands.MOVE;
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
}
