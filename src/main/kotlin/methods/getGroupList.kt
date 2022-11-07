package methods

import GROUPS_LIST_URL
import TNetwork.Companion.TGet
import com.alibaba.fastjson2.to
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import data.Groups
import isNotMe
import send

fun CommandHandlerEnvironment.getGroupList(){
    if (isNotMe(update)) return
    val groups = TGet(GROUPS_LIST_URL).to<Groups>().groups
    var result = "_此 Bot 关联的群组有以下 ${groups.size} 个：_\n\n"
    groups.forEach {
        result += "【*${it.name}*】\n"
        if (it.pub_id != null) {
            result += "群组公开链接：[@${it.pub_id}](https://t.me/${it.pub_id})\n"
        }
        result += "群组 ID：`${it.group_id}`\n\n"
    }
    send(result)
}