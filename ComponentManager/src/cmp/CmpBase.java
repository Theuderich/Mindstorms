package cmp;

import rda.Items.Item32;

public class CmpBase extends Thread {

	public CmpState state;
	private Item32 rdaState;
	
	public CmpBase( int threadId)
	{
		state = CmpState.INITIALIZING;
		rdaState = new Item32( threadId );
		rdaState.set(state.getValue());
		
		CmpManager.getInstance().register( this );

		setState( CmpState.INACTIVE );
	}

	private void setState( CmpState newState )
	{
		switch (state)
		{
		case INITIALIZING:
			if ( newState == CmpState.INACTIVE )
				state = newState;
			break;
		
		case INACTIVE:
			if ( newState == CmpState.STARTING )
				state = newState;
			break;
		
		case STARTING:
			if ( newState == CmpState.INACTIVE ||  newState == CmpState.RUNNING)
				state = newState;
			break;
		
		case RUNNING:
			if ( newState == CmpState.STOPPING || newState == CmpState.SUSPENDED )
				state = newState;
			break;
		
		case SUSPENDED:
			if ( newState == CmpState.RUNNING || newState == CmpState.STOPPING)
				state = newState;
			break;
		
		case STOPPING:
			if ( newState == CmpState.INACTIVE )
				state = newState;
			break;
		
		default:
				
		}
		rdaState.set(state.getValue());

	}
	
	public void terminate()
	{
		setState( CmpState.STOPPING );
	}
	
	@Override
	public void start()
	{
		setState( CmpState.STARTING );
		super.start();
		setState( CmpState.RUNNING );
	}

	public boolean isRunning()
	{
		return state == CmpState.RUNNING;
	}
}
