import time
import requests
from telethon import Button
import config


def group_list():
    response = requests.get(config.GROUPS_LIST_URL).json()["groups"]
    reply_msg = "__此 Bot 关联的群组有以下 " + str(len(response)) + " 个：__\n\n"
    for group in response:
        reply_msg += "【**" + group["name"] + "**】\n"
        if group["pub_id"] is not None:
            reply_msg += "群组公开链接：[@" + \
                         str(group["pub_id"]) + "](https://t.me/" + str(group["pub_id"]) + ")" + \
                         "\n"
        reply_msg += "群组 ID：`" + str(group["group_id"]) + "`\n\n"
    return reply_msg


def groups_button_list():
    response = requests.get(config.GROUPS_LIST_URL).json()["groups"]
    button_list = [[Button.inline("所有已索引群组", str(0))]]
    for i, group in enumerate(response):
        one_button = Button.inline(group["name"], str(group["group_id"]))
        if i % 2 != 0:
            button_list[len(button_list) - 1].append(one_button)
        else:
            button_list.append([one_button])
    return button_list


def search_list(data, raw_text):
    url = config.SEARCH_LIST_URL + "?q=" + raw_text
    if data[0] != 0:
        url += "&g=" + data[0]
    if len(data) > 1 and data[1] != "0":
        url += "&end=" + data[1]

    print(url)
    msg_response = requests.get(url).json()
    msgs = msg_response["messages"]
    reply_msg = "搜索范围："
    reply_msg += ("「" + msg_response["groupinfo"][str(data[0])][1] + "」\n") if data[0] != "0" else "所有已索引群组\n"
    end_msg_t = "0"
    for i, msg in enumerate(msgs):
        msg["id"] = int(msg["id"])
        msg["from_id"] = int(msg["from_id"])
        msg["group_id"] = int(msg["group_id"])
        msg["t"] = int(msg["t"])
        time_string = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(msg["t"]))
        msg_md = msg["html"].replace("<span class=\"keyword\">", "__").replace("</span>", "__")
        msg_link = "https://t.me/c/" + str(msg["group_id"]) + "/" + str(msg["id"])
        group_name = (" " + msg_response["groupinfo"][str(msg["group_id"])][1] + " ") if data[0] == "0" else ""
        reply_msg += "============= " + str(i + 1) + " =============\n\n"
        reply_msg += "`【" + msg["from_name"] + " 于 " + time_string + "】说：`\n" + \
                     msg_md + \
                     "\n[（查看群组" + group_name + "原消息）](" + msg_link + ")\n\n"
        # print(reply_msg)
    if msg_response["has_more"]:
        end_msg_t = msgs[-1]["t"]
        reply_msg = "__剩余搜索结果大于 10 条，请点击末尾按钮翻页搜索。__\n\n\n" + reply_msg
    reply_array = [reply_msg, end_msg_t]
    return reply_array
