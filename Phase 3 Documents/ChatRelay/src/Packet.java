import java.io.Serializable;
import java.time.LocalTime;

public class Packet implements Serializable{
    enum actionType {LOGIN, SEND_MESSAGE, GET_ALL_CHATS, GET_ALL_USERS, CREATE_CHAT, SUCCESS, ERROR, CREATE_USER, ENABLE_USER, DISABLE_USER}

    private static int count = 0;
    private String id;
    private actionType acType;
    private String[] actionArgs;
    private LocalTime timeCreated;
    private String senderId;

    public Packet(actionType acType, String[] actionArguments, String senderId) {}

    public String getId() {return id;}
    public LocalTime getTimeCreated() {return timeCreated;}
    public String getSenderId() {return senderId;}
    public actionType getActionType() {return acType;}
    public String[] getActionArguments() {return actionArgs;}
}
