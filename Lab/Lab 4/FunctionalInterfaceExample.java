package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalInterfaceExample {
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for (T s : list) {
            if (p.test(s)) {
                results.add(s);
            }
        }
        return results;
    }

    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for (T s : list) {
            c.accept(s);
        }
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> results = new ArrayList<>();
        for (T s : list) {
            results.add(f.apply(s));
        }
        return results;
    }

    public static void main(String[] args) {
        List<String> listOfStrings = new ArrayList<>();
        listOfStrings.add("");
        listOfStrings.add("abc");
        listOfStrings.add("\n");
        listOfStrings.add("e");
        System.out.println("List of strings");
        forEach(listOfStrings, (s) -> System.out.println(s));

        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
        System.out.println("List of non-empty strings");
        forEach(nonEmpty, (s) -> System.out.println(s));


        List<Integer> listStrLen = map(nonEmpty, (s) -> s.length());
        System.out.println("List of lengths of non-empty strings");
        forEach(listStrLen, (s) -> System.out.println(s));
    }
}
