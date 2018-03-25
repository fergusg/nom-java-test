package com.example;

public class Main {

    /**
     * This program  will read from 4 producers, each producing 100,000,000 'random' but non-unique long values.
     * Values across producers will also be duplicated, though set the values created by each producer will not be identical.
     *
     * The program will write to System.out a list of the 1000 most occurring values along with the count of how many
     * times they appeared across all producers.
     *
     * @param args no args allowed
     */
    public static void main(String[] args) {
        System.out.println("Starting");

        if (args != null && args.length > 0) {
            throw new IllegalArgumentException("No args allowed");
        }

        // Construct four producers which different seeds to produce different values
        int producerSize = 100_000_000;
        final Producer producer1 = new Producer(producerSize, 1);
        final Producer producer2 = new Producer(producerSize, 2);
        final Producer producer3 = new Producer(producerSize, 3);
        final Producer producer4 = new Producer(producerSize, 4);

        // Get the counter implementation.
        final Counter counter = getCounter();

        if (counter != null) {
            // Set the number of values to return to 1000, this will be much less than the number of unique values given
            // by the producers
            int limit = 1000;
            final long start = System.currentTimeMillis();
            final String top = counter.getTop(limit, producer1, producer2, producer3, producer4);
            System.out.println(top);
            System.out.println("Runtime: " + (System.currentTimeMillis() - start) + "ms");
            if (limit >= 10 && producerSize == 10_000_000) {
                assert (top.startsWith(
                        "-4964420948893066024,-4959463499243013640,-4961115986754665064,-4969378402838085704"));
            }
            if (limit >= 10 && producerSize == 100_000_000) {
                assert (top.startsWith(
                        "-4964420948893066024,-4969378402838085704,-4959463499243013640,-4961115986754665064"));
            }
        }

    }

    /**
     * To return a Counter implementation to count values from the producers.
     *
     * @return the counter
     */
    private static Counter getCounter() {
        return new MyCounter();
    }

}