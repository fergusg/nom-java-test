package com.example;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.function.Function.identity;
import static java.util.stream.StreamSupport.stream;

public class MyCounter implements Counter {

    /**
     * Here is the pain
     */
    public Map<Long, Long> bin(Producer producer) {
        final long start = System.currentTimeMillis();
        try {
            // ProducerIterator is a trivial wrapper to implement Iterator
            Iterable<Long> producerIterator = () -> new ProducerIterator(producer);
            return stream(producerIterator.spliterator(), false) // <-- true (parallel) fails badly
                    .collect(groupingBy(identity(), mapping(initial -> 1, summingLong(i -> i.longValue()))));
        } finally {
            System.out.println("bin: " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    public String getTop(int limit, Producer... producers) {

        ArrayList<Map<Long, Long>> allBinned = new ArrayList<Map<Long, Long>>();
        ArrayList<Thread> threads = new ArrayList<Thread>();

        // for efficiency, assumes #cores > #producers
        for (Producer producer : producers) {
            Runnable runner = () -> {
                Map<Long, Long> binned = bin(producer);
                synchronized (allBinned) {
                    allBinned.add(binned);
                }
            };
            Thread t = new Thread(runner);
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish.  This isn't optimal since we could start processing
        // each thread as it finishes.
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                // the interface doesn't throw an exception, so neither can we.
                e.printStackTrace();
                throw new RuntimeException("Ouch");
            }
        }

        // Merge all the maps.  Although pathological, we can't merge just the top <limit>
        // from each - it's conceivable that the <limit+N>th (say) entry is common to each and
        // would get bumped into the top list
        LinkedHashMap<Long, Long> collected = new LinkedHashMap<Long, Long>();
        for (Map<Long, Long> binned : allBinned) {
            collected = Stream.concat(collected.entrySet().stream(), binned.entrySet().stream())
                    .collect(toMap(e -> e.getKey(), e -> e.getValue(), Long::sum, LinkedHashMap::new));
        }

        return collected.entrySet().stream().sorted(reverseOrder(comparingByValue())).limit(limit)
                .map(e -> e.getKey().toString()).collect(joining(","));

    }
}
