package rda.basics;

import java.util.ArrayList;
import java.util.List;

import rda.Items.Detect;
import rda.Items.Identify;
import rda.Items.Item32;
import rda.Items.ItemStr;

public class RDAItemList {

	/*************************************************************************
	 * Singleton Implementation
	 */

	private static RDAItemList instance;

	private RDAItemList () 
	{
		itemList = new ArrayList<RDAItem>();
	}

	/**
	 * Constructs and return the singleton object
	 * @return - the singleton object
	 */
	public static synchronized RDAItemList getInstance () 
	{
		if (RDAItemList.instance == null) 
		{
			RDAItemList.instance = new RDAItemList();
	    }
		return RDAItemList.instance;
	}
	
	/*************************************************************************
	 * Basic Initialization
	 */
	public void init(int modelId_)
	{
		modelId = new Detect( modelId_ );
		clientId = new Identify();
		
		// product
		ItemStr s = new ItemStr(1); 
		s.set("MINDSTORM");	
		itemList.add(s);

		// hw version
		s = new ItemStr(2);
		s.set("01.00");	
		itemList.add(s);
		
		// sw version
		s = new ItemStr(5);
		s.set("01.00");
		itemList.add(s);
		
		// sw revision
		s = new ItemStr(6);
		s.set("xxxx");
		itemList.add(s);
		
	}
	
	public void recordItem( RDAItem item)
	{
		itemList.add(item);
	}
	
	/*************************************************************************
	 * Reference list to all RDA Items
	 */
	private List<RDAItem> itemList;
	private RDAItem modelId;
	private RDAItem clientId;
	
	
	public void processRequest(RDABuffer request, RDABuffer reply)
	{
		int commandId = request.getCommandCode();

		
		switch (commandId)
		{
		case RDABuffer.CMDID_DETECT:
			modelId.processRequest(request, reply);
			break;
			
		case RDABuffer.CMDID_IDENTIFY_CLIENT:
			clientId.processRequest(request, reply);
			break;
			
		case RDABuffer.CMDID_READITEM:
		case RDABuffer.CMDID_READITEM_DESTRUCTIVE:
		case RDABuffer.CMDID_WRITEITEM:	
			int itemId = request.getCommandPayloadWord(0) << 16;
			itemId += request.getCommandPayloadWord(1);
//			System.out.println(String.format("Item: %d", itemId));
			for( RDAItem item: itemList)
			{
				if( item.isItemId( itemId ))
				{
					item.processRequest(request, reply);
					break;
				}
			}

			break;
		
		default:
			System.out.println(String.format("Unknown CommandID: %d", commandId));
			// TODO Reply unknown Command Code
			break;
				
		}
	}
	
}
