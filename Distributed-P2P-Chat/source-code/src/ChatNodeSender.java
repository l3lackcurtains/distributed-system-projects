import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatNodeSender {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public boolean startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public String sendMessage(String msg) {
        out.println(msg);
        String response = "";
        try {
            // Print response
            response = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void stopConnection() {

        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}