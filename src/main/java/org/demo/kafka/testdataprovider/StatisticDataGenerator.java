package org.demo.kafka.testdataprovider;

import org.demo.kafka.KafkaBatchProducer;
import org.demo.kafka.KafkaConfig;
import org.demo.kafka.model.Statistic;
import org.demo.util.JsonMapper;

import java.util.List;

public class StatisticDataGenerator {

  public static void produceData(List<Statistic> statistics) {
    KafkaBatchProducer batchProducer = new KafkaBatchProducer(KafkaConfig.TOPIC);
    var messages = statistics.stream().map(JsonMapper::toJson).toList();
    batchProducer.sendBatch(messages);

    Runtime.getRuntime().addShutdownHook(new Thread(batchProducer::stop));
  }
}
