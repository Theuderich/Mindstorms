


import cmp.mgmt.CmpBase;
import rda.Items.Item32;

public class SumoEyeMon extends CmpBase {

	Item32 obstacle = new Item32( RdaIds.RDA_SUMOEYE_OBSTACLE );
	Item32 rawValue = new Item32( RdaIds.RDA_SUMOEYE_RAWVALUE );
	Item32 isLongRange = new Item32( RdaIds.RDA_SUMOEYE_ISLONGRANGE );

	SumoEyeMon()
	{
		super(RdaIds.RDA_SUMOEYE_TASK);
	}
	
	@Override
	public void run()
	{
		while ( isRunning() )
		{
			
			// Monitoring
			
			rawValue.set( obstacle.get() );
			isLongRange.set( 0 );
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		taskDone();
	}	
}
