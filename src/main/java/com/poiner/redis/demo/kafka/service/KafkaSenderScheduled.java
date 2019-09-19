package com.poiner.redis.demo.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenderScheduled {

    private static final String CRON = "*/10 * * * * ?";

    private final KafkaSender kafkaSender;

    @Autowired
    public KafkaSenderScheduled(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @Scheduled(cron = CRON)
    private void send() {

        kafkaSender.send();

    }
}
