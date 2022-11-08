import java.io.File

var MY_ID = 0L
var BOT_TOKEN: String = ""
var SHOW_LOG: Boolean = false
var LOCAL_PROXY = ""
var BASE_URL = "http://localhost:9008"
var BASE_URL_PREFIX = "luoxu"
var GROUPS_LIST_URL = "$BASE_URL/$BASE_URL_PREFIX/groups"
var SEARCH_LIST_URL = "$BASE_URL/$BASE_URL_PREFIX/search"

fun configInit() {
    val fileName = "config.yaml"
    val lines: List<String> = File(fileName).readLines()
    lines.forEach {
        if (it != "") {
            val value = it.substringAfter(":").trim()
            with(it) {
                when {
                    startsWith("my-id:") -> MY_ID = value.toLong()
                    startsWith("bot-token:") -> BOT_TOKEN = value
                    startsWith("show-log:") -> SHOW_LOG = value.toBoolean()
                    startsWith("local-proxy:") -> LOCAL_PROXY = value
                    startsWith("base-url:") -> BASE_URL = value
                    startsWith("base-url-prefix:") -> BASE_URL_PREFIX = value
                }
            }
        }
    }
    GROUPS_LIST_URL = "$BASE_URL/$BASE_URL_PREFIX/groups"
    SEARCH_LIST_URL = "$BASE_URL/$BASE_URL_PREFIX/search"

    println("读取到 my-id：$MY_ID")
    println("读取到 bot-token：$BOT_TOKEN")
    println("读取到 local-proxy：$LOCAL_PROXY")
    println("读取到 base-url：$BASE_URL")
    println("读取到 base-url-prefix：$BASE_URL_PREFIX")
    println("读取到 groups-list-url：$GROUPS_LIST_URL")
    println("读取到 search-list-url：$SEARCH_LIST_URL")
    println("开始运行")
}