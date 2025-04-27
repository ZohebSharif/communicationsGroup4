package chatRelay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat {
    private static int count = 0; // probably make atomic
    private String id;
    private List<AbstractUser> chatters = new ArrayList<>();
    private List<Message> messages = new ArrayList<>(); 
    private AbstractUser owner;
    private String roomName;
    private Boolean isPrivate;

    // constructor for new chats, doesnt take in an ID (needs to create unique id)
    // used for when users make new chats
    public Chat(AbstractUser chatOwner, String name) {
        this.id =  String.valueOf(++count);
        this.owner = chatOwner;
        this.roomName = name;
        this.chatters.add(chatOwner);
    }

    // when loading in data from the .txt file (reads in an ID)
    public Chat(AbstractUser chatOwner, String name, String id, List<AbstractUser> chatters, boolean isPrivate) {
    	this.owner = chatOwner;
    	this.roomName = name;
        this.id = id;
        this.chatters = chatters;
        this.isPrivate = isPrivate;
    }
    
    // add chatter
    public void addChatter(AbstractUser user) {
        if (!chatters.contains(user)) {
            chatters.add(user);
        }
    }
    
    // remove chatter
    public void removeChatter(AbstractUser user) {
        if (chatters.size() <= 1) {
            return; // cannot remove only user
        }
        
        chatters.remove(user);
    }
    
    // add message
    public void addMessage(Message msg) {
        messages.add(msg);
    }
    
    // change privacy
    // if private, only owner can add users
    // if public, anyone can add users
    public void changePrivacy(Boolean newState) {
        this.isPrivate = newState;
    }
    
    // return string representation of chat
    // this will be used for logging by admin
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
    
    public AbstractUser getOwner() {
        return owner;
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
