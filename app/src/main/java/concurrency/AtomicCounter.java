package concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private final AtomicInteger val = new AtomicInteger(1);

    public void increment() {
        val.incrementAndGet();
    }

    public AtomicInteger get() {
        return val;
    }
}
