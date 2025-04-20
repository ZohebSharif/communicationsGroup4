package chatRelay;

import org.junit.jupiter.api.Test;

public class ServerTesting {
    public void testServerConnect() {
    	Server server = new Server(1234, "nothing");
    	server.connect();
    }
}
