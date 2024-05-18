import java.util.Random;

public class RequestGenerator implements Runnable {

    private final int delta, maxCount, maxFloor;

    public RequestGenerator(int delta, int maxCount, int maxFloor) {
        this.delta = delta;
        this.maxCount = maxCount;
        this.maxFloor = maxFloor;
    }

    public void generateNewRequest(int id, int maxFloor) {
        Random random = new Random();
        int start = 0, end = 0;
        while (start == end) {
            start = random.nextInt(1, maxFloor);
            end = random.nextInt(1, maxFloor);
        }
        Request request = new Request(id, start, end);
        Building.requests.add(request);
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= maxCount; i++) {
                generateNewRequest(i, maxFloor);
                Thread.sleep(delta * 1000L);
            }
        } catch (InterruptedException e) {
            System.out.println("Генерация заявок была прервана");
        }
    }
}
