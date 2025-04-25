package chatRelay;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

// PROBABLY CAN MAKE MORE THINGS PRIVATE

public class DBManager {
	private ConcurrentHashMap<String, AbstractUser> users = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Chat> chats = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

	private Server server; // maybe not needed?
	private String txtFilePath;
	private String userTxtFilename;
	private String chatTxtFilename;
	private String messageTxtFilename;

//    filepath = "./src/chatRelay/dbFiles/development/"    (navigate from root path!)
	public DBManager(String filepath, String userTxtFilename, String chatTxtFilename, String messageTxtFilename) {
		this.txtFilePath = filepath;
		this.userTxtFilename = filepath + userTxtFilename; // username/password/id/firstName/lastName/isDisabled/isAdmin
		this.chatTxtFilename = filepath + chatTxtFilename; // id/owner/roomName/[userId1, userId2, userId3]/isPrivate
		this.messageTxtFilename = filepath + messageTxtFilename; // id/createdAt/content/authorId/chatId

//TESTING
//		print(userTxtFilename);
//		print(chatTxtFilename);
//		print(messageTxtFilename);

		loadUsers(); // convert TXT strings into real User objects, put those into the hashmap
		loadChats();
		loadMessages();
		
		
		for (AbstractUser user : users.values()) {
			System.out.println(user);
		}

		System.out.println("\n\nTest 'getUserById'");
		AbstractUser u1 = getUserById("1");
		System.out.println(u1);

	}

// TESTER - TO DELETE
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

	private void loadUsers() {
		try (Scanner scanner = new Scanner(new File(this.userTxtFilename))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);

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

				users.put(userId, newUser);

			}
			System.out.println("end of loadUsers()\n");
		} catch (IOException e) {
			System.out.println("Error loading users: " + e.getMessage());
		}
	}

	private void loadChats() {
		try (Scanner scanner = new Scanner(new File(this.chatTxtFilename))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);

				String[] words = line.split("/");

				String chatId = words[0];
				String ownerId = words[1];
				String roomName = words[2];
				boolean isPrivate = words[3].equals("true") ? true : false;
				String[] userIds = words[4].split(",");

				// TODO: Consider that this is adding the owner to chatters
				AbstractUser owner = getUserById(ownerId);
				List<AbstractUser> chatters = new ArrayList<>();
				for (String userId : userIds) {
					AbstractUser u = getUserById(userId);
					chatters.add(u);
				}

				// Add to hashmap
				Chat newChat = new Chat(owner, roomName, chatId, chatters);
				chats.put(chatId, newChat);

				// Add relationship on each User, to connect to this chat
				// TODO: POSSIBLE ALTERNATIVE - MAKE ADDCHATTERS() AS A SETTER
				// TO PREVENT ANOTHER LOOP HERE?
				for (String userId : userIds) {
					AbstractUser u = getUserById(userId);
					u.addChat(newChat);
				}

			}
			System.out.println("end of loadChats()\n");
		} catch (IOException e) {
			System.out.println("Error loading chats: " + e.getMessage());
		}
	}

	private void loadMessages() {
		try (Scanner scanner = new Scanner(new File(this.messageTxtFilename))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);

				String[] words = line.split("/");

				String messageId = words[0];
				long createdAt = Long.parseLong(words[1]);
				String content = words[2];
				String authorId = words[3];
				String chatId = words[4];
				
//				.txt format: id/createdAt/content/authorId/chatId				
//				TODO : ESCAPE "/" CHARACTER STUFF

				AbstractUser author = getUserById(authorId);
				Chat chat = getChatById(chatId);
				
				
				Message newMessage = new Message(messageId, createdAt, content, author, chat);
			
				chat.addMessage(newMessage);
				author.addChat(chat);
				
				
				
				messages.put(messageId, newMessage);
				


			}
			System.out.println("end of loadMessages()\n");
		} catch (IOException e) {
			System.out.println("Error loading messages: " + e.getMessage());
		}
	}

	// public User checkLoginCredentials(String username, String password) {}
	public void sendUserAllFreshData() {
	}

	public AbstractUser getUserById(String userId) {
		return users.get(userId);
	}

	public Chat getChatById(String chatId) {
		return chats.get(chatId);
	}
	// public Chat getChatById(String chatId) {}
	// public Message getMessageById(String messageId) {}
	// public List<User> fetchAllUsers() {}
	// public List<Chat> fetchAllChats() {}
	// public List<Message> fetchAllMessages() {}
	// public List<Chat> getChatsForUser(String userId) {}
	private void writeNewUser(User user) {
	}

	private void writeNewChat(Chat chat) {
	}

	private void writeNewMessage(Message message) {
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
