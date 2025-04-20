package chatRelay;

public abstract class AbstractUser {
    private static int count = 0;
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isDisabled = false;
    private Boolean isAdmin = false;
    private Chat[] chats;

    public AbstractUser(String firstname, String lastname, String password) {}

    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getUserName() {return username;}
    public String getId() {return id;}
    public Chat[] getChats() {return chats;}
    public String toString() { return ""; }

    public void CreateChat(User[] users) {}
    public void addUserToChat(User user) {}
    public void sendMessage() {}
}
