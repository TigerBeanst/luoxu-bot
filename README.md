# luoxu-bot
luoxu-bot 是类似于 [luoxu-web](https://github.com/lilydjwg/luoxu-web) 的 CJK 友好的 Telegram Bot，依赖于 [luoxu](https://github.com/lilydjwg/luoxu) 所创建的后端。

# 测试环境
- Python 3.7.9
- pip 21.1.2
> 开发中使用到的 Telethon 需要 Python 3+

# 配置
- 前往 [luoxu](https://github.com/lilydjwg/luoxu) 根据相关内容配置并运行 luoxu，等待**消息索引完毕**
> **由于 Telegram 的消息样式问题，建议修改 luoxu 项目中的 `luoxu/db.py` 文件，将 `SEARCH_LIMIT` 修改为 `10`**

- 克隆 [本项目](https://github.com/TigerBeanst/luoxu-bot) ， 并使用 `pip3 install -r requirements.txt` 等方式安装依赖
- 前往 [My Telegram](https://my.telegram.org/) ,获取 `API development tools` 中的 `api_id` 和 `api_hash`
- 在 Telegram 中向 [@BotFather](https://t.me/botfather) 申请 Bot，获取 `bot token`（由`数字:数字字母`构成，如`1111122222:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA`）
- 在 Telegram 中向 [@getidsbot](https://t.me/getidsbot) 发送 `/start` 以获取你的 Telegram 账号 ID（`my_id`）
- 打开 `config.py` 并填写相关字段（如果你在 luoxu 中修改了接口的端口或者前缀等，请同步修改）
- 使用 `python3 main.py` 运行项目，当出现 `开摆` 时代表项目已启动，可以向自己的 Bot 尝试发送指令（请提前向自己的 Bot 激活 `/start`）

# 使用
- `/list` 获取已索引群组的列表和相关信息

![https://s2.loli.net/2021/12/18/KDh76ai2O5ENqwS.png](https://s2.loli.net/2021/12/18/KDh76ai2O5ENqwS.png)

- 直接发送关键词，如 `我是垃圾`，其后通过点选对应的群组或「所有已索引群组」

![https://s2.loli.net/2021/12/18/JP3FVSzOAHofuYD.png](https://s2.loli.net/2021/12/18/JP3FVSzOAHofuYD.png)

- 搜索结果，点击「查看群组原消息」可以直接跳转到对应消息位置：

![https://s2.loli.net/2021/12/18/s6Xl4kNmLYDiMja.png](https://s2.loli.net/2021/12/18/s6Xl4kNmLYDiMja.png)

另外简单测试了一下，luoxu 作者提供的搜索字符串的功能应该是工作正常的（毕竟我没去注意过 URL 里的空格啥的……）
> 搜索消息时，搜索字符串不区分简繁（会使用 OpenCC 自动转换），也不进行分词（请手动将可能不连在一起的词语以空格分开）。
>
> 搜索字符串支持以下功能：
> 
> 1.以空格分开的多个搜索词是「与」的关系
> 
> 2.使用 `OR`（全大写）来表达「或」条件
> 
> 3.使用 `-` 来表达排除，如 `落絮 - 测试`
> 
> 4.使用小括号来分组

# 其他
- **搜索结果只有一页时，可能会有多余的「下一页」按钮，问题不大，懒得修了**
- 部分数据量过大的消息可能会让 Bot 一直保持在「少女祈祷中…」，建议换个详细点的词。一般是搜什么 test 之类的很容易出现在一条长消息里的（有人在群里直接粘贴发带了关键词的 log 之类的）
- 请使用 `screen` 等程序保持 Bot 在终端关闭后亦持续运行
- 可以使用已经配置在公网上的 API 吗？当然可以，但是这个项目出现的原因就是因为不想公开在公网上
- 本项目没有代理字段的配置。经过各方面的考虑~~以及我懒得再改~~，我认为一般需要部署此项目的机器都已经能正常连接 Telegram

# 为什么会有这个项目
luoxu 是一个很棒的项目，在此感谢 [@lilydjwg](https://github.com/lilydjwg) （

luoxu 提供了 API 接口以供前端使用，但这也带来一个问题，如果索引的内容存在不想公开的私密群组，那么要不就将整套前后端部署在自己的内网上，要不就需要通过设置 Base Auth 等方式在保护接口（

所以最后我选择摸一个 Telegram Bot（（（（

# 鸣谢
- [@lilydjwg](https://github.com/lilydjwg) ，给您磕头！！！！！！
- [@LonamiWebs/Telethon](https://github.com/LonamiWebs/Telethon)
- 我那会使用搜索引擎的双手和大脑
