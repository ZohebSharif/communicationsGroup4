package chatRelay;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTesting {
    
    private User testAuthor;
    private Chat testChat;
    private Message testMessage;
    
    @BeforeEach
    public void setUp() {
        // create user and chat
        testAuthor = new User("testUser", "password123", "u1", "Test", "User", false, false);
        testChat = new Chat(testAuthor, "Test Chat Room");
        
        // create a message with constructor
        testMessage = new Message("MSG_1", 123456789L, "Hello World", testAuthor, testChat);
    }
    
    @Test
    public void testConstructorFields() {
        assertEquals("MSG_1", testMessage.getId());
        assertEquals("Hello World", testMessage.getContent());
        assertEquals(123456789L, testMessage.getCreatedAt());
        assertEquals(testAuthor, testMessage.getSender());
        assertEquals(testChat, testMessage.getChat());
    }
    
    @Test
    public void testToStringFormat() {
        String result = testMessage.toString();
        assertTrue(result.contains("Hello World"));
        assertTrue(result.contains("from: testUser"));
        assertTrue(result.contains("at: 123456789"));
    }

    public static void main(String[] args) {
        org.junit.platform.launcher.core.LauncherFactory
            .create()
            .execute(org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(org.junit.platform.engine.discovery.DiscoverySelectors.selectClass(MessageTesting.class))
                    .build());
    }
}
