package com.mydomain;

import com.pkg.move.Driver;

import cmp.CmpBase;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import rda.Items.Item32;

public class ColorSensing extends CmpBase {

	Item32 colorRed = new Item32( RdaIds.RDA_COLOR_SENSOR );
	Item32 colorGreen = new Item32( RdaIds.RDA_COLOR_SENSOR );
	Item32 colorBlue = new Item32( RdaIds.RDA_COLOR_SENSOR );
	
	EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	SensorMode color = sensor.getRGBMode();
	float[] colorSample = new float[color.sampleSize()];
	
	
	ColorSensing()
	{
		super(RdaIds.RDA_COLOR_TASK);
	}	
	
	@Override
	public void run()
	{
		sensor.setFloodlight( true );
		while ( isRunning() )
		{
			color.fetchSample(colorSample, 0);

//			System.out.println( "----------" );
//			System.out.println( colorSample[0] );
//			System.out.println( colorSample[1] );
//			System.out.println( colorSample[2] );

			colorRed.set( (int)colorSample[0] * 100 );
			colorGreen.set( (int)colorSample[1] * 100 );
			colorBlue.set( (int)colorSample[2] * 100 );
			
			if( colorRed.get() == 0)
				Driver.getInstance().stop();

			if( colorGreen.get() == 0)
				Driver.getInstance().stop();
			
			if( colorBlue.get() == 0)
				Driver.getInstance().stop();

//			try {
//				sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}		
		sensor.setFloodlight( false );
	}	
}
