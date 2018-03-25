package com.example;

import java.util.Iterator;

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