package data

import com.alibaba.fastjson2.JSONObject

data class SearchResult(
    val groupinfo: JSONObject,
    val has_more: Boolean,
    val messages: List<Message>
)

data class Message(
    val edited: Long,
    val from_id: Long,
    val from_name: String,
    val group_id: Long,
    val html: String,
    val id: Long,
    val t: Long
)