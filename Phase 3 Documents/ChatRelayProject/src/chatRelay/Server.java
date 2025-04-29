package chatRelay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Don't send passwords to frontend! 

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
			handleSendMessage(clientId, packet);
			break;
		case LOGOUT:
			handleLogout(clientId);
			break;
		default:
			sendErrorMessage(clientId, "Unknown action type: " + packet.getActionType());
		}
	}

	// done inside clienthandler now.

//	private void handleLogin(String clientId, Packet packet) {
//		String[] args = packet.getActionArguments();
//		String username = args[0];
//		String password = args[1];
//
//		AbstractUser user = dbManager.checkLoginCredentials(username, password);
//
//		if (user == null) {
	////			sendErrorMessage()

//			return;
//		}
//
//		ClientHandler client = clients.get(clientId);
//
	////		clients.put(user.getId(), client)
//
//		sendSuccessMessage(clientId, "Login successful");
//
//		clients.put(clientId, clients.get(clientId)); // after successful login
//	}

	private void handleSendMessage(String clientId, Packet packet) {
		// TODO: parse packet arguments and broadcast message to intended users
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
		System.out.println("NOTE: Database is currently sensitive. Each .txt file needs 1 blank line under the last record");

		Server server = new Server(port, IP);

		server.connect();
	}
}
