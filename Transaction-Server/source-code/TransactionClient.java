import java.io.*;

public class TransactionClient {
	public static String serverIP = "127.0.0.1";
	public static String serverPort = "8899";
	
	public static int numAccounts;
	public static int numTrans;
	public static int initialBalance;
	
	public static void runTransactions() {
		for(int i = 0; i < numTrans; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						TransactionServerProxy trans = new TransactionServerProxy(serverIP, serverPort);
						
						int transId = trans.openTransaction();
						//System.out.println("Transaction #" + transId + " started");
						
						int accountFrom = (int)Math.floor(Math.random() * numAccounts);
						int accountTo = (int)Math.floor(Math.random() * numAccounts);
						int amount = (int)Math.floor(Math.random() * initialBalance);
						int balance;
						
						accountFrom = accountFrom > 0 ? accountFrom : 1;
						accountTo = accountTo > 0 ? accountTo : 1;
						
						System.out.println("transaction #" + transId + ", $" + amount + " " + accountFrom + "->" + accountTo);
						
						balance = trans.readBalance(accountFrom);
						trans.writeBalance(accountFrom, balance - amount);
						
						balance = trans.readBalance(accountTo);
						trans.writeBalance(accountTo, balance + amount);
						
						System.out.println("account #" + accountFrom + " final balance: " + trans.readBalance(accountFrom));
						System.out.println("account #" + accountTo + " final balance: " + trans.readBalance(accountTo));
						
						//System.out.println("transaction #" + transId + " finished");
					}
					catch(IOException e) {
						System.out.println(e);
					}
				}
				
			}.start();
		}
	}
	
	public static void main(String[] args) {
		numAccounts = Integer.parseInt(args[0]);
		numTrans = Integer.parseInt(args[1]);
		initialBalance = Integer.parseInt(args[2]);

		runTransactions();
	}
}
