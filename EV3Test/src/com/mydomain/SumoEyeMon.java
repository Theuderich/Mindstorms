package com.mydomain;

import cmp.CmpBase;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SumoEyesSensor;
import rda.Items.Item32;

public class SumoEyeMon extends CmpBase {

	Item32 obstacle = new Item32( RdaIds.RDA_SUMOEYE_OBSTACLE );
	Item32 rawValue = new Item32( RdaIds.RDA_SUMOEYE_RAWVALUE );
	Item32 isLongRange = new Item32( RdaIds.RDA_SUMOEYE_ISLONGRANGE );

	SumoEyesSensor sensor;
	
	SumoEyeMon()
	{
		super(RdaIds.RDA_SUMOEYE_TASK);
		sensor = new SumoEyesSensor( SensorPort.S2 );
	}
	
	@Override
	public void run()
	{
		System.out.println("Started");
		while ( isRunning() )
		{
			
			// Monitoring
			obstacle.set( sensor.getObstacle() );
			rawValue.set( sensor.getValue());
			isLongRange.set( sensor.isLongRange() ? 1:0 );
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
