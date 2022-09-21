package lab2;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Practice2 {
    @State(Scope.Thread)
    public static class MyState{
        int[] arrayImpl;
        List<Integer> arraylistImpl;
        List<Integer> linkedlistImpl;
        HashMap<Integer, Integer> intmapImpl;

        int LENGTH = 1000;
        int OFFSET = 12010001;

        int index;

        @Setup(Level.Iteration)
        public void setUp() {
            arrayImpl = new int[LENGTH];
            arraylistImpl = new ArrayList<>();
            linkedlistImpl = new LinkedList<>();
            intmapImpl = new HashMap<>();

            index = new Random().nextInt(LENGTH) + OFFSET;

            for(int i = OFFSET; i<OFFSET+LENGTH;i++){
                int age = new Random().nextInt(18,22);
                intmapImpl.put(i, age);

                arrayImpl[i - OFFSET] = age;
                arraylistImpl.add(age);
                linkedlistImpl.add(age);
                // TODO: initialize arrayImpl, arraylistImpl and linkedlistImpl
            }

        }

    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testintmap(MyState state){
        return state.intmapImpl.get(state.index);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testarraylist(MyState state){
        // TODO return the age by state.index
        return state.arraylistImpl.get(state.index - state.OFFSET);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testlinkedlist(MyState state){
        // TODO return the age by state.index
        return state.linkedlistImpl.get(state.index - state.OFFSET);
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testarray(MyState state){
        // TODO return the age by state.index
        return state.arrayImpl[state.index - state.OFFSET];
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Practice2.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}
