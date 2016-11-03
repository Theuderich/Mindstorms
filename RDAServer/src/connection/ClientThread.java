package connection;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
    private byte[] requestBuffer = new byte[RDAHandler.maxRequestBufferSizeWords*2];
    private int requestSize;
    private byte[] replyBuffer = new byte[RDAHandler.maxRequestBufferSizeWords*2];
    private int replySize;

    /**
     * Sets the whole request buffer content to 0 and resets the buffer size variable 
     */
	private void clearRequestBuffer()
	{
		for(int i=0; i<RDAHandler.maxRequestBufferSizeWords*2; i++)
			requestBuffer[i] = 0;
		requestSize = 0;
	}
	
    /**
     * Sets the whole reply buffer content to 0 and resets the buffer size variable 
     */
	private void clearReplyBuffer()
	{
		for(int i=0; i<RDAHandler.maxRequestBufferSizeWords*2; i++)
			replyBuffer[i] = 0;
		replySize = 0;
	}

	/**************************************************************************
	 * Send generated reply
	 */
	
	/**
	 * In this function the generated reply from the RDA Handler is being send to the client.
	 * First this function copies the reply data into an own reply buffer.
	 * 
	 * 
	 * @param data - pointer to the reply data buffer from the handler in words (U16)
	 * @param size - number of replay data word (U16)
	 */
	public void sendReply( int[] data, int size)
	{
		clearReplyBuffer();
		
		// convert the word (U16) buffer from the handler into byte oriented buffer
		for(int i=0; i<size; i++)
		{
			replyBuffer[i*2] = (byte) (( data[i] >> 8 ) & 0xFF);
			replyBuffer[i*2+1] = (byte) (( data[i] ) & 0xFF);
		}
		replySize = size*2;

		// transmit the reply buffer to the client
		try {
			mBufferedOutputStream.write(replyBuffer, 0, replySize);
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
				requestSize = mInputStream.read(requestBuffer, 0, RDAHandler.maxRequestBufferSizeWords*2);
				
				// If data are read from the socket successfully.
				if( requestSize > 0 )
				{
					// Prepare to allocate the RDA Handler.
					// In cases of status is false, the RDA Handler is busy.
					boolean status = false;
					while( status != true )
					{
						
						// Request the RDA Handler to take over the received request data.
						status = RDAHandler.getInstance().copyRequestData(this, requestBuffer, requestSize);
						
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
