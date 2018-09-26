package ir.sls.kafkaConsumer.service

import ir.sls.kafkaConsumer.model.DataRecord

class UrlConsumerService : ConsumerService<DataRecord>(DataRecord::class.java){

}