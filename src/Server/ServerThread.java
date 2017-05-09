package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by IvanOP on 09.05.2017.
 */
public class ServerThread extends Thread {
    private Socket socket = null;
    private BufferedReader toServer;
    private PrintWriter fromServer;
    private int identification = 0;
    private String name = null;
    private List<Integer> listOfClientsID;

    ServerThread (Socket socket, int identification, String name, List<Integer> list) {
        this.socket = socket;
        this.identification = identification;
        this.name = name;
        this.listOfClientsID = list;
    }

    @Override
    public void run() {
        String inputLine, outputLine;
        connect();
        ConnectionProtocol connectionProtocol = new ConnectionProtocol();
        try {
            while ((inputLine = toServer.readLine()) != null) {
                //TODO make this thing beautiful
                outputLine  = connectionProtocol.processInput(inputLine);
                fromServer.println(outputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            fromServer =  new PrintWriter(socket.getOutputStream());
            toServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
