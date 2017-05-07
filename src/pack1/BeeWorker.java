package pack1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IvanOP on 02.04.2017.
 */
public class BeeWorker extends AbstractBee implements IBehaviour {
    private static Image img;
    private String ident = String.valueOf(this.getClass());

    static {
        try {
            img = ImageIO.read(new File("ee.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, (int)currentLocation.getX(), (int)currentLocation.getY(), null);
    }

    BeeWorker(int lifeTime, long hashCode) {
        super(lifeTime, hashCode);

    }


}
