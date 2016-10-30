import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import con.ConListen;
import dap.DAPHandler;

public class TCPServer {

    static int mPort = 10002;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	    System.out.println( "Initialization the Server" );

	    DAPHandler.getInstance().start();
	    ConListen server = new ConListen(mPort); 
	    server.start();
	    
//	    System.out.println( "Starting the Server loop" );
//	      while(true)
//	         {
//	    	  System.out.println( "Server Iteration" );
//	            Socket connectionSocket = welcomeSocket.accept();
//	            BufferedReader inFromClient =
//	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//	            clientSentence = inFromClient.readLine();
//	            System.out.println("Received: " + clientSentence);
//	            capitalizedSentence = clientSentence.toUpperCase() + '\n';
//	            outToClient.writeBytes(capitalizedSentence);
//	         }
//
	}

}
