package cmp;

enum CmpState {
	INITIALIZING (0),
	INACTIVE (1),
	STARTING (2),
	RUNNING (3),
	SUSPENDED (4),
	STOPPING (5);
	
	private int val;
	
	CmpState( int v)
	{
		val = v;
	}
	
	public int getValue()
	{
		return val;
	}
	
}
