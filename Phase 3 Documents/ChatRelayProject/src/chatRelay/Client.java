package chatRelay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private Boolean isConnected;
    private String targetIP;
    private String targetPort;
    private List<Chat> chats;
    private List<AbstractUser> users;
    private String userId;
    private Boolean isITAdmin;
    
    private ObjectOutputStream objectStream;
    private ObjectInputStream objectInStream;

    public Client(String targetIp, String targetPort) {
        this.targetIP = targetIp;
        this.targetPort = targetPort;
        isConnected = false;
        try {
            socket = new Socket(targetIP, Integer.parseInt(targetPort));
            OutputStream outputStream = socket.getOutputStream();
            objectStream = new ObjectOutputStream(outputStream);

            InputStream inputStream = socket.getInputStream();
            objectInStream = new ObjectInputStream(inputStream);
        } catch (IOException | NumberFormatException e) {
        	e.printStackTrace();
        }
    }
    
    public void startUp() { //Added
    	
    	GUI clientGUI = new GUI(this);
    	
    	new Thread(clientGUI).start();
    	
    	ClientInput input = new ClientInput(objectInStream, this);
    	
    	new Thread(input).start();
    	
    }
    
public void login(String username, String password) {
    ArrayList<String> args = new ArrayList<>();
    args.add(username);
    args.add(password);
    Packet login = new Packet(actionType.LOGIN, args, "requesting");
    try {
        objectStream.writeObject(login);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void sendMessage(String authorId, String chatId, String content) {
    ArrayList<String> args = new ArrayList<>();
    args.add(authorId);
    args.add(chatId);
    args.add(content);
    Packet sendMessage = new Packet(actionType.SEND_MESSAGE, args, userId);
    try {
        objectStream.writeObject(sendMessage);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void createChat(String chatName, Boolean isPrivate) {
    ArrayList<String> args = new ArrayList<>();
    args.add(chatName);
    args.add(isPrivate.toString());
    Packet createChat = new Packet(actionType.CREATE_CHAT, args, userId);
    try {
        objectStream.writeObject(createChat);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void createUser(String username, String password, String firstname, String lastname, Boolean isAdmin) {
    if (isAdmin) {
        ArrayList<String> args = new ArrayList<>();
        args.add(username);
        args.add(password);
        args.add(firstname);
        args.add(lastname);
        Packet createUser = new Packet(actionType.CREATE_USER, args, userId);
        try {
            objectStream.writeObject(createUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void enableUser(String userId) {
    if (isITAdmin) {
        ArrayList<String> args = new ArrayList<>();
        args.add(userId);
        Packet enableUser = new Packet(actionType.ENABLE_USER, args, this.userId);
        try {
            objectStream.writeObject(enableUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void disableUser(String userId) {
    if (isITAdmin) {
        ArrayList<String> args = new ArrayList<>();
        args.add(userId);
        Packet disableUser = new Packet(actionType.DISABLE_USER, args, this.userId);
        try {
            objectStream.writeObject(disableUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void logout() {
    Packet logout = new Packet(actionType.LOGOUT, new ArrayList<>(), userId);
    try {
        objectStream.writeObject(logout);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    public Boolean getAdminStatus() { // Added
    	return isITAdmin;
    }
    
    // Made for getting Users for Create Chat in GUI
//    private void getUsers() {
//    	Packet getUsers = new Packet(actionType.GET_ALL_USERS, new String[] {}, userId);
//    	try {
//    		objectStream.writeObject(getUsers);
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	}
//    }
    
    private class ClientInput implements Runnable { // Added
    	
    	private ObjectInputStream inputStream;
    	private Client client;
    	
    	public ClientInput(ObjectInputStream input, Client client) {
    		this.inputStream = input;
    		this.client = client;
    	}

		@Override
		public void run() {
			Packet incoming;
			try {
				while((incoming = (Packet) inputStream.readObject()) != null) {
					switch(incoming.getActionType()) {
						// Parsing each Action Type
						case LOGIN -> {
							client.isConnected = true;

userId = incoming.getActionArguments().get(0);
isITAdmin = Boolean.valueOf(incoming.getActionArguments().get(1));
//			            	userId = incoming.getActionArguments()[0]; // Get this UserId when requesting a login
//			            	isITAdmin = Boolean.valueOf(incoming.getActionArguments()[1]); //refine with DBManger
			                break;
						}
						case SEND_MESSAGE -> {
							// Need to fill based on DB Manager
			                break;
						}
						case GET_ALL_CHATS -> {
							// Need to fill based on DB Manager
			                break;
						}
						case GET_ALL_USERS -> {
							// Need to fill based on DB Manager
			                break;
						}
						case CREATE_CHAT -> {
							// Need to fill based on DB Manager
			                break;
						}
						case CREATE_USER -> {
							// Need to fill based on DB Manager
			                break;
						}
						case ENABLE_USER -> {
							// Need to fill based on DB Manager
			                break;
						}
						case DISABLE_USER -> {
							// Need to fill based on DB Manager
			                break;
						}
						default -> {
							// Need to fill based on DB Manager
			                break;
						}
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
    }
}
