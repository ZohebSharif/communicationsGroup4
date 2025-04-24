package chatRelay;

public class User extends AbstractUser {

    public User(String username, String password, String id, String firstName, String lastName, boolean isDisabled, boolean isAdmin) {
        super(username, password, id, firstName, lastName, isDisabled, isAdmin);
    }
    
}
