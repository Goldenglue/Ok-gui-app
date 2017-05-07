package pack1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * Created by IvanOP on 02.04.2017.
 */
public class MaleBee extends AbstractBee implements IBehaviour {
    private static Image img;


    static {
        try {
            img = ImageIO.read(new File("ee.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String ident = String.valueOf(this.getClass());

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, xCoordinate, yCoordinate, null);
    }


    MaleBee(int lifeTime, long hashCode) {
        super(lifeTime, hashCode);
    }

}
