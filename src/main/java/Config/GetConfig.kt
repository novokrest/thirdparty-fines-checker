package Config

import java.io.File
import java.io.FileNotFoundException

class GetConfig {
    fun getConfig(): Map<String, String> {
        lateinit var file: File
        try {
            file = File("gisgmpmonitoring.cfg")
        } catch (e: FileNotFoundException) {
            println("Settings file gisgmpmonitoring.cfg not found")
            System.exit(1)
        }
        return file.readLines()
            .map { it.split("=") }
            .map { Pair(it[0], it[1]) }
            .toMap()
    }


    fun getPollingInterval(): Long = getConfig()["pollingInterval"].toString().toLong()
    fun getProxyHost() = getConfig()["proxyHost"].toString()
    fun getProxyPort() = getConfig()["proxyPort"].toString().toInt()
    fun getBotToken() = getConfig()["botToken"].toString()
    fun getChatId() = getConfig()["chatId"].toString()
    fun getGrzNum() = getConfig()["grzNum"].toString()
    fun getGrzRegion() = getConfig()["grzRegion"].toString()
    fun getSTS() = getConfig()["sts"].toString()
    fun isSuccessMessage() = getConfig()["isSuccessMessage"].toString().toBoolean()
    fun useProxy() = getConfig()["useProxy"].toString().toBoolean()


}