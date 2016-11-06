package rda.basics;

public abstract class RDAItem {

	public RDAType type;
	
	public RDAItem(RDAType t)
	{
		type = t;
	}
	
	public void processRequest(RDABuffer request, RDABuffer reply)
	{
		
	}

	public boolean isItemId(int code)
	{
		return false;
	}
	
}
