public class Request {
    private int startFloor;
    private int finishFloor;
    private int id;

    public Request(int id, int startFloor, int finishFloor) {
        this.id = id;
        this.startFloor = startFloor;
        this.finishFloor = finishFloor;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getFinishFloor() {
        return finishFloor;
    }

    public int getId() {
        return id;
    }
}
