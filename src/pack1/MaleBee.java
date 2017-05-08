package pack1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


/**
 * Created by IvanOP on 02.04.2017.
 */
public class MaleBee extends AbstractBee implements IBehaviour, Serializable {
    private transient static Image img;
    static String identification;

    static {
        try {
            img = ImageIO.read(new File("ee.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        identification = String.valueOf(MaleBee.class.getSimpleName());
    }



    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, (int)currentLocation.getX(), (int)currentLocation.getY(), null);
    }


    MaleBee(int lifeTime, long hashCode) {
        super(lifeTime, hashCode,identification);
    }

    public String getIdentification() {
        return identification;
    }

    @Override
    public void setBaseAI() {
        baseAI = new MaleBeeBaseAI(this);
    }

}
