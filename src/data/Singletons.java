package data;

import java.awt.Color;

import dataTypes.*;

public class Singletons {
	public static int		debugLevel 			= 5;
	
	// Color Control
	public static Glow		botColor		= new Glow( 198 , 255 , 0 , 0.5 );
	public static boolean	glowing			= false;
	
	// Map Size
	public static IVector2D gridSize		= new IVector2D( 59 , 34 );
	public static int		maxAStar		= 5;
	
	// Sync with PROLOG
	public static Grid 		gameGrid;
	public static int 		heroLife		= 100;
	public static int		heroScore		= 0;
	public static IVector2D	heroPosition	= new IVector2D( 0, 0 );
	public static int		heroDirection	= 1;
	
	
	static public void endProlog() {
	}
}
