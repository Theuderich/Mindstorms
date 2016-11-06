package rda.Items;

import rda.basics.RDABuffer;
import rda.basics.RDAItem;
import rda.basics.RDAType;

public class Item32 extends RDAItem {

	private int itemId;
	private int value;
	
	public Item32( int itemId_ )
	{
		super(RDAType.ITEM);
		itemId = itemId_;
		value = 0;
	}
	
	public int get()
	{
		return value;
	}
	
	public void set( int v )
	{
		value = v;
	}
	
	public void processRequest(RDABuffer request, RDABuffer reply)
	{
//		System.out.println("Prepare Item");
		reply.setMessageId(request.getCommandCode(), (byte) 2);
		reply.setMessagePayloadWord(0, (value>>16) & 0xFFFF );
		reply.setMessagePayloadWord(1, value & 0xFFFF );
		
	}

	public boolean isItemId(int code)
	{
		return itemId == code;
	}
	
	
}
