package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 02.04.2017.
 */
public abstract class AbstractBee {
    final Point initialLocation;
    Point currentLocation;
    int speed = 25;
    int lifeTime;
    private long hashCode;
    BeeWorkerBaseAI beeWorkerBaseAI;
    MaleBeeBaseAI maleBeeBaseAI;

    AbstractBee(int lifeTime, long hashCode) {
        this.initialLocation =  new Point((int) (Math.random() * 600),70 + (int) (Math.random() * 700));
        this.currentLocation =  new Point(initialLocation);
        this.lifeTime = lifeTime;
        this.hashCode = hashCode;
    }

    public abstract void paintComponent(Graphics g);
}
