import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Building {
    private final Elevator firstElevator;
    private final Elevator secondElevator;
    public static int maxFloor;
    private static double delta;

    public Building(double deltaTime, int max) {
        firstElevator = new Elevator(1, 1, maxFloor);
        secondElevator = new Elevator(2, 1, maxFloor);
        maxFloor = max;
        delta = deltaTime;
    }

    public void requestElevator(Request request) {
        if (firstElevator.getState() == ElevatorState.STOP) {
            firstElevator.addRequest(request);
        }
        else if (secondElevator.getState() == ElevatorState.STOP) {
            secondElevator.addRequest(request);
        }
        else if (request.getStartFloor() < firstElevator.getFloor() && firstElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > firstElevator.getFloor() && firstElevator.getState() == ElevatorState.UP) {
            firstElevator.addRequest(request);
        }
        else if (request.getStartFloor() < secondElevator.getFloor() && secondElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > secondElevator.getFloor() && secondElevator.getState() == ElevatorState.UP) {
            secondElevator.addRequest(request);
        } else {
            int firstDistance = Math.abs(firstElevator.getCurrentRequest().getFinishFloor() - request.getStartFloor());
            int secondDistance = Math.abs(secondElevator.getCurrentRequest().getFinishFloor() - request.getStartFloor());
            if (firstDistance <= secondDistance) {
                firstElevator.addRequest(request);
                return;
            }
            secondElevator.addRequest(request);
        }
    }

    public Request generateNewRequest() throws InterruptedException {
        Random random = new Random();
        int start = 0, end = 0;
        UUID id = UUID.randomUUID();
        while (start == end) {
            start = random.nextInt(1, maxFloor);
            end = random.nextInt(1, maxFloor);
        }
        return new Request(id, start, end);
    }

    public void startSimulation() throws ExecutionException, InterruptedException {
        Thread first = new Thread(firstElevator);
        Thread second = new Thread(secondElevator);
        first.start();
        second.start();
        while (true) {
            var request = generateNewRequest();
            requestElevator(request);
            Thread.sleep((int)(delta * 1000L));
        }
    }
}
