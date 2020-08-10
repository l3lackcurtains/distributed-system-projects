import java.util.ArrayList;
import java.util.Scanner;

public class Node {

    String userName;
    int portNumber;

    public static void main(String[] args) {
        Scanner nodeInput = new Scanner(System.in);

        System.out.println("Enter the <USERNAME:PORT:JOIN_PORT>");
        String inputValue = nodeInput.nextLine();

        String[] inputValueArray = inputValue.split(":", 3);

        String userName = inputValueArray[0];
        String portNumber = inputValueArray[1];
        String joinRequestPortNumber = inputValueArray[2];

        ChatNode chatNode = new ChatNode();
        chatNode.startNode(userName, Integer.parseInt(portNumber), Integer.parseInt(joinRequestPortNumber));
    }
}

class ChatNode {
    String userName;
    int portNumber;
    ArrayList<String> nodeList = new ArrayList<String>();
    String nextNode;

    public void startNode(String userName, int portNumber, int joinRequestPortNumber) {
        this.userName = userName;
        this.portNumber = portNumber;

        ChatNodeSender sender = new ChatNodeSender();
        boolean connected = sender.startConnection("127.0.0.1", joinRequestPortNumber);

        if (!connected) {
            System.out.println("Couldn't Join. Started a new network.");
            ChatNodeReceiver receiver = new ChatNodeReceiver(portNumber, userName);
            receiver.start();
        } else {

            String response = sender.sendMessage("JOIN:" + userName + ":" + portNumber);

            if(response.length() > 0 && response.startsWith("JOIN")) {
                String[] responseArray = response.split(":", 2);
                nextNode = responseArray[1];
                nodeList.add(nextNode);
                ChatNodeReceiver receiver = new ChatNodeReceiver(portNumber, userName);
                receiver.start();
            }
        }

        System.out.println(nodeList);
    }

}
