package prolog;

import dataTypes.CellType;

// Classe que faz tradução de alguns termos entre JAVA , PROLOG e HUMANO
public class Translations {
	
	public static String getGameAICommandString( Commands cmd ) {
		String msg = "";
		switch( cmd ) {
		case TURN_RIGHT:
			msg = "turn_right";
			break;
		case TURN_LEFT:
			msg = "turn_left";
			break;
		case MOVE_FORWARD:
			msg = "move_forward";
			break;
		case MOVE_BACKWARD:
			msg = "move_backward";
			break;			
		case PICKUP:
			msg = "pickup";
			break;
		case FIRE:
			msg = "fire";
			break;
		case EXIT:
			msg = "exit";
			break;
		}
		
		return msg;
	}
	
	
	public static String getPrologCellTypeString( CellType type ) {
		String msg = "";
		
		switch( type ) {
		
			case LANDMINE:
				msg = "buraco";
				break;
				
			case CYCLONE:
				msg = "teletransporte";
				break;
				
			case WATER:
				msg = "ouro";
				break;
				
			case GAS:
				msg = "power_up";
				break;
				
			case BOSS:
				msg = "inimigo(50,100)";
				break;
			
			case ENEMY:
				msg = "inimigo(20,100)";
				break;
				
			default:
				msg = null;
				break;
		}
		
		return msg;
	}
	
	public static CellType getJavaCellType( String prologString ) {
		CellType type;
		
		switch( prologString ) {
			case "parede":
				type = CellType.WALL;
				break;
		
			case "buraco":
				type = CellType.LANDMINE;
				break;
				
			case "teleporte":
				type = CellType.CYCLONE;
				break;
				
			case "ouro":
				type = CellType.WATER;
				break;
				
			case "power_up":
				type = CellType.GAS;
				break;
				
			default:
				type = CellType.CLEAN;
				break;
		}
		
		return type;
	}	
	
	public static String getPrologDirectionString( int dir ) {
		String msg = "";
		
		switch( dir ) {
		
			case 3:
				msg = "cima";
				break;
				
			case 2:
				msg = "direita";
				break;

			case 1:
				msg = "baixo";
				break;
				
			case 4:
				msg = "esquerda";
				break;
		}
		
		return msg;
	}
	
	public static int getJavaDirection( String str ) {
		switch( str ) {
		case "baixo":
			return 1;
		case "direita":
			return 2;
		case "cima":
			return 3;
		case "esquerda":
			return 4;
		}
		
		return 1;
	}
	
}
