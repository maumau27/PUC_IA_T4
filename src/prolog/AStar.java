package prolog;

import java.util.ArrayList;

import data.Singletons;
import dataTypes.*;

public class AStar {
	public static boolean debugMode = false;
	
	public static AStarPath getPath( IVector2D Destination ) {
		return AStar.getPath( new IVector2D( Singletons.heroPosition.x , Singletons.heroPosition.y ) , Singletons.heroDirection , Destination );
	}
	
	public static AStarPath getPath( IVector2D Origin , int OriginDirection ,  IVector2D Destination ) {
		if( AStar.debugMode == true ) { 
			System.out.println( "AStar Started: Origin: " + Origin.x + " , " + Origin.y 
					+ " | Destiny: " + Destination.x + " , " + Destination.y 
					+ " | Direction: " + OriginDirection
					);
		}
		AStarPath aspath = new AStarPath();
		aspath.origin = new IVector2D( Origin.x , Origin.y );
		aspath.originDirection = OriginDirection;
		
		Grid grid = Singletons.gameGrid;
		grid.resetAStarData();
		
		Cell oCell = grid.getCell( Origin.x , Origin.y );
		Cell tCell = oCell;
		Cell dCell = grid.getCell( Destination.x , Destination.y );
		Cell cCell = null;
		
		// Starting Cell Options
		tCell.ASData.predecessor 		= null;
		tCell.ASData.direction 			= OriginDirection;
		tCell.ASData.costToEnter 		= 0;
		tCell.ASData.accumulatedCost 	= 0;
		tCell.ASData.type				= 1;
		
		// Build Path
		ArrayList<Cell> frontier = new ArrayList<Cell>();
		while( tCell != dCell ) {
			if( AStar.debugMode == true ) { 
				System.out.println( "AStar Position: Origin: " + tCell.position.x + " , " + tCell.position.y + " | Direction: " + tCell.ASData.direction );
			}
			AStar.addFrontier( frontier , tCell , dCell );
			if( frontier.size() < 1 ) break;
			tCell = AStar.getBestCandidate( frontier );
		}
		
		// Check if pathExists
		if( tCell != dCell ) {
			return null;
		}
		
		// Build Path List
		aspath.destiny = new IVector2D( tCell.position.x , tCell.position.y );
		aspath.destinyDirection = tCell.ASData.direction;
		aspath.clusterCellsCleared = 0;
		while( tCell != null ) {
			if( tCell.cluster == oCell.cluster || tCell.cluster == dCell.cluster ) {
				aspath.clusterCellsCleared++;
			}
			
			if( tCell.frontier == true ) {
				aspath.cellsDiscovered++;
			}
			aspath.cellsWalked++;
			
			aspath.cellList.add( 0 , tCell );
			tCell = tCell.ASData.predecessor;
		}
		
		// Build Command List
		for( int i = 0 ; i < (aspath.cellList.size()-1) ; i++ ) {
			// Find the needed commands
			cCell = aspath.cellList.get(i+1);
			tCell = cCell.ASData.predecessor;
			
			aspath.cost++;
			
			for( int j = 0 ; j < AStar.getTurnCost( tCell.ASData.direction , cCell.ASData.direction ) ; j++ ) {
				aspath.commandList.add( Commands.TURN );
				aspath.cost++;
			}
			aspath.commandList.add( Commands.MOVE );
		}
		
		
		return aspath;
	}
	
	
	
	private static void addFrontier( ArrayList<Cell> frontier , Cell center , Cell destiny ) {
		Grid grid = Singletons.gameGrid;
		
		if( center == null ) {
			return;
		}
		int x = center.position.x;
		int y = center.position.y;
		
		Cell nei;

		nei = grid.getCell(x+1, y);
		AStar.addCellToList( frontier , nei , center , destiny , 2 );
		
		nei = grid.getCell(x-1, y);
		AStar.addCellToList( frontier , nei , center , destiny , 4 );
		
		nei = grid.getCell(x, y+1);
		AStar.addCellToList( frontier , nei , center , destiny , 1 );

		nei = grid.getCell(x, y-1);
		AStar.addCellToList( frontier , nei , center , destiny , 3 );
	}
	
	private static int getTurnCost( int heroDirection , int destinationDirection ) {
		if( destinationDirection < heroDirection  ) {
			destinationDirection += 4;
		}
		return ( destinationDirection - heroDirection );
	}
	
	private static int getManhattan( IVector2D origin , IVector2D destiny ) {
		return ( Math.abs( origin.x - destiny.x ) + Math.abs( origin.y - destiny.y ) );
	}
	
	private static void addCellToList( ArrayList<Cell> list , Cell cell , Cell predecessor , Cell finalDestination , int cellRelativeDirection ) {
		// Cells to avoid:
		if( cell == null || cell.ASData.type != 0 ) return;
 		if( cell.discovered == false && cell.frontier == false && cell != finalDestination ) return;
		if( cell.destroyed == false ) {
			if( cell != finalDestination ) {
				switch( cell.type ) {
					case BOSS:
					case ENEMY:
					case CYCLONE:
					case LANDMINE:
						return;
				}
			}
		}
		
		
		

		// Calculate new cell data
		int tCost = AStar.getTurnCost( predecessor.ASData.direction , cellRelativeDirection );
		int goalEstimative = AStar.getManhattan( cell.position , finalDestination.position );
		
		// Cell new Specs
		cell.ASData.predecessor = predecessor;
		cell.ASData.costToEnter = tCost + 1;
		cell.ASData.accumulatedCost = predecessor.ASData.accumulatedCost + cell.ASData.costToEnter;
		cell.ASData.heuristicFinalCost = cell.ASData.accumulatedCost + goalEstimative + ( tCost * 0 );
		cell.ASData.type = 2;	
		cell.ASData.direction = cellRelativeDirection;
		
		if( AStar.debugMode == true ) { 
			System.out.println( "ASCell: " + cell.position.x + "," +  cell.position.y + " | " + cell.ASData.costToEnter + " , " + cell.ASData.heuristicFinalCost );
		}
		
		list.add( cell );
	}
	
	private static Cell getBestCandidate( ArrayList<Cell> list ) {
		Cell best = list.get(0);

		for( Cell cell : list ) {
			if( cell.ASData.heuristicFinalCost == best.ASData.heuristicFinalCost ) {
				if( best.frontier == false  && cell.frontier == true ) {
					best = cell;
				}
			} else if( cell.ASData.heuristicFinalCost < best.ASData.heuristicFinalCost ) {
				best = cell;
			}
		}
		
		list.remove( best );
		
		return best;
	}
 } 
