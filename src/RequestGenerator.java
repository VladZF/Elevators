import java.util.Random;
import java.util.concurrent.Callable;

public class RequestGenerator implements Callable<String> {

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
    public String call() throws InterruptedException {
        for (int i = 1; i <= maxCount; i++) {
            generateNewRequest(i, maxFloor);
            Thread.sleep(delta * 1000L);
        }
        return "Генерация заявок успешна";
    }
}
