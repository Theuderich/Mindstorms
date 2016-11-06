import java.io.IOException;

import connection.RDAServer;
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
	    
	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    server.terminate();
	    RDAHandler.getInstance().terminate();
	    
	}

}
