package connection;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import rda.basics.RDABuffer;
import rda.basics.RDAHandler;



public class ClientThread extends Thread {

	/**************************************************************************
	 * Construction 
	 */
	
	/**
	 * Constructor of the client thread
	 * @param s - communication socket to the client ot of the server listening
	 * @throws IOException - forward creation exceptions to the server thread
	 */
	public ClientThread(Socket s) throws IOException
	{
		mSocket = s;
		mInputStream = mSocket.getInputStream();
		mOutputStream = mSocket.getOutputStream();
		mBufferedOutputStream = new BufferedOutputStream(mOutputStream);
		clearRequestBuffer();
	}
	
	/**************************************************************************
	 * Network communication 
	 */
	private Socket mSocket;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private BufferedOutputStream mBufferedOutputStream;
	
	/**************************************************************************
     * read and reply buffer allocated individually for each client instance
     */
	public RDABuffer request = new RDABuffer();
	public RDABuffer reply = new RDABuffer();
	
	
    /**
     * Sets the whole request buffer content to 0 and resets the buffer size variable 
     */
	private void clearRequestBuffer()
	{
		request.clear();
	}
	
    /**
     * Sets the whole reply buffer content to 0 and resets the buffer size variable 
     */
	private void clearReplyBuffer()
	{
		reply.clear();		
	}

	/**************************************************************************
	 * Send generated reply
	 */
	
	/**
	 * In this function the generated reply from the RDA Handler is being send to the client.
	 * @param data - pointer to the reply data buffer from the handler in words (U16)
	 * @param size - number of replay data word (U16)
	 */
	public void sendReply()
	{
		// transmit the reply buffer to the client
		
		for(int i=0; i<reply.size; i++)
			System.out.println(String.format("Byte: 0x%02X", reply.buffer[i]));
		
		try {
			mBufferedOutputStream.write(reply.buffer, 0, reply.size);
			mBufferedOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    
	/**************************************************************************
	 * Thread run function
	 */
	
	/**
	 * Client thread function which runs in an own thread.
	 * All received request data from the client are being forwarded to the RDA Handler.
	 * In cases the RDA Handle is busy, the client thread delay some time and no further request are being processed.
	 */
	public void run()
	{
		while(mSocket.isConnected() && !mSocket.isClosed() && mSocket.isBound() )
		{
			
			try {
				
				// Reading from the socket till data are available.
				request.size = mInputStream.read(request.buffer, 0, request.max);
				
				// If data are read from the socket successfully.
				if( request.size > 0 )
				{
					// Prepare to allocate the RDA Handler.
					// In cases of status is false, the RDA Handler is busy.
					boolean status = false;
					while( status != true )
					{
						
						// Request the RDA Handler to take over the received request data.
						status = RDAHandler.getInstance().allocateHandler( this );
						
						// In cases the RDA Handler is busy, wait some time. 
						if( status == false )
							Thread.sleep(100);
					}
					
				// In this case the socked has been closed by the client. 
				} else {
 	            	break;
 	            }
 	            
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("Client Closed");
	}
	
}
