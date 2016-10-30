package com.mydomain;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.Port;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

//jrun -jar HelloWorld.jar

public class HelloWorld {

	
	private static EV3ColorSensor colorSensor;

	
	public static void main(String[] args) throws IOException {
	    String clientSentence;
	    String capitalizedSentence;
	    ServerSocket welcomeSocket = new ServerSocket(6789);

		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		final SensorMode colorRGBSensor = colorSensor.getRGBMode();
		int sampleSize = colorRGBSensor.sampleSize();            
		float[] sample = new float[sampleSize];

		// TODO Auto-generated method stub
		System.out.println("Starting ...");
		Delay.msDelay(100);

		
		Motor.A.setSpeed(720);// 2 RPM
		Motor.B.setSpeed(720);
		Motor.A.forward();
		Motor.B.forward();
		System.out.println("Running ...");
		Delay.msDelay(500);
		
//		System.out.println(String.format("Samples: %d", sampleSize));
//		for(int i=0; i<100; i++)
//		{
//			colorRGBSensor.fetchSample(sample, 0);
//			System.out.println(String.format("Sample 0: %f", sample[0]));
//			
//		}
		
		System.out.println("Stopping ...");
		Delay.msDelay(100);
		Motor.A.stop();
		Motor.B.stop();
		
		Delay.msDelay(100);
		
	      while(true)
	         {
	            Socket connectionSocket = welcomeSocket.accept();
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	            clientSentence = inFromClient.readLine();
	            System.out.println("Received: " + clientSentence);
	            capitalizedSentence = clientSentence.toUpperCase() + '\n';
	            outToClient.writeBytes(capitalizedSentence);
	         }
	  		

	}

}
