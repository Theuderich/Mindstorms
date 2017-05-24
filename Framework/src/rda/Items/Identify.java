package rda.Items;

import rda.basics.RDABuffer;
import rda.basics.RDAItem;
import rda.basics.RDAType;

public class Identify extends RDAItem {

	private String client;
	
	public Identify( )
	{
		super(RDAType.IDENTIFY);
		client = new String();
	}
	
	public void processRequest(RDABuffer request, RDABuffer reply)
	{
		// Identify Client
		int commandLength = request.getCommandLength();
		
		client = "";
		
		for( int i=0; i<commandLength*2; i++ )
		{
			client += (char)(request.getCommandPayloadByte(i));
		}
		System.out.println(client);
		
		// TODO Clean this up
		reply.setWord(3, 0x0201);
		reply.setWord(4, 0x0001);
	}
	
}
