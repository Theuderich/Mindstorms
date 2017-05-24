package cmp.mgmt;

import rda.Items.TaskCtrl;

public class CmpBase extends Thread {

	private TaskCtrl rdaCtrl;
	
	public CmpBase( int threadId)
	{
		
		rdaCtrl = new TaskCtrl( threadId, this );
		rdaCtrl.setState(CmpState.INITIALIZING);
		
		CmpManager.getInstance().register( this );

		rdaCtrl.setState( CmpState.IDLE );
	}

	public void onStateChangeRequest( )
	{
		if( rdaCtrl.getState() == CmpState.STARTING )
			start();
		else if( rdaCtrl.getState() == CmpState.SUSPENDED )
		{
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		else if( rdaCtrl.getState() == CmpState.RESUME )
		{
//			notify();
		}
	}
	
	public void terminate()
	{
		rdaCtrl.setState( CmpState.STOPPING );
	}
	
	public void taskDone()
	{
		rdaCtrl.setState( CmpState.DONE );
	}
	
	@Override
	public void start()
	{
		rdaCtrl.setState( CmpState.STARTING );
		super.start();
		rdaCtrl.setState( CmpState.RUNNING );
	}

	public boolean isRunning()
	{
		return (rdaCtrl.getState() == CmpState.RUNNING) ||
				(rdaCtrl.getState() == CmpState.SUSPENDED) ||
				(rdaCtrl.getState() == CmpState.RESUME);
	}
	
}