package chatRelay;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BasicClient {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String userId;

	public BasicClient(String ip, int port) {
		System.out.println("BasicClient constructor fired");
		try {
			socket = new Socket(ip, port);
			OutputStream outputStream = socket.getOutputStream();
			out = new ObjectOutputStream(outputStream);

			InputStream inputStream = socket.getInputStream();
			in = new ObjectInputStream(inputStream);

			System.out.println("Connected to server at " + ip + ":" + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(String username, String password) {
		System.out.println("BasicClient.login() fired");
		try {
			ArrayList<String> loginArgs = new ArrayList<>();
			loginArgs.add(username);
			loginArgs.add(password);

			Packet loginPacket = new Packet(Status.NONE, actionType.LOGIN, loginArgs, "tempClient");
			out.writeObject(loginPacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String authorId, String chatId, String content) {
		System.out.println("BasicClient.sendMessage() fired");
		try {
			ArrayList<String> messageArgs = new ArrayList<>();
			messageArgs.add(authorId);
			messageArgs.add(chatId);
			messageArgs.add(content);

			Packet messagePacket = new Packet(Status.NONE, actionType.SEND_MESSAGE, messageArgs, authorId);
			out.writeObject(messagePacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logout(String userId) {
		try {
			ArrayList<String> logoutArgs = new ArrayList<>();
			Packet logoutPacket = new Packet(Status.NONE, actionType.LOGOUT, logoutArgs, userId);
			out.writeObject(logoutPacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAllUsers() {
		try {
			ArrayList<String> args = new ArrayList<>();
			Packet getUsersPacket = new Packet(Status.NONE, actionType.GET_ALL_USERS, args, userId);
			out.writeObject(getUsersPacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAllChats() {
		try {
			ArrayList<String> args = new ArrayList<>();
			Packet getChatsPacket = new Packet(Status.NONE, actionType.GET_ALL_CHATS, args, userId);
			out.writeObject(getChatsPacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		System.out.println("in listen() loop");
		try {
			while (true) {
				Packet incoming = (Packet) in.readObject();
				System.out.println("Received from server: " + incoming.getActionType());

				ArrayList<String> args = incoming.getActionArguments();
				System.out.println("Arguments received:");
				for (String arg : args) {
					System.out.println(" - " + arg);
				}

				switch (incoming.getActionType()) {
				case LOGIN -> {
					if (incoming.getStatus() == Status.SUCCESS && args.size() >= 3) {
						userId = args.get(1);
						System.out.println("Logged in as: " + userId + ", Admin: " + args.get(2));
					}
				}
				case SEND_MESSAGE -> {
					if (!args.isEmpty()) {
						System.out.println("Message received: " + args.get(0));
					}
				}
				case GET_ALL_USERS -> System.out.println("Handled GET_ALL_USERS");
				case GET_ALL_CHATS -> System.out.println("Handled GET_ALL_CHATS");
				case GET_ALL_MESSAGES -> System.out.println("Handled GET_ALL_MESSAGES");
				case SUCCESS -> System.out.println("Action successful.");
				case ERROR -> {
					if (!args.isEmpty()) {
						System.out.println("Error from server: " + args.get(0));
					}
				}
				default -> {
					System.out.println("Unhandled packet type: " + incoming.getActionType());
				}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnected from server.");
		} finally {
			close();
		}
	}

	public void close() {
		try {
			socket.close();
			System.out.println("Disconnected.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BasicClient client = new BasicClient("127.0.0.1", 1337);
		
		
		System.out.println("Some users you can log into, otherwise it'll log into \"biljoe\": chrsmi kenkot stearm zohsha talsha biljoe ");
		System.out.println("juse type in a username into CLI next time");
		
		
		String username = "bilJoe"; // non-admin, NOT disabled
		String password = "asdf/"; // testing "/" is valid
		
		if (args.length == 1) {
			username = args[0];
			password= "asdf";
		}  
		
		
		client.login(username, password);
//		client.login("biljoe", "asdf/"); // non-admin
//		client.login("chrsmi", "asdf"); // admin
		
		client.listen();
	}
}
