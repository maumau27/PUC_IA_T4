package prolog;

import java.util.ArrayList;

import dataTypes.*;

// Classe de estrutura de Dados
public class AStarPath {
	public ArrayList<Cell>		cellList 		= new ArrayList<Cell>();
	public ArrayList<Commands>	commandList		= new ArrayList<Commands>();
	
	public IVector2D	origin = new IVector2D(0,0);
	public int			originDirection = 0;
	
	public IVector2D	destiny = new IVector2D(0,0);	
	public int			destinyDirection = 0;
	
	public double 		cost = 0;
	
	public int 			cellsDiscovered;
	public int			cellsWalked;
	
	
	public void addToPath( AStarPath aPath ) {
		this.cellList.addAll( aPath.cellList );
		this.commandList.addAll( aPath.commandList );
		
		this.destiny 			= aPath.destiny;
		this.destinyDirection	= aPath.destinyDirection;
		
		this.cost += aPath.cost;
	}
}
