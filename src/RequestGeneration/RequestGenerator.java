package House;

import java.util.Random;

public class RequestGenerator implements Runnable {

    private int floorsCount;
    private double requestDeltaTime;
    private double capacityOfEachElevator;
    private Building building;

    public RequestGenerator(int floorsCount, int capacity, double deltaTime) {
        this.floorsCount = floorsCount;
        this.requestDeltaTime = deltaTime;
        this.capacityOfEachElevator = capacity;
    }

    public Request generateRequest() {
        Random random = new Random();
        int start = 1, finish = 1;
        while (start == finish) {
            start = random.nextInt(1, floorsCount);
            finish = random.nextInt(1, floorsCount);
        }
        return new Request(start, finish);
    }

    @Override
    public void run() {

    }
}
