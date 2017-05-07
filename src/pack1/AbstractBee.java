package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 02.04.2017.
 */
public abstract class AbstractBee {
    int xCoordinate;
    int yCoordinate;
    int lifeTime;
    private long hashCode;

    AbstractBee(int lifeTime, long hashCode) {
        this.xCoordinate = (int) (Math.random() * 500);
        this.yCoordinate = 70 + (int) (Math.random() * 500);
        this.lifeTime = lifeTime;
        this.hashCode = hashCode;
    }

    public abstract void paintComponent(Graphics g);
}
