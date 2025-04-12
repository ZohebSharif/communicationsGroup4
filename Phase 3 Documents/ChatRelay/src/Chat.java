public class Chat {
    private static int count = 0;
    private String id;
    private Message[] messages;
    private User owner;
    private String roomName;
    private User[] chatters;
    private Boolean isPrivate = false;

    public Chat(User chatOwner, String name) {}

    public void addChatter() {}
    public void removeChatter() {}
    public void addMessage(Message msg) {}
    public void changePrivacy(Boolean newState) {}
    public String toString() { return ""; }
}
