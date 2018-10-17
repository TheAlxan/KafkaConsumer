package ir.sls.kafkaConsumer

import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.ProcessService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import ir.sls.kafkaConsumer.service.url.UrlProcessService
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.sql.DriverManager


class AggregatorTest: DatabaseTest("/CreateTables.sql") {
    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    fun getDataForAggregation():ArrayList<UrlDataRecord>{
        val dataRecord1 = UrlDataRecord("normalizedUrl1", arrayListOf("orginalUrl1"), 1)
        val dataRecord2 = UrlDataRecord("normalizedUrl2", arrayListOf("orginalUrl2", "orginalUrl3"), 2)
        val dataRecord3 = UrlDataRecord("normalizedUrl2", arrayListOf("orginalUrl4", "orginalUrl3"), 2)
        val dataRecord4 = UrlDataRecord("normalizedUrl2", arrayListOf("orginalUrl5", "orginalUrl3"), 2)
        return arrayListOf(dataRecord1,dataRecord2,dataRecord3,dataRecord4)
    }

    @Test
    fun testAggregation(){
        var heap = arrayListOf<UrlDataRecord>()
        val dataRecord1 = UrlDataRecord("normalizedUrl1", arrayListOf("orginalUrl1"), 1)
        val dataRecord2 = UrlDataRecord("normalizedUrl2", arrayListOf("orginalUrl2", "orginalUrl3", "orginalUrl4", "orginalUrl3", "orginalUrl5", "orginalUrl3"), 6)
        heap.add(dataRecord1)
        heap.add(dataRecord2)

        //Assertions.assertThat(heap).isEqualTo(AggregatorService.aggregate(getDataForAggregation()))
    }

    @Ignore
    @Test
    fun testDatabase(){
        val urlDatabaseService = UrlDatabaseService()
        urlDatabaseService.setProperties(jdbcUrl,username,password,driver)
        val urlProcessService = UrlProcessService(urlDatabaseService)
        urlProcessService.processData(getDataForAggregation())

        val con = DriverManager.getConnection(jdbcUrl,username,password)
        val preparedStatement = con.createStatement()
        //******************************** Asserting normalizedUrl ***************************************
        val resultSet = preparedStatement.executeQuery("select count(id) from normalizedUrl;")
        resultSet.next()
        Assertions.assertThat(resultSet.getInt(1)).isEqualTo(2)
        val resultSet2 = preparedStatement.executeQuery("select count from normalizedUrl where url='normalizedUrl2';")
        resultSet2.next()
        Assertions.assertThat(resultSet2.getInt(1)).isEqualTo(6)
        //******************************** Asserting originalUrl ***************************************
        val resultSet3 = preparedStatement.executeQuery("select count(id) from orginalUrl;")
        resultSet3.next()
        Assertions.assertThat(resultSet3.getInt(1)).isEqualTo(5)

    }
}