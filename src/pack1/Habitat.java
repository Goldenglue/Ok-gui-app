package pack1;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

/**
 * Created by IvanOP on 02.04.2017.
 */
public class Habitat extends JPanel {

    public Timer maleBeeUpdateTimer;
    public Timer beeWorkerUpdateTimer;
    public Timer checkForDeadBees;
    public int maleBeeCounter;
    public int beeWorkerCounter;
    public int hivePopulation;
    public double probability;
    private long simulationStartTime;
    public long simulationTime;
    private AbstractFactory factory;
    private boolean doIShowTime;
    public float maleBeeUpdatePeriod;
    public float beeWorkerUpdatePeriod;


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

    }


    public Dimension getPreferredSize() {
        return new Dimension(700, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (doIShowTime) {
            g.drawString("Simulation time: " + String.valueOf(simulationTime), 50, 50);
        }


        CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> abstractBee.paintComponent(g));

    }

    private void Updater() {
        maleBeeUpdateTimer = new Timer(((int) maleBeeUpdatePeriod), actionEvent -> {
            if (((float) maleBeeCounter / (float) hivePopulation) < probability) {
                CollectionsForObjects.getInstance().addObject(factory.produceMaleBee());
                long uniqueIdentity = (long) (Math.random() * Long.MAX_VALUE);
                CollectionsForObjects.getInstance().getAbstractBeeHashSet().add(uniqueIdentity);
                simulationTime = (System.currentTimeMillis() - simulationStartTime) / 1000;
                CollectionsForObjects.getInstance().getLongLongTreeMap().put(uniqueIdentity, simulationTime);
                maleBeeCounter++;
                hivePopulation++;
                repaint();
            }
        });

        beeWorkerUpdateTimer = new Timer(((int) beeWorkerUpdatePeriod), actionEvent -> {
            CollectionsForObjects.getInstance().addObject(factory.produceBeeWorker());
            long uniqueIdentity = (long) (Math.random() * Long.MAX_VALUE);
            CollectionsForObjects.getInstance().getAbstractBeeHashSet().add(uniqueIdentity);
            simulationTime = (System.currentTimeMillis() - simulationStartTime) / 1000;
            CollectionsForObjects.getInstance().getLongLongTreeMap().put(uniqueIdentity, simulationTime);
            beeWorkerCounter++;
            hivePopulation++;
            repaint();
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
            repaint();

        });


    }

    public void startSimulation() {
        simulationStartTime = System.currentTimeMillis();
        beeWorkerUpdateTimer.start();
        maleBeeUpdateTimer.start();
        checkForDeadBees.start();
    }

    public void stopSimulation() {
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

    public void showSimulationTime() {
        if (!doIShowTime) {
            doIShowTime = !doIShowTime;
        } else {
            doIShowTime = !doIShowTime;
        }
        repaint();
    }

}
