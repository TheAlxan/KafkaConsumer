package ir.sls.kafkaConsumer.service.url

import ir.sls.kafkaConsumer.config.ReadConfig
import ir.sls.kafkaConsumer.dao.urlDao.NormalizedUrlDao
import ir.sls.kafkaConsumer.dao.urlDao.OriginalUrlDao
import ir.sls.kafkaConsumer.metric.InitMeter
import ir.sls.kafkaConsumer.model.UrlDataRecord
import ir.sls.kafkaConsumer.service.base.DatabaseService
import mu.KotlinLogging
import java.sql.SQLException


/**
 * persisting a hashmap of dataRecords into database
 * @param  HashMap of dataRecords
 * @exception <SQLException>.
 * @author Reza Varmazyari
 */

class UrlDatabaseService : DatabaseService<UrlDataRecord>()
{

    private val logger = KotlinLogging.logger {}

    private var jdbcUrl:String = ""
    private var username:String = ""
    private var password:String = ""
    private var driver:String = ""
    private fun readConfig(){
        jdbcUrl = ReadConfig.config.dataBase.jdbcUrl
        username = ReadConfig.config.dataBase.username
        password = ReadConfig.config.dataBase.password
        driver = ReadConfig.config.dataBase.driver
    }

    init
    {
        //readConfig()
        //DBConnection.setProperties(driver, jdbcUrl, username, password)
    }

    fun setProperties(jdbcUrl2: String, username2: String, password2: String, driver2: String)
    {
        jdbcUrl = jdbcUrl2
        username = username2
        password = password2
        driver = driver2
        DBConnection.setProperties(driver, jdbcUrl, username, password)
    }


    override fun persistData(heap: List<UrlDataRecord>): Boolean
    {
        var allSaveSuccess = false

        var con = DBConnection.getConnection()
        var timeOut = 1000L
        while (con == null)
        {
            con = DBConnection.getConnection()
            if (con != null)
                break

            Thread.sleep(timeOut)
            timeOut *= 2
            if (timeOut == ReadConfig.config.dataBase.databaseConnectionMaxTimeout)
                timeOut = 1000
        }

        val normalizedUrlDao = NormalizedUrlDao(con!!)
        val originalUrlDao = OriginalUrlDao(con)
        try
        {
            con.autoCommit = false
            normalizedUrlDao.persist(heap)
            originalUrlDao.persist(heap)
            con.commit()
            InitMeter.markDatabaseWrite(heap.size.toLong())
            allSaveSuccess = true
        }
        catch (e: KotlinNullPointerException)
        {
            logger.error(e) { "Failed to create connection to database" }
        }
        catch (e: SQLException)
        {
            con.rollback()
            logger.error(e) { "Failed to write in database" }
        }
        finally
        {
            con.close()
        }

        return allSaveSuccess
    }

}
