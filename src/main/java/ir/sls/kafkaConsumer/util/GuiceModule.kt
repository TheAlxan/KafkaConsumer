package ir.sls.kafkaConsumer.util

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import ir.sls.kafkaConsumer.model.UrlDataRecord
import com.google.inject.TypeLiteral
import ir.sls.kafkaConsumer.service.base.DatabaseService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService


class GuiceModule : AbstractModule() {
    override fun configure() {
        bind(object : TypeLiteral<DatabaseService<UrlDataRecord>>() {}).to(UrlDatabaseService::class.java)
    }
}