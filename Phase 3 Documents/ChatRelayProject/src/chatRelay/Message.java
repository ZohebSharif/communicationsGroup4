package chatRelay;

import java.time.LocalDateTime;

public class Message {
    private static int count = 0;
    private String id;
    private LocalDateTime createdAt;
    private String content;
    private User author;
    private Chat chat;

    public Message(String content, User sender) {}
    public String getID() {return id;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public User getSender() {return author;}
    public String toString() { return ""; }
}
