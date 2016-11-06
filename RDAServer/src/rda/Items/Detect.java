package rda.Items;

import rda.basics.RDABuffer;
import rda.basics.RDAItem;
import rda.basics.RDAType;

public class Detect extends RDAItem {

	private int id;
	
	public Detect( int id_)
	{
		super(RDAType.DETECT);
		id = id_;
	}
	
	public void processRequest(RDABuffer request, RDABuffer reply)
	{
//		System.out.println("Prepare Detect");
		reply.setMessageId(request.getCommandCode(), (byte) 4);
		reply.setMessagePayloadWord(0, id);
		reply.setMessagePayloadWord(1, 0);
		reply.setMessagePayloadWord(2, 0);
		reply.setMessagePayloadWord(3, 0);
	}

}
