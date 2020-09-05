package kafka.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * kafka自定义分区策略实现
 *
 * @author : sk
 */
@Slf4j
public class KafkaCustPartitionStrategy implements Partitioner {

    @Override
    public int partition(String s, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
        int partitionNum = 0;
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic("part-topic");
        if (key==null){
            partitionNum= 0;
        } else{
            partitionNum= Math.abs(key.hashCode()%partitionInfos.size());
        }
        log.info("应用自定义分区策略发送->partition:" + partitionNum + " key:" + key +" value:" + value);
        return partitionNum;
    }

    @Override
    public void close() {

    }

    @Override
    public void onNewBatch(String topic, Cluster cluster, int prevPartition) {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
