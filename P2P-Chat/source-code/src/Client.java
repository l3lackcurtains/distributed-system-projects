import java.net.*;
import java.io.*;
import java.util.*; 

public class Client implements Runnable {
	private Socket clientSocket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	Boolean connected = false;


	private BufferedReader inputReader;
	
    private static ArrayList<String> ports = new ArrayList<String>(){{add("3000"); add("3001"); add("3002");}};
	
	public boolean Connect(int port, String action) {

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
			inputReader.close();
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

	private boolean isConnected() {
		return connected;
	}
	
	public void connectToRing() {
		try {
			clientSocket = new Socket(ChatNode.node.getIpAddress(), ChatNode.node.getPort());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream (clientSocket.getInputStream());
			connected = true;
		} catch (IOException e) {
			connected = false;
			e.printStackTrace();
		}
	}
	public void run() {

		connectToRing();

		if(isConnected()) {
			System.out.println("Connected");
		} else {
			System.out.println("Coundn't connect.");
			System.exit(0);
		}

		this.inputReader = new BufferedReader(new InputStreamReader(System.in));
		String stringLine = "";
		while(true) {
			try {
				stringLine = inputReader.readLine();

				if(stringLine.contentEquals("Leave")){
					Connect(Integer.parseInt(ChatNode.succPort), "Leave");
					break;
				}

				else if(stringLine.contentEquals("config")) {
					System.out.println(ChatNode.succPort);
				}
				else {
					Forward("Node " + ChatNode.myId + ": " + stringLine);
				}		
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
		CloseConnection();
	}
}
