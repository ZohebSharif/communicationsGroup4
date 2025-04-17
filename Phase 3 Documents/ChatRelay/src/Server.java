import java.util.HashMap;

public class Server {
    private HashMap<userId, ClientHandler> clients;
    private DBManager dbManager;
    private int port;
    private String IP;

    public Server(int port, String IP) {
        this.port = port;
        this.IP = IP;
    }

    public void connect() {}
    public void disconnect() {}
    public void recievePacket(String clientId, Packet packet) {}
    public void sendErrorMessage(String userId, String errorMessage) {}
    public void sendSuccessMessage(String userId, String successMessage) {}
    public void sendPacketToUsers(Packet packet, String[] userIds) {}
}
