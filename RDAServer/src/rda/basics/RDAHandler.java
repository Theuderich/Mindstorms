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
		freeClientRef();
	}

	/**
	 * Constructs and return the singleton object
	 * @return - the singleton object
	 */
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
	
	/**
	 * boolean variable to signal the run function a termination request
	 */
	private volatile boolean running = true;
	
	/**
	 * Method to start the RDA Handler thread
	 * implemented in run()
	 */
	@Override
	public void start()
	{
		running = true;
		super.start();
	}

	/**
	 * Requests the RDA Handler to close the run thread.
	 * Therefore the termination request is being send over the running variable.
	 * To wake up the thread a notification signal is being triggered  
	 */
	public void terminate()
	{

		running = false;
		synchronized(LOCK){
			LOCK.notify();
		}
	}
	
	
	/*************************************************************************
	 * Concurrency protection
	 */
	
	/**
	 * Semaphore for protecting the buffer for simultaneous access in different threads
	 */
	private Semaphore mutex = new Semaphore(1);
	
	/**
	 * request a lock of the buffer access
	 * @return Return false if lock was not successful
	 */
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

	/**
	 * release the lock of buffer access
	 */
	private void release()
	{
		mutex.release();
	}
	
	/*************************************************************************
	 * Definition of Request Buffer
	 */
	
	/**
	 * Defining the RDA Protocol
	 */
	private static final int maxAppSizeBytes = 512;
	private static final int headerWords = 3;
	private static final int maxOverheadWords = headerWords + 1;
	public static final int maxRequestBufferSizeBytes = (maxOverheadWords + maxAppSizeBytes);
	public static final int maxRequestBufferSizeWords = (maxRequestBufferSizeBytes/2);
	
	/*************************************************************************
	 * Definition of Client Lock 
	 */
	
	/**
	 * Reference to the client thread which request is currently processed.
	 */
	private ClientThread clientRef;
	
	/**
	 * Clears the client reference.
	 */
	private void freeClientRef()
	{
		clientRef = null;
	}

	/**
	 * Stores the reference to the client thread which request should be processed
	 * @param thread Client thread reference
	 */
	private void setClientRef( ClientThread thread)
	{
		clientRef = thread;
	}

	/**
	 * Boolean check for the client thread if the RDA Handler is currently processing any request.
	 * The concurrency issue is so far being handled together with the buffer access lock.
	 * @return True if no request processing is in progress.
	 */
	private boolean isHanlderFree()
	{
		return clientRef==null;
	}
	
	/*************************************************************************
	 * API to Client Thread
	 */
	
	/**
	 * Interface function to the client thread for copying the request data into the RDA Handler
	 * @param thread Client thread reference
	 * @param data Request buffer reference
	 * @param size Received bytes
	 * @return True if request data are being accepted. False if handler is busy.
	 */
	public boolean allocateHandler(ClientThread thread )
	{
		boolean success = true;
		
		if( !lock() )
			success = false;
			
		if( success )
		{
			if( !isHanlderFree() )
			{
				success = false;
			}
		}
		
		if( success )
		{
			if( thread.request.isRequestComplete() )
			{
				success = false;
			}
		}
		
		if( success )
		{
			setClientRef( thread );
		}
		
		if( success )
		{
			// Start Thread
			synchronized(LOCK){
				LOCK.notify();
			}
		}

		release();		
		
		return success;
	}
	
	  
	/**
	 * Thread function of the handler which processes the complete received request.
	 * Prepares the reply message in the local reply buffer.
	 * Transfers the generated reply into the client thread context and send them out to the socket.
	 */
	public void run()
	{
		while(running)
		{
			try {
				 synchronized(LOCK){
					 LOCK.wait();
				 }
				 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running = false;
			}
			
			if( lock() )
			{
				
				if( !isHanlderFree() )
				{
					
					clientRef.reply.clear();

					// Process RDA
					int packetId = clientRef.request.getWord(0);
					int destinationId = clientRef.request.getWord(1);
					int payloadSize = clientRef.request.getWord(2);
					
//					int commandId = clientRef.request.getCommandCode();
//					int commandLength = clientRef.request.getCommandLength();
					
					clientRef.reply.setWord(0, packetId);
					clientRef.reply.setWord(1, destinationId);
					clientRef.reply.setWord(2, payloadSize & 0xFE00);
					
					RDAItemList.getInstance().processRequest(clientRef.request, clientRef.reply);

//					int usedPayload = 0;
//					if( commandId == 1 )
//					{
//						
//						// Detect
//						clientRef.reply.setWord(3, 0x0104);
//						clientRef.reply.setWord(4, 0x00C7);	//103 = SCP = 0x0067
//						clientRef.reply.setWord(5, 0x0000);
//						clientRef.reply.setWord(6, 0x0000);
//						clientRef.reply.setWord(7, 0x0000);
//						
//						usedPayload = 5;
//						
//					}
//					else if( commandId == 2 )
//					{
//						
//						// Identify Client
//						String tmp = "";
//						for( int i=0; i<commandLength; i++ )
//						{
//							tmp += (char)((clientRef.request.getWord(4+i) >> 8 ) & 0xFF);
//							tmp += (char)((clientRef.request.getWord(4+i) ) & 0xFF);
//						}
//						System.out.println(tmp);
//						
//						clientRef.reply.setWord(3, 0x0201);
//						clientRef.reply.setWord(4, 0x0001);
//						
//						usedPayload = 2;
//					}
//					else if( commandId == 64 )
//					{
//						// Read Item
//						clientRef.reply.setWord(3, ( commandId << 8 ) | 0x2);
//						clientRef.reply.setWord(4, 0xDEAD);
//						clientRef.reply.setWord(5, 0xBEAF);
//						
//						usedPayload = 3;
//						
//					}
//					
					int usedPayload = clientRef.reply.getCommandLength() + 1;
					clientRef.reply.setWord(2, (payloadSize & 0xFE00) + usedPayload);
					clientRef.reply.attachReplyChecksum();
					
					if( usedPayload != 0)
					{
						clientRef.sendReply();
					}
	
				}
	
				
				// processing done
				freeClientRef();
				release();	
			}
		}
	}
}
