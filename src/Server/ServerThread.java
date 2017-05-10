package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * Created by IvanOP on 09.05.2017.
 */
public class ServerThread extends Thread {
    private Socket socket = null;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private int identification = 0;
    private String name = null;
    private Map<Integer,ServerThread> serverThreadMap;

    ServerThread(Socket socket, int identification, String name, Map<Integer,ServerThread> serverThreadMap) {
        this.socket = socket;
        this.identification = identification;
        this.name = name;
        this.serverThreadMap = serverThreadMap;
        this.start();
    }

    @Override
    public void run() {
        String inputLine, outputLine;
        connect();
        ConnectionProtocol connectionProtocol = new ConnectionProtocol();
        try {
            while ((inputLine = fromClient.readLine()) != null) {
                //TODO make this thing beautiful

                outputLine = connectionProtocol.processInput(inputLine);
                System.out.println(inputLine);
                //toClient.println(outputLine);
                toClient.println("ya tuta");
                toClient.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            toClient = new PrintWriter(socket.getOutputStream());
            fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
