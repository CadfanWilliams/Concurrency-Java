package concurrency;

public class SynchronizedCounter {

        private int val = 0;

        public synchronized void increment() {
            val += 1;
        }

        public int get() {
            return val;
        }
}
