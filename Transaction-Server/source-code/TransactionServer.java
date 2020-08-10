import java.net.*;
import java.io.*;

public class TransactionServer {
	public static TransactionManager transactionManager;
	public static AccountManager accountManager;
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		 try {
	    	  serverSocket = new ServerSocket(8899); 
	      }
	      catch(IOException e) {
	    	  System.out.println("Can't listen on port 8899");
	    	  System.exit(1);
	      }
		 
		 //Create Transaction Manager
		 transactionManager = new TransactionManager();
		 
		 //Create Account Manager
		 int numAccounts = Integer.parseInt(args[0]);
		 int initialBalance = Integer.parseInt(args[1]);
		 accountManager = new AccountManager(numAccounts, initialBalance);
		 
		 while(true) {
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
		      
		      //Start a new thread and pass the clientSocket object 
		      //to handle the current client's connection
		      try {
			      transactionManager.runTransaction(clientSocket);
		      }
		      catch(Exception e) {
		    	  e.printStackTrace();
		    	  serverSocket.close();
			      System.exit(1);
		      }
		 }
	}
	
}
