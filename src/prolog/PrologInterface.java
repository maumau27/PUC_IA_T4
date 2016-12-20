package prolog;

import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;

import data.Singletons;
import dataTypes.Cell;
import prolog.Translations;

abstract public class PrologInterface {

	public static void updateFromProlog( UpdateTypes updateType ) {
		Query query;
		Map<String, Term>[] solution;
		int x , y , w , z , o;
		
		switch( updateType ) {
			case ALL:
				PrologInterface.updateFromProlog( UpdateTypes.POSITION );
				PrologInterface.updateFromProlog( UpdateTypes.MAP );
				break;
		
			case POSITION:
				// Posicao
				query = MyProlog.doQuery("posicao(X,Y)" );			
				if( query.hasSolution() ) {
					solution = query.allSolutions();
					x = java.lang.Integer.parseInt( String.valueOf(solution[0].get("X")) );
					y = java.lang.Integer.parseInt( String.valueOf(solution[0].get("Y")) );	
					
					Singletons.heroPosition.x = x;
					Singletons.heroPosition.y = y;	
				}
				
				// Direcao
				query = MyProlog.doQuery("orientacao(X)" );	
				if( query.hasSolution() ) {
					solution = query.allSolutions();
				
					Singletons.heroDirection = Translations.getJavaDirection( String.valueOf(solution[0].get("X")) );
				}
				
				break;
		
			case MAP:
				query = MyProlog.doQuery("certeza( ( X , Y ), O )" );	
				solution = query.allSolutions();
				Cell cell;
				for( int i = 0 ; i < solution.length ; i++ ) {
					x = java.lang.Integer.parseInt( String.valueOf(solution[i].get("X")) );
					y = java.lang.Integer.parseInt( String.valueOf(solution[i].get("Y")) );
					
					cell = Singletons.gameGrid.getCell(x, y);
					if( cell != null ) {
						cell.discovered = true;
						cell.frontier = false;
						cell.type = Translations.getJavaCellType(String.valueOf(solution[i].get("O")));
					}
					else {
						System.out.println("ERROR! NULL CERTEZA CELL AT: " + x +"," +y);
					}
				}	
							
				query = MyProlog.doQuery("fronteira(X,Y)" );	
				solution = query.allSolutions();
				for( int i = 0 ; i < solution.length ; i++ ) {
					x = java.lang.Integer.parseInt( String.valueOf(solution[i].get("X")) );
					y = java.lang.Integer.parseInt( String.valueOf(solution[i].get("Y")) );
					
					cell = Singletons.gameGrid.getCell(x, y);
					if( cell != null ) {
						cell.frontier = true;
					}
					else {
						System.out.println("ERROR! NULL FRONTEIRA CELL AT: " + x +"," +y);
					}
				}	
				
				query = MyProlog.doQuery("consumido(X,Y)" , true );	
				solution = query.allSolutions();
				Cell tCell;
				for( int i = 0 ; i < solution.length ; i++ ) {
					x = java.lang.Integer.parseInt( String.valueOf(solution[i].get("X")) );
					y = java.lang.Integer.parseInt( String.valueOf(solution[i].get("Y")) );
					
					cell = Singletons.gameGrid.getCell(x, y);
					if( cell != null ) {					
						tCell = Singletons.gameGrid.getCell(x, y);
						tCell.destroyed = true;
					}
					else {
						System.out.println("ERROR! NULL CELL AT: " + x +"," +y);
					}
				}	
				
				break;
			
				
		}
	}
	
	public static void printFromProlog( PrintTypes printType ) {
		Query query;
		Map<String, Term>[] solution;
		int x , y , w , z , o;

		
		switch( printType ) {
			case ALL:
				System.out.println("Estado do Prolog:");
				PrologInterface.printFromProlog( PrintTypes.POSITION );				
				PrologInterface.printFromProlog( PrintTypes.SENSORS );
				PrologInterface.printFromProlog( PrintTypes.DOUBTS );
				PrologInterface.printFromProlog( PrintTypes.KNOWN );					
				break;
		
			case SENSORS:
				query = MyProlog.doQuery("sensor( (X,Y), (W,Z), O )" );	
				solution = query.allSolutions();
				for( int i = 0 ; i < solution.length ; i++ ) {
					System.out.println("Sensor: " 
							+ String.valueOf(solution[i].get("X")) + " | " + String.valueOf(solution[i].get("Y")) + " | "
							+ String.valueOf(solution[i].get("W")) + " | " + String.valueOf(solution[i].get("Z")) + " | "
							+ String.valueOf(solution[i].get("O")) );

				}
				break;
				
			case POSITION:
				query = MyProlog.doQuery("posicao(X,Y)" );	
				if( query.hasSolution() ) {
					solution = query.allSolutions();
					x = java.lang.Integer.parseInt( String.valueOf(solution[0].get("X")) );
					y = java.lang.Integer.parseInt( String.valueOf(solution[0].get("Y")) );	
					System.out.println( "Posicao Atual: x: " + x + " | y: " + y );
				}

				query = MyProlog.doQuery("orientacao(X)" );	
				if( query.hasSolution() ) {
					solution = query.allSolutions();
					System.out.println( "Orientacao: " + String.valueOf(solution[0].get("X"))  );
				}				
				break;
				
			case DOUBTS:
				query = MyProlog.doQuery("fronteira(X,Y)" );	
				solution = query.allSolutions();
				for( int i = 0 ; i < solution.length ; i++ ) {
					System.out.println("Fronteira: " 
							+ String.valueOf(solution[i].get("X")) + " | " + String.valueOf(solution[i].get("Y")) );
				}
				break;
				
			case KNOWN:
				query = MyProlog.doQuery("certeza( (X,Y), O )" );	
				solution = query.allSolutions();
				for( int i = 0 ; i < solution.length ; i++ ) {
					System.out.println("Certeza: " 
							+ String.valueOf(solution[i].get("X")) + " | " + String.valueOf(solution[i].get("Y")) + " | " + String.valueOf(solution[i].get("O"))  );
				}
				break;
				

		}
	}	
	
}
