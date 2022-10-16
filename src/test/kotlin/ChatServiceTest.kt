import Exception.ChatNotFoundException
import Exception.MessageNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun createMessageForChat() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(8 to 1, "hi")
        assertEquals(ChatService.chatList.size, 1)
    }

    @Test
    fun createMessageAndNewChat() {
        ChatService.createMessage(8 to 1, "hello")
        assert(ChatService.chatList.isNotEmpty())
    }

    @Test
    fun editMessageSuccess() {
        ChatService.createMessage(8 to 1, "hello")
        val result = ChatService.editMessage(1,0,"Hi test")
        assert(result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.editMessage(1,1,"Hi test")
    }

    @Test
    fun deleteMessage() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(8 to 1, "test")
        ChatService.deleteMessage(1)
        assertEquals(ChatService.chatList[8 to 1]?.messages?.size, 1)
    }

    @Test
    fun deleteMessageAndEmptyChat() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.deleteMessage(0)
        assertEquals(ChatService.chatList.size, 0)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.deleteMessage(1)
    }

    @Test
    fun deleteChatSuccess() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(4 to 1, "hello")
        ChatService.createMessage(3 to 1, "hello")
        ChatService.deleteChat(3 to 1)
        assertEquals(ChatService.chatList.size, 2)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.deleteChat(4 to 2)
    }

    @Test
    fun getChatsSuccess() {
        ChatService.createMessage(8 to 1, "hello")
        val result = ChatService.getChatsForUser(1)
        assert(result.isNotEmpty())
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatsError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.getChatsForUser(5)
    }

    @Test
    fun getUnreadChatsCountSuccess() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(8 to 2, "test1")
        ChatService.createMessage(8 to 4, "test2")
        val result = ChatService.getUnreadChatsCount(8)
        assertEquals(result, 3)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getUnreadChatsCountError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(8 to 2, "test1")
        ChatService.createMessage(8 to 4, "test2")
        ChatService.getUnreadChatsCount(5)
    }

    @Test
    fun getChatMessageSuccess() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.createMessage(8 to 1, "test1")
        ChatService.createMessage(8 to 1, "test2")
        val result = ChatService.getChatMessages(8 to 1, 0, 4)
        assert(result.isNotEmpty())
    }

    @Test(expected = MessageNotFoundException::class)
    fun getChatMessageError() {
        ChatService.createMessage(8 to 1, "hello")
        ChatService.getChatMessages(4 to 2, 0, 4)
    }

    @Test
    fun readMessageAfterGet() {
        ChatService.createMessage(8 to 1, "hello")
        val result = ChatService.getChatMessages(8 to 1, 0, 4)
        assert(result[0].isRead)
    }
}