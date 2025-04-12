import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String userId;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(Socket socket, Server server) {}

    public void start() {}
    public void stop() {}
    public void setUserId(String userId) {}
    public String getUserId() {return userId;}
    public ObjectInputStream getInputStream() {return inputStream;}
    public ObjectOutputStream getOutputStream() {return outputStream;}
    public void sendPacket(Packet packet) {}
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
