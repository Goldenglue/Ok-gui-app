package pack1;

/**
 * Created by IvanOP on 07.05.2017.
 */
public abstract class BaseAI  {
    Thread movementThread;

    BaseAI() {
        movementThread = new Thread();
    }
}
