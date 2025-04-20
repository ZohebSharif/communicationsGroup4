import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTesting {
    public void testServerConnect() {
    	Server server = new Server(1234, "nothing");
    	server.connect();
    }
}
