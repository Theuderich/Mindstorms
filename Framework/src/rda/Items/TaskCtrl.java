package rda.Items;

import cmp.mgmt.CmpBase;
import cmp.mgmt.CmpState;

public class TaskCtrl extends Item32{

	private CmpBase task;
	private CmpState state;
	
	public TaskCtrl( int itemId_, CmpBase t )
	{
		super(itemId_);
		state = CmpState.INITIALIZING;
		task = t;
	}
	
	public boolean setState( CmpState newState )
	{
		
		switch (state)
		{
		case INITIALIZING:
			if ( newState == CmpState.IDLE )
				state = newState;
			break;
		
		case IDLE:
			if ( newState == CmpState.STARTING )
				state = newState;
			break;
		
		case STARTING:
			if ( newState == CmpState.RUNNING)
				state = newState;
			break;
		
		case RUNNING:
			if ( newState == CmpState.STOPPING || newState == CmpState.SUSPENDED )
				state = newState;
			break;
		
		case SUSPENDED:
			if ( newState == CmpState.RESUME || newState == CmpState.RUNNING)
				state = newState;
			break;
		
		case RESUME:
			if ( newState == CmpState.RUNNING || newState == CmpState.SUSPENDED)
				state = newState;
			break;
		
		case STOPPING:
			if ( newState == CmpState.DONE )
				state = newState;
			break;
		
		case DONE:
			break;
			
		default:
				
		}
		
		if( state == newState )
		{
			super.set( state.getValue() );
			return true;
		}
		
		return false;

	}
	
	public CmpState getState()
	{
		return state;
	}
	
	@Override
	public int get()
	{
		return super.get();
	}
	
	@Override
	public void set( int v )
	{
		if( setState(CmpState.fromInteger(v)) );
			task.onStateChangeRequest( state );
	}

	
	
}
