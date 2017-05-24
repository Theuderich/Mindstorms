import java.io.IOException;

import cmp.mgmt.CmpManager;
import rda.Items.Item32;
import rda.basics.RDAHandler;
import rda.basics.RDAItemList;
import rda.connection.RDAServer;

public class TCPServer {

    static int mPort = 10002;
	
	public static void main(String[] args) throws IOException {

	    System.out.println( "Initialization the Server" );

	    // first initializes the RDA Item Management
	    RDAItemList.getInstance().init( 199 );
	    
	    RDAHandler.getInstance().start();
	    RDAServer server = new RDAServer( mPort );

	    CmpManager.getInstance();
//	    TestThread thread = new TestThread();
	    SumoEyeMon sensor = new SumoEyeMon();
//	    sensor.start();
	    CmpManager.getInstance().runAll();


	    server.start();

	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    
	    
	    CmpManager.getInstance().terminateAll();
	    server.terminate();
	    RDAHandler.getInstance().terminate();
	    
	}

}
