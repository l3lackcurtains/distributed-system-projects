import java.net.*;
import java.io.*;

public class Server implements Runnable{
	
	public void Forward(String message) {
		try {
			if(ChatNode.succPort != "") {
				Socket socket = new Socket("127.0.0.1", Integer.parseInt(ChatNode.succPort));
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				BufferedReader servInp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.writeBytes(message + "\n");
				
				out.close();
				servInp.close();
				socket.close();
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void run(){
		  //variable for holding server socket
	      ServerSocket serverSocket = null;
	      
	      try {
	    	  serverSocket = new ServerSocket(Integer.parseInt(ChatNode.myPort)); 
	      }
	      catch(IOException e) {
	    	  System.out.println("Can't listen on port " + ChatNode.myPort);
	    	  System.exit(1);
	      }
	      
	      while(true) {
	    	  //variable for holding client socket
		      Socket clientSocket = null;
		      //System.out.println("Listening for connections...");
		      
		      //try accepting the connection from client 
		      //if fails then print appropriate message on terminal
		      try {
		    	  clientSocket = serverSocket.accept();
		      }
		      catch(IOException e) {
		    	  System.out.println("Accept failed");
		      }
		      
		      
		      try {
		    	  //create a variable to reference client output stream
		          PrintStream toClient = new PrintStream(clientSocket.getOutputStream());

		    	  //create a variable to reference client input stream
		    	  BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    	  
		    	  //read from client input stream in an infinite loop
		    	  String line = "";

	    		  line = fromClient.readLine();
	    		  //System.out.println("Server: " + line);
	    		  String[] parts = line.split("#");
	    		  
	    		  
	    		  if(parts[1].contentEquals("Join")) {
	    			  if(!ChatNode.succPort.contentEquals("")) {
	    				  toClient.println(ChatNode.myId + "#UpdSucc#" + ChatNode.succPort);
	    			  }
	    			  else {
	    				  toClient.println(ChatNode.myId + "#UpdSucc#" + ChatNode.myPort);
	    			  }
	    			  ChatNode.succPort = "300" + parts[0];
	    		  }
	    		  if(parts[1].contentEquals("UpdSucc")) {
	    			  ChatNode.succPort = parts[2];
	    		  }
	    		  if(parts[1].contentEquals("Leave")) {
	    			  if(ChatNode.succPort.contentEquals("300" + parts[0])) {
	    				  Forward(ChatNode.myId + "#Close");
	    				  ChatNode.succPort = !parts[2].contentEquals(ChatNode.succPort) ? parts[2] : "";
	    			  }
	    			  else {
	    				  if(parts[0] != ChatNode.myId) {
	    					  Forward(line);
	    				  }
	    			  }
	    			  System.out.println("Node " + parts[0] + " has left the chat");
	    		  }
		    	  if(parts[1].contentEquals("Broad") && !parts[0].contentEquals(ChatNode.myId)) {
		    		  System.out.println(parts[2]);
		    		  Forward(line);
		    	  }
		    	  
		    	  //close the socket connection and input/output streams to this client
		    	  //toClient.close();
		    	  toClient.close();
		    	  fromClient.close();
		    	  clientSocket.close();
		    	  
	    	  	  if(parts[1].contentEquals("Close")) {
	    	  		  break;
		    	  }
		      } 
		      catch (Exception e) {
		    	  e.printStackTrace();
		      }
	      }
	      try {
	    	  serverSocket.close();
	      }
	      catch(IOException e) {
	    	  e.printStackTrace();
	      }
	}
}
