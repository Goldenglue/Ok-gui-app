package pack1;

/**
 * Created by IvanOP on 02.04.2017.
 */
public class ConcreteFactory implements AbstractFactory {
    @Override
    public AbstractBee produceMaleBee() {
        return new MaleBee();
    }

    @Override
    public AbstractBee produceBeeWorker() {
        return new BeeWorker();
    }
}
