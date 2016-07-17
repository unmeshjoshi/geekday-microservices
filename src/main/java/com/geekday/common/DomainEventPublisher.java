package com.geekday.common;

import com.geekday.messaging.Producer;

public class DomainEventPublisher {
    private static Producer producer;
    public static void initialize() { //needed to explicitly initialize zeromq publisher
        producer = Producer.getInstance();
    }

    public void publish(DomainEvent event) {
        producer.publish(event.getType(), event.getCsv());
    }
}
