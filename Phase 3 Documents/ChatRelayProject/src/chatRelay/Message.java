package chatRelay;

public class Message {
	private static int count = 0;
	private String id;

	private long createdAt;

	private String content;
	private AbstractUser author;
	private Chat chat;

	public Message(String content, AbstractUser author)
	{
	
			this.id = String.valueOf(++count);
			this.createdAt = System.currentTimeMillis(); // unix miliseconds
			this.content = content;
			this.author = author;
			this.chat = null; // chat is set later
		
	}
	public Message(String id, long createdAt, String content, AbstractUser author, Chat chat) {
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
	public AbstractUser getSender() {
		return this.author;
	}

    // message (from: username at: time)
	public String toString() {
		return content + " (from: " + author.getUserName() + " at: " + createdAt + ")";
	}
}