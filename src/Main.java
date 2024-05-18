import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите количество этажей: ");
        var floors = scan.nextInt();
        System.out.println("Введите максимальную вместимость лифта (количество человек): ");
        var capacity = scan.nextInt();
        System.out.println("Введите частоту появления заявок (через сколько секунд появляется новая заявка): ");
        var delta = scan.nextInt();
        System.out.println("Введите максимальное количество заявок: ");
        var maximum = scan.nextInt();

        Building building = new Building(capacity, delta, floors, maximum);
        try {
            building.startSimulation();
            System.out.println("Симуляция успешно завершена");
        } catch (InterruptedException e) {
            System.out.println("Работа симуляции была прервана");
        }
    }
}