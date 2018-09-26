import ir.sls.aggregator.service.UrlConsumerService
import ir.sls.aggregator.service.UrlDatabaseService
import ir.sls.aggregator.service.UrlProcessService

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
    urlProcessService.health()
    UrlConsumerService().start()
}
