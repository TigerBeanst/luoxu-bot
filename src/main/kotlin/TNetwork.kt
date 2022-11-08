import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class TNetwork {
    companion object {
        private val client = HttpClient(CIO) {
            install(HttpTimeout){
                requestTimeoutMillis = 100000
                requestTimeoutMillis = 100000
            }
            engine {
                proxy = getProxy()
            }
        }
        fun TGet(url: String): String {
            println("GET $url")
            var result = ""
            runBlocking {
                val response: HttpResponse = client.get(url)
                result = response.body()
            }
            return result
        }

        fun TPost(url: String, bodyJson: String): String {
            println("POST $url")
            var result = ""
            runBlocking {
                val response: HttpResponse =
                    client.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(bodyJson)
                    }
                result = response.body()
            }
            return result
        }
    }
}