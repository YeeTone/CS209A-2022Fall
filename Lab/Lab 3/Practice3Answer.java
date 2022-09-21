package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Practice3Answer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Please input the function No:
                    1 - Get even numbers
                    2 - Get odd numbers
                    3 - Get prime numbers
                    4 - Get prime numbers that are bigger than 5
                    0 - Quit
                    """);
            int select = sc.nextInt();
            if (select == 0) {
                break;
            }
            System.out.println("Input size of the list:");
            int n = sc.nextInt();

            System.out.println("Input elements of the list:");
            List<Long> values = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                values.add(sc.nextLong());
            }

            List<Long> result = new ArrayList<>();
            values.stream().filter(e -> select == 1 && e % 2 == 0).forEach(result::add);
            values.stream().filter(e -> select == 2 && e % 2 == 1).forEach(result::add);
            values.stream().filter(e -> select == 3 && isPrime(e)).forEach(result::add);
            values.stream().filter(e -> select == 4 && isPrime(e) && e > 5).forEach(result::add);

            System.out.println("Filter results:");
            System.out.println(result);

        }
    }

    private static boolean isPrime(long v) {
        if (v == 2) {
            return true;
        }

        if (v % 2 == 0) {
            return false;
        }

        for (int i = 3; i < Math.sqrt(v); i += 2) {
            if (v % i == 0) {
                return false;
            }
        }

        return true;
    }

}
