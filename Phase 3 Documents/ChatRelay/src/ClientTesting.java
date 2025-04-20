import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTesting {
	@Test
    public void testLogin() {
    	Client client = new Client("localhost", "1234");
    	client.testLogin();
    }
}
