package rda.basics;

import java.util.ArrayList;
import java.util.List;

import rda.Items.Detect;
import rda.Items.Identify;
import rda.Items.Item32;

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
		Item32 val = new Item32(0x1234);
		val.set(0xDEADBEEF);
		itemList.add(val);
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
//		System.out.println(String.format("Processing CommandID: %d", commandId));
		
		switch (commandId)
		{
		case RDABuffer.CMDID_DETECT:
			modelId.processRequest(request, reply);
			break;
			
		case RDABuffer.CMDID_IDENTIFY_CLIENT:
			clientId.processRequest(request, reply);
			break;
			
		case RDABuffer.CMDID_READITEM:
			int itemId = request.getCommandPayloadWord(0) << 16;
			itemId += request.getCommandPayloadWord(1);
//			System.out.println(String.format("ItemID: %d", itemId));
			for( RDAItem item: itemList)
			{
				if( item.isItemId( itemId ) || true)
				{
					item.processRequest(request, reply);
					break;
				}
			}
//			System.out.println(String.format("ItemID: %d not found", itemId));
			break;
		
		case RDABuffer.CMDID_READITEM_DESTRUCTIVE:
//			System.out.println(String.format("Unhandled CommandID: %d", commandId));
			break;
			
		default:
			System.out.println(String.format("Unknown CommandID: %d", commandId));
			// TODO Reply unknown Command Code
			break;
				
		}
	}
	
}
