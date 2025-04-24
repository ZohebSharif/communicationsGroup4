package chatRelay;

public class ITAdmin extends AbstractUser {

public ITAdmin(String username, String password, String id, String firstName, String lastName, boolean isDisabled, boolean isAdmin) {
    super(username, password, id, firstName, lastName, isDisabled, isAdmin);
    }

    public void createUser() {}
    public void disableUser(User user) {}
    public void enableUser(User user) {}
    public void writeChatLog(Chat chat) {}
}
