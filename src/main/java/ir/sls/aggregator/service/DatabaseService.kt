package ir.sls.aggregator.service

import mu.KotlinLogging

abstract class DatabaseService<T>
{
    private val logger = KotlinLogging.logger {}

    abstract fun persistData(heap: ArrayList<T>): Boolean
}