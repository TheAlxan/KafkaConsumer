package ir.sls.kafkaConsumer.service.url

import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.DatabaseService
import ir.sls.kafkaConsumer.service.base.ProcessService
import java.util.*
import kotlin.collections.ArrayList

class UrlProcessService(val urlDatabaseService: DatabaseService<UrlDataRecord>) : ProcessService<UrlDataRecord>(urlDatabaseService){

    fun aggregate(dataRecords: ArrayList<UrlDataRecord>): ArrayList<UrlDataRecord> {
        var heap = hashMapOf<String, UrlDataRecord>()
        dataRecords.forEach {
            if (heap[it.normalizedUrl] == null)
                heap[it.normalizedUrl] = it
            else {
                var dataRecord: UrlDataRecord = heap[it.normalizedUrl]!!
                dataRecord.count += it.count
                it.originalUrls.forEach {
                    dataRecord.originalUrls.add(it)
                }
                heap[it.normalizedUrl] = dataRecord
            }
        }
        var recordsArray:ArrayList<UrlDataRecord> = arrayListOf()
        heap.forEach{
            recordsArray.add(it.value)
        }
        return recordsArray
    }

    override fun processData(recordsArray: ArrayList<UrlDataRecord>): Boolean {

        logger.info("Got ${recordsArray.size} records")
        val heap: ArrayList<UrlDataRecord> = aggregate(recordsArray)
        val t1 = Date().time
        val saveSuccess = databaseService.persistData(heap)
        logger.info("Saved :: $saveSuccess")
        val t2 = Date().time
        logger.info("Time :: " + (t2 - t1))
        return saveSuccess
    }

}
