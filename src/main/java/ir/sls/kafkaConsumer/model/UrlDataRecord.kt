package ir.sls.kafkaConsumer.model

import com.google.inject.Inject

/**
 * The class of our data
 * @author Aryan Gholamlou
 *
 */

data class UrlDataRecord(var normalizedUrl:String, var originalUrls:ArrayList<String>, var count:Int)