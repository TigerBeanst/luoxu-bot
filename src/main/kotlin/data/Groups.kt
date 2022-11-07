package data

data class Groups(
    val groups: List<Group>
)

data class Group(
    val group_id: Long,
    val name: String,
    val pub_id: String?
)