package chatRelay;

public abstract class AbstractUser {
    private static int count = 0;        // probably need to use atomic for potential concurrency issue?
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isDisabled = false;
    private Boolean isAdmin = false;
    private Chat[] chats;
    
    // probably add a 2nd constructor that DOESN'T take in an ID - for when an admin creates a new user

    public AbstractUser(String username, String password, String id, String firstname, String lastname, boolean isDisabled, boolean isAdmin  ) {
    	this.username = username;
    	this.password = password;
    	this.id = id;
    	this.firstName = firstname;
    	this.lastName = lastname;
    	this.isDisabled = isDisabled;
    	this.isAdmin = isAdmin;
    }

    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getUserName() {return username;}
    public String getId() {return id;}
    public Chat[] getChats() {return chats;}
    
	public String toString() {
		return "UserId=" + id + ", " + firstName + " " + lastName + " (" + username + "), Admin: " + isAdmin;
	}

    public void CreateChat(User[] users) {}
    public void addUserToChat(User user) {}
    public void sendMessage() {}
}
