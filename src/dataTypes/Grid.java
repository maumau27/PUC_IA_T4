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
	
	public void calculateClustering() {
		ArrayList<Cell> unclustered = new ArrayList<Cell>();
		ArrayList<Cell> clustered = new ArrayList<Cell>();
		Cell cell = null;
		
		// Get All Frontier Cells into a list
		for( int y = 0 ; y < rows ; y++ ) {
			ArrayList<Cell> newRow = this.cells.get(y);
			for( int x = 0 ; x < cols ; x++ ) {
				cell = newRow.get(x);
				if( cell.frontier == true ) {
					cell.clusterWeight = 0;
					cell.cluster = new ArrayList<Cell>();
					cell.cluster.add( cell );
					unclustered.add( cell );	
				}
			}	
		}
		
		
		// Clusterize Adjacent Cells 
		ArrayList<Cell> neigh = null;
		while( unclustered.size() > 0 ) {
			cell = unclustered.get(0);
			
			unclustered.remove( cell );
			clustered.add( cell );

			neigh = this.getProximity( cell.position.x , cell.position.y , 1 , true );

			for( Cell c : neigh ) {
				if( unclustered.contains( c ) ) {
					unclustered.remove(c);
					clustered.add( c );
				}
				Grid.mergeCluster( cell , c );
			}
		}
		
		
		// Group Unique Clusters
		ArrayList<ArrayList<Cell>> semiClusters = new ArrayList<ArrayList<Cell>>();
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		for( Cell c : clustered ) {
			if( semiClusters.contains( c.cluster ) == false ) {
				semiClusters.add( c.cluster );
				clusters.add( new Cluster( c.cluster ) );
			}
		}
		if( semiClusters.size() == 0 ) {
			return;
		}
		
		
		// D: Calculate Values
		// D.1: Pairs of clusters
		ArrayList<ClusterPair> pairs = new ArrayList<ClusterPair>();
		for( int i = 0 ; i < clusters.size() ; i++ ) {
			for( int j = i+1 ; j < clusters.size() ; j++ ) {
				pairs.add( new ClusterPair( clusters.get(i) , clusters.get(j) ) );
			}
		}
		// D.2: Update weights based on distance
		int minDist;
		for( ClusterPair pair : pairs ) {
			minDist = pair.getMinDist();
			
			pair.cluster1.finalValue += ( pair.cluster2.clusterSize / minDist );
			pair.cluster2.finalValue += ( pair.cluster1.clusterSize / minDist );
		}
		
		
		// Transfer Cluster Info to its Cells
		for( Cluster cluster : clusters ) {
			for( Cell c : cluster.cells ) {
				c.clusterWeight = cluster.finalValue;
				
				//System.out.println("Cluster: " + c.position.x + " , " + c.position.y + " | " + cluster.finalValue );
			}
		}
	}
	
	
	private static void mergeCluster( Cell cellA , Cell cellB ) {
		if( cellA.cluster == cellB.cluster ) {
			return;
		}
		
		Cell greaterCluster = cellA;
		Cell smallerCluster = cellB;
		
		// TODO: Find the real smaller cluster
		
		// Merge clusters
		greaterCluster.cluster.addAll( smallerCluster.cluster );
		for( Cell cel : smallerCluster.cluster ) {
			cel.cluster = greaterCluster.cluster;
		}
		
		smallerCluster.cluster = greaterCluster.cluster;
	}
}
