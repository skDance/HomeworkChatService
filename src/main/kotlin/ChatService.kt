import Exception.ChatNotFoundException
import Exception.MessageNotFoundException

object ChatService {
    var chatList = mutableMapOf<Pair<Int, Int>, Chat>()
    var messageIdCount = 0

    fun clear() {
        chatList = mutableMapOf<Pair<Int, Int>, Chat>()
        messageIdCount = 0
    }

    fun createMessage(usersChat: Pair<Int, Int>, text: String) {
        when {
            chatList[usersChat] != null -> chatList[usersChat]?.messages?.add(
                Message(
                    messageIdCount++,
                    usersChat.first,
                    usersChat.second,
                    text
                )
            )

            chatList[Pair(usersChat.second, usersChat.first)] != null -> chatList[Pair(
                usersChat.second,
                usersChat.first
            )]?.messages?.add(Message(messageIdCount++, usersChat.second, usersChat.first, text))

            else -> {
                val newChat =
                    Chat().also { it.messages.add(Message(messageIdCount++, usersChat.first, usersChat.second, text)) }
                chatList[usersChat] = newChat
            }
        }
    }

    fun editMessage(userId: Int, messageId: Int, text: String): Boolean {
        chatList.forEach { (key, chat) ->
            chat.messages.forEach {
                if (it.messageId == messageId && (key.first == userId || key.second == userId)) {
                    it.text = text
                    return true
                }
            }
        }
        throw MessageNotFoundException("Message not found")
    }

    fun deleteMessage(messageId: Int): Boolean {
        chatList.forEach { (key, chat) ->
            chat.messages.forEach {
                if (it.messageId == messageId) {
                    chat.messages.remove(it)
                    if (chat.messages.isEmpty()) {
                        chatList.remove(key)
                    }
                    return true
                }
            }
        }
        throw MessageNotFoundException("Message not found")
    }

    fun deleteChat(userChat: Pair<Int, Int>): Boolean {
        chatList.forEach { (key) ->
            if (key == userChat || (key.second == userChat.first && key.first == userChat.second)) {
                chatList.remove(key)
                return true
            }
        }
        throw ChatNotFoundException("No chat with users: $userChat")
    }

    fun getChatsForUser(userId: Int): MutableMap<Pair<Int, Int>, Chat> {
        var getChatList = mutableMapOf<Pair<Int, Int>, Chat>()
        chatList.forEach {
            if (it.key.first == userId || it.key.second == userId) getChatList[it.key] = it.value
        }
        if (getChatList.isNotEmpty()) return getChatList else throw ChatNotFoundException("No chat with user: $userId")
    }

    fun getUnreadChatsCount(userId: Int): Int {
        var unreadChatCount = 0
        chatList.forEach forEachChat@{ (key, chat) ->
            if (key.first == userId || key.second == userId) {
                chat.messages.forEach {
                    if (!it.isRead) unreadChatCount++
                    return@forEachChat
                }
            }
        }
        if (unreadChatCount != 0) {
            println("You have $unreadChatCount unread chats")
            return unreadChatCount
        } else throw ChatNotFoundException("No unread chat with user: $userId")
    }

    fun getChatMessages(userChat: Pair<Int, Int>, messageOffset: Int, count: Int): MutableList<Message> {
        var getMessagesList = mutableListOf<Message>()
        chatList.forEach { (key, chat) ->
            if (key == userChat || (key.second == userChat.first && key.first == userChat.second)) {
                chat.messages.forEach {
                    if (it.messageId >= messageOffset && getMessagesList.size < count) {
                        getMessagesList += it
                        it.isRead = true
                    }
                }
            }
        }
        if (getMessagesList.isNotEmpty()) return getMessagesList else throw MessageNotFoundException("Messages not found")
    }

}