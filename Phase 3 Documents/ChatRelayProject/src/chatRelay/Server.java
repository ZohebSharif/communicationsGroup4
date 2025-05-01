package chatRelay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Don't send passwords to frontend! 
// TODO: Add something that deletes STALE connections? 

public class Server {
	private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

	private DBManager dbManager;
	private int port;
	private String IP;

	public Server(int port, String IP) {
		this.port = port;
		this.IP = IP;

//		this.dbManager = new DBManager("./src/chatRelay/dbFiles/development/", "Users.txt", "Chats.txt",
//				"Messages.txt");

		// use this version why running from terminal
		this.dbManager = new DBManager("./chatRelay/dbFiles/development/", "Users.txt", "Chats.txt", "Messages.txt");
	}

	public void connect() {
		System.out.println("Server.connect() fired");
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			serverSocket.setReuseAddress(true);
			while (true) {
				Socket socket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(socket, this);
				new Thread(clientHandler).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receivePacket(String clientId, Packet packet) {

		System.out.println("Server.receivePacket() fired");
		switch (packet.getActionType()) {
//		case LOGIN:
//			handleLogin(clientId, packet);
//			break;
		case SEND_MESSAGE:
			System.out.println("Server.receievePacket() SEND_MESSAGE switch fired");
			handleSendMessage(clientId, packet);
			break;
		case CREATE_CHAT:
			System.out.println("Server.receievePacket() CREATE_CHAT switch fired");
			handleCreateChat(clientId, packet);
			break;
		case CREATE_USER:
			System.out.println("Server.receievePacket() CREATE_USER switch fired");
			handleCreateUser(clientId, packet);
			break;
		case LOGOUT:
			handleLogout(clientId);
			break;
		default:
			sendErrorMessage(clientId, "Unknown action type: " + packet.getActionType());
		}
	}

	private void handleCreateUser(String clientId, Packet packet) {
		ArrayList<String> broadcastingArgs = new ArrayList<>();

		// Requestor must be an admin
		if (!dbManager.getUserById(clientId).isAdmin()) {
			broadcastingArgs.add("You're not an admin, get out of here!");
			Packet errorPacket = new Packet(Status.ERROR, actionType.CREATE_USER, broadcastingArgs, "Server");
			broadcastToRequestor(clientId, errorPacket);
			return;
		}

		ArrayList<String> args = packet.getActionArguments();
		// TODO: frontend should replace the "/",
//		or Packet could have done that 

//		TODO: VALIDATIONS:
//		-first/lastname only letters, 
//		-username should be alphanum

		String username = args.get(0);
		String password = args.get(1);
		String firstname = args.get(2);
		String lastname = args.get(3);
		boolean isDisabled = args.get(4).equals("true");
		boolean isAdmin = args.get(5).equals("true");

		// No duplicate user names
		// TODO: semi-major refactor to pass more than clientId, and the whole user into
		// receivePacket() could be good
		if (dbManager.getUserByUsername(username) != null) {
			broadcastingArgs.add("That username already exists");
			Packet errorPacket = new Packet(Status.ERROR, actionType.CREATE_USER, broadcastingArgs, "Server");
			broadcastToRequestor(clientId, errorPacket);
			return;
		}

		AbstractUser newUser = dbManager.writeNewUser(username, password, firstname, lastname, isDisabled, isAdmin);

		broadcastingArgs.add(newUser.getId());
		broadcastingArgs.add(newUser.getUserName());
		broadcastingArgs.add(newUser.getFirstName());
		broadcastingArgs.add(newUser.getLastName());
		broadcastingArgs.add(String.valueOf(newUser.isAdmin()));
		broadcastingArgs.add(String.valueOf(newUser.isDisabled()));

		Packet newUserPacket = new Packet(Status.SUCCESS, actionType.NEW_USER_BROADCAST, broadcastingArgs, "Server");
		broadcastToUsersConnected(newUserPacket);

	}

	private void handleCreateChat(String clientId, Packet packet) {
		ArrayList<String> args = packet.getActionArguments();
		ArrayList<String> userIds = new ArrayList<>();

		for (String userId : args.get(0).split("/")) {
			userIds.add(userId);
		}

		// TODO: frontend should replace the "/",
//		or Packet could have done that 

		String roomName = args.get(1);
		boolean isPrivate = args.get(2).equals("true");

		Chat newChat = dbManager.writeNewChat(clientId, roomName, userIds, isPrivate);

		ArrayList<String> broadcastingArgs = new ArrayList<>();
		broadcastingArgs.add(newChat.getId());
		broadcastingArgs.add(newChat.getOwner().getId());
		broadcastingArgs.add(String.join("/", newChat.getChattersIds()));

		Packet chatPacket = new Packet(Status.SUCCESS, actionType.NEW_CHAT_BROADCAST, broadcastingArgs, "Server");

//		TODO: if not private, broadcast to every, if private, broadcast to only some

		if (newChat.isPrivate()) {
			broadcastToUsers(newChat.getChatters(), chatPacket);
		} else {
			broadcastToUsersConnected(chatPacket);
		}

	}

	private void handleSendMessage(String clientId, Packet packet) {
		ArrayList<String> args = packet.getActionArguments();
		String content = args.get(0);
		String chatId = args.get(1);

		Message newMessage = dbManager.writeNewMessage(content, clientId, chatId);

		Chat chat = dbManager.getChatById(chatId);

		ArrayList<String> broadcastingArgs = new ArrayList<>();
		broadcastingArgs.add(newMessage.getId());
		broadcastingArgs.add(String.valueOf(newMessage.getCreatedAt()));
		broadcastingArgs.add(newMessage.getContent());
		broadcastingArgs.add(newMessage.getSender().getId());
		broadcastingArgs.add(newMessage.getChat().getId());

		Packet messagePacket = new Packet(Status.SUCCESS, actionType.NEW_MESSAGE_BROADCAST, broadcastingArgs, "Server");
		broadcastToUsers(chat.getChatters(), messagePacket);
	}

	// send Packet to ALL users
	// TODO: ADD TRY/CATCH ?
	private void broadcastToUsers(List<AbstractUser> usersToSendTo, Packet packet) {
		for (AbstractUser user : usersToSendTo) {
			ClientHandler client = clients.get(user.getId());

			if (client != null) {
				client.sendPacket(packet);
			}
		}
	}

	// send Packet to Users Currently connected
	// TODO: ADD TRY/CATCH?
	private void broadcastToUsersConnected(Packet chatPacket) {
		for (ClientHandler client : clients.values()) {
			client.sendPacket(chatPacket);
		}
	}

	
	// Only send packet to the initial Packet requestor
	// TODO: ADD TRY/CATCH?
	private void broadcastToRequestor(String requestorId, Packet packet) {
		ClientHandler client = clients.get(requestorId);
		client.sendPacket(packet);
	}
	
	
	public void handleLogout(String clientId) {
		clients.remove(clientId);
		System.out.println(clientId + " logged out and removed from clients.");
	}

	public void sendErrorMessage(String userId, String errorMessage) {
	}

	public void sendSuccessMessage(String userId, String successMessage) {
	}

	// instead get active userids living on clienthandler??
	public void sendPacketToUsers(Packet packet, String[] userIds) {
		for (String userId : userIds) {
			ClientHandler client = clients.get(userId);
			if (client != null) {
				client.sendPacket(packet);
			}
		}
	}

	public DBManager getDBManager() {
		return this.dbManager;
	}

	public void addClient(String userId, ClientHandler ch) {
		clients.put(userId, ch);
	}

	public static void main(String[] args) {

// commands to compile + run
//Src % javac chatRelay/*.java
//Src % java chatRelay.Server
//Src % java chatRelay.BasicClient
		int port = 1337;
		String IP = "127.0.0.1";

		System.out.println("Server.java's main() fired\n");
		System.out.println(
				"NOTE: Database is currently sensitive. Each .txt file needs 1 blank line under the last record");

		Server server = new Server(port, IP);

		server.connect();
	}
}
