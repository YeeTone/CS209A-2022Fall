package lab7;

import lab2.Practice2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    @State(Scope.Thread)
    public static class MyState{
        Account account1, account2;
        ExecutorService service1, service2;

        @Setup(Level.Iteration)
        public void setUp(){
            account1 = new Account(false);
            account2 = new Account(true);
            service1 = Executors.newFixedThreadPool(100);
            service2 = Executors.newFixedThreadPool(100);
        }
    }

    public static void main(String[] args) throws InterruptedException, RunnerException {
        /*Account account1 = new Account(true);
        ExecutorService service = Executors.newFixedThreadPool(100);
        for(int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(account1, 10));
        }

        service.shutdown();

        while(!service.isTerminated()) {

        }

        System.out.printf("Test Lock: %f\n", account1.getBalance());
        assert account1.getBalance() == 1000;*/
        Options options = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .measurementIterations(1)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static int testSynchronized(MyState myState){
        ExecutorService service = myState.service1;
        for(int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(myState.account1, 10));
        }

        service.shutdown();

        while(!service.isTerminated()) {

        }


        assert myState.account1.getBalance() == 1000;

        myState.service1 = Executors.newFixedThreadPool(100);

        return 0;
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static int testLock(MyState myState){
        ExecutorService service = myState.service2;
        for(int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(myState.account2, 10));
        }

        service.shutdown();

        while(!service.isTerminated()) {

        }

        assert myState.account2.getBalance() == 1000;

        myState.service2 = Executors.newFixedThreadPool(100);

        return 0;
    }
}
