package org.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConfig {

  public static final String TOPIC = "test_topic";
  public static final String SERVER_CONFIG = "localhost:9092";

  public static Properties producerConfigs() {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_CONFIG);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16 KB batch size
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1); // 1 ms linger time
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32 MB buffer memory
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); // Optional: compression

    return props;
  }

  public static KafkaProducer<String, String> createProducer() {
    Properties props = producerConfigs();
    return new KafkaProducer<>(props);
  }

  public static Properties getConsumerConfigs() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_CONFIG);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // Disable auto-commit

    return props;
  }

  public static KafkaConsumer<String, String> createConsumer() {
    Properties props = getConsumerConfigs();
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(TOPIC));
    return consumer;
  }
}
