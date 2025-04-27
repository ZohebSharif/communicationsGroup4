package chatRelay;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTesting {
    
    private Message message;
    private Message messageWithoutChat;
    private User user;
    private Chat chat;
    private final String MESSAGE_ID = "123";
    private final long TIMESTAMP = System.currentTimeMillis();
    private final String CONTENT = "Hello, this is a test message!";
    
    @BeforeEach
    public void setUp() {
        // create a test
        user = new User("testuser", "password", "USER_123", "Zoheb", "Sharif", false, false);
        
        // create a test chat
        chat = new Chat(user, "Test Chat", "CHAT_123", null);
        
        // create a a test message with other constructor
        message = new Message(MESSAGE_ID, TIMESTAMP, CONTENT, user, chat);
        
        // create a test message
        messageWithoutChat = new Message(CONTENT, user);
    }
    
    @Test
    public void testFullConstructor() {
    	//verify message was created with attributes using full constructor
        assertNotNull(message, "Message should not be null");
        assertEquals(MESSAGE_ID, message.getId(), "Message ID should match");
        assertEquals(TIMESTAMP, message.getCreatedAt(), "Timestamp should match");
        assertEquals(CONTENT, message.getContent(), "Content should match");
        assertEquals(user, message.getSender(), "Author should match");
        assertEquals(chat, message.getChat(), "Chat should match");
    }
    
    @Test
    public void testSimpleConstructor() {
    	// verify the message was created with all attributes using simple constructor
        assertNotNull(messageWithoutChat, "Message should not be null");
        assertNotNull(messageWithoutChat.getId(), "ID should be generated");
        assertTrue(messageWithoutChat.getCreatedAt() > 0, "Timestamp should be set");
        assertEquals(CONTENT, messageWithoutChat.getContent(), "Content should match");
        assertEquals(user, messageWithoutChat.getSender(), "Author should match");
        assertNull(messageWithoutChat.getChat(), "Chat should be null");
    }
    
    @Test
    public void testIdGeneration() {
    	// make two messages in a row to test ID generation
        Message firstMsg = new Message("First message", user);
        Message secondMsg = new Message("Second message", user);
        
        // IDs should be unique and sequential
        assertNotEquals(firstMsg.getId(), secondMsg.getId(), "Message IDs should be different");
        
        // converts to integers since IDs are strings
        int firstId = Integer.parseInt(firstMsg.getId());
        int secondId = Integer.parseInt(secondMsg.getId());
        assertEquals(firstId + 1, secondId, "Second ID should be one more than first ID");
    }
    
    @Test
    public void testGetId() {
        assertEquals(MESSAGE_ID, message.getId(), "getId() should return the correct message ID");
    }
    
    @Test
    public void testGetCreatedAt() {
        assertEquals(TIMESTAMP, message.getCreatedAt(), "getCreatedAt() should return the correct timestamp");
    }
    
    @Test
    public void testGetContent() {
        assertEquals(CONTENT, message.getContent(), "getContent() should return the correct message content");
    }
    
    @Test
    public void testGetSender() {
        assertEquals(user, message.getSender(), "getSender() should return the correct user");
    }
    
    @Test
    public void testGetChat() {
        assertEquals(chat, message.getChat(), "getChat() should return the correct chat");
    }
    
    @Test
    public void testToString() {
        String expected = CONTENT + " (from: " + user.getUserName() + " at: " + TIMESTAMP + ")";
        assertEquals(expected, message.toString(), "toString() should return correctly formatted string");
    }
    
    @Test
    public void testMessageInChat() {
    	// add message to chat
        chat.addMessage(message);
        
        // verify message was put into chat
        assertTrue(chat.getMessages().contains(message), "Message should be in the chat's message list");
    }
}