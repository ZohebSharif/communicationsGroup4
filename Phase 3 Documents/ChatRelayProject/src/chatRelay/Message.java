package chatRelay;

import java.time.LocalDateTime;

public class Message {
    private static int count = 0;
    private String id;

    private long createdAt;
    
    private String content;
    private AbstractUser author;
    private Chat chat;

    public Message(String id, long createdAt, String content, AbstractUser author, Chat chat) {
    	this.id = id;
    	this.createdAt = createdAt;
    	this.content = content;
    	this.author = author;
    	this.chat = chat;
    }
    
    
    // update to be "LONG"
    public LocalDateTime getCreatedAt() {return createdAt;}
    
    
    public String getID() {return id;}
    public User getSender() {return author;}
    public String toString() { return ""; }
}
