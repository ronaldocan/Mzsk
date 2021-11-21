package com.rona.can.config;

import com.rona.can.pojo.PointPo;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.BiConsumer;

@Configuration
public class InfluxConfig {

    @Value("${spring.influx.database}")
    private String influxDatabase;
    @Value("${spring.influx.url}")
    private String influxUrl;
    @Value("${spring.influx.user}")
    private String influxUser;
    @Value("${spring.influx.password}")
    private String influxPassword;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxUrl, influxUser, influxPassword);
        BiConsumer<Iterable<PointPo>, Throwable> exceptionHandler = (batch, exception) -> {

        };
        // 设置批量插入，满足条件1000条开始插入，每一秒插入一次
        BatchOptions options = BatchOptions.DEFAULTS.bufferLimit(5000).actions(100).flushDuration(1000).jitterDuration(20);
        //开启批量插入
        influxDB.enableBatch(options);
        influxDB.setDatabase(influxDatabase);
        return influxDB;
    }
}
