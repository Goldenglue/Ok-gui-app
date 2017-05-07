package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 02.04.2017.
 */
public abstract class AbstractBee {
    int xCoordinate;
    int yCoordinate;
    long lifeTime;


    public abstract void paintComponent(Graphics g);
}
