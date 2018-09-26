import ir.sls.kafkaConsumer.service.url.UrlConsumerService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import ir.sls.kafkaConsumer.service.url.UrlProcessService

/**
 * KDocs Syntax by Aryan Gholamlou
 * Logging by Mohammad hossein Liaghat
 * local kafka server by Mohammad hossein Liaghat
 * Hocon by Mohammad hossein Liaghat
 */

fun main(args: Array<String>)
{
    val urlDatabaseService = UrlDatabaseService()
    val urlProcessService = UrlProcessService(urlDatabaseService)
    val urlConsumerService = UrlConsumerService(urlProcessService)
    urlConsumerService.start()
}
