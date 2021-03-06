package INF1771_GameAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Atom;
import org.jpl7.Term;
import org.jpl7.fli.Prolog;

import INF1771_GameAI.Map.Position;

import data.*;
import prolog.*;
import dataTypes.*;


public class GameAI
{
    Position player = new Position();
    String state = "ready";
    String dir = "north";
    long score = 0;
    int energy = 0;
    boolean firstTime = true;
    
    ArrayList<Commands> commandList;
    

    public GameAI(){
        Query query;
        query = new Query("consult", new Term[] {new Atom("T4.pl")});
        System.out.println("consult " + (query.hasSolution() ? "succeeded" : "failed"));
        MyProlog.doQuery("extern_instanciar_tamanho_mapa( " + Singletons.gridSize.x + "," + Singletons.gridSize.y + ")");
        
        // Java State
        this.commandList = new ArrayList<Commands>();
        Singletons.gameGrid = new Grid( Singletons.gridSize.y , Singletons.gridSize.x );
    }
    
    /**
     * Refresh player status
     * @param x			player position x
     * @param y			player position y
     * @param dir		player direction
     * @param state		player state
     * @param score		player score
     * @param energy	player energy
     */
    public void SetStatus(int x, int y, String dir, String state, long score, int energy)
    {
        player.x = x;
        player.y = y;
        this.dir = dir.toLowerCase();

        this.state = state;
        this.score = score;
        this.energy = energy;

    	String prologDir;
    	switch( dir.toLowerCase() ) {
    	case "south":
    		prologDir = "baixo";
    		break;
    	case "west":
    		prologDir = "esquerda";
    		break;
    	case "east":
    		prologDir = "direita";
    		break;
    	default:
    		prologDir = "cima";
    		break;
    	}
    	MyProlog.doQuery("atualizar_orientacao( "+ prologDir +" )" );    
        MyProlog.doQuery("atualizar_posicao(" + player.x + "," + player.y + ")");
        MyProlog.doQuery("atualizar_energia(" + energy + ")" );
        
        PrologInterface.updateFromProlog( UpdateTypes.POSITION );
        PrologInterface.updateFromProlog( UpdateTypes.DIRECTION ); 
    }

    public String getDirection(){
        return null;
    }

    /**
     * Get list of observable adjacent positions
     * @return List of observable adjacent positions 
     */
    public List<Position> GetObservableAdjacentPositions()
    {
        List<Position> ret = new ArrayList<Position>();

        ret.add(new Position(player.x - 1, player.y));
        ret.add(new Position(player.x + 1, player.y));
        ret.add(new Position(player.x, player.y - 1));
        ret.add(new Position(player.x, player.y + 1));

        return ret;
    }

    /**
     * Get list of all adjacent positions (including diagonal)
     * @return List of all adjacent positions (including diagonal)
     */
    public List<Position> GetAllAdjacentPositions()
    {
        List<Position> ret = new ArrayList<Position>();

        ret.add(new Position(player.x - 1, player.y - 1));
        ret.add(new Position(player.x, player.y - 1));
        ret.add(new Position(player.x + 1, player.y - 1));

        ret.add(new Position(player.x - 1, player.y));
        ret.add(new Position(player.x + 1, player.y));

        ret.add(new Position(player.x - 1, player.y + 1));
        ret.add(new Position(player.x, player.y + 1));
        ret.add(new Position(player.x + 1, player.y + 1));

        return ret;
    }

    /**
     * Get next forward position
     * @return next forward position
     */
    public Position NextPosition()
    {
        Position ret = null;
        if(dir.equals("north"))
                ret = new Position(player.x, player.y - 1);
        else if(dir.equals("east"))
                ret = new Position(player.x + 1, player.y);
        else if(dir.equals("south"))
                ret = new Position(player.x, player.y + 1);
        else if(dir.equals("west"))
                ret = new Position(player.x - 1, player.y);

        return ret;
    }

    /**
     * Player position
     * @return player position
     */
    public Position GetPlayerPosition()
    {
        return player;
    }
    
    /**
     * Set player position
     * @param x		x position
     * @param y		y position
     */
    public void SetPlayerPosition(int x, int y)
    {
        player.x = x;
        player.y = y;

    }
    
    
    public void prologObservation(List<String> o) {
        boolean flag 		= false;

        MyProlog.doQuery( "observar(" +player.x +"," +player.y +")" ); 
        
        System.out.println("GameAI pos: " +player.x + "," +player.y);
        
        // Tenho Observacoes
        if( o != null ) {
            for (String s : o)
            {
                if(s.equals("blocked")){
                	Position pos = this.NextPosition();
                	if(pos.x >= 0 && pos.x < 59 &&
                			pos.y >= 0 && pos.y < 34)
                		MyProlog.doQuery("definir_certeza(" + pos.x +"," +pos.y +",parede)" );
            		flag = true;
                } else if(s.equals("steps")){

                } else if(s.equals("breeze")){
                    MyProlog.doQuery("criar_sensores_em(" + player.x + "," + player.y + "," +  "buraco)" );
                } else if(s.equals("flash")){
                    MyProlog.doQuery("criar_sensores_em(" + player.x + "," + player.y + "," +  "teleporte)" );
                } else if(s.equals("blueLight")){
                	this.commandList.clear();
					this.commandList.add(Commands.PICKUP);
                    MyProlog.doQuery("definir_certeza(" + player.x + "," + player.y + "," +  "ouro)" );
                    flag = true;
                } else if(s.equals("redLight")){
                    MyProlog.doQuery("definir_certeza(" + player.x + "," + player.y + "," +  "power_up)" );
                    flag = true;
	            } else if (s.indexOf("enemy#") > -1) {
					try{
						int steps = Integer.parseInt(s.replaceFirst("enemy#", ""));
						if(steps > 0){
							Position enemy = NextPositionAhead(steps);
						}
					}
					catch(Exception ex){
						
					}
					this.commandList.clear();
					this.commandList.add(Commands.FIRE);
				}
            }
        }
        
        
        
        // Finalize the code
        if(!flag){
            MyProlog.doQuery("definir_certeza(" + player.x + "," + player.y + "," +  "nada)" );
        }
        MyProlog.doQuery("validar_fronteiras()" );
        PrologInterface.updateFromProlog( UpdateTypes.ALL );
    }


