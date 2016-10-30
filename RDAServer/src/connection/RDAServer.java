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
	
	public void run()
	{
		try {
			mListenSocket = new ServerSocket(mPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		while(true)
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
				e.printStackTrace();
				return;
			}

		}
		
	}
	
}
