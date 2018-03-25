package com.example;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Produces n long values where n = the given size.
 * The longs produced are fixed "random" and duplicated, where the distribution of duplication is determined by the seed.
 */
public class Producer {

    private final int seed;
    private final Random random1;
    private final Random random2;
    private final long size;
    private int count = 0;

    public Producer(int size, int seed) {
        this.size = size;
        this.seed = seed;
        this.random1 = new Random(1);
        this.random2 = new Random(seed);
    }


    /**
     * Returns true if the producer has more long elements.
     *
     * @return true if the producer has more long elements
     */
    public boolean hasNext() {
        return count < size;
    }

    /**
     * Returns the long.
     *
     * @return the next long element
     * @throws NoSuchElementException if no further long elements exist
     */
    public long next() {
        if (hasNext()) {
            count++;
            if (count > ((long) random2.nextInt(count)) * ((long) random2.nextInt(count)) / 10) {
                random1.setSeed(seed);
            }
            return random1.nextLong();
        } else {
            throw new NoSuchElementException();
        }

    }

}