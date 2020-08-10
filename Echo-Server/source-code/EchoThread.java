import java.net.*;
import java.io.*;

public class EchoThread implements Runnable {
	   //private variable for storing client socket connection object
	   private Socket clientSocket;
	   
	   //private variable to maintain the current state of the state machine
	   //state = -1 is the initial state of the machine
	   private int state = -1;
	   
	   //initialize the clientSocket with the provided socket object
	   EchoThread(Socket socket) {
	      clientSocket = socket;
	   }
	   
	   //This function implements the state machine logic to keep track of word quit
	   //It returns boolean indicating if we have reached the final state i.e. seen the word 'quit'
	   //state = -1 is the initial state, changing the state is equivalent to incrementing this variable
	   
	   private boolean setState(char ch) {
		   //if the current character is 'q' then move to state 0
		   if(ch == 'q') {
			   state = 0;
		   }
		   
		   //if the current character is 'u' and machine was is state where 'q' was seen(state == 0)
		   //then move to state 1 (equivalent to seeing "qu")
		   else if((ch == 'u') && (state == 0)) {
			   state = 1;
		   }
		   
   		   //if the current character is 'i' and machine was is state where 'qu' was seen(state == 1)
		   //then move to state 2 (equivalent to seeing "qui")
		   else if((ch == 'i') && (state == 1)) {
			   state = 2;
		   }
		   
   		   //if the current character is 't' and machine was is state where 'qui' was seen(state == 2)
		   //then move to state 3 (equivalent to seeing "quit")
		   else if((ch == 't') && (state == 2)) {
			   state = 3;
		   }
		   
		   //for any other case set the state machine back to initial state(-1)
		   else {
			   state = -1;
		   }
		   
		   //return true if we reach state 3
		   return (state == 3);
	   }
	   
	   public void run() {
	      try {
	    	  //create a variable to reference client output stream
	    	  PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);

	    	  //create a variable to reference client input stream
	    	  InputStreamReader fromClient = new InputStreamReader(clientSocket.getInputStream());
	    	  
	    	  //read from client input stream in an infinite loop
	    	  while(true) {
	    		  char charFromClient = (char)fromClient.read();
	    		  
	    		  System.out.println("Server: " + charFromClient);
	    		  
	    		  if ((charFromClient >= 'a' && charFromClient <= 'z') || 
	    			  (charFromClient >= 'A' && charFromClient <= 'Z')) {
	    			  
	    			  toClient.println(charFromClient);
	    			  
	    			  //check if we have seen the word 'quit' and break the loop if so
	    			  //otherwise set the state of the state machine
	    			  if(this.setState(Character.toLowerCase(charFromClient))) {
	    				  break;
	    			  }
	    		  }
	    	  }
	    	  
	    	  //close the socket connection and input/output streams to this client
	    	  toClient.close();
	    	  fromClient.close();
	    	  clientSocket.close();  
	      } 
	      catch (Exception e) {
	    	  e.printStackTrace();
	      }
	   }
   }   