import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Elevator implements Runnable {
    private final int id;
    private final AtomicInteger currentFloor;
    private volatile ElevatorState state;
    private Request currentRequest;
    private final ConcurrentLinkedDeque<Request> requests;
    private final ConcurrentLinkedDeque<Request> passengers;

    public Elevator(int id, int currentFloor, int maxFloor) {
        this.id = id;
        this.currentFloor = new AtomicInteger(currentFloor);
        this.state = ElevatorState.STOP;
        currentRequest = null;
        requests = new ConcurrentLinkedDeque<>();
        passengers = new ConcurrentLinkedDeque<>();
    }

    public ElevatorState getState() {
        return state;
    }

    public int getFloor() {
        return currentFloor.intValue();
    }

    public int getId() {
        return id;
    }


    public synchronized void setCurrentRequest(Request request) {
        currentRequest = request;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void getIn() {
        var iterator = requests.iterator();
        while (iterator.hasNext()) {
            var request = iterator.next();
            if (request.getStartFloor() == currentFloor.intValue()) {
                System.out.printf("Пассажир %s сел в лифт %d на этаже %d\n", request.getId(), id, currentFloor.intValue());
                passengers.add(request);
                iterator.remove();
            }
        }
    }

    public void getOut() {
        var iterator = passengers.iterator();
        while (iterator.hasNext()) {
            var passenger = iterator.next();
            if (passenger.getFinishFloor() == currentFloor.intValue()) {
                System.out.printf("Пассажир %s вышел из лифта %d на этаже %d\n", passenger.getId(), id, currentFloor.intValue());
                iterator.remove();
            }
        }
    }

    public synchronized void move(int floor) throws InterruptedException {
        if (currentFloor.intValue() == floor
                || currentFloor.intValue() == Building.maxFloor && state == ElevatorState.UP
                || currentFloor.intValue() == 1 && state == ElevatorState.DOWN) {
            state = ElevatorState.STOP;
            System.out.printf("Лифт %d [ОСТАНОВЛЕН]; ", id);
            if (currentFloor.intValue() == currentRequest.getStartFloor()) {
                passengers.add(currentRequest);
            } else {
                passengers.remove(currentRequest);
            }
            return;
        }

        getOut();
        getIn();

        while (currentFloor.intValue() != floor) {
            Thread.sleep(1000);
            switch (state) {
                case UP -> currentFloor.incrementAndGet();
                case DOWN -> currentFloor.decrementAndGet();
            }
            System.out.printf("Лифт %d: этаж %d [%s]; Пассажиры: %d\n",
                    id, currentFloor.intValue(), state == ElevatorState.UP ? "ВВЕРХ" : "ВНИЗ", passengers.size());
        }
    }


    @Override
    public void run() {
        while (true){
            while (!requests.isEmpty()) {
                try {
                    currentRequest = requests.poll();
                    System.out.printf("Лифт %d отправился за пассажиром %s на этаж %d\n", id, currentRequest.getId(), currentRequest.getStartFloor());
                    state = currentRequest.getStartFloor() < currentFloor.intValue() ? ElevatorState.DOWN : ElevatorState.UP;
                    move(currentRequest.getStartFloor());
                    System.out.printf("Пассажир %s подобран на этаже %d\n", currentRequest.getId(), currentRequest.getStartFloor());
                    state = currentRequest.getDirection();
                    move(currentRequest.getFinishFloor());
                    System.out.printf("Пассажир %s доставлен на этаж %d\n", currentRequest.getId(), currentRequest.getFinishFloor());
                } catch (InterruptedException e) {
                    System.out.println("Работа лифта " + id + " была прервана");
                }
            }
        }
    }
}