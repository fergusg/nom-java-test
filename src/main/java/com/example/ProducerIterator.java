package com.example;

import java.util.Iterator;

/**
 * Producer doesn't actually implement Iterator, so we have to proxy it here.
 */
public class ProducerIterator implements Iterator<Long> {
    private Producer producer;

    public ProducerIterator(Producer p) {
        this.producer = p;
    }

    @Override
    public boolean hasNext() {
        return producer.hasNext();
    }

    @Override
    public Long next() {
        return producer.next();
    }

}