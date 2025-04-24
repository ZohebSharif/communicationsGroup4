package chatRelay;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DBManager {

	// private HashMap<userId, User> users;
	// private HashMap<chatId, Chat> chats;
	// private HashMap<messageId, Message> messages;
	private Server server;
	private String txtFilePath;
	private String userTxtFilename;
	private String chatTxtFilename;
	private String messageTxtFilename;

//    filepath = "./src/chatRelay/dbFiles/development/"    (navigate from root path!)
	public DBManager(String filepath, String userTxtFilename, String chatTxtFilename, String messageTxtFilename) {
		this.txtFilePath = filepath;
		this.userTxtFilename = userTxtFilename; // username/password/id/firstName/lastName/isDisabled/isAdmin
		this.chatTxtFilename = chatTxtFilename; // id/owner/roomName/[userId1, userId2, userId3]/isPrivate
		this.messageTxtFilename = messageTxtFilename; // id/createdAt/content/authorId/chatId

//TESTING

		print(filepath + userTxtFilename);
		print(filepath + chatTxtFilename);
		print(filepath + messageTxtFilename);
	}

// FORMATS:	

// TESTER
	private void print(String fullPath) {
		try (Scanner scanner = new Scanner(new File(fullPath))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
			}
			System.out.println("+++++++++++++++++++");
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}
	}

	public void loadAllFiles() {
	}

	// public User checkLoginCredentials(String username, String password) {}
	public void sendUserAllFreshData() {
	}

	// public User getUserById(String userId) {}
	// public Chat getChatById(String chatId) {}
	// public Message getMessageById(String messageId) {}
	// public List<User> fetchAllUsers() {}
	// public List<Chat> fetchAllChats() {}
	// public List<Message> fetchAllMessages() {}
	// public List<Chat> getChatsForUser(String userId) {}
	public void writeNewUser(User user) {
	}

	public void writeNewChat(Chat chat) {
	}

	public void writeNewMessage(Message message) {
	}

	// private User stringToUser(String userString) {}
	// private Chat stringToChat(String chatString) {}
	// private Message StringToMessage(String messageString) {}
	private void getSanitizedCharacter(String input, String output) {
	}

	public void addUserToChat(String userId) {
	}
	// public Boolean usernameExists(String name) {}
	// private Boolean validUsername(String name) {}
}
