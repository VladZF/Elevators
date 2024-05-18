import java.util.Random;
import java.util.concurrent.*;

public class Building {
    public static ConcurrentLinkedDeque<Request> requests;
    private final Elevator firstElevator;
    private final Elevator secondElevator;
    public static int maxRequestsCount;
    public static int completedRequests;
    private RequestGenerator generator;
    public ExecutorService requestService;
    public ExecutorService elevatorsService;

    public Building(int capacity, int delta, int maxFloor, int maxRequests) {
        firstElevator = new Elevator(1, 1, capacity, maxFloor);
        secondElevator = new Elevator(2, 1, capacity, maxFloor);
        requests = new ConcurrentLinkedDeque<>();
        generator = new RequestGenerator(delta, maxRequests, maxFloor);
        maxRequestsCount = maxRequests;
        completedRequests = 0;
        requestService = Executors.newSingleThreadExecutor();
        elevatorsService = Executors.newFixedThreadPool(2);
    }

    public void requestElevator() {
        Request request = requests.peek();
        if (request == null)
            return;
        Elevator choosed;
        if (firstElevator.getState() == ElevatorState.STOP
                || request.getStartFloor() < firstElevator.getFloor() && firstElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > firstElevator.getFloor() && firstElevator.getState() == ElevatorState.UP) {
            firstElevator.setDestination(request.getFinishFloor());
            choosed = firstElevator;
        }
        else if (secondElevator.getState() == ElevatorState.STOP
                || request.getStartFloor() < secondElevator.getFloor() && secondElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > secondElevator.getFloor() && secondElevator.getState() == ElevatorState.UP) {
            secondElevator.setDestination(request.getFinishFloor());
            choosed = secondElevator;
        } else { choosed = firstElevator; }
        if (choosed.getDestinationFloor() > request.getStartFloor() && choosed.getState() == ElevatorState.DOWN
            || choosed.getDestinationFloor() < request.getStartFloor() && choosed.getState() == ElevatorState.UP)
            choosed.setDestination(request.getStartFloor());
        System.out.printf("Пассажир %d вызвал лифт на этаже %d", request.getId(), request.getStartFloor());
        elevatorsService.submit(choosed);
    }



    public void startSimulation() {
        Future<String> generationResult = requestService.submit(generator);
    }
}
