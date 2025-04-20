import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Boolean isConnected;
    private String targetIP;
    private String targetPort;
    private Chat[] chats;
    private User[] users;
    private String userId;
    private Boolean isITAdmin;

    public Client(String targetIp, String targetPort) {
        this.targetIP = targetIp;
        this.targetPort = targetPort;
    }

    public void testLogin() {
        try {
            socket = new Socket(targetIP, Integer.parseInt(targetPort));
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            String[] args = {"new Login"};
            Packet testPack = new Packet(Packet.actionType.LOGIN, args, "this.id");
            objectStream.writeObject(testPack);

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInStream = new ObjectInputStream(inputStream);
            Packet incoming = (Packet) objectInStream.readObject();
            if (incoming.getActionType().equals(Packet.actionType.SUCCESS)) {
                System.out.println("Packet Recieved");
            }
        } catch (IOException | NumberFormatException | ClassNotFoundException e) {
        }
    }
    
    public void login(String username, String password) {}
    public void sendMessage(String authorId, String chatId, String content) {}
    public void createChat(String chatName, Boolean isPrivate) {}
    public void updateState() {}
    public void createUser(String username, String password, String firstname, String lastname, Boolean isAdmin) {}
    public void enableUser(String userId) {}
    public void disableUser(String userId) {}
    public void saveChatToTxt(Chat chat) {}
    public void logout() {}
}
