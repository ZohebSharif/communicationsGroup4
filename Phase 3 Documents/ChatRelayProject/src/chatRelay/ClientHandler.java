package chatRelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

	private Socket clientSocket;
	private String userId;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Server server;

	public ClientHandler(Socket socket, Server server) {
		this.clientSocket = socket;
		this.server = server;

		try {
			InputStream inStream = clientSocket.getInputStream();
			inputStream = new ObjectInputStream(inStream);

			OutputStream outStream = clientSocket.getOutputStream();
			outputStream = new ObjectOutputStream(outStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void start() {
//		try {
//			Packet checkLogin = (Packet) inputStream.readObject();
//
//			if (checkLogin.getActionType().equals(actionType.LOGIN)) {
//				String[] args = { "Success" };
//				Packet accept = new Packet(actionType.SUCCESS, args, "Client");
//				System.out.println("Got: " + accept.getActionType().toString());
//				outputStream.writeObject(accept);
//			}
//		} catch (IOException | ClassNotFoundException e) {
//
//		}
//	}
//
//	public void stop() {
//		try {
//			Packet checkLogout = (Packet) inputStream.readObject();
//
//			if (checkLogout.getActionType().equals(actionType.LOGOUT)) {
//				String[] args = { "Success" };
//				Packet accept = new Packet(actionType.SUCCESS, args, "Client");
//				System.out.println("Got: " + accept.getActionType().toString());
//				outputStream.writeObject(accept);
//			}
//		} catch (IOException | ClassNotFoundException e) {
//
//		}
//	}

	public void setUserId(String userId) { // What is the use?
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void sendPacket(Packet packet) {
		try {
			outputStream.writeObject(packet);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		System.out.println("Client.run() fired");
		try {
//			Step 1 - Handle login
			Packet packet = (Packet) inputStream.readObject();

			if (packet.getActionType() == actionType.LOGIN) {
//				String[] args = packet.getActionArguments();
				ArrayList<String> args = packet.getActionArguments();
				String username = args.get(0);
				String password = args.get(1);

				System.out.println("username: " + username + " password: " + password);

				AbstractUser user = server.getDBManager().checkLoginCredentials(username, password);

				if (user != null) {
					System.out.println("WE GOT A USER!");
					// set user id
					this.userId = user.getId();

					server.addClient(userId, this);

					// send packet response
					// 1 packet with everything? or send multiple packets?


					System.out.println("allUsersStringed Packet created/sent");
					ArrayList<String> allUsersStringed = server.getDBManager().fetchAllUsers();
					System.out.println("allUsersStringed: " + allUsersStringed);
					Packet usersPacket = new Packet(actionType.GET_ALL_USERS, allUsersStringed, "SERVER");
					sendPacket(usersPacket);

//					List<Chat> filteredChats = server.getDBManager().fetchAllChats(userId);
//
//					List<Message> filteredMessages;

//					client.sendPacket(usersPacket);
//					client.sendPacket(chatsPacket);
//					client.sendPacket(messagesPacket);

				} else {
//					server.sendErrorMessage("Invalid login");
//					connect should close
					return;
				}
			} else {
//				server.sendErrorMessage("Expected login packet first");
//					connect should close
				return;
			}

// Step 2 - Now that user is logged in, process their subsequent steps			
			while (true) {

				System.out.println("Packet nextPacket part fired");
				Packet nextPacket = (Packet) inputStream.readObject();
				server.receivePacket(userId, nextPacket);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (userId != null) {
				server.handleLogout(userId);
				System.out.println("Client with id of  " + userId + "  has disconnected");
			}
		}
	}

}
