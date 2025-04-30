package unitTesting;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import chatRelay.Packet;
import chatRelay.actionType;

public class PacketTesting {
    
	// create test objects
    private static final String SENDER_ID = "user123";
    private static final String[] LOGIN_ARGS = {"username", "password"};
    private static final String[] MESSAGE_ARGS = {"Hello, this is a test message!", "chat001"};
    private static final String[] CREATE_CHAT_ARGS = {"[user1, user2, user3]", "Test Chat Room"};
    
    // create packets
    private Packet loginPacket;
    private Packet messagePacket;
    private Packet createChatPacket;
    
    @BeforeEach
    // set up test packets with action types
    public void setUp() {
        loginPacket = new Packet(actionType.LOGIN, LOGIN_ARGS, SENDER_ID);
        messagePacket = new Packet(actionType.SEND_MESSAGE, MESSAGE_ARGS, SENDER_ID);
        createChatPacket = new Packet(actionType.CREATE_CHAT, CREATE_CHAT_ARGS, SENDER_ID);
    }
    
    // test the constructor
    @Test
    public void testConstructor() {
        assertNotNull(loginPacket, "Packet should not be null");
        assertEquals(actionType.LOGIN, loginPacket.getActionType(), "Action type should match");
        assertArrayEquals(LOGIN_ARGS, loginPacket.getActionArguments(), "Action arguments should match");
        assertEquals(SENDER_ID, loginPacket.getSenderId(), "Sender ID should match");
        assertNotNull(loginPacket.getTimeCreated(), "Creation time should not be null");
    }
    
    // check the getactiontypes
    @Test
    public void testGetActionType() {
        assertEquals(actionType.LOGIN, loginPacket.getActionType(), "getActionType() should return LOGIN for login packet");
        assertEquals(actionType.SEND_MESSAGE, messagePacket.getActionType(), "getActionType() should return SEND_MESSAGE for message packet");
        assertEquals(actionType.CREATE_CHAT, createChatPacket.getActionType(), "getActionType() should return CREATE_CHAT for create chat packet");
    }
    
    // test getactionargument methods
    @Test
    public void testGetActionArguments() {
        assertArrayEquals(LOGIN_ARGS, loginPacket.getActionArguments(), "getActionArguments() should return correct login args");
        assertArrayEquals(MESSAGE_ARGS, messagePacket.getActionArguments(), "getActionArguments() should return correct message args");
        assertArrayEquals(CREATE_CHAT_ARGS, createChatPacket.getActionArguments(), "getActionArguments() should return correct create chat args");
    }
    
    // test getter for senderid
    @Test
    public void testGetSenderId() {
        assertEquals(SENDER_ID, loginPacket.getSenderId(), "getSenderId() should return the correct sender ID");
        assertEquals(SENDER_ID, messagePacket.getSenderId(), "getSenderId() should return the correct sender ID");
        assertEquals(SENDER_ID, createChatPacket.getSenderId(), "getSenderId() should return the correct sender ID");
    }
    
    // test gettimecreated method
    @Test
    public void testGetTimeCreated() {
        LocalTime now = LocalTime.now();
        LocalTime packetTime = loginPacket.getTimeCreated();
        
        // Time difference should be very small (within a few seconds)
        long secondsDiff = ChronoUnit.SECONDS.between(packetTime, now);
        assertTrue(secondsDiff < 5, "Packet creation time should be very close to current time");
    }
    
    // test the serialization 
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        // Serialize the packet
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(messagePacket);
        oos.close();
        
        // deserialze the packet
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Packet deserializedPacket = (Packet) ois.readObject();
        ois.close();
        
        // verify deserialized packet matches original
        assertEquals(messagePacket.getActionType(), deserializedPacket.getActionType(), "Action type should match after serialization");
        assertArrayEquals(messagePacket.getActionArguments(), deserializedPacket.getActionArguments(), "Action arguments should match after serialization");
        assertEquals(messagePacket.getSenderId(), deserializedPacket.getSenderId(), "Sender ID should match after serialization");
    }
    
    // test with empty action arguments
    @Test
    public void testEmptyActionArguments() {
        // create packet
        Packet emptyArgsPacket = new Packet(actionType.LOGOUT, new String[]{}, SENDER_ID);
        
        assertEquals(0, emptyArgsPacket.getActionArguments().length, "Action arguments array should be empty");
        assertEquals(actionType.LOGOUT, emptyArgsPacket.getActionType(), "Action type should be LOGOUT");
    }
    
    // test with null action arguments
    @Test
    public void testNullActionArguments() {
    	// create packet with null action args
        Packet nullArgsPacket = new Packet(actionType.LOGOUT, null, SENDER_ID);
        
        assertNull(nullArgsPacket.getActionArguments(), "Action arguments should be null");
    }
    
    // test sending null sender id
    @Test
    @DisplayName("Test with null sender ID")
    public void testNullSenderId() {
        Packet nullSenderPacket = new Packet(actionType.LOGOUT, new String[]{}, null);
        assertNull(nullSenderPacket.getSenderId(), "Sender ID should be null");
    }
    
    // test varied action types
    @Test
    public void testAllActionTypes() {

        Packet loginPacket = new Packet(actionType.LOGIN, LOGIN_ARGS, SENDER_ID);
        Packet logoutPacket = new Packet(actionType.LOGOUT, new String[]{}, SENDER_ID);
        Packet sendMessagePacket = new Packet(actionType.SEND_MESSAGE, MESSAGE_ARGS, SENDER_ID);
        Packet getAllChatsPacket = new Packet(actionType.GET_ALL_CHATS, new String[]{}, SENDER_ID);
        Packet getAllUsersPacket = new Packet(actionType.GET_ALL_USERS, new String[]{}, SENDER_ID);
        Packet createChatPacket = new Packet(actionType.CREATE_CHAT, CREATE_CHAT_ARGS, SENDER_ID);
        Packet successPacket = new Packet(actionType.SUCCESS, new String[]{"Operation completed successfully"}, SENDER_ID);
        Packet errorPacket = new Packet(actionType.ERROR, new String[]{"Error occurred"}, SENDER_ID);
        Packet createUserPacket = new Packet(actionType.CREATE_USER, new String[]{"username", "password", "firstName", "lastName"}, SENDER_ID);
        Packet enableUserPacket = new Packet(actionType.ENABLE_USER, new String[]{"user456"}, SENDER_ID);
        Packet disableUserPacket = new Packet(actionType.DISABLE_USER, new String[]{"user789"}, SENDER_ID);
        
        // verify all of these packets have action types
        assertEquals(actionType.LOGIN, loginPacket.getActionType());
        assertEquals(actionType.LOGOUT, logoutPacket.getActionType());
        assertEquals(actionType.SEND_MESSAGE, sendMessagePacket.getActionType());
        assertEquals(actionType.GET_ALL_CHATS, getAllChatsPacket.getActionType());
        assertEquals(actionType.GET_ALL_USERS, getAllUsersPacket.getActionType());
        assertEquals(actionType.CREATE_CHAT, createChatPacket.getActionType());
        assertEquals(actionType.SUCCESS, successPacket.getActionType());
        assertEquals(actionType.ERROR, errorPacket.getActionType());
        assertEquals(actionType.CREATE_USER, createUserPacket.getActionType());
        assertEquals(actionType.ENABLE_USER, enableUserPacket.getActionType());
        assertEquals(actionType.DISABLE_USER, disableUserPacket.getActionType());
    }
}