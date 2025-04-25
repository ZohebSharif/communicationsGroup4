package chatRelay;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private static int count = 0;
    private String id;
    private List<AbstractUser> chatters;
    private List<Message> messages; 
    private AbstractUser owner;
    private String roomName;
    private Boolean isPrivate = false;

// constructor takes in a user object and string for user
//    public Chat(User chatOwner, String name) {
    

    // TO DO:
    // LIKE FOR ALL OTHER CLASSES - MAKE 2nd CONSTRUCTOR THAT DOESN"T TAKE IN AN ID
    // TO ADDRESS 2 DIFFERENT FLOWS WE CREATE A USER OBJECT
    // 1) LOADING IN AN OBJECT (already has an ID)
    // 2) CREATING A RECORD (ID NEEDS TO BE GENERATED)
    
    public Chat(AbstractUser chatOwner, String name, String id, List<AbstractUser> chatters) {
//    	this.id = "CHAT_" + (++count);
    	this.id = id;
    	
    	
        this.owner = chatOwner;
        this.roomName = name;
        this.messages = new ArrayList<>();
        this.chatters = new ArrayList<>();
        this.chatters.add(chatOwner);
    }
    
    // add a chatter
    public void addChatter(User user) {
//        User[] newChatters = new User[chatters.length + 1];
//        for (int i = 0; i < chatters.length; i++) {
//            newChatters[i] = chatters[i];
//        }
//        newChatters[chatters.length] = user;
//        chatters = newChatters;
    	chatters.add( (User) user);
    }
    
    // remove a chatter
    public void removeChatter(User user) {
//        if (chatters.length <= 1) {
//            return; // cannot remove only user
//        }
//        
//        User[] newChatters = new User[chatters.length - 1];
//        int index = 0;
//        for (int i = 0; i < chatters.length; i++) {
//            if (!chatters[i].getId().equals(user.getId())) {
//                newChatters[index++] = chatters[i];
//            }
//        }
//        // only update if user was found and removed
//        if (index == newChatters.length) {
//            chatters = newChatters;
//        }
    	if(user != owner) {
    		chatters.remove((User) user);
    	}	
    }
    
    // add a message
    public void addMessage(Message msg) {
//        Message[] newMessages = new Message[messages.length + 1];
//        for (int i = 0; i < messages.length; i++) {
//            newMessages[i] = messages[i];
//        }
//        newMessages[messages.length] = msg;
//        messages = newMessages;
    	messages.add(msg);
    	return;
    }
    // change privacy to provided state
    public void changePrivacy(Boolean newState) {
        this.isPrivate = newState;
    }
    // return string representation of chat
    public String toString() {
        return "Chat [id=" + id + ", roomName=" + roomName + ", owner=" + owner.getUserName() + ", private=" + isPrivate + ", chatters=" + chatters.size() + "]";
    }
    
    // getters
    public String getId() {
        return id;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public User getOwner() {
        return (User) owner;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public List<AbstractUser> getChatters() {
        return chatters;
    }
    
    public Boolean isPrivate() {
        return isPrivate;
    }
}
