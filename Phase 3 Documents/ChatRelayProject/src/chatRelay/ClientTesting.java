package chatRelay;

import org.junit.jupiter.api.Test;

public class ClientTesting {
	@Test
    public void testLogin() {
    	Client client = new Client("localhost", "1234");
    	client.testLogin();
    }
}
