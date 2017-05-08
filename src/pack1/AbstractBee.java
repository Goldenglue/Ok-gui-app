package pack1;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by IvanOP on 02.04.2017.
 */
public abstract class AbstractBee implements Serializable {
    Point initialLocation;
    Point currentLocation;
    int speed = 25;
    int lifeTime;
    private long hashCode;
    transient BaseAI baseAI;
    private String identification;

    AbstractBee(int lifeTime, long hashCode, String ident) {
        this.initialLocation =  new Point((int) (Math.random() * 600),70 + (int) (Math.random() * 700));
        this.currentLocation =  new Point(initialLocation);
        this.lifeTime = lifeTime;
        this.hashCode = hashCode;
        this.identification =  ident;
        if (ident.equals("BeeWorker")) {
            baseAI = new BeeWorkerBaseAI(this);
        } else {
            baseAI = new MaleBeeBaseAI(this);
        }

    }

    AbstractBee() {
    }


    public abstract void paintComponent(Graphics g);
    public String getIdentification() {return identification;}
    public void setBaseAI() {}
}
