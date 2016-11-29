package com.mydomain;

import cmp.CmpBase;
import lejos.hardware.Battery;
import rda.Items.Item32;

public class AdminDataMon extends CmpBase {

	Item32 battery = new Item32( RdaIds.RDA_ADMIN_BATTERY );

	public AdminDataMon()
	{
		super(RdaIds.RDA_ADMIN_TASK);
	}
	
	@Override
	public void run()
	{
		while ( isRunning() )
		{
			battery.set( Battery.getVoltageMilliVolt() );
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}		
}
