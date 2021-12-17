import logging

from telethon import Button
from telethon.sync import TelegramClient, events

import config
from utils.search_utils import group_list, search_list
from utils.search_utils import groups_button_list

logging.basicConfig(format='[%(levelname) 5s/%(asctime)s] %(name)s: %(message)s',
                    level=logging.WARNING)

bot = TelegramClient('bot', config.api_id, config.api_hash).start(bot_token=config.bot_token)

global_event = None
query_page = ["0"]


@bot.on(events.CallbackQuery)
async def click_button(event):
    data = event.data.decode('utf-8').split('$')
    await event.edit("少女祈祷中…")
    reply_array = search_list(data, global_event.raw_text)

    button_pre_next = []
    global query_page
    if len(data) > 1:
        if len(query_page) > 1 and query_page[-2] == data[1]:
            query_page.pop()
        else:
            query_page.append(data[1])
    if reply_array[1] == 0 and len(query_page) == 1:  # 单页
        await event.edit(reply_array[0], link_preview=False)
    else:  # 多页
        if len(query_page) > 1:
            button_pre_next.append(Button.inline("上一页", data[0] + "$" + str(query_page[-2])))
        if reply_array[1] != 0:
            button_pre_next.append(Button.inline("下一页", data[0] + "$" + str(reply_array[1])))
        await event.edit(reply_array[0], buttons=button_pre_next, link_preview=False)


async def reply_msg(event, reply_msg, button_list=None):
    if event.sender_id == config.my_id:
        markup = None
        if button_list is not None:
            markup = button_list
        await event.reply(reply_msg, buttons=markup, link_preview=False)
    else:
        await event.reply("您并非此 Bot 的持有者，无法使用此功能")


@bot.on(events.NewMessage)
async def event_handler(event):
    if '/list' == event.raw_text or '、list' == event.raw_text:
        await reply_msg(event, group_list())
    else:
        global query_page
        query_page = ["0"]
        button_list = groups_button_list()
        list_length = (len(button_list) - 2) * 2 + len(button_list[-1])
        reply_word = "您想要搜索的内容是 「**" + event.raw_text + "**」\n\n" + \
                     "请在以下 " + str(list_length) + " 个群组中选择搜索范围：\n"
        global global_event
        global_event = event
        await reply_msg(event, reply_word, button_list)


print('开摆')
bot.run_until_disconnected()
