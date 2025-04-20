package chatRelay;

public class ITAdmin extends AbstractUser {

    public ITAdmin(String firstname, String lastname, String password) {
        super(firstname, lastname, password);
    }
    
    public void createUser() {}
    public void disableUser(User user) {}
    public void enableUser(User user) {}
    public void writeChatLog(Chat chat) {}
}
