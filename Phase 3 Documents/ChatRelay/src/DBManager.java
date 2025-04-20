public class DBManager {
    
    private HashMap<userId, User> users;
    private HashMap<chatId, Chat> chats;
    private HashMap<messageId, Message> messages;
    private Server server;
    private String txtFilePath;
    private String userTxtFilename;
    private String chatTxtFilename;
    private String messageTxtFilename;

    public DBManager(String filepath, String userTxtFilename, String chatTxtFilename, String messageTxtFilename) {}

    public void loadAllFiles() {}
    public User checkLoginCredentials(String username, String password) {}
    public void sendUserAllFreshData() {}
    public User getUserById(String userId) {}
    public Chat getChatById(String chatId) {}
    public Message getMessageById(String messageId) {}
    public List<User> fetchAllUsers() {}
    public List<Chat> fetchAllChats() {}
    public List<Message> fetchAllMessages() {}
    public List<Chat> getChatsForUser(String userId) {}
    public void writeNewUser(User user) {}
    public void writeNewChat(Chat chat) {}
    public void writeNewMessage(Message message) {}
    private User stringToUser(String userString) {}
    private Chat stringToChat(String chatString) {}
    private Message StringToMessage(String messageString) {}
    private getSanitizedCharacter(String input, String output) {}
    public void addUserToChat(String userId) {}
    public Boolean usernameExists(String name) {}
    private Boolean validUsername(String name) {}
}
