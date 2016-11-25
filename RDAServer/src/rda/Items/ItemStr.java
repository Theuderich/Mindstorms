package rda.Items;

import java.nio.charset.StandardCharsets;

import rda.basics.RDABuffer;
import rda.basics.RDAItem;
import rda.basics.RDAType;

public class ItemStr extends RDAItem {

	private static final int MAX_STRING_LEN = 32;
	private int itemId;
	private String val;
	
	public ItemStr( int itemId_ )
	{
		super(RDAType.ITEM);
		itemId = itemId_;
		val = new String();
	}

	@Override
	public boolean isItemId(int code)
	{
		return itemId == code;
	}

	@Override
	public void processRequest(RDABuffer request, RDABuffer reply)
	{

		byte[] b = val.getBytes(StandardCharsets.US_ASCII);
		int len = val.length();
		int buf = 0;
		int payload = 0; 
		
		reply.setMessageId(request.getCommandCode(), (byte) (MAX_STRING_LEN/2) );
		for( int i=0; i<MAX_STRING_LEN; i++)
		{
			
			buf <<= 8;
			if( i < len )
			{
				buf += b[i];
			}

			if( i%2 == 1)
			{
				reply.setMessagePayloadWord(payload++, buf&0xFFFF);
				buf = 0;
			}
		}
	}

	public String get()
	{
		return val;
	}
	
	public void set( String v )
	{
		val = v;
	}
	
	
	
	
	
}
