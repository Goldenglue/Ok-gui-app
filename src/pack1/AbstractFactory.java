package pack1;

import java.awt.*;

/**
 * Created by IvanOP on 02.04.2017.
 */
public interface AbstractFactory {
    AbstractBee produceMaleBee(int lifeTime, long hasCode);

    AbstractBee produceBeeWorker(int lifeTime, long hasCode);
}
