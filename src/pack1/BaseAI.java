package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 07.05.2017.
 */
abstract class BaseAI implements Runnable{
    AbstractBee bee;
    Point destinationPoint;
    Point initialPoint;
    int distance;
    double dx;
    double dy;
    Thread movementThread;
    boolean isRunning = true;

    public BaseAI(AbstractBee bee) {
        this.bee = bee;
    }

    synchronized void pauseThread() {}
    synchronized void resumeThread() {}
}
