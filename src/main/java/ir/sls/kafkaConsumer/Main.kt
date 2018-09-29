import com.google.inject.Guice
import com.google.inject.Injector
import ir.sls.kafkaConsumer.service.url.UrlConsumerService
import ir.sls.kafkaConsumer.util.GuiceModule

/**
 * KDocs Syntax by Aryan Gholamlou
 * Logging by Mohammad hossein Liaghat
 * local kafka server by Mohammad hossein Liaghat
 * Hocon by Mohammad hossein Liaghat
 */

fun main(args: Array<String>)
{
    val injector:Injector = Guice.createInjector(GuiceModule())

    val urlConsumerService = injector.getInstance(UrlConsumerService::class.java)
    urlConsumerService.start()
}
