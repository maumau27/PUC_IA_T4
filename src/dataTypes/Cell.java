package dataTypes;

import java.util.ArrayList;

public class Cell {
	public class AStarData {
		public int 		direction;
		public Cell		predecessor = null;
		public int		costToEnter = 0;
		public int		accumulatedCost = 0;
		public int		heuristicFinalCost = 0;
		public int		type = 0; // 0 = undiscovered , 1 = passed , 2 = frontier
	}
	
	
	// Apenas para Consulta
	public IVector2D 	position = new IVector2D( 0 , 0 );
	public CellType 	type = CellType.CLEAN;
	
	// Atualizar com o ProLog
	public boolean		discovered = false;
	public boolean		frontier = false;
	public boolean		destroyed = false;
	public AStarData	ASData = new AStarData();
	
	
	public double			clusterWeight;
	public ArrayList<Cell>	cluster;
	
	// Dados do MAURICIO
	
	// Dados do LUCAS
	

	public Cell( int x , int y ) {
		this.position.x = x;
		this.position.y = y;
		this.type = CellType.CLEAN;
	}
	

	
	public boolean isSameOf( Cell cell ) {
		if( this.position.x == cell.position.x && this.position.y == cell.position.y ) {
			return true;
		} else {
			return false;
		}
	}
	
	public void cleanASData() {
		this.ASData = new AStarData();
	}
	
	public int getManDistTo( Cell cellTo ) {
		int dist;
		dist = Math.abs( this.position.x - cellTo.position.x ) + Math.abs( this.position.y + cellTo.position.y );
		return dist;
	}
}
