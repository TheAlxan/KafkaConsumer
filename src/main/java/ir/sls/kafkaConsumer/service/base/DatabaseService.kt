package ir.sls.kafkaConsumer.service.base

import mu.KotlinLogging

abstract class DatabaseService<T>
{
    private val logger = KotlinLogging.logger {}

    abstract fun persistData(heap: List<T>): Boolean
}