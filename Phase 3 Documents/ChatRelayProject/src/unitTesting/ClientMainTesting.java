package unitTesting;

//import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.Test;

import chatRelay.Client;

public class ClientMainTesting {
//    @Test
    public static void testLogin() {
        Client client = new Client("192.168.4.213", "1234");
        client.startUp();
    }
    
    public static void main(String[] args) {
        testLogin();
    }
}