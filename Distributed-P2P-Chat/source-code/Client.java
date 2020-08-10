import java.net.*;
import java.io.*;
import java.util.*; 

public class Client implements Runnable {
	private BufferedReader inp;
	
    private static ArrayList<String> ports = new ArrayList<String>(){{add("3000"); add("3001"); add("3002");}};
	
	public boolean Connect(int port, String action) {
		Socket newSocket = null;
		DataOutputStream outNew = null;
		BufferedReader servInpNew = null;
		try {
			newSocket = new Socket("127.0.0.1", port);
			outNew = new DataOutputStream(newSocket.getOutputStream());
			servInpNew = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
			if(action.contentEquals("Join")){
				String toSend = ChatNode.myId + "#Join";
				outNew.writeBytes(toSend + "\n");
				String response = servInpNew.readLine();
				String[] parts = response.split("#");
				ChatNode.succPort = parts[2];
			}
			if(action.contentEquals("Leave")){
				String toSend = ChatNode.myId + "#Leave#" + ChatNode.succPort;
				outNew.writeBytes(toSend + "\n");
			}
		}
		catch(UnknownHostException e) {
			return false;
		}
		catch(IOException e) {
			return false;
		}
		try {
			outNew.close();
			servInpNew.close();
			newSocket.close();
		}
		catch(IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean Join() {
		for(String p: ports) {
			if(p != ChatNode.myPort) {
				if(Connect(Integer.parseInt(p), "Join")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void CloseConnection() {
		try {
			inp.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void Forward(String message) {
		try {
			String toSend = ChatNode.myId + "#Broad#" + message;
			if(!ChatNode.succPort.contentEquals("")){
				Socket socket = new Socket("127.0.0.1", Integer.parseInt(ChatNode.succPort));
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				BufferedReader servInp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.writeBytes(toSend);
				
				out.close();
				servInp.close();
				socket.close();
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void JoinRing() {
		if(!Join()) {
			System.out.println("You're the first one here");
		}
		else {
			System.out.println("Joined the chat with other guests");
			Forward("Node " + ChatNode.myId + " has joined the chat");
		}
	}
	public void run() {
		JoinRing();
		this.inp = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while(true) {
			try {
				line = inp.readLine();
				if(line.contentEquals("Leave")){
					Connect(Integer.parseInt(ChatNode.succPort), "Leave");
					break;
				}
				else if(line.contentEquals("config")) {
					System.out.println(ChatNode.succPort);
				}
				else {
					Forward("Node " + ChatNode.myId + ": " + line);
				}		
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
		CloseConnection();
	}
}
