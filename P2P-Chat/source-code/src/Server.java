import Message.Message;
import org.w3c.dom.Node;

import java.net.*;
import java.io.*;
import Message.NodeMetaData;
public class Server implements Runnable {
    @Override
    public void run() {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Integer.parseInt(ChatNode.portNumber));
        } catch (IOException e) {
            System.out.println("Can't listen on port " + ChatNode.portNumber);
            System.out.println("Exiting...");
            System.exit(1);
        }

        while (true) {

            //variable for holding client socket
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Couldn't listen to client request.");
            }


            try {
                //create a variable to reference client output stream
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

				ObjectInputStream in = new ObjectInputStream (clientSocket.getInputStream());

				Message clientMesage = (Message) in.readObject();

                switch(clientMesage.getType()) {
                    case "JOIN":
                        if (ChatNode.successorNode == ChatNode.node) {
                            ChatNode.successorNode = clientMesage.getNodeMetaData();
                            Message returnMessage = new Message("UPDATE_SUCCESSOR_PREDECESSOR", null, ChatNode.node, ChatNode.node);
                            out.writeObject(returnMessage);
                        } else {
                            Message returnMessage = new Message("UPDATE_SUCCESSOR_PREDECESSOR", null, ChatNode.successorNode, ChatNode.node);
                            ChatNode.successorNode = clientMesage.getNodeMetaData();
                            out.writeObject(returnMessage);
                        }
                }

                /*
                if (parts[1].contentEquals("Join")) {
                    if (!ChatNode.succPort.contentEquals("")) {
                        toClient.println(ChatNode.myId + "#UpdSucc#" + ChatNode.succPort);
                    } else {
                        toClient.println(ChatNode.myId + "#UpdSucc#" + ChatNode.myPort);
                    }
                    ChatNode.succPort = "300" + parts[0];
                }
                if (parts[1].contentEquals("UpdSucc")) {
                    ChatNode.succPort = parts[2];
                }
                if (parts[1].contentEquals("Leave")) {
                    if (ChatNode.succPort.contentEquals("300" + parts[0])) {
                        Forward(ChatNode.myId + "#Close");
                        ChatNode.succPort = !parts[2].contentEquals(ChatNode.succPort) ? parts[2] : "";
                    } else {
                        if (parts[0] != ChatNode.myId) {
                            Forward(line);
                        }
                    }
                    System.out.println("Node " + parts[0] + " has left the chat");
                }
                if (parts[1].contentEquals("Broad") && !parts[0].contentEquals(ChatNode.myId)) {
                    System.out.println(parts[2]);
                    Forward(line);
                }

                 */

                in.close();
                out.close();
                clientSocket.close();

                if (parts[1].contentEquals("Close")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Forward(String message) {
        try {
            if (ChatNode.succPort != "") {
                Socket socket = new Socket("127.0.0.1", Integer.parseInt(ChatNode.succPort));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                BufferedReader servInp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.writeBytes(message + "\n");

                out.close();
                servInp.close();
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
