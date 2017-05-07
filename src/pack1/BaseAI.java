package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 07.05.2017.
 */
public abstract class BaseAI {
    AbstractBee bee;
    Point destinationPoint;
    Point initialPoint;
    int distance;
    double dx;
    double dy;
    Thread movementThread;
    boolean isRunning = true;

    void startThread() {}
    void pauseThread() {}
    void resumeThread() {}
}
