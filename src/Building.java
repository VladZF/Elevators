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
        Request request = requests.poll();
        if (request == null)
            return;
        Elevator chosen;
        if (firstElevator.getState() == ElevatorState.STOP
                || request.getStartFloor() < firstElevator.getFloor() && firstElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > firstElevator.getFloor() && firstElevator.getState() == ElevatorState.UP) {
            firstElevator.setDestination(request.getFinishFloor());
            chosen = firstElevator;
        }
        else if (secondElevator.getState() == ElevatorState.STOP
                || request.getStartFloor() < secondElevator.getFloor() && secondElevator.getState() == ElevatorState.DOWN
                || request.getStartFloor() > secondElevator.getFloor() && secondElevator.getState() == ElevatorState.UP) {
            secondElevator.setDestination(request.getFinishFloor());
            chosen = secondElevator;
        } else { chosen = firstElevator; }
        if (chosen.getDestinationFloor() > request.getStartFloor() && chosen.getState() == ElevatorState.DOWN
            || chosen.getDestinationFloor() < request.getStartFloor() && chosen.getState() == ElevatorState.UP)
            chosen.setDestination(request.getStartFloor());
        System.out.printf("Пассажир %d вызвал лифт на этаже %d\n", request.getId(), request.getStartFloor());
        elevatorsService.submit(chosen);
    }



    public void startSimulation() throws InterruptedException {
        requestService.submit(generator);
        while (completedRequests != maxRequestsCount) {
            if (!requests.isEmpty())
                requestElevator();
            else {
                Thread.sleep(500);
            }
        }
    }
}
