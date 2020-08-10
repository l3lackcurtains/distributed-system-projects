import java.io.*;
import java.net.*;

public class ChatNodeReceiver extends Thread{
    ServerSocket serverSocket;
    String username;

    ChatNodeReceiver(int port, String username) {
        // creates a new server socket in port 23
        this.serverSocket = null;
        this.username = username;
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // while the server socket is active
            while (true) {
                // creates a client socket that the server socket has connected to
                Socket cSocket = this.serverSocket.accept();
                // creates a run of the program with clientSocket (cSocket)
                Runnable runHolder = new ChatNodeThread(cSocket);
                // creates a new thread with the runHolder
                Thread threadHolder = new Thread(runHolder);
                // starts the thread
                threadHolder.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}