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

		int commandId = request.getCommandCode();
		switch (commandId )
		{
		case RDABuffer.CMDID_READITEM:
		case RDABuffer.CMDID_READITEM_DESTRUCTIVE:
			reply.setMessageId(request.getCommandCode(), (byte) 2);
			reply.setMessagePayloadWord(0, (value>>16) & 0xFFFF );
			reply.setMessagePayloadWord(1, value & 0xFFFF );
			break;
			
		case RDABuffer.CMDID_WRITEITEM:
			
			int itemValue = request.getCommandPayloadWord(2) << 16;
			itemValue += request.getCommandPayloadWord(3);
			
			set(itemValue);

			reply.setMessageId(request.getCommandCode(), (byte) 1);
			reply.setMessagePayloadWord(0, 2 );
			break;
		
		}
		
		
	}

	public boolean isItemId(int code)
	{
		return itemId == code;
	}
	
	
}
