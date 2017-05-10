package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IvanOP on 09.05.2017.
 */
public class Server {
    private int portNumber = 4444;
    private boolean isListening = true;
    private int numberOfClients = 0;
    private Map<Integer, ServerThread> serverThreadMap = new HashMap<Integer, ServerThread>();

    private Thread serverThread = new Thread(() -> {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (isListening) {
                int identification = getIdentification();
                System.out.println("Server is waiting for client");
                ServerThread serverThread = new ServerThread(serverSocket.accept(), identification, getName(), serverThreadMap);
                serverThreadMap.put(identification,serverThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    public static void main(String[] args) {
        Server server = new Server();
        server.serverThread.start();
    }

    private int getIdentification() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    private String getName() {
        return "Client number: " + numberOfClients++;
    }

}
