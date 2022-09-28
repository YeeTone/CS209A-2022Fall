package lab4;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamProcessingOrder {
    public static void main(String[] args) {

        Stream.of("CS", "209", "A").filter(s -> {
            System.out.println("filter: " + s);
            return true;
        });
        //nothing is printed to the console.

        Stream.of("CS", "209", "A").filter(s -> {
            System.out.println("filter: " + s);
            return true;
        }).forEach(s -> System.out.println("forEach: " + s));
        //output:
        //filter: CS
        //forEach: CS
        //filter: 209
        //forEach: 209
        //filter: A
        //forEach: A

        Stream.of("CS", "209", "A").map(s -> {
            System.out.println("map: " + s);
            return s.toLowerCase();
        })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("c");
                });
        //output:
        //map: CS
        //anyMatch: cs

        Stream.of("209", "CS", "303", "A", "B")
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("C");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toLowerCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        //output:
        //sort: CS; 209
        //sort: 303; CS
        //sort: 303; CS
        //sort: 303; 209
        //sort: A; 303
        //sort: A; CS
        //sort: B; A
        //sort: B; CS
        //filter: 209
        //filter: 303
        //filter: A
        //filter: B
        //filter: CS
        //map: CS
        //forEach: cs

        Stream.of("209", "CS", "303", "A", "B")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("C");
                })
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toLowerCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        //output:
        //filter: 209
        //filter: CS
        //filter: 303
        //filter: A
        //filter: B
        //map: CS
        //forEach: cs

        Stream<String> stream =
                Stream.of("CS", "209", "A")
                        .filter(s -> s.startsWith("C"));

        stream.anyMatch(s -> true);    // ok
        //stream.noneMatch(s -> true);   // exception

        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("CS", "209", "A")
                        .filter(s -> s.startsWith("C"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok


    }
}
