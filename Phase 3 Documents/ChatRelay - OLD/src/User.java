public class User extends AbstractUser {

    public User(String firstname, String lastname, String password) {
        super(firstname, lastname, password);
    }
    
}
