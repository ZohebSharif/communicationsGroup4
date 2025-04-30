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
    private AbstractUser thisUser;
    
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

    public Boolean getIsConnected()
    {
    	return isConnected;
    }
    
    public void startUp() { //Added
    	
    	GUI clientGUI = new GUI(this);
    	
    	new Thread(clientGUI).start();
    	
    	ClientInput input = new ClientInput(objectInStream, this);
    	
    	new Thread(input).start();
    	
    }
    
    public void login(String username, String password) {
		Packet login = new Packet(actionType.LOGIN, new String[] {username, password}, "requesting");
    	try {
    		objectStream.writeObject(login);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void sendMessage(String chatId, String content) {
    	Packet sendMessage = new Packet(actionType.SEND_MESSAGE, new String[] {content, chatId}, userId);
    	try {
    		objectStream.writeObject(sendMessage);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void createChat(String chatName, Boolean isPrivate) {
    	Packet createChat = new Packet(actionType.CREATE_CHAT, new String[] {users.toString(), chatName}, userId); 
    	// TODO: Users should parse into a list with slashes EX: "userId/userId2/userId3"
    	try {
    		objectStream.writeObject(createChat);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void updateState() {}
    
    public void createUser(String username, String password, String firstname, String lastname, Boolean isAdmin) {
    	if (isAdmin) {
    		Packet createUser = new Packet(actionType.CREATE_USER, new String[] {username, password, firstname, lastname}, userId);
    		try {
        		objectStream.writeObject(createUser);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	}
    }
    
    public void enableUser(String userId) {
    	if (isITAdmin) {
    		Packet enableUser = new Packet(actionType.ENABLE_USER, new String[] {userId}, this.userId);
    		try {
        		objectStream.writeObject(enableUser);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	}
    }
    
    public void disableUser(String userId) {
    	if (isITAdmin) {
    		Packet disableUser = new Packet(actionType.DISABLE_USER, new String[] {userId}, this.userId);
    		try {
        		objectStream.writeObject(disableUser);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	}
    }
    
    public void saveChatToTxt(Chat chat) {
    	if (isITAdmin) {
    		String fileName = "chat_logs_" + chat.getRoomName() +".txt";

            try {
                String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
                File file = new File(downloadsPath, fileName);

                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(chat.toString());
                bufferedWriter.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
    	}
    }
    
    public void logout() {
    	Packet logout = new Packet(actionType.LOGOUT, new String[] {}, userId);
    	try {
    		objectStream.writeObject(logout);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public Boolean getIsConnected() {
    	return isConnected;
    }
    
    public AbstractUser getThisUser() {
    	return thisUser;
    }
    
    public Boolean getAdminStatus() { // Added
    	return isITAdmin;
    }
    
    public List<Chat> getChats() {
    	return chats;
    }
    
    public List<AbstractUser> getUsers() {
    	return users;
    }
    
     //Made for getting Users for Create Chat in GUI
    public void getAllUsers() {
    	Packet getUsers = new Packet(actionType.GET_ALL_USERS, new String[] {}, userId);
    	try {
    		objectStream.writeObject(getUsers);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void getAllChats() {
    	Packet getChats = new Packet(actionType.GET_ALL_CHATS, new String[] {}, userId);
    	try {
    		objectStream.writeObject(getChats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private class ClientInput implements Runnable { // Added
    	
    	private ObjectInputStream inputStream;
    	private Client client;
    	
    	public ClientInput(ObjectInputStream input, Client client) {
    		this.inputStream = input;
    		this.client = client;
    	}

		@Override
		public void run() {
			try {
				Packet incoming = (Packet) inputStream.readObject();
				do {
					switch(incoming.getActionType()) {
						// Parsing each Action Type
						case LOGIN -> {
							client.isConnected = true;
			            	userId = incoming.getActionArguments()[1]; // Get this UserId when requesting a login
			            	isITAdmin = Boolean.valueOf(incoming.getActionArguments()[2]); //refine with DBManger
			                break;
						}
						case SEND_MESSAGE -> {
							// Need to fill based on DB Manager
							// Does not require admin
							// is a broadcast to receive information
			                break;
						}
						case GET_ALL_CHATS -> {
							// Requesting chats from server
							for (String line : incoming.getActionArguments()) {
								String[] words = line.split("/");

								String chatId = words[0];
								String ownerId = words[1];
								String roomName = words[2];
								boolean isPrivate = words[3].equals("true") ? true : false;
								String[] userIds = words[4].split(",");

								// TODO: Consider that this is adding the owner to chatters
								AbstractUser owner = null;
								List<AbstractUser> chatters;
								for (AbstractUser user : users) {
									if (user.getId().equals(ownerId)) {
										owner = user;
									}
								}
								
								chatters = new ArrayList<>();
								for (AbstractUser user : users) {
									for (String userId : userIds) {
										if (user.getId().equals(userId)) {
											chatters.add(user);
										}
									}
								}
								
								Chat newChat = new Chat(owner, roomName, chatId, chatters, isPrivate);
								chats.add(newChat);
							}
			                break;
						}
						case GET_ALL_USERS -> {
							for (String line : incoming.getActionArguments()) {
								String[] words = line.split("/");

								String username = words[0];
								String password = words[1];
								String userId = words[2];
								String firstName = words[3];
								String lastName = words[4];
								boolean isDisabled = words[5].equals("true") ? true : false;
								boolean isAdmin = words[6].equals("true") ? true : false;

								AbstractUser newUser;

								if (isAdmin) {
									newUser = new ITAdmin(username, password, userId, firstName, lastName, isDisabled, isAdmin);
								} else {
									newUser = new User(username, password, userId, firstName, lastName, isDisabled, isAdmin);
								}
								users.add(newUser);
							}
			                break;
						}
						case GET_ALL_MESSAGES -> {
							
						}
						case CREATE_CHAT -> {
							// Need to fill based on DB Manager
							// Does not require admin
			                break;
						}
						case CREATE_USER -> {
							// Need to fill based on DB Manager
							// Does require admin
			                break;
						}
						case ENABLE_USER -> {
							// Need to fill based on DB Manager
							// Does require admin
			                break;
						}
						case DISABLE_USER -> {
							// Need to fill based on DB Manager
							// Does require admin
			                break;
						}
						case ERROR -> {
							// Need to fill based on DB Manager
							// Does not require admin
			                break;
						}
						case LOGOUT -> {
							// Need to fill based on DB Manager
							// Does not require admin
							break;
						}
						case SUCCESS -> { 
							// Need to fill based on DB Manager
							// Does not require admin
							break;
						}
						default -> {
							// Need to fill based on DB Manager
							// Does not require admin
							break;
						}
					}
				} while((incoming = (Packet) inputStream.readObject()) != null && isConnected);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
    }
}
