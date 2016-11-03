import java.io.IOException;

import connection.RDAServer;
import rda.basics.RDAHandler;

public class TCPServer {

    static int mPort = 10002;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	    System.out.println( "Initialization the Server" );

	    RDAHandler.getInstance().startHandler();
	    RDAServer server = new RDAServer( mPort );
	    server.startServer();
	    
	    System.out.println("Press any key to exit ...");
	    System.in.read();
	    server.stopServer();
	    RDAHandler.getInstance().stopHandler();
	    
	    
	    
//	    DAPHandler.getInstance().start();
//	    ConListen server = new ConListen(mPort); 
//	    server.start();
	    
	}

}
