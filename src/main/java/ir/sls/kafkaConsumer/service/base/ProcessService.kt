package ir.sls.kafkaConsumer.service.base


import com.google.inject.Inject
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import mu.KLogger
import mu.KotlinLogging
import kotlin.collections.ArrayList

abstract class ProcessService<T> @Inject constructor(var databaseService: DatabaseService<T>) {

    val logger: KLogger = KotlinLogging.logger {}

    abstract  fun processData(recordsArray: List<T>) : Boolean


}