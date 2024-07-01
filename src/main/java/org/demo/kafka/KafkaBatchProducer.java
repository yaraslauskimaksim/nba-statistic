package org.demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaBatchProducer {
  private static final Logger log = LogManager.getLogger(KafkaBatchProducer.class);

  private final KafkaProducer<String, String> producer;
  private final String topic;
  private final ExecutorService executorService;

  public KafkaBatchProducer(String topic) {
    this.producer = KafkaConfig.createProducer();
    this.topic = topic;
    this.executorService = Executors.newSingleThreadExecutor();
  }

  public void sendBatch(List<String> messages) {
    executorService.submit(() -> {
      log.debug("Sending " + messages.size() + " messages to topic: " + topic);
      try {
        for (String message : messages) {
          ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
          producer.send(record);
          log.debug("Sent " + message + " to topic: " + topic);
        }
        log.debug("Sent {} messages", messages.size());
      } catch (Exception e) {
        log.debug(e);
      } finally {
        producer.flush(); // Ensure all messages are sent before closing
        producer.close();
      }
    });
  }

  public void stop() {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
    }
  }
}
