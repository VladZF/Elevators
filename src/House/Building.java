package House;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ControlsSystem implements Runnable {
    private Elevator firstElevator;
    private Elevator secondElevator;
    private ConcurrentLinkedQueue<Request> requests;


    @Override
    public void run() {

    }
}
