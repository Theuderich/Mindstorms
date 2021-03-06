package com.mydomain;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.Port;

import com.pkg.move.Driver;

import cmp.mgmt.CmpManager;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
import rda.basics.RDAHandler;
import rda.connection.RDAServer;
import rda.basics.*;

//jrun -jar HelloWorld.jar

public class HelloWorld {

	
    static int mPort = 10002;
    private static EV3ColorSensor colorSensor;

	
	public static void main(String[] args) throws IOException {

		// initialization of Driver Class
		Driver.getInstance();
		
		
	    // first initializes the RDA Item Management
	    RDAItemList.getInstance().init( 199 );

	    RDAHandler.getInstance().start();
	    RDAServer server = new RDAServer( mPort );

		Driver.getInstance().setSpeed( 400 );
		Driver.getInstance().setAcceleration( 800 );
		Driver.getInstance().setForward();
	    
	    
	    CmpManager.getInstance();
	    SumoEyeMon thread = new SumoEyeMon();
	    AdminDataMon admin = new AdminDataMon();
//	    Driving drive = new Driving();
	    ColorSensing color = new ColorSensing();
	    CmpManager.getInstance().runAll();
	    
		// TODO Auto-generated method stub
		System.out.println("Starting ...");
	    server.start();
		Delay.msDelay(100);
	    Button.waitForAnyPress();

	    CmpManager.getInstance().terminateAll();
	    server.terminate();
	    RDAHandler.getInstance().terminate();
	    
//		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
//		final SensorMode colorRGBSensor = colorSensor.getRGBMode();
//		int sampleSize = colorRGBSensor.sampleSize();            
//		float[] sample = new float[sampleSize];


		
//		Motor.A.setSpeed(720);// 2 RPM
//		Motor.B.setSpeed(720);
//		Motor.A.forward();
//		Motor.B.forward();
//		System.out.println("Running ...");
//		Delay.msDelay(500);
//		
//		System.out.println("Stopping ...");
//		Delay.msDelay(100);
//		Motor.A.stop();
//		Motor.B.stop();
//		
//		Delay.msDelay(100);
		
	}

}
