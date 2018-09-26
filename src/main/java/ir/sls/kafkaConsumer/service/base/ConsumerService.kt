package ir.sls.kafkaConsumer.service.base

import ir.sls.kafkaConsumer.config.KafkaFactory
import ir.sls.kafkaConsumer.config.ReadConfig
import ir.sls.kafkaConsumer.metric.InitMeter
import ir.sls.kafkaConsumer.service.SparkServer
import ir.sls.kafkaConsumer.util.gson
import kafka.common.KafkaException
import mu.KLogger
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecords
import java.time.Duration
import kotlin.collections.ArrayList

/**
 * gathering date and aggregate  then persist in Database (MySql)
 * @return persist in database
 * @exception <kafkaException>
 * @author Aryan Gholamlou , Reza Varmazyari , Email : Aryan.gholamlou@gmail.com ,  the.alxan@gmail.com
 */

abstract class ConsumerService<T>{
    val logger:KLogger = KotlinLogging.logger {}
    private var dataType:Class<T>
    lateinit var processService: ProcessService<T>

    constructor(dataType:Class<T>, processService: ProcessService<T>){
        this.dataType = dataType
        this.processService = processService
    }

/*
    fun setProcess(processService: ProcessService<T>){
        this.processService = processService
    }
*/

    fun start() {
        SparkServer().metricService()
        var saveSuccess = true
        val consumer = KafkaFactory.createKafkaConsumer() ?: throw IllegalStateException()
        consumer?.subscribe(arrayListOf(ReadConfig.config.kafka.subscription))
        var records: ConsumerRecords<String, String> = ConsumerRecords.empty()
        while (true) {
            var recordsArray:ArrayList<T> = arrayListOf()
            if (saveSuccess) {
                try {
                    records = consumer!!.poll(Duration.ofMinutes(1))
                    InitMeter.markKafkaRead(records.count().toLong())
                } catch (e: KafkaException) {
                    logger.error(e) { "Kafka Error" }
                }
            }
            records.map { record ->
                val dataRecord: T = gson.fromJson<T>(record.value(),dataType)
                recordsArray.add(dataRecord)
                if (recordsArray.size == 1)
                    logger.info("Partition :: ${record.partition()} , Offset :: ${record.offset()}")
            }
            if (recordsArray.size > 0) {

                saveSuccess = processService.processData(recordsArray)
                if (saveSuccess) {
                    records =  ConsumerRecords.empty()
                    consumer?.commitSync()
                }
            }

        }
    }


}