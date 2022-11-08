package methods

import InlineButton
import SEARCH_LIST_URL
import TNetwork.Companion.TGet
import com.alibaba.fastjson2.to
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import data.SearchResult
import edit
import highlight
import isNotMe
import pureMessage
import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.util.*

val searchPageList = mutableListOf<Long>()
fun CallbackQueryHandlerEnvironment.afterSearch() {
    if (isNotMe(update)) return
    edit("少女祈祷中…")
    val searchKeyword = callbackQuery.message?.replyToMessage?.text!!
    val searchGroup: Long = parseLong(callbackQuery.data.split(",")[0])
    val endTimeParam = when (val statusFlag = callbackQuery.data.split(",")[1]) {
        "none" -> ""
        "prev" -> {
            val last = if (searchPageList.size > 1) {
                searchPageList[searchPageList.size - 2]
            } else 0L
            searchPageList.removeLast()
            if (last == 0L) "" else "&end=${last}"
        }
        else -> {
            searchPageList.add(parseLong(statusFlag))
            "&end=${statusFlag}"
        }
    }

    val searchResult =
        TGet("${SEARCH_LIST_URL}?q=${searchKeyword}&g=$searchGroup${endTimeParam}").to<SearchResult>()
    val resultGroupInfo = searchResult.groupinfo.getJSONArray(searchGroup.toString())
    val groupString = if (searchGroup == 0L) {
        "所有已索引群组"
    } else {
        "「${resultGroupInfo[1]}」" + if (resultGroupInfo[0] != null) "(@${resultGroupInfo[0]})" else ""
    }
    var resultString = "搜索范围：$groupString\n"
    if (searchResult.has_more) resultString =
        "_剩余搜索结果大于 10 条，请点击末尾按钮翻页搜索。_\n\n\n$resultString"
    searchResult.messages.forEachIndexed { index, message ->
        resultString += "=============${(index + 1)}=============\n\n"
        val timeLocal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        timeLocal.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
        resultString += "`【${message.from_name} 于 " + //每条大概宽限 380 个字符
                "${timeLocal.format(message.t * 1000)}】说：`\n" +
                message.html.pureMessage().highlight(searchKeyword) +
                "\n[（查看群组原消息）](https://t.me/c/${message.group_id}/${message.id})\n\n"
    }
    val inlineKeyboardMarkup: InlineKeyboardMarkup
    val buttonList = mutableListOf<InlineKeyboardButton.CallbackData>()
    if (searchPageList.size > 0) {
        buttonList.add(InlineButton("上一页", "$searchGroup,prev"))
    }
    if (searchResult.has_more) {
        buttonList.add(InlineButton("下一页", "$searchGroup,${searchResult.messages.last().t}"))
    }
    inlineKeyboardMarkup = InlineKeyboardMarkup.create(buttonList)
    if (buttonList.size == 0) {
        edit(resultString)
    } else {
        edit(resultString, replyMarkup = inlineKeyboardMarkup)
    }
}
