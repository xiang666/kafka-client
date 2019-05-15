package com.sprintboot.kafkaclient.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Iterator;
import java.util.List;

/**
 * 批量消费配置
 */
public class KafkaMessageReceiver {

    @KafkaListener(topics = {"test"}, containerFactory = "listenerContainerFactory")
    public void registryReceiver(List<ConsumerRecord<Integer, String>> integerStringConsumerRecords, Acknowledgment acknowledgment) {
        Iterator<ConsumerRecord<Integer, String>> it = integerStringConsumerRecords.iterator();
        while (it.hasNext()){
            ConsumerRecord<Integer, String> consumerRecords = it.next();
            //dosome
            acknowledgment.acknowledge();
        }
    }

}
