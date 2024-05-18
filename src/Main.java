import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scan = new Scanner(System.in).useLocale(Locale.US);
        System.out.println("Введите количество этажей: ");
        int floors = scan.nextInt();
        System.out.println("Введите частоту появления заявок (через сколько секунд появляется новая заявка): ");
        double delta = scan.nextDouble();

        Building building = new Building(delta, floors);
        building.startSimulation();
    }
}