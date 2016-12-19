package data;

import dataTypes.*;

public class Singletons {
	public static int		debugLevel 			= 0;
	
	// Map Size
	public static IVector2D gridSize		= new IVector2D( 59 , 34 );
	
	// Sync with PROLOG
	public static Grid 		gameGrid;
	public static int 		heroLife		= 100;
	public static int		heroScore		= 0;
	public static IVector2D	heroPosition	= new IVector2D( 0, 0 );
	public static int		heroDirection	= 1;
	
	
	static public void endProlog() {
	}
}
