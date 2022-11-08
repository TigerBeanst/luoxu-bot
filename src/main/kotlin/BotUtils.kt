import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONReader
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import java.net.InetSocketAddress
import java.net.Proxy

fun String.containsEnhanced(vararg strs: String) = strs.any { this.contains(it) }

fun getProxy(): Proxy? {
    if (LOCAL_PROXY == "") return null
    val proxyType = if (LOCAL_PROXY.contains("http")) Proxy.Type.HTTP else Proxy.Type.SOCKS
    val proxyAddress = LOCAL_PROXY.substringAfter("://").substringBefore(":")
    val proxyPort = LOCAL_PROXY.substringAfter("://").substringAfter(":").toInt()
    return Proxy(proxyType, InetSocketAddress(proxyAddress, proxyPort))
}

fun JSONObject.getResultOrEmpty(result: String): String {
    if (this.containsKey(result)) return this.getString(result)
    return ""
}

inline fun <reified T> String.to(): T {
    return JSON.parseObject(this, T::class.java, JSONReader.Feature.IgnoreSetNullValue)
}

data class BotUpdate(
    val update: Update?,
    val bot: Bot?
)

fun Any.getBotUpdate(): BotUpdate {
    return when (this) {
        is CommandHandlerEnvironment -> {
            BotUpdate(this.update, this.bot)
        }

        is CallbackQueryHandlerEnvironment -> {
            BotUpdate(this.update, this.bot)
        }

        else -> {
            BotUpdate((this as TextHandlerEnvironment).update, this.bot)
        }
    }
}

fun Any.send(
    text: String,
    replyMarkup: ReplyMarkup? = null,
    disablePreview: Boolean? = true,
    isReply: Boolean = true
) {
    val botUpdate = getBotUpdate()
    botUpdate.bot!!.sendMessage(
        chatId = ChatId.fromId(botUpdate.update!!.message!!.chat.id),
        text = text,
        disableWebPagePreview = disablePreview,
        parseMode = ParseMode.MARKDOWN,
        replyMarkup = replyMarkup,
        replyToMessageId = if (isReply) botUpdate.update.message!!.messageId else null
    )
}

fun Any.edit(
    text: String,
    replyMarkup: ReplyMarkup? = null,
    disablePreview: Boolean? = true
) {
    val botUpdate = getBotUpdate()
    botUpdate.bot!!.editMessageText(
        chatId = ChatId.fromId(botUpdate.update!!.callbackQuery!!.message!!.chat.id),
        messageId = botUpdate.update.callbackQuery!!.message!!.messageId,
        text = text,
        disableWebPagePreview = disablePreview,
        parseMode = ParseMode.MARKDOWN,
        replyMarkup = replyMarkup
    )
}

fun Any.isNotMe(update: Update): Boolean {
    val id = if (update.message != null) {
        update.message!!.from!!.id
    } else {
        update.callbackQuery!!.from.id
    }
    if (id != MY_ID.toLong()) {
        send("您并非此 Bot 的持有者，无法使用此功能")
        return true
    }
    return false
}

fun InlineButton(text: String, data: String): InlineKeyboardButton.CallbackData {
    return InlineKeyboardButton.CallbackData(text = text, callbackData = data)
}

fun String.pureMessage(): String {
    var result = this
    var webpageFlag = false
    if (result.contains("\n[webpage]\nhttp")) {
        result = result.replace("\n[webpage]\n", "`【网页预览】")
        webpageFlag = true
    }
    result = result.replace("<[^>]+>".toRegex(), "").replace("\n", " ")
    return result + if (webpageFlag) "`" else ""
}

fun String.pureNoneMarkDown():String{
    return this.replace("*", "\\*")
        .replace("_", "\\_")
        .replace("__", "\\_\\_")
        .replace("[", "\\[")
        .replace("]", "\\]")
        .replace("(", "\\(")
        .replace(")", "\\)")
        .replace("`", "\\`")
}

fun String.highlight(keyword: String, length: Int = 380): String {
    if (keyword.containsEnhanced(" ", " OR ", " - ")
        || (keyword.contains("(") && keyword.contains(")"))
    ) return this.substring(0, length) //当遇到复杂的搜索条件时，不进行断句高亮，因为他们通常不会连在一起
    val result = if (this.length >= length) { // 整个消息过长时
        val position = this.indexOf(keyword)
        val startFrom = position - (length - keyword.length) / 2
        val endTo = position + (length - keyword.length) / 2
        val before = this.substringBefore(keyword).length
        val after = this.substringAfter(keyword).length

        if (before + keyword.length < length) {
            this.substring(0, length) + "..."
        } else if (after + keyword.length < length) {
            "..." + this.substring(this.length - length)
        } else {
            "..." + this.substring(startFrom, endTo) + "..."
        }
    } else {
        this
    }
    return result.pureNoneMarkDown().replace(keyword, "_${keyword}_")
}