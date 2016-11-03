package rda.basics;

import java.util.concurrent.Semaphore;

import connection.ClientThread;


public class RDAHandler extends Thread {

	public static final Object LOCK = new Object();

	/*************************************************************************
	 * Singleton Implementation
	 */
	private static RDAHandler instance;

	private RDAHandler () 
	{
		setHandlerFree();
		clearRequestBuffer();
		clearReplyBuffer();
	}

	public static synchronized RDAHandler getInstance () 
	{
		if (RDAHandler.instance == null) 
		{
			RDAHandler.instance = new RDAHandler();
	    }
		return RDAHandler.instance;
	}
	
	
	/*************************************************************************
	 * Thread control
	 */
	
	private volatile boolean running = true;
	
	public void startHandler()
	{
		System.out.println("Starting RDA Handler ...");
		running = true;
		super.start();
	}
	
	public void stopHandler()
	{
		System.out.println("Stoping RDA Handler ...");
		running = false;
		synchronized(LOCK){
			LOCK.notify();
		}
	}
	
	
	/*************************************************************************
	 * Concurrency protection
	 */
	private Semaphore mutex = new Semaphore(1);
	
	private boolean lock()
	{
		boolean success = false;
		try {
			mutex.acquire();
			success = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	private void release()
	{
		mutex.release();
	}
	
	/*************************************************************************
	 * Definition of Request Buffer
	 */
	private static final int maxAppSizeBytes = 512;
	private static final int headerWords = 3;
	private static final int maxOverheadWords = headerWords + 1;
	public static final int maxRequestBufferSizeWords = (maxOverheadWords + maxAppSizeBytes)/2;
	private int[] requestBuffer = new int[maxRequestBufferSizeWords];
	private int bufferSize;
	private int[] replyBuffer = new int[maxRequestBufferSizeWords];
	private int replyBUfferSize;
	
	private void clearRequestBuffer()
	{
		for(int i=0; i<maxRequestBufferSizeWords; i++)
			requestBuffer[i] = 0;
		bufferSize = 0;
	}
	
	private boolean isRequestComplete()
	{
		boolean status = true;
		
		if( bufferSize == 0 )
		{
			status = false;
		}
		
		if( status )
		{
			int checksum = 0;
			for( int i=0; i<(bufferSize-1); i++)
			{
				checksum += requestBuffer[i] & 0xFFFF;
			}

			if( requestBuffer[bufferSize-1] != (checksum & 0xFFFF) )
			{
				status = false;
			}

			System.out.println(String.format("Checksum %d: %04X == %04X is %b", bufferSize-1, requestBuffer[bufferSize-1]&0xFFFF, checksum&0xFFFF, status));
		}
		
		return status;
	}

	private void copyToBuffer( byte[] data, int size )
	{
		for(int i=0; i<size/2; i++)
		{
			requestBuffer[i] = (data[i*2] << 8) & 0xFF00;
			requestBuffer[i] += data[i*2+1] & 0xFF;
		}
		bufferSize = size/2;
		
		for(int i=0; i<size/2; i++)
			System.out.println(String.format("%02d: %04X", i, requestBuffer[i]));
		
	}
	
	private void clearReplyBuffer()
	{
		for(int i=0; i<maxRequestBufferSizeWords; i++)
			replyBuffer[i] = 0;
		replyBUfferSize = 0;
	}
	
	private void attachReplyChecksum()
	{
		int checksum = 0;
		for( int i=0; i<(replyBUfferSize); i++)
		{
			checksum += replyBuffer[i] & 0xFFFF;
		}
		replyBuffer[replyBUfferSize] = checksum & 0xFFFF;
		replyBUfferSize++;
	}
	
	/*************************************************************************
	 * Definition of Client Lock 
	 */
	private ClientThread clientRef;
	
	private void setHandlerFree()
	{
		clientRef = null;
	}

	private void setHandler( ClientThread thread)
	{
		clientRef = thread;
	}

	public boolean isHanlderFree()
	{
		return clientRef==null;
	}
	
	/*************************************************************************
	 * API to Client Thread
	 */
	public boolean copyRequestData(ClientThread thread, byte[] data, int size)
	{
		boolean success = true;
		
		if( !lock() )
			success = false;
			
		if( success )
		{
			System.out.println("Checking Client Ref");
			if( !isHanlderFree() )
			{
				success = false;
			}
		}
		
		if( success )
		{
			System.out.println("Checking Buffer Ready");
			if( isRequestComplete() )
			{
				success = false;
			}
		}
		
		if( success )
		{
			System.out.println("Handler got Data");
			setHandler( thread );
			copyToBuffer( data, size );
		}
		
		if( success )
		{
			if( isRequestComplete() )
			{
				// Start Thread
				System.out.println("Starting Processing");
				synchronized(LOCK){
					LOCK.notify();
				}
			}
		}

		
		System.out.println("Handler done");
		release();		
		
		return success;
	}
	
	  
	public void run()
	{
		while(running)
		{
			try {
				 synchronized(LOCK){
					 System.out.println("Handler waiting for data ...");
					 LOCK.wait();
					 System.out.println("Handler waiting done");
				 }
				 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running = false;
			}
			
			System.out.println("Processing is running");
			if( lock() )
			{
				
				System.out.println("Processing is checking buffer");
				if( isRequestComplete() )
				{
					
					clearReplyBuffer();
					System.out.println("Processing is decoding buffer");
					// Process RDA
					int packetId = requestBuffer[0];
					int destinationId = requestBuffer[1];
					int payloadSize = requestBuffer[2];
					
					int messageId = requestBuffer[3];
					int commandId = ( messageId >> 8 ) & 0xFF;
					int commandLength = ( messageId ) & 0xFF;
					
					replyBuffer[0] = packetId;
					replyBuffer[1] = destinationId;
					replyBuffer[2] = payloadSize;
					replyBuffer[2] = replyBuffer[2] & 0xFE00;
					
					int usedPayload = 0;
					if( commandId == 1 )
					{
						System.out.println("Detect detected.");
						
						replyBuffer[3] = 0x0104;
						replyBuffer[4] = 0x0067;
						replyBuffer[5] = 0x0000;
						replyBuffer[6] = 0x0000;
						replyBuffer[7] = 0x0000;
	
						usedPayload = 5;
						
					}
					else if( commandId == 2 )
					{
						System.out.println("Identify Client detected.");
						String tmp = "";
						for( int i=0; i<commandLength; i++ )
						{
							tmp += (char)((requestBuffer[4+i] >> 8 ) &0xFF);
							tmp += (char)((requestBuffer[4+i] ) &0xFF);
						}
						System.out.println(tmp);
						
						replyBuffer[3] = 0x0201;
						replyBuffer[4] = 0x0001;
						
						usedPayload = 2;
					}
					else if( commandId == 64 )
					{
						replyBuffer[3] = ( commandId << 8 ) | 0x2;
						replyBuffer[4] = 0xDEAD;
						replyBuffer[5] = 0xBEAF;
						
						usedPayload = 3;
						
					}
					
					replyBuffer[2] += usedPayload;
					replyBUfferSize = usedPayload+3;
					attachReplyChecksum();
					
					if( usedPayload != 0)
					{
						clientRef.sendReply(replyBuffer, replyBUfferSize);
					}
	
				}
	
				
				// processing done
				System.out.println("Processing is done");
				setHandlerFree();
				clearRequestBuffer();
				
				release();	
			}
		}
	}
}
