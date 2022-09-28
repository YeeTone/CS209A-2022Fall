package lab4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamCreation {
    public static void main(String[] args) {

        //create a stream of a fixed number of integers.
        Stream.of(1,2,3).forEach(System.out::println);
        //output:
        //1
        //2
        //3

        //use Stream.of() to create a stream from a bunch of object references.
        Stream.of("CS", "209", "A")
                .findFirst()
                .ifPresent(System.out::println);
        // output:
        // CS

        //IntStreams can replace the regular for-loop utilizing IntStream.range()
        IntStream.range(1, 4)
                .forEach(System.out::println);
        //output:
        //1
        //2
        //3

        int[] array = new int[]{1,2,3};
        Arrays.stream(array).map(n->n*n).average().ifPresent(System.out::println);
        //output:
        //4.666666666666667

        Stream.of(array).map(n->{
            double sum = 0;
            for (int a:n) {
                sum += a*a;
            }
            return sum / n.length;
        }).forEach(System.out::println);
        //output:
        //4.666666666666667

        //Lists and Sets support methods stream() to create a sequential stream
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.stream().map(n->n*n).reduce((i,j)->i+j).ifPresent(System.out::println);
        //output:
        //14

        //object streams support the special mapping operations mapToInt(), mapToLong() and mapToDouble:
        Stream.of("C1", "C2", "C3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);
        //output:
        //3

        //Primitive streams can be transformed to object streams via mapToObj():
        IntStream.range(1, 4)
                .mapToObj(i -> "C" + i)
                .forEach(System.out::println);
        //output:
        //C1
        //C2
        //C3

        //a combined example: the stream of doubles is first mapped to an int stream and
        // then mapped to an object stream of strings
        Stream.of(6.7, 8.7, 9.7)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "Level" + i)
                .forEach(System.out::println);
        //output:
        //Level6
        //Level8
        //Level9

    }
}
