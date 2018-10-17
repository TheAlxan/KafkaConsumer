package ir.sls.kafkaConsumer

import io.mockk.*
import ir.sls.kafkaConsumer.config.*
import ir.sls.kafkaConsumer.service.url.UrlConsumerService
import ir.sls.kafkaConsumer.service.url.UrlDatabaseService
import ir.sls.kafkaConsumer.service.url.UrlProcessService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class KafkaMock : DatabaseTest("/CreateTables.sql") {
    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun KafkaMocker() {
        mockkObject(ReadConfig)
        every { ReadConfig.config } returns Config(DataBase(), Kafka(), Spark(8001))
        val sep = "-----------------------------------------------------------------"
        val urldatabase = UrlDatabaseService()
        val urlProcess = UrlProcessService(urldatabase)
        val urlConsumer = spyk(UrlConsumerService(urlProcess))
        urlConsumer.processService = urlProcess
        urldatabase.setProperties(jdbcUrl, username, password, driver)
        val lr = arrayListOf<ConsumerRecord<String, String>>()
        val hm = HashMap<TopicPartition, List<ConsumerRecord<String, String>>>()
        fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start
        var norms: HashMap<Int, Int> = hashMapOf()
        var orgs: HashMap<Int, Int> = hashMapOf()
        var orgsCount = 0
        var k = 0
        for (i in 1..1000) {
            k++
            var n = (0..10).random()
            if (norms[n] == null)
                norms[n] = 1
            else
                norms[n] = norms[n]!!.plus(1)
            var ss = "["
            for (j in 0..(0..10).random()) {
                orgsCount++
                ss = ss + "\"$j\","
            }
            ss = ss.substring(0, ss.length - 1) + "]"
            val cr = ConsumerRecord<String, String>("1", 1, 1, UUID.randomUUID().toString(), "{\"count\":1,\"normalizedUrl\":\"${n}\",\"originalUrls\":$ss}")
            lr.add(cr)
        }
        hm[TopicPartition("t1", 1)] = lr
        every { urlConsumer.pollFromKafka() } returns ConsumerRecords(hm) andThen ConsumerRecords(emptyMap())
        thread {
            Thread.sleep(5000)
            val con = DriverManager.getConnection(jdbcUrl, username, password)
            val preparedStatement = con.createStatement()
            val preparedStatement3 = con.createStatement()
            val resultSet = preparedStatement.executeQuery("select count(id) from normalizedUrl;")
            val resultsets: ArrayList<ResultSet> = arrayListOf()
            for (i in 0..10) {
                val preparedStatement2 = con.createStatement()
                resultsets.add(preparedStatement2.executeQuery("select count from normalizedUrl where url=$i;"))
            }
            val resultSet3 = preparedStatement3.executeQuery("select sum(count) from normalizedUrl;")
            resultSet.next()
            resultSet3.next()
            println(sep)
            println("\t\tNorms")
            println(sep)
            println("Total count of ids :: ${resultSet.getInt(1)}")
            println(sep)
            for (i in 0..10) {
                resultsets[i].next()
                println("Count for $i :: ${resultsets[i].getInt(1)} from DB , Total generated :: ${norms[i]}")
            }
            println(sep)
            println("Total count from DB :: ${resultSet3.getInt(1)}")
            println("Total count from Sum :: $k")
            println(sep)
            println("\t\tOrgs")
            println(sep)
            val preparedStatement4 = con.createStatement()
            val resultSet4 = preparedStatement4.executeQuery("select count(id) from orginalUrl;")
            resultSet4.next()
            println("Total orginalUrl from DB :: ${resultSet4.getInt(1)} , Total generated :: $orgsCount")
            println(sep)

        }
        urlConsumer.start()


    }
}