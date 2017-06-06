package com.pkg.move;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import rda.Items.Item32;
import rda.basics.RDAHandler;

public class Driver {

	RegulatedMotor left;
	RegulatedMotor right;
	
	/*************************************************************************
	 * Singleton Implementation
	 */
	private static Driver instance;

	private Driver () 
	{
		left = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("A"));
		right = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("B"));
		left.synchronizeWith( new RegulatedMotor[] {right} );
	}

	/**
	 * Constructs and return the singleton object
	 * @return - the singleton object
	 */
	public static synchronized Driver getInstance () 
	{
		if (Driver.instance == null) 
		{
			Driver.instance = new Driver();
	    }
		return Driver.instance;
	}
	
	/*************************************************************************
	 * Singleton Implementation
	 */

	public int getMaxSpeedLeft()
	{
		return (int) (left.getMaxSpeed() * 100);
	}
	
	public int getMaxSpeedRight()
	{
		return (int) (right.getMaxSpeed() * 100);
	}
	
	
	public void setSpeed( int speed )
	{
		left.startSynchronization();
		left.setSpeed(speed);
		right.setSpeed(speed);
		left.endSynchronization();
		left.waitComplete();
		right.waitComplete();
	}
	
	public void setAcceleration( int acceleration )
	{
		left.startSynchronization();
		left.setAcceleration(acceleration);
		right.setAcceleration(acceleration);
		left.endSynchronization();
		left.waitComplete();
		right.waitComplete();
	}

	public void setForward( )
	{
//		left.startSynchronization();
		left.forward();
		right.forward();
//		left.endSynchronization();
//		left.waitComplete();
//		right.waitComplete();
	}
	
	public void setBackward( )
	{
//		left.startSynchronization();
		left.backward();
		right.backward();
//		left.endSynchronization();
//		left.waitComplete();
//		right.waitComplete();
	}
	
	public void stop()
	{
		left.flt(true);
		right.flt(true);
	}
}
