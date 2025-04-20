package chatRelay;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import chatRelay.Packet.actionType;

public class ClientTesting {
	@Test
    public void testLogin() {
    	Client client = new Client("localhost", "1234");
    	client.testLogin();
    	
    }
}
