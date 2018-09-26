package ir.sls.kafkaConsumer.service

import ir.sls.kafkaConsumer.config.ReadConfig
import ir.sls.kafkaConsumer.metric.InitMeter.getPerMinuteKafka
import ir.sls.kafkaConsumer.util.gson
import spark.Spark.port
import spark.kotlin.get

/**
 * fun [metricService] to register Spark server
 * @author Mohammad Hossein Liaghat E-mail : mohamadliaghat@gmail.com
 * @return json of metrics
 */
class SparkServer {
    fun metricService() {
        port(ReadConfig.config.spark.port)

        get("/metric") {

            val metricConfig: String = gson.toJson(getPerMinuteKafka())

            metricConfig
        }
    }
}