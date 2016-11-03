import java.io.IOException;

import connection.RDAServer;
import rda.basics.RDAHandler;

public class TCPServer {

    static int mPort = 10002;
	
	public static void main(String[] args) throws IOException {

	    System.out.println( "Initialization the Server" );

	    RDAHandler.getInstance().startHandler();
	    RDAServer server = new RDAServer( mPort );
	    server.start();
	    
	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    server.terminate();
	    RDAHandler.getInstance().stopHandler();
	    
	}

}
