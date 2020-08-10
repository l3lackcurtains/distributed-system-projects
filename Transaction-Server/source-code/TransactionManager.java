import java.util.*;
import java.net.*;
import java.io.*;

public class TransactionManager {
	public Socket clientSocket = null;
	
	public static int transactions = 0;
	public static int timestamp = 0;
	
	Hashtable<Integer, String> readSets = new Hashtable<Integer, String>();
	Hashtable<Integer, String> writeSets = new Hashtable<Integer, String>();
	
	Hashtable<Integer, Integer> timestamps = new Hashtable<Integer, Integer>();
	Hashtable<Integer, Integer> initialTimeStamps = new Hashtable<Integer, Integer>();
	
	public void runTransaction(Socket clientSocket) {
		(new TransactionManagerWorker(clientSocket)).start();
	}

	public int openTransaction() {
		int transId = ++transactions;
		initialTimeStamps.put(transId, timestamp);
		
		return transId;
	}
	
	public int readBalance(int transId, int accountNo) {
		String reads = readSets.get(transId);
		reads += "#" + accountNo;
		readSets.put(transId,  reads);
		
		return TransactionServer.accountManager.readBalance(accountNo);
	}
	
	public void writeBalance(int transId, int accountNo, int amount) {
		String writes = writeSets.get(transId);
		writes += "#" + accountNo;
		writeSets.put(transId,  writes);
		
		TransactionServer.accountManager.writeBalance(accountNo, amount);
	}
	
	public boolean doIntersect(String[] arr1, String[] arr2) {
		for(int i = 0; i < arr1.length; i++) {
			for(int j = 0; j < arr2.length; j++) {
				if(arr1[i].contentEquals(arr2[j])) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean validateTransaction(int transId) {
		int finalTimeStamp = timestamp;
		int initialTimeStamp = initialTimeStamps.get(transId);
		
		int initialTrans = timestamps.get(initialTimeStamp);
		int finalTrans = timestamps.get(finalTimeStamp);
		
		String[] readAccounts = readSets.get(transId).split("#");
		
		for(int t = initialTrans; t <= finalTrans; t++) {
			String writes = writeSets.get(t);
			String[] writeAccounts = writes.split("#");
			if(doIntersect(readAccounts, writeAccounts)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void closeTransaction(int transId) {
		timestamps.put(++timestamp, transId);
	}
	
	public class TransactionManagerWorker extends Thread{
		Socket clientSocket = null;
		
		public TransactionManagerWorker(Socket socket) {
			this.clientSocket = socket;
		}
		
		public void run() {
			try {
				PrintStream toClient = new PrintStream(clientSocket.getOutputStream());
		    	BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    	
		    	while(true) {
		    		//read from client input stream and store the line read in this variable
			    	String line = fromClient.readLine();
			    	
			    	if(line == null || line.contentEquals("")) {
			    		continue;
			    	}
			    	
			    	String[] parts = line.split("#");
			    	
			    	if(parts[0].contentEquals("open")) {
			    		int transId = TransactionServer.transactionManager.openTransaction();
			    		toClient.println(transId);
			    	}
			    	else if(parts[1].contentEquals("read")){
			    		int transId = Integer.parseInt(parts[0]);
			    		int accountNo = Integer.parseInt(parts[2]);
			    		int balance = TransactionServer.transactionManager.readBalance(transId, accountNo);
			    		
			    		toClient.println(balance);
			    	}
			    	else if(parts[1].contentEquals("write")) {
			    		int transId = Integer.parseInt(parts[0]);
			    		int accountNo = Integer.parseInt(parts[2]);
			    		int amount = Integer.parseInt(parts[3]);
			    		
			    		TransactionServer.transactionManager.writeBalance(transId, accountNo, amount);
			    	}
			    	else if(parts[1].contentEquals("close")) {
			    		int transId = Integer.parseInt(parts[0]);
			    		if(TransactionServer.transactionManager.validateTransaction(transId)) {
			    			TransactionServer.transactionManager.closeTransaction(transId);
			    			toClient.println("success");
			    		}
			    		else {
			    			toClient.println("failed");
			    		}
			    	}
		    	}	    	
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
	}
}
