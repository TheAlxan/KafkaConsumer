package ir.sls.kafkaConsumer.service.base


import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import mu.KLogger
import mu.KotlinLogging
import kotlin.collections.ArrayList

abstract class ProcessService<T>(val databaseService: DatabaseService<T>) {

    val logger: KLogger = KotlinLogging.logger {}
//    lateinit var databaseService: DatabaseService<T>

/*
    fun setDatabase(databaseService: DatabaseService<T>){
        this.databaseService = databaseService
    }
*/

    abstract  fun processData(recordsArray: ArrayList<T>) : Boolean


}