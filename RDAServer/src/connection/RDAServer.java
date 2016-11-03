package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class RDAServer extends Thread {

	/*************************************************************************
	 * Instance Variables
	 */

	private ServerSocket mListenSocket;
	private int mPort;

	/*************************************************************************
	 * Construction of server instance
	 */

	/**
	 * Constructor
	 * @param port - port number of listening socket
	 */
	public RDAServer(int port)
	{
		mPort = port;
	}

	/*************************************************************************
	 * Thread control
	 */

	/**
	 * boolean variable to signal the run function a termination request
	 */
	private volatile boolean running = true;
	
	/**
	 * Method to start the TCP server thread
	 * implmented in run()
	 */
	@Override
	public void start()
	{
		try {
			mListenSocket = new ServerSocket(mPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		running = true;
		super.start();
	}
	
	/**
	 * Request the TCP Server thread to terminate.
	 * The listening socked is being closed which raises an exception
	 * In cases where the running variable is set to false this exception will not be processed 
	 */
	public void terminate()
	{

		running = false;
		
		try {
			mListenSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*************************************************************************
	 * Thread function
	 */

	/**
	 * Function which runs in the own thread environment.
	 * Once a client connects to the server listening socket an new client thread is being created
	 * From the server point of view the client connection is processed in a new port.
	 * the server listening port is always ready for new connection requests. 
	 */
	public void run()
	{
		while(running)
		{
			Socket connectionSocket = null;
			try {
			    
				// Waiting for clients to connect to the server.
				connectionSocket = mListenSocket.accept();
				
				// For every new connection a new client thread is being created
				ClientThread c = new ClientThread(connectionSocket);
				c.start();
				
			} catch (IOException e) {
				
				// print the exception only if no termination is requested
				if(running)
				{
					e.printStackTrace();
				}
				return;
			}

		}
		
	}
	
}
