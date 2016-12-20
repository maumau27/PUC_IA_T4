package dataTypes;

import java.util.ArrayList;

import data.Singletons;


class Cluster {
	public ArrayList<Cell> cells;
	public double clusterSize;
	public double finalValue;
	
	public Cluster( ArrayList<Cell> cellCluster ) {
		this.cells = cellCluster;
		this.clusterSize = cellCluster.size();
		this.finalValue = cellCluster.size();
	}
}


class ClusterPair {
	public Cluster cluster1;
	public Cluster cluster2;
	
	public ClusterPair( Cluster cluster1 , Cluster cluster2 ) {
			this.cluster1 = cluster1;
			this.cluster2 = cluster2;
	}
	
	public boolean isEqual( ClusterPair compareTo ) {
		if( this.cluster1 == compareTo.cluster1 && this.cluster2 == compareTo.cluster2 ) return true;
		if( this.cluster1 == compareTo.cluster2 && this.cluster2 == compareTo.cluster1 ) return true;
		
		return false;
	}
	
	public int getMinDist() {
		int 	minDist = 0;
		int 	tempDist = 0;
		boolean flag = false;
		
		for( Cell c1 : cluster1.cells ) {
			for( Cell c2 : cluster2.cells ) {
				tempDist = c1.getManDistTo( c2 );
				
				if( flag == false || tempDist < minDist ) {
					minDist = tempDist;
				}
			}
		}
		
		return minDist;
	}

}




public class Grid {
	public ArrayList<ArrayList<Cell>> cells;
	
	public int rows;
	public int cols;
	
	public int waterQtd = 0;
	
	public IVector2D cellDimensions = new IVector2D( 20 , 20 );
	
	public Grid( int rows , int cols ) {
		this.cells = new ArrayList<ArrayList<Cell>>();
		this.rows = rows;
		this.cols = cols;
		
		for( int y = 0 ; y < rows ; y++ ) {
			ArrayList<Cell> newRow = new ArrayList<Cell>();
			cells.add( newRow );
			for( int x = 0 ; x < cols ; x++ ) {
				newRow.add( new Cell( x , y ) );
			}	
		}
	}
	
	public Grid( ArrayList<ArrayList<Cell>> cells ) {
		this.cells = cells;
		this.rows = cells.size();
		this.cols = cells.get(0).size();
	}
	
	public boolean isInGrid( int x , int y ) {
		if( x < 0 ) {
			return false;
		}
		if( x >= this.cols ) {
			return false;
		}
		if( y < 0 ) {
			return false;
		}
		if( y >= this.rows ) {
			return false;
		}
		return true;
	}
	
	public Cell getCell( int x , int y ) {
		if( this.isInGrid(x, y) == false ) {
			return null;
		}
		
		return this.cells.get(y).get(x);
	}
	
	public ArrayList<Cell> getNeighbors( int x , int y ) {
		ArrayList<Cell> neighbors = new ArrayList<Cell>();

		Cell center = this.getCell( x , y );
		if( center == null ) {
			return neighbors;
		}
		
		Cell nei;
		
		nei = this.getCell(x+1, y);
		if( nei != null ) {
			neighbors.add( nei );
		}
		
		nei = this.getCell(x-1, y);
		if( nei != null ) {
			neighbors.add( nei );
		}
		
		nei = this.getCell(x, y+1);
		if( nei != null ) {
			neighbors.add( nei );
		}

		nei = this.getCell(x, y-1);
		if( nei != null ) {
			neighbors.add( nei );
		}		
		
		return neighbors;
	}
	
	public ArrayList<Cell> getProximity( int x , int y , int dist , boolean frontier ) {
		ArrayList<Cell> neighbors = new ArrayList<Cell>();

		Cell center = this.getCell( x , y );
		if( center == null ) {
			return neighbors;
		}
		
		Cell nei;
		
		int manDist = 0;
		for( int tx = x - dist ; tx <= x+dist ; tx++ ) {
			for( int ty = y - dist ; ty <= y+dist ; ty++ ) {
				manDist = Math.abs( ty - y ) + Math.abs( tx - x );
				if( manDist > dist || manDist <= 0 ) continue;

				nei = this.getCell(tx,ty);
				if( nei != null && nei.frontier == frontier ) {
					neighbors.add( nei );
				}
			}
		}
	
		return neighbors;
	}
	
	
	public void resetAStarData( ) {
		for( int y = 0 ; y < rows ; y++ ) {
			ArrayList<Cell> newRow = this.cells.get(y);
			for( int x = 0 ; x < cols ; x++ ) {
				newRow.get(x).cleanASData();
			}	
		}
	}
	

	public void printGrid() {
		System.out.print("\nGRID\n");
		for( int y = 0 ; y < rows ; y++ ) {
			ArrayList<Cell> newRow = this.cells.get(y);
			System.out.print("\n");
			for( int x = 0 ; x < cols ; x++ ) {
				Cell cell = newRow.get(x);
				if( cell.discovered == true ) {
					String mark;
					
					switch( cell.type ) {
					case CLEAN:
						mark = "-";
						break;
					
					case WALL:
						mark = "#";
						break;
						
						
					default:
						mark = "?";
						break;
						
					}
					
					System.out.print(mark);
				}
				else if ( cell.frontier == true ) {
					System.out.print("O");
				}
				else {
					System.out.print(".");
				}
			}	
		}
	}
	
}
