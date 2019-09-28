package ShtrafyOnline

import main.Utils.HttpRequestType
import main.Utils.httpClient
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import ru.yamoney.test.hellper.retrieveJson


class ShtrafyOnlineSteps {

//    val closeableHttpClient by di<CloseableHttpClient>()

    fun uriRequest(
        uri: String = "https://api.shtrafy-gibdd.ru/api.php",
        httpRequestType: HttpRequestType = HttpRequestType.GET,
        headers: Map<String, String>? = null,
        params: Map<String, String>? = null,
        init: RequestBuilder.() -> Unit = {}
    ): HttpUriRequest {

        val builder = RequestBuilder.create(httpRequestType.name)
        with(builder) {
            setUri(uri)
            headers?.let { it.forEach { addHeader(it.key, it.value) } }
            params?.let { it.forEach { addParameter(it.key, it.value) } }
        }
        builder.init()
        return builder.build()
    }

    fun authorize(): String {
        return try {
            val response = httpClient.retrieveJson(
                request = uriRequest(
                    uri = "https://shtrafy-gibdd.ru/frontend/authorize"
                )
            )
            response
                .getJsonObject("data")
                .getJsonString("access_token")
                .string
        } catch (e: Exception){
            "service failed"
        }
    }

    fun getRrqsId(params: Map<String, String>): String {
        return try {
            val response = httpClient.retrieveJson(
                request = uriRequest(
                    params = params
                )
            )
            response
                .getJsonString("reqs_id")
                .string
        }
        catch(e: Exception){
            return "service failed"
        }
    }

    fun getBills(params: Map<String, String>): String {
        return try {
            val response = httpClient.retrieveJson(
                request = uriRequest(
                    params = params
                )
            )
            response.getJsonArray("fines").size.toString()
        }
        catch (e: Exception){
            return "service failed"
        }

    }
}
