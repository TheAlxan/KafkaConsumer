import com.google.inject.Guice
import com.google.inject.Injector
import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.ConsumerService
import ir.sls.kafkaConsumer.service.url.UrlConsumerService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import ir.sls.kafkaConsumer.service.url.UrlProcessService
import ir.sls.kafkaConsumer.util.GuiceModule

/**
 * KDocs Syntax by Aryan Gholamlou
 * Logging by Mohammad hossein Liaghat
 * local kafka server by Mohammad hossein Liaghat
 * Hocon by Mohammad hossein Liaghat
 */

fun main(args: Array<String>)
{
//    val injector:Injector = Guice.createInjector(GuiceModule())
    val urlDatabaseService = UrlDatabaseService()
    val urlProcessService = UrlProcessService(urlDatabaseService)
    val urlConsumerService = UrlConsumerService(urlProcessService)
//    val urlConsumerService = injector.getInstance(UrlConsumerService::class.java)
    urlConsumerService.start()
}
