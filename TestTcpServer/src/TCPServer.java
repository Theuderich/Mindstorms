import java.io.IOException;

import cmp.CmpManager;
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

	    CmpManager.getInstance();
	    TestThread thread = new TestThread();
	    CmpManager.getInstance().runAll();


		

	    
	    
	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    
	    
	    CmpManager.getInstance().terminateAll();
	    server.terminate();
	    RDAHandler.getInstance().terminate();
	    
	}

}
