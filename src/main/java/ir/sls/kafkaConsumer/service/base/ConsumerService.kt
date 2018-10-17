package ir.sls.kafkaConsumer.service.base

import com.google.inject.Inject
import ir.sls.kafkaConsumer.config.KafkaFactory
import ir.sls.kafkaConsumer.config.ReadConfig
import ir.sls.kafkaConsumer.metric.InitMeter
import ir.sls.kafkaConsumer.service.SparkServer
import ir.sls.kafkaConsumer.util.gson
import kafka.common.KafkaException
import mu.KLogger
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import kotlin.collections.ArrayList

/**
 * gathering date and aggregate  then persist in Database (MySql)
 * @return persist in database
 * @exception <kafkaException>
 * @author Aryan Gholamlou , Reza Varmazyari , Email : Aryan.gholamlou@gmail.com ,  the.alxan@gmail.com
 */

abstract class ConsumerService<T> {
    val logger: KLogger = KotlinLogging.logger {}
    private var dataType: Class<T>
    private lateinit var consumer: KafkaConsumer<String, String>
    var processService: ProcessService<T>
    var stop: Boolean = false
    constructor(dataType: Class<T>, processService: ProcessService<T>) {
        this.dataType = dataType
        this.processService = processService
    }

    fun pollFromKafka(): ConsumerRecords<String, String> {
        return consumer!!.poll(Duration.ofMinutes(1))
    }

    fun start() {
        SparkServer().metricService()
        var saveSuccess = true
        consumer = KafkaFactory.createKafkaConsumer() ?: throw IllegalStateException()
        consumer.subscribe(arrayListOf(ReadConfig.config.kafka.subscription))
        var recordsArray: List<T> = arrayListOf()
        var i = 0L
        while (!stop) {
            if (saveSuccess) {
                try {
                    recordsArray = pollFromKafka().apply {
                        InitMeter.markKafkaRead(count().toLong())
                    }.map {
                        if (i++ % 1000L == 0L)
                            logger.info("Partition :: ${it.partition()} , Offset :: ${it.offset()}")
                        gson.fromJson<T>(it.value(), dataType)
                    }
                } catch (e: KafkaException) {
                    logger.error(e) { "Kafka Error" }
                }
            }
            if (recordsArray.isNotEmpty()) {
                saveSuccess = processService.processData(recordsArray)
                if (saveSuccess)
                    consumer?.commitSync()
            }

        }
    }
}