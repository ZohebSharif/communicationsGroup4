package chatRelay;

public class Message {
	private static int count = 0;
	private String id;

	private long createdAt;

	private String content;
    //updated to use User and not AbstractUser, as per the UML diagram 
	private User author;
	private Chat chat;

	public Message(String id, long createdAt, String content, User author, Chat chat) {
		this.id = id;
		this.createdAt = createdAt;
		this.content = content;
		this.author = author;
		this.chat = chat;
	}

	// update to be "LONG"
	public long getCreatedAt() {
		return this.createdAt;
	}

	public String getId() {
		return this.id;
	}
	
	public String getContent() {
		return this.content;
	}
	public Chat getChat() {
		return this.chat;
	}

    // changes to user to reflect UML
	public User getSender() {
		return this.author;
	}

    // message (from: username at: time)
	public String toString() {
		return content + " (from: " + author.getUserName() + " at: " + createdAt + ")";
	}
}