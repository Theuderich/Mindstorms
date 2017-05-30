package com.mydomain;

import com.pkg.move.Driver;

import cmp.CmpBase;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SumoEyesSensor;
import rda.Items.Item32;

public class Driving extends CmpBase {

	Item32 leftMaxSpeed = new Item32( RdaIds.RDA_DRIVING_MAX_SPEED_LEFT );
	Item32 rightMaxSpeed = new Item32( RdaIds.RDA_DRIVING_MAX_SPEED_RIGHT );
	
	Driving()
	{
		super(RdaIds.RDA_DRIVING_TASK);
	}	
		
	@Override
	public void run()
	{
		Driver.getInstance().setSpeed( 400 );
		Driver.getInstance().setAcceleration( 800 );
		
		while ( isRunning() )
		{
			
			leftMaxSpeed.set( Driver.getInstance().getMaxSpeedLeft() );
			rightMaxSpeed.set( Driver.getInstance().getMaxSpeedRight() );
			
			try {
				Driver.getInstance().setForward();
				sleep(1000);
				Driver.getInstance().setBackward();
				sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
