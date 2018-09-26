package ir.sls.aggregator.service

import ir.sls.aggregator.model.DataRecord
import java.util.*
import kotlin.collections.ArrayList

class UrlProcessService : ProcessService<DataRecord>(){

    fun aggregate(dataRecords: ArrayList<DataRecord>): ArrayList<DataRecord> {
        var heap = hashMapOf<String, DataRecord>()
        dataRecords.forEach {
            if (heap[it.normalizedUrl] == null)
                heap[it.normalizedUrl] = it
            else {
                var dataRecord: DataRecord = heap[it.normalizedUrl]!!
                dataRecord.count += it.count
                it.originalUrls.forEach {
                    dataRecord.originalUrls.add(it)
                }
                heap[it.normalizedUrl] = dataRecord
            }
        }
        var recordsArray:ArrayList<DataRecord> = arrayListOf()
        heap.forEach{
            recordsArray.add(it.value)
        }
        return recordsArray
    }

    override fun processData(recordsArray: ArrayList<DataRecord>): Boolean {

        logger.info("Got ${recordsArray.size} records")
        val heap: ArrayList<DataRecord> = aggregate(recordsArray)
        val t1 = Date().time
        val saveSuccess = databaseService.persistData(heap)
        logger.info("Saved :: $saveSuccess")
        val t2 = Date().time
        logger.info("Time :: " + (t2 - t1))
        return saveSuccess
    }

}
