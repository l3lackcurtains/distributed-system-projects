import java.net.*;
import java.io.*;

public class EchoServer {
	public static void main(String args[]) throws IOException {
		  //variable for holding server socket
	      ServerSocket serverSocket = null;
	      
	      try {
	    	  serverSocket = new ServerSocket(8899); //server will listen on port 8899
	      }
	      catch(IOException e) {
	    	  System.out.println("Can't listen on port 8899");
	    	  System.exit(1);
	      }
	      
	      while(true) {
	    	  //variable for holding client socket
		      Socket clientSocket = null;
		      System.out.println("Listening for connections...");
		      
		      //try accepting the connection from client 
		      //if fails then print appropriate message on terminal
		      try {
		    	  clientSocket = serverSocket.accept();
		      }
		      catch(IOException e) {
		    	  System.out.println("Accept failed");
		      }
		      
		      //print an informative message
		      System.out.println("Connection successful");
		      System.out.println("Listening for input...");
		      
		      //Start a new thread and pass the clientSocket object 
		      //to handle the current client's connection
		      try {
			      Thread t = new Thread(new EchoThread(clientSocket));
			      t.start();
		      }
		      catch(Exception e) {
		    	  e.printStackTrace();
		    	  serverSocket.close();
			      System.exit(1);
		      }
	      }
	}
}
