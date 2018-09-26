import ir.sls.kafkaConsumer.service.UrlConsumerService
import ir.sls.kafkaConsumer.service.UrlDatabaseService
import ir.sls.kafkaConsumer.service.UrlProcessService

/**
 * KDocs Syntax by Aryan Gholamlou
 * Logging by Mohammad hossein Liaghat
 * local kafka server by Mohammad hossein Liaghat
 * Hocon by Mohammad hossein Liaghat
 */

fun main(args: Array<String>)
{
    val urlDatabaseService = UrlDatabaseService()
    val urlProcessService = UrlProcessService()
    urlProcessService.setDatabase(urlDatabaseService)
    val urlConsumerService = UrlConsumerService()
    urlConsumerService.setProcess(urlProcessService)
    urlConsumerService.start()
}
