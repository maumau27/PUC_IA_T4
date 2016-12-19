package prolog;

import dataTypes.CellType;

// Classe que faz tradução de alguns termos entre JAVA , PROLOG e HUMANO
public class Translations {
	public static String getCommandString( Commands cmd ) {
		String msg = "";
		switch( cmd ) {
		case TURN:
			msg = "TURN";
			break;
		case MOVE:
			msg = "MOVE";
			break;
		case PICKUP:
			msg = "PICK UP";
			break;
		case FIRE:
			msg = "FIRE";
			break;
		case EXIT:
			msg = "EXIT";
			break;
		}
		
		return msg;
	}
	
	
	
	public static String getPrologCommandString( Commands cmd ) {
		String msg = "";
		switch( cmd ) {
		case TURN:
			msg = "virar_a_direita";
			break;
		case MOVE:
			msg = "mover_para_frente";
			break;
		case PICKUP:
			msg = "pegar_objeto";
			break;
		case FIRE:
			msg = "atirar";
			break;
		case EXIT:
			msg = "subir";
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
		
			case "buraco":
				type = CellType.LANDMINE;
				break;
				
			case "teletransporte":
				type = CellType.CYCLONE;
				break;
				
			case "ouro":
				type = CellType.WATER;
				break;
				
			case "power_up":
				type = CellType.GAS;
				break;
				
			case "inimigo(50,100)":
				type = CellType.BOSS;
				break;
			
			case "inimigo(20,50)":
				type = CellType.ENEMY;
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
		
			case 1:
				msg = "cima";
				break;
				
			case 2:
				msg = "direita";
				break;

			case 3:
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
		case "cima":
			return 1;
		case "direita":
			return 2;
		case "baixo":
			return 3;
		case "esquerda":
			return 4;
		}
		
		return 1;
	}
	
}
