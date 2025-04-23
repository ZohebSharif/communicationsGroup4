package chatRelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Boolean isConnected;
    private String targetIP;
    private String targetPort;
    private Chat[] chats;
    private User[] users;
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
    	try {
    		String[] args = {username, password};
    		Packet loginPack = new Packet(actionType.LOGIN, args, "requesting");
    		objectStream.writeObject(loginPack);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void sendMessage(String authorId, String chatId, String content) {}
    public void createChat(String chatName, Boolean isPrivate) {}
    public void updateState() {}
    public void createUser(String username, String password, String firstname, String lastname, Boolean isAdmin) {}
    public void enableUser(String userId) {}
    public void disableUser(String userId) {}
    public void saveChatToTxt(Chat chat) {}
    public void logout() {}
    
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
					if (incoming.getActionType().equals(actionType.SUCCESS)) {
		            	client.isConnected = true;
		                System.out.println("Packet Recieved");
		            }
//					switch(incoming.getActionType()) {
//						// Parsing each Action Type
//			            
//					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
    }
}
