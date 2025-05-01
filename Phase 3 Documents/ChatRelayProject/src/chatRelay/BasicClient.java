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
//			messageArgs.add(authorId);
			messageArgs.add(content);
			messageArgs.add(chatId);

			Packet messagePacket = new Packet(Status.NONE, actionType.SEND_MESSAGE, messageArgs, authorId);
			out.writeObject(messagePacket);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createChat(String chatName, boolean isPrivate, String[] userIds) {
		System.out.println("BasicClient.createChat() fired");
		try {
			String joinedUsers = String.join("/", userIds); // format: "1/2/6"
			ArrayList<String> args = new ArrayList<>();
			args.add(joinedUsers);
			args.add(chatName);
			args.add(String.valueOf(isPrivate));

			Packet packet = new Packet(Status.NONE, actionType.CREATE_CHAT, args, userId);
			out.writeObject(packet);
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

	public void createUser(String username, String password, String firstName, String lastName, boolean isDisabled,
			boolean isAdmin) {
		System.out.println("BasicClient.createUser() fired");
		try {
			ArrayList<String> args = new ArrayList<>();
			args.add(username);
			args.add(password);
			args.add(firstName);
			args.add(lastName);
			args.add(String.valueOf(isDisabled));
			args.add(String.valueOf(isAdmin));

			Packet packet = new Packet(Status.NONE, actionType.CREATE_USER, args, userId);
			out.writeObject(packet);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateUserIsDisabled(String targetUserId, boolean shouldBeDisabled) {
		System.out.println("BasicClient.updateUserIsDisabled() fired");
		try {
			ArrayList<String> args = new ArrayList<>();
			args.add(targetUserId);
			args.add(String.valueOf(shouldBeDisabled));

			Packet packet = new Packet(Status.NONE, actionType.UPDATE_USER, args, userId);
			out.writeObject(packet);
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
				case LOGIN:
					if (incoming.getStatus() == Status.SUCCESS && args.size() >= 3) {
						userId = args.get(0);
						System.out.println("Logged in as userId: " + userId);
					}

					//
					// dirty testing
					// test sending a message after successfull login

					// TESTING TO SEND A MESSAGE
					String chatId = "2";
					sendMessage(userId, chatId, "TESTING sendMessage() / SEND_MESSAGE");

					System.out.println("\n");

					// TESTING TO CREATE A CHAT
					String[] userIds = { "1", "2", "6" };
					createChat("test chat created from BasicClient!", true, userIds);

					// TESTING TO CREATE A USER
					createUser("sarcon", "asdf", "Sara", "Connor", false, false);
					System.out.println("\n");

					// TESTING TO UPDATE USER'S isDisabled()
					updateUserIsDisabled("8", true); // this is Bill Samsung
					break;
					
//				case SEND_MESSAGE -> {
				case NEW_MESSAGE_BROADCAST:
					System.out.println("─────────────────────────────────────");
					System.out.println("!!! RECEIVED A NEW BROADCAST !!!");
					String messageId = args.get(0);
					String timestamp = args.get(1);
					String content = args.get(2);
					String senderId = args.get(3);
					chatId = args.get(4);

					System.out.println("id:" + chatId + ", " + senderId + " at " + timestamp + ": " + content);
					System.out.println("─────────────────────────────────────");
					break;

				case NEW_CHAT_BROADCAST:
					System.out.println("──────────── NEW CHAT BROADCAST ────────────");
					for (String arg : args) {
						System.out.println("chat info: " + arg);
					}
					System.out.println("─────────────────────────────────────");
					break;

				case NEW_USER_BROADCAST:
					System.out.println("──────────── NEW USER BROADCAST ────────────");
					for (String arg : args) {
						System.out.println("user info: " + arg);
					}
					System.out.println("─────────────────────────────────────");
					break;

				case UPDATED_USER_BROADCAST:
					System.out.println("──────────── USER STATUS UPDATED ────────────");
					System.out.println("UserID: " + args.get(0));
					System.out.println("IsDisabled: " + args.get(1));
					System.out.println("─────────────────────────────────────");
					break;
					
				case GET_ALL_USERS:
					System.out.println("Handled GET_ALL_USERS\n");
					break;
					
				case GET_ALL_CHATS:
					System.out.println("Handled GET_ALL_CHATS\n");
					break;
					
				case GET_ALL_MESSAGES:
					System.out.println("Handled GET_ALL_MESSAGES\n");
					break;
					
				case SUCCESS:
					System.out.println("Action successful.");
					break;
					
				case ERROR:
					if (!args.isEmpty()) {
						System.out.println("Error from server: " + args.get(0));
					}
					break;
					
				default:
					System.out.println("Unhandled packet type: " + incoming.getActionType());
					break;
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
		BasicClient client = new BasicClient("134.154.79.255", 1337);

		System.out.println(
				"Some users you can log into, otherwise it'll log into \"biljoe\": chrsmi kenkot stearm zohsha talsha biljoe ");
		System.out.println("juse type in a username into CLI next time");

		// default user to log in if no CLI args given
		String username = "bilJoe"; // non-admin, NOT disabled
		String password = "asdf/"; // testing "/" is valid

		if (args.length == 1) {
			username = args[0];
			password = "asdf";
		}

		client.login(username, password);
//		client.login("biljoe", "asdf/"); // non-admin
//		client.login("chrsmi", "asdf"); // admin

		client.listen();
	}
}