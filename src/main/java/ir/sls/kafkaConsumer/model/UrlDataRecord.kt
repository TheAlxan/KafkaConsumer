package ir.sls.kafkaConsumer.model

/**
 * The class of our data
 * @author Aryan Gholamlou
 *
 */
// Amin: originalUrls should be Set
data class UrlDataRecord(var normalizedUrl:String, var originalUrls:ArrayList<String>, var count:Int)