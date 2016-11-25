package com.mydomain;

import cmp.CmpBase;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SumoEyesSensor;
import rda.Items.Item32;

public class SensorMon extends CmpBase {

	Item32 val1 = new Item32(0x1234);
	Item32 val2 = new Item32(0x1235);

	SumoEyesSensor sensor;
	
	SensorMon()
	{
		super(0x100);
		val1.set(0xDEADBEEF);
		val2.set(0x1CEC001);
		sensor = new SumoEyesSensor( SensorPort.S2 );
	}
	
	@Override
	public void run()
	{
		System.out.println("Started");
		while ( isRunning() )
		{
//			System.out.println("Run");
			val1.set( sensor.getObstacle() );
			val2.set( sensor.getValue());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
