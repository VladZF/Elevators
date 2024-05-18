import java.util.UUID;

public class Request {
    private final int startFloor;
    private final int finishFloor;
    private final UUID id;
    private final ElevatorState direction;

    public Request(UUID id, int startFloor, int finishFloor) {
        this.id = id;
        this.startFloor = startFloor;
        this.finishFloor = finishFloor;
        direction = startFloor < finishFloor ? ElevatorState.UP : ElevatorState.DOWN;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getFinishFloor() {
        return finishFloor;
    }

    public UUID getId() {
        return id;
    }

    public ElevatorState getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "{" + id + "; " + startFloor + "; " + finishFloor + "}";
    }
}
