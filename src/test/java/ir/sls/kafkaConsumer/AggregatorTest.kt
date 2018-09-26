package ir.sls.kafkaConsumer

import ir.sls.kafkaConsumer.model.DataRecord
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
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

    fun getDataForAggregation():ArrayList<DataRecord>{
        val dataRecord1 = DataRecord("normalizedUrl1", arrayListOf("orginalUrl1"), 1)
        val dataRecord2 = DataRecord("normalizedUrl2", arrayListOf("orginalUrl2", "orginalUrl3"), 2)
        val dataRecord3 = DataRecord("normalizedUrl2", arrayListOf("orginalUrl4", "orginalUrl3"), 2)
        val dataRecord4 = DataRecord("normalizedUrl2", arrayListOf("orginalUrl5", "orginalUrl3"), 2)
        return arrayListOf(dataRecord1,dataRecord2,dataRecord3,dataRecord4)
    }

    @Test
    fun testAggregation(){
        var heap = hashMapOf<String, DataRecord>()
        val dataRecord1 = DataRecord("normalizedUrl1", arrayListOf("orginalUrl1"), 1)
        val dataRecord2 = DataRecord("normalizedUrl2", arrayListOf("orginalUrl2", "orginalUrl3", "orginalUrl4", "orginalUrl3", "orginalUrl5", "orginalUrl3"), 6)
        heap["normalizedUrl1"] = dataRecord1
        heap["normalizedUrl2"] = dataRecord2

        //Assertions.assertThat(heap).isEqualTo(AggregatorService.aggregate(getDataForAggregation()))
    }

    @Test
    fun testDatabase(){
        //UrlDatabaseService.setProperties(jdbcUrl,username,password,driver)
        //UrlConsumerService().processData(getDataForAggregation())

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