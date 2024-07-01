package org.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.kafka.model.Statistic;
import org.demo.service.StatisticService;
import org.demo.util.JsonMapper;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaListener {
  private static final Logger log = LogManager.getLogger(KafkaListener.class);

  private final KafkaConsumer<String, String> consumer;
  private final ExecutorService executorService;
  private final StatisticService statisticService;

  public KafkaListener(StatisticService statisticService) {
    this.consumer = KafkaConfig.createConsumer();
    this.executorService = Executors.newSingleThreadExecutor();
    this.statisticService = statisticService;
  }

  public void start() {
    executorService.submit(() -> {
      try {
        while (true) {
          ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
          for (ConsumerRecord<String, String> record : records) {
            processMessage(record);
          }
          consumer.commitSync();
        }
      } catch (Exception e) {
        log.debug(e);
      } finally {
        consumer.close();
      }
    });
  }

  private void processMessage(ConsumerRecord<String, String> record) {
    var statistic = JsonMapper.fromJson(record.value(), Statistic.class);
    System.out.println("Received Message: " + statistic);
    statisticService.createOrUpdate(statistic);
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
