import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.logging.LogLevel
import methods.afterSearch
import methods.beforeSearch
import methods.getGroupList

fun main() {
    configInit()
    val bot = bot {
        token = BOT_TOKEN
        if(getProxy() != null) {
            proxy = getProxy()!!
        }
        dispatch {
            command("list") { getGroupList() }
            text() { beforeSearch() }
            callbackQuery { afterSearch() }
        }
        logLevel = if (SHOW_LOG) LogLevel.All() else LogLevel.None
    }
    bot.startPolling()
}


