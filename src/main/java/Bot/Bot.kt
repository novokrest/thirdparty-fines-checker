package Bot

import Config.GetConfig
import java.io.BufferedInputStream
import java.net.*

class Bot {
    private fun botConnection(message: String): URLConnection {
        var urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
        val apiToken = GetConfig().getBotToken()
        val chatId = GetConfig().getChatId()
        urlString = String.format(urlString, apiToken, chatId, message)
        val url = URL(urlString)
        val proxyHost = GetConfig().getProxyHost()
        val proxyPort = GetConfig().getProxyPort()

        return if (GetConfig().useProxy()){
            url.openConnection(
                Proxy(
                    Proxy.Type.SOCKS,
                    InetSocketAddress(
                        proxyHost,
                        proxyPort
                    )
                )
            )
        } else {
            return url.openConnection()
        }
    }
    fun sendMessage(message: String) {
        var sent = 0
        while (sent == 0){
            try {
                val conn = botConnection(message) as HttpURLConnection
                println(conn.url.toString())
                println(conn.usingProxy())
                BufferedInputStream(conn.inputStream)
                sent = 1
            }
            catch (e: Exception){
                println(e.message)
            }
        }
    }
}