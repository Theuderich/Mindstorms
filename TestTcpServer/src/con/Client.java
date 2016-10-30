package con;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import dap.DAPHandler;

public class Client extends Thread {

	/**************************************************************************
	 * Construction 
	 */
	public Client(Socket s) throws IOException
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
     * read buffer allocated individually for each client instance
     * but just one of each
     */
    private byte[] requestBuffer = new byte[DAPHandler.maxRequestBufferSizeWords*2];
    private int requestSize;
    private byte[] replyBuffer = new byte[DAPHandler.maxRequestBufferSizeWords*2];
    private int replySize;
    
	private void clearRequestBuffer()
	{
		for(int i=0; i<DAPHandler.maxRequestBufferSizeWords*2; i++)
			requestBuffer[i] = 0;
		requestSize = 0;
	}
	
	private void clearReplyBuffer()
	{
		for(int i=0; i<DAPHandler.maxRequestBufferSizeWords*2; i++)
			replyBuffer[i] = 0;
		replySize = 0;
	}

	public void sendReply( int[] data, int size)
	{
		clearReplyBuffer();
		
		for(int i=0; i<size; i++)
		{
			replyBuffer[i*2] = (byte) (( data[i] >> 8 ) & 0xFF);
			replyBuffer[i*2+1] = (byte) (( data[i] ) & 0xFF);
		}
		replySize = size*2;
		
		for(int i=0; i<replySize; i++)
			System.out.println(String.format("%02d: %02X", i, replyBuffer[i]));
		
		try {
			mBufferedOutputStream.write(replyBuffer, 0, replySize);
			mBufferedOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
	/**************************************************************************
	 * Thread run function
	 */
	public void run()
	{
		while(mSocket.isConnected() && !mSocket.isClosed() && mSocket.isBound() )
		{
			System.out.println("Client Read");
			
			try {
//	            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
// 	            DataOutputStream outToClient = new DataOutputStream(mSocket.getOutputStream());
// 	            String clientSentence = inFromClient.readLine();
// 	            if( clientSentence != null )
// 	            {
//	 	            System.out.println("Received: " + clientSentence);
//	 	            String capitalizedSentence = clientSentence.toUpperCase() + '\n';
//	 	            outToClient.writeBytes(capitalizedSentence);
// 	            }
				requestSize = mInputStream.read(requestBuffer, 0, DAPHandler.maxRequestBufferSizeWords*2);
				System.out.println("Process read");
				if( requestSize > 0 )
				{
					boolean status = false;
					while( status != true )
					{
						System.out.println("Ask Handler");
						
						status = DAPHandler.getInstance().copyRequestData(this, requestBuffer, requestSize);
						if( status == false )
							System.out.println( "Client waiting for Handler" );
						Thread.sleep(100);
					}
					
				}
 	            else
 	            {
 	            	break;
 	            }
 	            
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Client Read Done");
		}
		System.out.println("Client Closed");
	}
	
}
