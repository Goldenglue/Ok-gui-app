package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanOP on 09.05.2017.
 */
public class Server{
    private int portNumber = 4444;
    private boolean isListening = true;
    private int numberOfClients = 0;
    private List<Integer> listOfClientsID = new ArrayList<>();

    private Thread serverThread = new Thread(() -> {
        try (ServerSocket serverSocket =  new ServerSocket(portNumber)) {
            while (isListening) {
                int identification = getIdentification();
                listOfClientsID.add(identification);
                System.out.println("Server is waiting for client");
                new ServerThread(serverSocket.accept(),identification ,getName(), listOfClientsID).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    public static void main (String[] args) {
        Server server = new Server();
        server.serverThread.start();
    }

    private int getIdentification() {
        return (int) (Math.random()*Integer.MAX_VALUE);
    }

    private String getName() {
        return "Client number: " + numberOfClients++ ;
    }

}
