package com.mydomain;

import cmp.CmpBase;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SumoEyesSensor;

public class Driving extends CmpBase {

	
	Driving()
	{
		super(RdaIds.RDA_DRIVING_TASK);
	}	
		
	@Override
	public void run()
	{
		while ( isRunning() )
		{
			
			try {
				System.out.println("Start Driving");
				Motor.A.forward();
				Motor.B.forward();
				sleep(500);
				Motor.A.stop();
				Motor.B.stop();
				sleep(1000);
				Motor.A.backward();
				Motor.B.backward();
				sleep(500);
				Motor.A.stop();
				Motor.B.stop();
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
