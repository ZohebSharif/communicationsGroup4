 import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class ChatTest {
    
    private User owner;
    private User user1;
    private User user2;
    private Chat chat;
    
    @BeforeEach
    public void setUp() {
        // Create test users
        owner = new User("Zoheb", "password123", "id1", "Zoheb", "Sharif", false, false);
        user1 = new User("Talhah", "password456", "id2", "Talhah", "Shaik", false, false);
        user2 = new User("Kenny", "password789", "id3", "Kenny", "Kottenstette", false, false);
        
        // Create a test chat
        chat = new Chat(owner, "Test Chat Room");
    }
    
    @Test
    public void testChatConstructor() {
        // Test that the chat is initialized correctly
        assertEquals("Test Chat Room", chat.getRoomName());
        assertEquals(owner, chat.getOwner());
        assertFalse(chat.isPrivate());
        assertNotNull(chat.getId());
        assertTrue(chat.getId().startsWith("CHAT_"));
        
        // Test that the owner is added to chatters
        User[] chatters = chat.getChatters();
        assertEquals(1, chatters.length);
        assertEquals(owner, chatters[0]);
        
        // Test that messages array is initialized empty
        assertEquals(0, chat.getMessages().length);
    }
    
    @Test
    public void testAddChatter() {
        // Add a user to the chat
        chat.addChatter(user1);
        
        // Check if the user was added correctly
        User[] chatters = chat.getChatters();
        assertEquals(2, chatters.length);
        assertEquals(owner, chatters[0]);
        assertEquals(user1, chatters[1]);
        
        // Add another user
        chat.addChatter(user2);
        
        // Check if the second user was added correctly
        chatters = chat.getChatters();
        assertEquals(3, chatters.length);
        assertEquals(owner, chatters[0]);
        assertEquals(user1, chatters[1]);
        assertEquals(user2, chatters[2]);
    }
    
    @Test
    public void testRemoveChatter() {
        // Add users to the chat
        chat.addChatter(user1);
        chat.addChatter(user2);
        
        // Check initial state
        assertEquals(3, chat.getChatters().length);
        
        // Remove a user
        chat.removeChatter(user1);
        
        // Check if the user was removed correctly
        User[] chatters = chat.getChatters();
        assertEquals(2, chatters.length);
        assertEquals(owner, chatters[0]);
        assertEquals(user2, chatters[1]);
        
        // Try to remove the last user (should not remove)
        chat.removeChatter(user2);
        chat.removeChatter(owner);
        
        // Check that at least one user remains
        chatters = chat.getChatters();
        assertTrue(chatters.length >= 1);
    }
    
    @Test
    public void testAddMessage() {
        // Create a test message
        Message message1 = new Message("Hello, world!", owner);
        
        // Add the message to the chat
        chat.addMessage(message1);
        
        // Check if the message was added correctly
        Message[] messages = chat.getMessages();
        assertEquals(1, messages.length);
        assertEquals(message1, messages[0]);
        
        // Add another message
        Message message2 = new Message("How are you?", user1);
        chat.addMessage(message2);
        
        // Check if the second message was added correctly
        messages = chat.getMessages();
        assertEquals(2, messages.length);
        assertEquals(message1, messages[0]);
        assertEquals(message2, messages[1]);
    }
    
    @Test
    public void testChangePrivacy() {
        // Check initial state
        assertFalse(chat.isPrivate());
        
        // Change privacy to true
        chat.changePrivacy(true);
        assertTrue(chat.isPrivate());
        
        // Change privacy back to false
        chat.changePrivacy(false);
        assertFalse(chat.isPrivate());
    }
    
    @Test
    public void testToString() {
        // Test the toString method
        String chatString = chat.toString();
        
        // Verify that the string contains important information
        assertTrue(chatString.contains(chat.getId()));
        assertTrue(chatString.contains("Test Chat Room"));
        assertTrue(chatString.contains(owner.getUserName()));
        assertTrue(chatString.contains("private=false"));
        assertTrue(chatString.contains("chatters=1"));
    }
    
    public static void main(String[] args) {
        // This allows running the tests directly
        org.junit.platform.launcher.core.LauncherFactory
            .create()
            .execute(org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(org.junit.platform.engine.discovery.DiscoverySelectors.selectClass(ChatTest.class))
                    .build());
    }
}
