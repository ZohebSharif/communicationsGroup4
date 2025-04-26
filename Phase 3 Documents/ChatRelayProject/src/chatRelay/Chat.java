package chatRelay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat {
    private static int count = 0;
    private String id;
    private List<Message> messages = new ArrayList<>(); 
    private User owner;
    private String roomName;
    private List<User> chatters = new ArrayList<>();
    private Boolean isPrivate = false;

    // constructor for new chats, doesnt take in an ID
    // used for when users make new chats
    public Chat(User chatOwner, String name) {
        this.id = "CHAT_" + (++count);
        this.owner = chatOwner;
        this.roomName = name;
        this.chatters.add(chatOwner);
    }
    // constructor for new chats, takes in an ID
    public Chat(User chatOwner, String name, String id, User[] users) {
        this.id = id;
        this.owner = chatOwner;
        this.roomName = name;
        if (users != null) {
            this.chatters.addAll(Arrays.asList(users));
        } else {
            this.chatters.add(chatOwner);
        }
    }
    
    // add chatter
    public void addChatter(User user) {
        if (!chatters.contains(user)) {
            chatters.add(user);
        }
    }
    
    // remove chatter
    public void removeChatter(User user) {
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
    
    public User getOwner() {
        return owner;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public List<User> getChatters() {
        return chatters;
    }
    
    public Boolean isPrivate() {
        return isPrivate;
    }
}
