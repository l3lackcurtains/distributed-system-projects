import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ChatNodeThread implements Runnable {

    private Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    private DataOutputStream toClient;

    ArrayList<String> nodesList = new ArrayList<String>();

    ChatNodeThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.in = null;
        this.out = null;
    }

    @Override
    public void run() {
        try {

            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));

            String clientCommand = in.readLine();

            String[] commandArray = clientCommand.split(":", 2);

            String command = commandArray[0];
            String nodeId = commandArray[1];

            out.flush();
            switch (command) {
                case "JOIN":
                    nodesList.add(nodeId);
                    
                    out.println();
                    break;

                default:
                    break;
            }


        } catch (IOException e) {
            System.err.println(e);
        }
    }

}