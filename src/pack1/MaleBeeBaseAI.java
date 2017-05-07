package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 07.05.2017.
 */
public class MaleBeeBaseAI extends BaseAI {

    private boolean doIMoveToCorner = true;
    private static final Point topLeftCorner = new Point(0, 50);
    private static final Point topRightCorner = new Point(600, 50);
    private static final Point bottomLeftCorner = new Point(0, 700);
    private static final Point bottomRightCorner = new Point(600, 700);
    private static final Point[] corners = {topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner};

    private Runnable movement = () -> {
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
            if (doIMoveToCorner) {
                moveToDestinationPoint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                moveToInitialPoint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    MaleBeeBaseAI(AbstractBee bee) {
        this.bee = bee;
        destinationPoint = new Point(getCorner());
        initialPoint = new Point(bee.initialLocation);
        distance = (int) Math.sqrt(Math.pow(initialPoint.getX() - destinationPoint.getX(), 2) +
                Math.pow(initialPoint.getY() - destinationPoint.getY(), 2));
        dx = (((destinationPoint.getX() - initialPoint.getX()) / distance) * bee.speed);
        dy = (((destinationPoint.getY() - initialPoint.getY()) / distance) * bee.speed);
        startThread();
    }

    private Point getCorner() {
        int rand = (int) (Math.random() * 4);
        return corners[rand];
    }

    private synchronized void moveToDestinationPoint() {
        bee.currentLocation.setLocation(bee.currentLocation.getX() + dx, bee.currentLocation.getY() + dy);
        int traveledDistance = (int) Math.sqrt(Math.pow(bee.currentLocation.getX() - initialPoint.getX(), 2)
                + Math.pow(bee.currentLocation.getY() - initialPoint.getY(), 2));
        if (traveledDistance > distance) {
            bee.currentLocation.setLocation(destinationPoint);
            doIMoveToCorner = false;
        }
    }

    private synchronized void moveToInitialPoint() {
        bee.currentLocation.setLocation(bee.currentLocation.getX() - dx, bee.currentLocation.getY() - dy);
        int traveledDistance = (int) Math.sqrt(Math.pow(bee.currentLocation.getX() - destinationPoint.getX(), 2) +
                Math.pow(bee.currentLocation.getY() - destinationPoint.getY(), 2));
        if (traveledDistance > distance) {
            bee.currentLocation.setLocation(initialPoint);
            doIMoveToCorner = true;
        }
    }

    @Override
    synchronized void startThread() {
        movementThread = new Thread(movement);
        movementThread.start();
    }

    @Override
    synchronized void pauseThread() {
        isRunning = false;
    }

    @Override
    synchronized void resumeThread() {
        isRunning = true;
        this.notify();
    }
}
