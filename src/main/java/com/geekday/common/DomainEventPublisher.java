package com.geekday.common;

import com.geekday.messaging.Producer;

public class DomainEventPublisher {
    private Producer producer = Producer.getInstance();

    public void publish(DomainEvent event) {
        producer.publish(event.getType(), event.getJson());
    }
}
