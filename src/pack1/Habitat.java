package pack1;

import Server.ConnectionProtocol;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IvanOP on 02.04.2017.
 */
public class Habitat extends JPanel {

    Timer maleBeeUpdateTimer;
    Timer beeWorkerUpdateTimer;
    private Timer checkForDeadBees;
    Socket socket;
    InetAddress host;
    int serverPort = 4444;
    PrintWriter toServer;
    BufferedReader fromServer;
    int maleBeeCounter;
    int beeWorkerCounter;
    int hivePopulation;
    double probability;
    private long simulationStartTime;
    long simulationTime;
    private AbstractFactory factory;
    private boolean doIShowTime;
    float maleBeeUpdatePeriod;
    float beeWorkerUpdatePeriod;
    int lifeTimeOfMaleBee = 5;
    int lifeTimeOfBeeWorker = 5;
    private Thread paintThread;
    PipedWriter pipedOutputStreamToConsole;
    PipedReader pipedInputStreamFromConsole;
    private int currentAliveMaleBeeNumber;
    private int currentAliveBeeWorkerNumber;
    private boolean isReturningMaleBee = false;
    private boolean isReturningBeeWorker = false;
    private char[] buffer = new char[6];
    private Thread pipedOutputStreamToConsoleThread = new Thread(() -> {
        while (true) {
            try {
                if (isReturningMaleBee) {
                    pipedOutputStreamToConsole.write(currentAliveMaleBeeNumber);
                    currentAliveMaleBeeNumber = 0;
                    isReturningMaleBee = false;
                } else if (isReturningBeeWorker) {
                    pipedOutputStreamToConsole.write(currentAliveBeeWorkerNumber);
                    currentAliveBeeWorkerNumber = 0;
                    isReturningBeeWorker = false;
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread pipedInputStreamFromConsoleThread = new Thread(() -> {
        while (true) {
            try {
                pipedInputStreamFromConsole.read(buffer, 0, 6);
                String string = String.valueOf(buffer);
                if (string.equals("gcambn")) {
                    CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> {
                        if (abstractBee.getIdentification().equals("MaleBee")) {
                            currentAliveMaleBeeNumber++;
                        }
                    });
                    isReturningMaleBee = true;
                } else if (string.equals("gcabwn")) {
                    CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> {
                        if (abstractBee.getIdentification().equals("BeeWorker")) {
                            currentAliveBeeWorkerNumber++;
                        }
                    });
                    isReturningBeeWorker = true;
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    private Runnable paint = () -> {
        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    public Habitat() {
        this.simulationTime = 0;
        this.maleBeeCounter = 0;
        this.hivePopulation = 0;
        this.probability = 0.3;
        this.factory = new ConcreteFactory();
        this.doIShowTime = false;
        this.maleBeeUpdatePeriod = 1300;
        this.beeWorkerUpdatePeriod = 1000;
        Updater();
        connectToServer();
        paintThread = new Thread(paint);
        paintThread.start();
    }


    public Dimension getPreferredSize() {
        return new Dimension(700, 800);
    }

    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (doIShowTime) {
            g.drawString("Simulation time: " + String.valueOf(simulationTime), 50, 50);
        }
        CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> abstractBee.paintComponent(g));

    }

    private void Updater() {
        maleBeeUpdateTimer = new Timer(((int) maleBeeUpdatePeriod), actionEvent -> {
            if (((float) maleBeeCounter / (float) hivePopulation) < probability) {
                long uniqueIdentity = (long) (Math.random() * Long.MAX_VALUE);
                System.out.println(uniqueIdentity);
                CollectionsForObjects.getInstance().addObject(factory.produceMaleBee(lifeTimeOfMaleBee, uniqueIdentity));
                CollectionsForObjects.getInstance().getAbstractBeeHashSet().add(uniqueIdentity);
                simulationTime = (System.currentTimeMillis() - simulationStartTime) / 1000;
                CollectionsForObjects.getInstance().getLongLongTreeMap().put(uniqueIdentity, simulationTime);
                maleBeeCounter++;
                hivePopulation++;
            }
        });

        beeWorkerUpdateTimer = new Timer(((int) beeWorkerUpdatePeriod), actionEvent -> {
            long uniqueIdentity = (long) (Math.random() * Long.MAX_VALUE);
            System.out.println(uniqueIdentity);
            CollectionsForObjects.getInstance().addObject(factory.produceBeeWorker(lifeTimeOfBeeWorker, uniqueIdentity));
            CollectionsForObjects.getInstance().getAbstractBeeHashSet().add(uniqueIdentity);
            simulationTime = (System.currentTimeMillis() - simulationStartTime) / 1000;
            CollectionsForObjects.getInstance().getLongLongTreeMap().put(uniqueIdentity, simulationTime);
            beeWorkerCounter++;
            hivePopulation++;
        });

        checkForDeadBees = new Timer(1000, actionEvent -> {
            Iterator<Map.Entry<Long, Long>> treeMapIterator = CollectionsForObjects.getInstance().getLongLongTreeMap().entrySet().iterator();
            Map.Entry<Long, Long> temp;
            while (treeMapIterator.hasNext()) {
                temp = treeMapIterator.next();
                Iterator<AbstractBee> abstractBeeIterator = CollectionsForObjects.getInstance().getAbstractBeeArrayList().iterator();
                while (abstractBeeIterator.hasNext()) {
                    if (simulationTime >= abstractBeeIterator.next().lifeTime + temp.getValue()) {
                        CollectionsForObjects.getInstance().getAbstractBeeHashSet().remove(temp.getKey());
                        abstractBeeIterator.remove();
                        treeMapIterator.remove();
                        if (treeMapIterator.hasNext()) {
                            temp = treeMapIterator.next();
                        } else {
                            break;
                        }

                    }
                }
            }

        });


    }

    void initializePipedStreams() throws IOException {
        pipedOutputStreamToConsole = new PipedWriter();
        pipedInputStreamFromConsole = new PipedReader();
        pipedOutputStreamToConsoleThread.start();
        pipedInputStreamFromConsoleThread.start();
    }

    void startSimulation() {
        simulationStartTime = System.currentTimeMillis();
        beeWorkerUpdateTimer.start();
        maleBeeUpdateTimer.start();
        checkForDeadBees.start();
    }

    void stopSimulation() {
        maleBeeUpdateTimer.stop();
        beeWorkerUpdateTimer.stop();
        checkForDeadBees.stop();
        CollectionsForObjects.getInstance().getAbstractBeeArrayList().clear();
        hivePopulation = 0;
        beeWorkerCounter = 0;
        maleBeeCounter = 0;
        simulationTime = 0;
        repaint();
    }

    void showSimulationTime() {
        if (!doIShowTime) {
            doIShowTime = !doIShowTime;
        } else {
            doIShowTime = !doIShowTime;
        }
        repaint();
    }

    private void connectToServer() {
        try {
            System.out.println("Connecting to server on port " + serverPort);
            host = InetAddress.getByName("localhost");
            socket = new Socket(host, serverPort);
            toServer = new PrintWriter(socket.getOutputStream(),true);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            toServer.println("Hello from " + socket.getLocalSocketAddress());
            toServer.flush();
            System.out.println(fromServer.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
