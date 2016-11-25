import java.io.IOException;

import connection.RDAServer;
import rda.Items.Item32;
import rda.basics.RDAHandler;
import rda.basics.RDAItemList;

public class TCPServer {

    static int mPort = 10002;
	
	public static void main(String[] args) throws IOException {

	    System.out.println( "Initialization the Server" );

	    // first initializes the RDA Item Management
	    RDAItemList.getInstance().init( 199 );
	    
	    RDAHandler.getInstance().start();
	    RDAServer server = new RDAServer( mPort );
	    server.start();

		Item32 val1 = new Item32(0x1234);
		Item32 val2 = new Item32(0x1235);

		val1.set(0xDEADBEEF);
		val2.set(0x1CEC001);

		while( val2.get() == 0x1CEC001 )
		{
			System.out.println("Waiting");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	    
	    
	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    server.terminate();
	    RDAHandler.getInstance().terminate();
	    
	}

}
