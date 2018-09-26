package ir.sls.kafkaConsumer.service.url

import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.ConsumerService

class UrlConsumerService(urlProcessService: UrlProcessService) : ConsumerService<UrlDataRecord>(UrlDataRecord::class.java,urlProcessService){

}