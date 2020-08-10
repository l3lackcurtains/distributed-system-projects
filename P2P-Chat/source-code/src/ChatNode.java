import Message.NodeMetaData;
import org.w3c.dom.Node;

import java.util.Scanner;

public class ChatNode {

	public static NodeMetaData node = null;
	public static NodeMetaData successorNode = null;
	public static NodeMetaData predecessorNode = null;
	
	public static void main(String[] args) {

		Scanner nodeInput = new Scanner(System.in);

		System.out.println("Enter the <USERNAME:PORT>");
		String inputValue = nodeInput.nextLine();

		String[] inputValueArray = inputValue.split(":", 2);

		String userName = inputValueArray[0];
		String portNumber = inputValueArray[1];

		node = new NodeMetaData(userName,"127.0.0.1", Integer.parseInt(portNumber));
		successorNode = node;
		predecessorNode = node;

		Thread server = new Thread(new Server());
		server.start();

		Thread client = new Thread(new Client());
		client.start();
	}
}
