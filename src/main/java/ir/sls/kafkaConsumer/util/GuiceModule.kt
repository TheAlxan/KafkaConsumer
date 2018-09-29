package ir.sls.kafkaConsumer.util

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import ir.sls.kafkaConsumer.model.UrlDataRecord
import com.google.inject.TypeLiteral
import ir.sls.kafkaConsumer.service.base.DatabaseService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService


class GuiceModule : AbstractModule() {
    override fun configure() {
//        bind(UrlDataRecord::class.java).annotatedWith(Names.named("foo")).to(UrlDataRecord::class.java)
//        bind(TypeLiteral<DatabaseService<UrlDataRecord>> {})
//        bind(UrlDatabaseService::class.java)
//        bind(DatabaseService::class.java).to(DatabaseService<UrlDataRecord>)
        bind(object : TypeLiteral<DatabaseService<UrlDataRecord>>() {}).to(UrlDatabaseService::class.java)
    }
}