	public Position NextPositionAhead(int steps) {
		Position ret = null;
		if (dir.equals("north"))
			ret = new Position(player.x, player.y - steps);
		else if (dir.equals("east"))
			ret = new Position(player.x + steps, player.y);
		else if (dir.equals("south"))
			ret = new Position(player.x, player.y + steps);
		else if (dir.equals("west"))
			ret = new Position(player.x - steps, player.y);

		return ret;
	}

    /**
     * Observations received
     * @param o	 list of observations
     */
    public void GetObservations(List<String> o)
    {
    	this.prologObservation(o);
    }

    /**
     * No observations received
     */
    public void GetObservationsClean()
    {
    	this.prologObservation(null);
    }

    /**
     * Get Decision
     * @return command string to new decision
     */
    public String GetDecision()
    {
    	
    	// Get Steps 
        if( this.commandList.size() == 0 ) {
            fillNextStepList();
        }

        // Do Steps
        if( this.commandList.size() > 0 ) {
            Commands cmd = commandList.get(0);

            commandList.remove(0);

            return Translations.getGameAICommandString(cmd);
        } 
        else {
        	return "";
        }
    }

    public void fillNextStepList(){
        Query query;
        Map<String, Term>[] solution;
        int x , y , o;
        
        ArrayList<PrologCellDecidions> cells = new ArrayList<PrologCellDecidions>();        

        // Get and store Prolog Solutions
        MyProlog.doQuery("decidir()" );
        query = MyProlog.doQuery("proximo_passo((X,Y),O,T)" , true ); 
        solution = query.allSolutions();
        
        System.out.println("Prolog Decisions:");
        for( int i = 0 ; i < solution.length ; i++ ) {
            x = java.lang.Integer.parseInt( String.valueOf(solution[i].get("X")) );
            y = java.lang.Integer.parseInt( String.valueOf(solution[i].get("Y")) );
            o = java.lang.Integer.parseInt( String.valueOf(solution[i].get("O")) );
            
            cells.add( new PrologCellDecidions(x,y,o,String.valueOf(solution[i].get("T"))) );
            
            
            System.out.println( "Prolog CMD: x: " + x + " | y: " + y + " | T : " + String.valueOf(solution[i].get("T")) );
        }
        
        
        // Get paths to cells
        ArrayList<AStarPath> pathList = new ArrayList<AStarPath>();
        AStarPath       newPath = null;

        boolean repeatFlag = false;
        for( PrologCellDecidions cell : cells ) {
            switch( cell.cmd ) {                
                case MOVE_FORWARD:
                case FIRE:
                case EXIT:
                    if( cell.x == Singletons.heroPosition.x && cell.y == Singletons.heroPosition.y ) {
                        newPath = null;
                        newPath = AStar.getPath( new IVector2D(cell.x,cell.y));
                    } else {
                        newPath = AStar.getPath( new IVector2D(cell.x,cell.y));
                    }
                    
                    if( newPath != null ) {
                        pathList.add(newPath);  
                    }
                    
                    if( cell.cmd == Commands.FIRE ) {
                        newPath.commandList.remove( newPath.commandList.size() - 1 );
                        newPath.commandList.add( Commands.FIRE );
                    } 
                    else if( cell.cmd == Commands.EXIT ) {
                        newPath.commandList.add( Commands.EXIT );
                    }
                    break;  
                    
                    
                case REPEATLAST:
                    repeatFlag = true;
                    break;
            }
        }
    
        // Check If AStar have a path
        if( pathList.size() == 0 ) {
        	this.commandList.clear();
        }
        else {     
	        // Find Best Path
	        AStarPath   bestPathRaw = pathList.get(0);
	        pathList.remove(0);       
	        for( AStarPath aPath : pathList ) {
	            if( aPath.cost >= bestPathRaw.cost ) {
	            	continue;
	            }
	            else {
	            	 bestPathRaw = aPath;
	            }
	        }
	        
	        System.out.println("AStar Decisions:");
	        System.out.println( bestPathRaw.origin.x + " , " + bestPathRaw.origin.y + " | " + bestPathRaw.destiny.x + " , " + bestPathRaw.destiny.y );
	        
	        this.commandList = bestPathRaw.commandList;  
        }
    }
}
