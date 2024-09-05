package concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 10_000; i++) {
            SynchronizedCounter synchronizedCounter = new SynchronizedCounter();

            CompletableFuture<Void> increment1 = CompletableFuture.runAsync(synchronizedCounter::increment, pool);
            CompletableFuture<Void> increment2 = CompletableFuture.runAsync(synchronizedCounter::increment, pool);

            CompletableFuture<Void> all = CompletableFuture.allOf(increment1, increment2);
            all.thenApply((v) -> {
                if (synchronizedCounter.get() != 2) {
                    System.out.println("Incorrect counter value: " + synchronizedCounter.get());
                }

                return null;
            });
        }

        System.out.println("Synchronized Counter Finished");

        for (int i = 0; i < 10_000; i++) {
            AtomicCounter atomicCounter = new AtomicCounter();

            CompletableFuture<Void> increment1 = CompletableFuture.runAsync(atomicCounter::increment, pool);
            CompletableFuture<Void> increment2 = CompletableFuture.runAsync(atomicCounter::increment, pool);

            CompletableFuture<Void> all = CompletableFuture.allOf(increment1, increment2);
            all.thenApply((v) -> {
                if (Integer.parseInt(atomicCounter.get().toString()) != 2) {
                    System.out.println("Incorrect counter value: " + atomicCounter.get().toString());
                }

                return null;
            });
        }



        waitForThreadpoolShutdown(pool);
        System.out.println("Atomic Counter Finished");
    }

    private static void waitForThreadpoolShutdown(ExecutorService pool) throws InterruptedException {
        pool.shutdownNow();
        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
            System.err.println("Pool did not complete within 10 seconds");
            pool.shutdownNow();
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Pool did not terminate");
            }
        }
    }

    //PART 5
    //If a two threads read the value of a map at the same time they will get the same answer and then they will both
    //try to update the map, one will win and one piece of data will be lost
}
