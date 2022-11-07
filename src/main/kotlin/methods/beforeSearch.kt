package methods

import GROUPS_LIST_URL
import InlineButton
import TNetwork.Companion.TGet
import com.alibaba.fastjson2.to
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import data.Groups
import isNotMe
import send

fun TextHandlerEnvironment.beforeSearch(){
    if (isNotMe(update)) return
    if (text == "/list") return
    val groups = TGet(GROUPS_LIST_URL).to<Groups>().groups
    var groupsButtonArray = arrayOf(
        listOf(InlineButton("所有已索引群组", (0L).toString()+",none"))
    )
    var tempList = mutableListOf<InlineKeyboardButton.CallbackData>()
    groups.forEachIndexed { index, group ->
        tempList.add(InlineButton(group.name, group.group_id.toString()+",none"))
        if (index % 2 != 0) {
            groupsButtonArray += tempList
            tempList = mutableListOf()
        } else if (index == groups.size - 1) {
            groupsButtonArray += tempList
            tempList = mutableListOf()
        }
    }
    val resultString = "您想要搜索的内容是「*${text}*」\n\n" +
            "请在以下 ${groups.size} 个群组中选择搜索范围：\n"
    val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
        *groupsButtonArray
    )
    searchPageList.clear()
    send(resultString, replyMarkup = inlineKeyboardMarkup)
}