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

    public Client(String targetIp, String targetPort) {}
    
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
