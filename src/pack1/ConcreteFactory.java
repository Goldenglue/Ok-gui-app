package pack1;

/**
 * Created by IvanOP on 02.04.2017.
 */
public class ConcreteFactory implements AbstractFactory {
    @Override
    public AbstractBee produceMaleBee(int lifeTime, long hasCode) {
        return new MaleBee(lifeTime, hasCode);
    }

    @Override
    public AbstractBee produceBeeWorker(int lifeTime, long hasCode) {
        return new BeeWorker(lifeTime, lifeTime);
    }
}
