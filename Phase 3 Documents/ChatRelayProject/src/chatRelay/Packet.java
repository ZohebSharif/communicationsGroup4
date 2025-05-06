package chatRelay;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Packet implements Serializable {

	private static final String ESCAPED_SLASH = "<<<SLASH>>>";
	private static final String ESCAPED_NEWLINE = "<<<NEWLINE>>>";

	public static String sanitize(String input) {
		String output = input.replace("/", ESCAPED_SLASH);
		output = output.replace("\n", ESCAPED_NEWLINE);
		return output;
	}

	public static String unsanitize(String input) {
		String output = input.replace(ESCAPED_SLASH, "/");
		output = output.replace(ESCAPED_NEWLINE, "\n");
		return output;
	}

	private static int count = 0;
	private String id;
	private actionType acType;
	private Status status;
	private ArrayList<String> actionArgs;
	private LocalTime timeCreated;
	private String senderId;

	public Packet(Status status, actionType acType, ArrayList<String> actionArguments, String senderId) {
		this.status = status;
		this.acType = acType;
		this.actionArgs = actionArguments;
		this.senderId = senderId;
		this.timeCreated = LocalTime.now();
		

// when Packet is going from Client to Server, remove "\n" and "/" to protect DB
		if (!senderId.equals("Server")) {
			switch (acType) {
			case LOGIN:
				actionArguments.set(0, sanitize(actionArguments.get(0)));
				actionArguments.set(1, sanitize(actionArguments.get(1)));
				break;
			case SEND_MESSAGE:
				actionArguments.set(0, sanitize(actionArguments.get(0)));
				break;
			case CREATE_CHAT:
				actionArguments.set(1, sanitize(actionArguments.get(1)));
				break;
			case CREATE_USER:
				actionArguments.set(0, sanitize(actionArguments.get(0)));
				actionArguments.set(1, sanitize(actionArguments.get(1)));
				break;
			case UPDATE_USER:
				break;
			case ADD_USER_TO_CHAT:
				break;
			default:
				break;
			}
		}
	}

	public ArrayList<String> getActionArguments() {
		return actionArgs;
	}

	public String getId() {
		return id;
	}

	public LocalTime getTimeCreated() {
		return timeCreated;
	}

	public String getSenderId() {
		return senderId;
	}

	public actionType getActionType() {
		return acType;
	}

	public Status getStatus() {
		return status;
	}
}
