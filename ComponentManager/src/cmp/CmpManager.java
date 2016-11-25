package cmp;

import java.util.ArrayList;
import java.util.List;

public class CmpManager {

	/*************************************************************************
	 * Singleton Implementation
	 */

	private static CmpManager instance;

	private CmpManager () 
	{
		cmpList = new ArrayList<CmpBase>();
	}

	/**
	 * Constructs and return the singleton object
	 * @return - the singleton object
	 */
	public static synchronized CmpManager getInstance () 
	{
		if (CmpManager.instance == null) 
		{
			CmpManager.instance = new CmpManager();
	    }
		return CmpManager.instance;
	}
	
	/*************************************************************************
	 * Component List
	 */
	private List<CmpBase> cmpList;
	
	public void register( CmpBase cmp )
	{
		cmpList.add( cmp );
	}
	
	/*************************************************************************
	 * Component Management
	 */
	
	public void terminateAll()
	{
		for( CmpBase cmp : cmpList )
		{
			cmp.terminate();
		}
	}
	
	public void runAll()
	{
		for( CmpBase cmp : cmpList )
		{
			cmp.start();
		}
	}
}
