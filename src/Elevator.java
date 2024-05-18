import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class Elevator implements Callable<String> {
    private int id;
    private int currentFloor;
    private ElevatorState state;
    private int destinationFloor;
    private int capacity;
    private int maxFloor;
    private HashSet<Integer> passengers;

    public Elevator(int id, int currentFloor, int capacity, int maxFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.capacity = capacity;
        this.maxFloor = maxFloor;
        this.state = ElevatorState.STOP;
        passengers = new HashSet<>();
    }

    public ElevatorState getState() {
        return state;
    }

    public int getFloor() {
        return currentFloor;
    }

    public void setDestination(int destination) {
        destinationFloor = destination;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void getOut() {
        var ids = new HashSet<Integer>();
        for (var request : Building.requests) {
            if (request.getFinishFloor() == currentFloor) {
                ids.add(request.getId());
                passengers.remove(request.getId());
                System.out.printf("Пассажир %d вышел из лифта %d на %d этаже\n", request.getId(), id, currentFloor);
            }
        }
        for (var id : ids) {
            Building.requests.removeIf(x -> x.getId() == id);
        }
    }

    public void getIn() {
        for (var request : Building.requests) {
            if (request.getStartFloor() == currentFloor) {
                passengers.add(request.getId());
                System.out.printf("Пассажир %d сел в лифт %d на %d этаже\n", request.getId(), id, currentFloor);
            }
        }
    }

    public synchronized void move() throws InterruptedException {
        if (currentFloor == destinationFloor && state != ElevatorState.STOP
                || currentFloor == 1 && state == ElevatorState.DOWN
                || currentFloor == maxFloor && state == ElevatorState.UP) {
            state = ElevatorState.STOP;
        }

        getOut();
        getIn();

        if (currentFloor != destinationFloor)
            state = currentFloor < destinationFloor ? ElevatorState.UP : ElevatorState.DOWN;

        Thread.sleep(1000);
        switch (state) {
            case UP -> currentFloor++;
            case DOWN -> currentFloor--;
        }
        System.out.println("Лифт " + id + " переместился на этаж " + currentFloor + ", пассажиры: " + passengers);
        }

    @Override
    public String call() throws Exception {
        while (currentFloor != destinationFloor)
            move();
        return "";
    }
}