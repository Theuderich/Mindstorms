import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class BrickClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		String sentence;
		  String modifiedSentence;
//		  BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			System.out.println("Stating Client ..."); 
		  Socket clientSocket = new Socket("localhost", 10000);
		  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		  sentence = inFromUser.readLine();
		  sentence = "Test";
		  outToServer.writeBytes(sentence + '\n');
		  modifiedSentence = inFromServer.readLine();
		  System.out.println("FROM SERVER: " + modifiedSentence);
		  clientSocket.close();
	}

}
