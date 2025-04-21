package chatRelay;

import org.junit.jupiter.api.Test;

public class ServerTesting {
    public static void testServerConnect() {
    	Server server = new Server(1234, "nothing");
    	server.connect();
    }
    
    public static void main(String[] args) {
		testServerConnect();
	}
}
