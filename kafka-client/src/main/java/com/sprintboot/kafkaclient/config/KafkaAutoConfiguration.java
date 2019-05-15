package com.sprintboot.kafkaclient.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaProperties.class)
//@Import(KafkaAnnotationDrivenConfiguration.class)
public class KafkaAutoConfiguration {

        private final KafkaProperties properties;

        private final RecordMessageConverter messageConverter;

        public KafkaAutoConfiguration(KafkaProperties properties,
                                      ObjectProvider<RecordMessageConverter> messageConverter) {
            this.properties = properties;
            this.messageConverter = messageConverter.getIfUnique();
        }

        @Bean
        @ConditionalOnMissingBean(KafkaTemplate.class)
        public KafkaTemplate<?, ?> kafkaTemplate(
                ProducerFactory<Object, Object> kafkaProducerFactory,
                ProducerListener<Object, Object> kafkaProducerListener) {
            KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(
                    kafkaProducerFactory);
            if (this.messageConverter != null) {
                kafkaTemplate.setMessageConverter(this.messageConverter);
            }
            kafkaTemplate.setProducerListener(kafkaProducerListener);
            kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
            return kafkaTemplate;
        }

        @Bean
        @ConditionalOnMissingBean(ConsumerFactory.class)
        public ConsumerFactory<?, ?> kafkaConsumerFactory() {
            return new DefaultKafkaConsumerFactory<>(
                    this.properties.buildConsumerProperties());
        }

        @Bean
        @ConditionalOnMissingBean(ProducerFactory.class)
        public ProducerFactory<?, ?> kafkaProducerFactory() {
            DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
                    this.properties.buildProducerProperties());
            String transactionIdPrefix = this.properties.getProducer()
                    .getTransactionIdPrefix();
            if (transactionIdPrefix != null) {
                factory.setTransactionIdPrefix(transactionIdPrefix);
            }
            return factory;
        }

        //略略略

}
