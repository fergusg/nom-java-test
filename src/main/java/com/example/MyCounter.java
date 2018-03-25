package com.example;

import static java.util.Collections.reverseOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.Entry;
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
    public Map<Long, Long> bin(Stream<Long> stream) {
        final long start = System.currentTimeMillis();
        Map<Long, Long> result = stream
                .collect(groupingBy(identity(), mapping(initial -> 1, summingLong(i -> i.longValue()))));
        System.out.println("bin: " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    public String getTop(int limit, Producer... producers) {
        // ProducerIterator is a trivial wrapper to implement Iterator
        Iterable<Long> producer = () -> new ProducerIterator(producers[0]); // <--- just 1st

        Map<Long, Long> binned = bin(stream(producer.spliterator(), false))
                .entrySet().stream()
                .collect(toMap(Entry::getKey, Entry::getValue, (key, value) -> key, LinkedHashMap::new));

        return binned.entrySet().stream().sorted(reverseOrder(comparingByValue())).limit(limit)
                .map(e -> e.getKey().toString()).collect(joining(","));
    }
}
