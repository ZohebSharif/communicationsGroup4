package chatRelay;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Packet implements Serializable {

	private static int count = 0;
	private String id;
	private actionType acType;
	private Status status;
	private ArrayList<String> actionArgs; //changed from String[]
	private LocalTime timeCreated;
	private String senderId;

	public Packet(Status status, actionType acType, ArrayList<String> actionArguments, String senderId) {
		this.status = status;
		this.acType = acType;
		this.actionArgs = actionArguments;
		this.senderId = senderId;
		this.timeCreated = LocalTime.now();
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
