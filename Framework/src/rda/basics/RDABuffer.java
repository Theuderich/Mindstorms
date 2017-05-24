package rda.basics;

public class RDABuffer {

	public RDABuffer()
	{
		clear();
	}
	
	
	/*************************************************************************
	 * Protocol definitions
	 */
	
	private final static int RDABYTE_PACKETID = 0;
	private final static int RDABYTE_DESTINATIONID = 2;
	private final static int RDABYTE_PAYLOAD_SIZE = 4;
	private final static int RDABYTE_PAYLOAD = 6;
	
	private final static int RDABYTE_MESSAGEID = RDABYTE_PAYLOAD;
	private final static int RDABYTE_MESSAGEDATA = RDABYTE_PAYLOAD+2;
	private final static int RDABYTE_COMMANDCODE = RDABYTE_PAYLOAD;
	private final static int RDABYTE_COMMANDLENGTH = RDABYTE_PAYLOAD+1;
	
	
	public final static int CMDID_DETECT = 1;
	public final static int CMDID_IDENTIFY_CLIENT = 2;
	public final static int CMDID_READITEM = 64;
	public final static int CMDID_READITEM_DESTRUCTIVE = 65;
	public final static int CMDID_WRITEITEM = 80;
			
	/*************************************************************************
	 * Error Code definitions
	 */
	
	public final static int APP_ERROR_CMDID = 255;
	public final static int APP_ERROR_LENGTH = 1;
	public final static int APP_ERROR_UNKNOWN_CMD = 0;
	public final static int APP_ERROR_UNKNOWN_ITEM = 1;
	public final static int APP_ERROR_INVALID_DATA = 2;
	public final static int APP_ERROR_UNSUPPORTED = 3;
	public final static int APP_ERROR_BUSY = 4;
	public final static int APP_ERROR_DATA_REJECTED = 5;
			
	
	/*************************************************************************
	 * private buffer variable
	 */
	public final int max = RDAHandler.maxRequestBufferSizeBytes;
	public byte[] buffer = new byte[max];
	public int size;

	/**
	 * clears the content of the buffer and its size variable
	 */
	public void clear()
	{
		for( int i=0; i<max; i++)
			buffer[i] = 0;
		size = 0;
	}
	
	/**
	 * return the specific word (U16) at the given index
	 * @param index Index where to build the word
	 * @return Word at the given index
	 */
	public int getWord( int index )
	{
		int word = 0;
		int byteIndex = index*2;
		
		//TODO exception out of bound
		if( (byteIndex+1) >= max )
			return word;
		
		word = (buffer[byteIndex] << 8) & 0xFF00;
		word += buffer[byteIndex+1] & 0xFF;
		return word;
	}

	/**
	 * Writes two bytes into the buffer at a specific position.
	 * The internal buffer size variable stores at the end the index of max buffer access.
	 * @param index Index where to write two bytes
	 * @param word U16 value to be written at the specified index
	 */
	public void setWord( int index, int word )
	{
		int byteIndex = index*2;
		
		//TODO exception out of bound
		if( (byteIndex+1) >= max )
			return;
		
		buffer[byteIndex++] = (byte) ((word >> 8 ) & 0xFF);
		buffer[byteIndex++] = (byte) (word & 0xFF);
		size = Math.max(size, byteIndex);
	}
	
	/*************************************************************************
	 * Protocol related functions 
	 */
	
	/**
	 * Calculates the RDA checksum for the buffer contend.
	 * In cases where the checksum is already inside of the data 
	 * the offset can reduce the data range taken for calculation 
	 * @param shift Relative value to reduce the data range
	 * @return Calculated RDA offset
	 */
	public int calcChecksum( int shift )
	{
		int checksum = 0;
		int tmp = 0;
		int i;
		
		// Iterate over the buffer array in byte steps
		for( i=0; i<(size-shift); i++)
		{
			// build word (U16) out of two byte elements
			if( i%2 == 0 )
			{
				tmp = (buffer[i]<<8) & 0xFF00;
			
			// if word is complete add to checksum
			} else {
				
				tmp += buffer[i] & 0xFF;
				checksum += tmp;
			}
		}
		
		// in case of the size is not in complete word counts
		if( i%2 != 0 )
		{
			tmp += buffer[i] & 0xFF;
			checksum += tmp;
		}
		
		
		checksum &= 0xFFFF;
		return checksum;
	}
	
	/**
	 * Check if the request buffer contains a complete RDA package.
	 * This means the checksum needs to be correct.
	 * @return True if request is complete.
	 */
	public boolean isRequestComplete()
	{
		boolean complete = true;
		
		complete = (size != 0);
		
		// TODO check also the RDA header bytes
		if( complete )
		{
			int checksum = calcChecksum( -2 );
			
			if( buffer[size-2] != ((checksum>>8) & 0xFF) )
			{
				complete = false;
			}

			if( buffer[size-1] != (checksum & 0xFF) )
			{
				complete = false;
			}
			
		}
		
		return complete;
		
	}
	
	/**
	 * Appends the RDA checksum at the end of the prepared reply data
	 * Increases the buffer size variable by one word (U16)
	 */
	public void attachReplyChecksum()
	{
		// TODO protect out of bound access if buffer is already full
		
		int checksum = calcChecksum( 0 );
		buffer[size++] = (byte) ((checksum>>8) & 0xFF);
		buffer[size++] = (byte) (checksum & 0xFF);
	}
	 
	public byte getCommandCode()
	{
		return buffer[this.RDABYTE_COMMANDCODE];
	}

	public byte getCommandLength()
	{
		return buffer[this.RDABYTE_COMMANDLENGTH];
	}
	
	public byte getCommandPayloadByte(int index)
	{
		// TODO out of bound check
		return buffer[this.RDABYTE_MESSAGEDATA+index];
	}
	
	public int getCommandPayloadWord(int index)
	{
		// TODO out of bound check
		return getWord(this.RDABYTE_MESSAGEDATA/2+index);
	}
	
	
	
	public void setMessageId( byte commandCode, byte commandLength)
	{
		buffer[this.RDABYTE_COMMANDCODE] = commandCode;
		buffer[this.RDABYTE_COMMANDLENGTH] = commandLength;
		size = Math.max(size, this.RDABYTE_COMMANDLENGTH); 
	}

	public void setMessagePayloadByte( int index, byte value)
	{
		buffer[this.RDABYTE_MESSAGEDATA+index] = value;
		size = Math.max(size, this.RDABYTE_MESSAGEDATA+index); 
	}
	
	public void setMessagePayloadWord( int index, int value)
	{
		setWord(this.RDABYTE_MESSAGEDATA/2+index, value);
	}
	
}
