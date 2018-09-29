package ir.sls.kafkaConsumer.service.url

import com.google.inject.Inject
import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.ConsumerService

class UrlConsumerService @Inject constructor(urlProcessService: UrlProcessService) : ConsumerService<UrlDataRecord>(UrlDataRecord::class.java,urlProcessService){

}