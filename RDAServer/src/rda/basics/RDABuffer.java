package rda.basics;

public class RDABuffer {

	/**
	 * private buffer variable
	 */
	public final int max = RDAHandler.maxRequestBufferSizeBytes;
	public byte[] buffer = new byte[max];
	public int size;
	
	public RDABuffer()
	{
		
	}
	
	public void clear()
	{
		for( int i=0; i<max; i++)
			buffer[i] = 0;
		size = 0;
	}
	
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
	 * @return True if request is complete.
	 */
	public boolean isRequestComplete()
	{
		boolean complete = true;
		
		complete = (size != 0);
		
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
	 * Increases the buffer size variable by one
	 */
	public void attachReplyChecksum()
	{
		// TODO protect out of bound access if buffer is already full
		
		int checksum = calcChecksum( 0 );
		buffer[size++] = (byte) ((checksum>>8) & 0xFF);
		buffer[size++] = (byte) (checksum & 0xFF);
	}
	
}
