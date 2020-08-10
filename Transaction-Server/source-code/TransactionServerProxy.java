import java.net.*;
import java.io.*;

public class TransactionServerProxy{
	String serverIP;
	int serverPort;
	int transactionId;
	
	DataOutputStream out;
	BufferedReader servInp;
	Socket socket;
	
	public TransactionServerProxy(String IP, String port) {
		serverIP = IP;
		serverPort = Integer.parseInt(port);
	}
	
	public int openTransaction() throws IOException{
		try {
			socket = new Socket(serverIP, serverPort);
			out = new DataOutputStream(socket.getOutputStream());
			servInp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String toSend = "open";
			out.writeBytes(toSend + "\n");
			
			return Integer.parseInt(servInp.readLine());
		}
		catch(IOException e) {
			throw e;
		}
	}
	
	public int readBalance(int accountNo) throws IOException {
		try {
			String toSend = transactionId + "#read#" + accountNo;
			out.writeBytes(toSend + "\n");
		
			return Integer.parseInt(servInp.readLine());
		}
		catch(IOException e) {
			throw e;
		}
	}
	
	public void writeBalance(int accountNo, int amount) throws IOException{
		try {
			String toSend = transactionId + "#write#" + accountNo + "#" + amount;
			out.writeBytes(toSend + "\n");
		}
		catch(IOException e) {
			throw e;
		}
	}
	
	public void closeTransaction() {
		
	}
}
