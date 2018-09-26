package ir.sls.kafkaConsumer.dao.urlDao

import ir.sls.kafkaConsumer.model.UrlDataRecord
import org.apache.commons.codec.digest.DigestUtils
import java.sql.Connection
import java.sql.PreparedStatement

/**
 * data access object of original urls. creates a batch of originalUrls and
 * then when the batch reaches to a specified value , persists the batch to database
 * @author Reza Varmazyari
 *
 */

class OriginalUrlDao {
    var con: Connection? = null
    private var preparedStatement:PreparedStatement? = null

    constructor(con: Connection){
        this.con = con
        preparedStatement = con?.prepareStatement("INSERT INTO orginalUrl (url,hash,normalizedUrl) VALUES (? , ? , ?) on duplicate key update hash = hash;")
    }

    fun persist(heap:ArrayList<UrlDataRecord>){
        heap.forEach{
            it.originalUrls.forEach { itt:String ->
                preparedStatement?.setString(1,itt)
                preparedStatement?.setString(2,DigestUtils.sha1Hex(itt))
                preparedStatement?.setString(3,DigestUtils.sha1Hex(it.normalizedUrl))
                preparedStatement?.addBatch()
            }
        }
        preparedStatement?.executeBatch()
    }
}