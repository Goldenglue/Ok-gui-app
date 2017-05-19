package pack1;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IvanOP on 07.05.2017.
 */
public class BeeWorkerBaseAI extends BaseAI implements Runnable {

    BeeWorkerBaseAI(AbstractBee bee) {
        super(bee);
        setRandomDestination();
        distance = (int) Math.sqrt(Math.pow(bee.currentLocation.getX() - destinationPoint.getX(), 2) +
                Math.pow(bee.currentLocation.getY() - destinationPoint.getY(), 2));
        movementThread = new Thread(this);
        movementThread.start();
        timer.start();
    }

    private void setRandomDestination() {
        destinationPoint = new Point((int) (Math.random() * 600), 70 + (int) (Math.random() * 600));
        distance = (int) Math.sqrt(Math.pow(bee.currentLocation.getX() - destinationPoint.getX(), 2) +
                Math.pow(bee.currentLocation.getY() - destinationPoint.getY(), 2));
        dx = (((destinationPoint.getX() - bee.currentLocation.getX()) / distance) * bee.speed);
        dy = (((destinationPoint.getY() - bee.currentLocation.getY()) / distance) * bee.speed);
    }

    private void moveSomewhere() {
        int traveledDistance = (int) Math.sqrt(Math.pow(bee.currentLocation.getX() - destinationPoint.getX(), 2) +
                Math.pow(bee.currentLocation.getY() - destinationPoint.getY(), 2));
        if (traveledDistance > distance) {
            setRandomDestination();
        }
        bee.currentLocation.setLocation(bee.currentLocation.getX() + dx, bee.currentLocation.getY() + dy);
    }

    private Timer timer = new Timer(1000, actionEvent -> {
        setRandomDestination();
    });

    @Override
    synchronized void pauseThread() {
        isRunning = false;
    }

    @Override
    synchronized void resumeThread() {
        isRunning = true;
        this.notify();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (!isRunning) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            moveSomewhere();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
