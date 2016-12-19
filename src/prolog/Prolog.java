package prolog;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.jpl7.*;

public class Prolog {
	public static Prolog 		prolog = null;
	public static PrologState	state;
	
	public static Commands lastCommand;

	
	private Prolog(){
		Query query;
		query = new Query("consult", new Term[] {new Atom("T2.pl")});
		System.out.println("consult " + (query.hasSolution() ? "succeeded" : "failed"));
		
	}

	public static Query doQuery(String string ) {
		return Prolog.doQuery(string, false );
	}
	
	public static Query doQuery(String string , boolean failAllowed ) {
		Query query = new Query(string);
		if( query.hasSolution() == false && failAllowed == false ) {
			//System.out.println( "DEBUG FALSE: " + string );
		}
		return query;
	}
}
