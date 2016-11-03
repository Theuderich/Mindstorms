package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class RDAServer extends Thread {
	
	ServerSocket mListenSocket;
	int mPort;
	
	public RDAServer(int port)
	{
		mPort = port;
	}

	private volatile boolean running = true;
	
	public void startServer()
	{
		try {
			mListenSocket = new ServerSocket(mPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
		System.out.println("Starting RDA Server");
		running = true;
		super.start();
	}
	
	public void stopServer()
	{
		System.out.println("Stoping RDA Server");
		running = false;
		try {
			mListenSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(running)
		{
			Socket connectionSocket = null;
			try {
			    System.out.println( "Listening for Clients" );
				connectionSocket = mListenSocket.accept();
				ClientThread c;
				c = new ClientThread(connectionSocket);
				c.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(running)
					e.printStackTrace();
				return;
			}

		}
		
	}
	
}
