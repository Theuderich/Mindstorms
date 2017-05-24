package cmp.mgmt;

public enum CmpState {
	INITIALIZING (0),
	IDLE (1),
	STARTING (2),
	RUNNING (3),
	SUSPENDED (4),
	RESUME (5),
	STOPPING (6),
	DONE (7),
	UNDEFINED (7);
	
	private int val;
	
	CmpState( int v)
	{
		val = v;
	}
	
	public int getValue()
	{
		return val;
	}
	
	public static CmpState fromInteger( int v )
	{
		switch (v)
		{
		case 0:
			return INITIALIZING;
		case 1:
			return IDLE;
		case 2:
			return STARTING;
		case 3:
			return RUNNING;
		case 4:
			return SUSPENDED;
		case 5:
			return RESUME;
		case 6:
			return STOPPING;
		case 7:
			return DONE;
		default:
			return UNDEFINED;
		}
		
	}
	
}
