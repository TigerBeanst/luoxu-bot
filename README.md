# luoxu-bot
luoxu-bot 是依赖于 [luoxu](https://github.com/lilydjwg/luoxu) 后端的 Telegram Bot，以在不公开 luoxu 接口的情况下快速进行 CJK 消息的查询。使用 Kotlin 重构。

# 配置
- 前往 [luoxu](https://github.com/lilydjwg/luoxu) 根据相关内容配置并运行 luoxu，等待**消息索引完毕**
> **由于 Telegram 的消息样式问题，建议修改 luoxu 项目中的 `luoxu/db.py` 文件，将 `SEARCH_LIMIT` 修改为 `10`**

- 前往 [GitHub Releases](https://github.com/TigerBeanst/luoxu-bot/releases) ， 下载获得 `luoxu-bot-?.?-all.jar` 文件；
- 在 `luoxu-bot-?.?-all.jar` 同一个目录下创建 `config.yaml` 文件，内容在后文；
- 上传至服务器（当然也可以本地），运行 `java -jar luoxu-bot-?.?-all.jar` 即可，请使用 `screen` 等程序保持 Bot 在终端关闭后亦持续运行，程序本身不处理后台运行事项。

# config.yaml
```yaml
my-id: 19260817
bot-token: 1111122222:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
local-proxy: 

base-url: http://localhost:9008
base-url-prefix: luoxu
```
1. `my-id` 为你的 Telegram 账号 ID，向 [@getidsbot](https://t.me/getidsbot) 发送 `/start` 可以获取
2. `bot-token` 为你的 Bot Token，向 [@BotFather](https://t.me/botfather) 申请 Bot，获取 `bot-token`（由`数字:数字字母`构成，如`1111122222:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA`），获取完毕后记得向 Bot 本身发送 `/start` 之类的消息以让其获悉你的 id
3. `local-proxy` 为你的代理，如 `socks5://127.0.0.1:7890`，留空则不使用代理
4. `base-url` 为你的 luoxu 后端地址，如 `http://localhost:9008`
5. `base-url-prefix` 为你的 luoxu 后端地址前缀，如 `luoxu`

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
- 可以使用已经配置在公网上的 API 吗？当然可以，但是这个项目出现的原因就是因为不想 API 公开在公网上
- 本项目没有代理字段的配置。经过各方面的考虑~~以及我懒得再改~~，我认为一般需要部署此项目的机器都已经能正常连接 Telegram

# 为什么会有这个项目
luoxu 是一个很棒的项目，在此感谢 [@lilydjwg](https://github.com/lilydjwg) （

luoxu 提供了 API 接口以供前端使用，但这也带来一个问题，如果索引的内容存在不想公开的私密群组，那么要不就将整套前后端部署在自己的内网上，要不就需要通过设置 Base Auth 等方式在保护接口（

所以最后我选择摸一个 Telegram Bot（（（（

# 鸣谢
- [@lilydjwg](https://github.com/lilydjwg) ，给您磕头！！！！！！
- [@kotlin-telegram-bot/kotlin-telegram-bot](https://github.com/kotlin-telegram-bot/kotlin-telegram-bot)
- 我那会使用搜索引擎的双手和大脑
