package chatRelay;

import java.awt.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.*;

public class GUI extends JFrame implements Runnable {
    private Client client;
    private JFrame frame;

    public GUI(Client client) {
    	this.client = client;
    }

	@Override
	public void run() {
		loginPane();
		try { 
			synchronized (frame) {
				frame.wait();
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
		if (client.getAdminStatus()) {
			buildITGUI();
		} else {
			buildUserGUI();
		}
	}
	
	private void loginPane() {
		frame = new JFrame("Login Pane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(null);
        frame.setResizable(false);

        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(null);
        outerPanel.setBounds(20, 20, 440, 220);
        outerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        frame.add(outerPanel);

        JPanel iconPanel = new JPanel();
        iconPanel.setBounds(20, 30, 130, 130);
        iconPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        JLabel iconLabel = new JLabel("<html><center>Chat<br>Relay<br>Icon</center></html>", SwingConstants.CENTER); // Should be changed to 
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconPanel.add(iconLabel);
        outerPanel.add(iconPanel);

        JLabel loginLabel = new JLabel("Sign into your Account");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loginLabel.setBounds(170, 20, 200, 30);
        outerPanel.add(loginLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(170, 60, 240, 30);
        outerPanel.add(usernameField);

        JTextField passwordField = new JPasswordField("Password");
        passwordField.setBounds(170, 100, 240, 30);
        outerPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(170, 150, 100, 30);
        outerPanel.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(310, 150, 100, 30);
        outerPanel.add(cancelButton);
        
        usernameField.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusGained(FocusEvent e) {
        		if(usernameField.getText().equals("Username")) {
        			usernameField.setText("");
        		} else {
        			usernameField.setText(usernameField.getText());
        		}
        	}
        	
        	@Override
        	public void focusLost(FocusEvent e) {
        		if (usernameField.getText().equals("Username") || usernameField.getText().length() == 0) {
        			usernameField.setText("Username");
        			usernameField.setForeground(Color.GRAY);
        		} else {
        			usernameField.setText(usernameField.getText());
        			usernameField.setForeground(Color.BLACK);
        		}
        	}
        });
        
        passwordField.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusGained(FocusEvent e) {
        		if(passwordField.getText().equals("Password")) {
        			passwordField.setText("");
        		} else {
        			passwordField.setText(passwordField.getText());
        		}
        	}
        	
        	@Override
        	public void focusLost(FocusEvent e) {
        		if (passwordField.getText().equals("Password") || passwordField.getText().length() == 0) {
        			passwordField.setText("Password");
        			passwordField.setForeground(Color.GRAY);
        		} else {
        			passwordField.setText(passwordField.getText());
        			passwordField.setForeground(Color.BLACK);
        		}
        	}
        });
        
        loginButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		client.login(usernameField.getText(), passwordField.getText());
        		frame.dispose();
        	}
        });
        
        cancelButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        // Add Enter Key Acceptance using a Key Press Event

        frame.setVisible(true);
	}
	
	private void buildUserGUI() {
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Top Panel (User name and Chat title)
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("Full Name"); // Updated with information
        JButton addButton = new JButton("+"); // Opens create chat dialog
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.add(userLabel);
        userInfoPanel.add(addButton);

        JLabel chatTitleLabel = new JLabel("Team/Chat Name • Member List", SwingConstants.CENTER); // Updated with information
        topPanel.add(userInfoPanel, BorderLayout.WEST);
        topPanel.add(chatTitleLabel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        // Left Panel (Private Chats and Group Chats)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Private Chats title
        JLabel privateChatsTitle = new JLabel("Private Chats");
        leftPanel.add(privateChatsTitle);

        // Private Chats list (in a scroll pane)
        JPanel privateChatsList = new JPanel();
        privateChatsList.setLayout(new BoxLayout(privateChatsList, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) {
        	if (chat.getChatters().size() < 3) {
        		String owner = chat.getOwner().getFirstName();
            	String members = "";
            	for (AbstractUser user : chat.getChatters()) {
            		members += user.getFirstName() + ", ";
            	}
            	long lastMessageTime = chat.getMessages().get(chat.getMessages().size() - 1).getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
                JButton chatButton = new JButton("<html>" + owner + "<br>" + members + "<br>" + time + "</html>"); // Updated with information
                privateChatsList.add(chatButton);
        	}
        }
        JScrollPane privateScroll = new JScrollPane(privateChatsList);
        privateScroll.setPreferredSize(new Dimension(200, 150));
        leftPanel.add(privateScroll);

        // Group Chats title
        JLabel groupChatsTitle = new JLabel("Group Chats");
        leftPanel.add(groupChatsTitle);

        // Group Chats list (in a scroll pane)
        JPanel groupChatsList = new JPanel();
        groupChatsList.setLayout(new BoxLayout(groupChatsList, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) {
        	if (chat.getChatters().size() < 3) {
        		String owner = chat.getOwner().getFirstName();
            	String members = "";
            	for (AbstractUser user : chat.getChatters()) {
            		members += user.getFirstName() + ", ";
            	}
            	long lastMessageTime = chat.getMessages().get(chat.getMessages().size() - 1).getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
                JButton groupButton = new JButton("<html>" + owner + "<br>" + members + "<br>" + time + "</html>"); // Updated with information
                groupChatsList.add(groupButton);
        	}
        }
        JScrollPane groupScroll = new JScrollPane(groupChatsList);
        groupScroll.setPreferredSize(new Dimension(200, 150));
        leftPanel.add(groupScroll);

        // Right Panel (Chat messages)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Chat messages list
        JPanel chatMessagesPanel = new JPanel();
        chatMessagesPanel.setLayout(new BoxLayout(chatMessagesPanel, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) { // Updated with information
        	for (Message message : chat.getMessages() ) {
        		JPanel messagePanel = new JPanel(new BorderLayout());
        		String sender = message.getSender().getFirstName();
        		long lastMessageTime = message.getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
            	String content = message.getContent();
                JLabel messageLabel = new JLabel("<html>" + sender + " • " + time + "<br>" + content + "</html>"); // Updated with information
                messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                messagePanel.add(messageLabel, BorderLayout.CENTER);
                chatMessagesPanel.add(messagePanel);
        	}
        }
        JScrollPane chatScroll = new JScrollPane(chatMessagesPanel);
        rightPanel.add(chatScroll, BorderLayout.CENTER);

        // Message input area
        JPanel messageInputPanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        messageInputPanel.add(messageField, BorderLayout.CENTER);
        messageInputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(messageInputPanel, BorderLayout.SOUTH);

        // Split Pane (Left - Right)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250);
        frame.add(splitPane, BorderLayout.CENTER);

        // Show frame
        frame.setVisible(true);
	}
	
	private void buildITGUI() {
		frame = new JFrame("Chat Application - IT Admin View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Top Panel (Role, User name and Chat title)
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fullNameLabel = new JLabel("Full Name");
        JButton addButton = new JButton("+");
        fullNameLabel.setForeground(Color.RED);
        userInfoPanel.add(fullNameLabel);
        userInfoPanel.add(addButton);

        JLabel chatTitleLabel = new JLabel("Team/Chat Name • Member List", SwingConstants.CENTER);

        topPanel.add(userInfoPanel, BorderLayout.CENTER);
        topPanel.add(chatTitleLabel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);

        // Left Panel (IT Badge + Private + Group Chats)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // IT Badge
        JLabel badgeLabel = new JLabel("IT Badge");
        badgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(badgeLabel);

        // Private Chats Title
        JLabel privateChatsTitle = new JLabel("Private Chats");
        privateChatsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(privateChatsTitle);

        // Private Chats list
        JPanel privateChatsList = new JPanel();
        privateChatsList.setLayout(new BoxLayout(privateChatsList, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) {
        	if (chat.getChatters().size() < 3) {
        		String owner = chat.getOwner().getFirstName();
            	String members = "";
            	for (AbstractUser user : chat.getChatters()) {
            		members += user.getFirstName() + ", ";
            	}
            	long lastMessageTime = chat.getMessages().get(chat.getMessages().size() - 1).getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
                JButton chatButton = new JButton("<html>" + owner + "<br>" + members + "<br>" + time + "</html>"); // Updated with information
                if (!chat.getChatters().contains(client.getThisUser())) {
                	chatButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
                privateChatsList.add(chatButton);
        	}
        }
        JScrollPane privateScroll = new JScrollPane(privateChatsList);
        privateScroll.setPreferredSize(new Dimension(200, 200));
        leftPanel.add(privateScroll);

        // Group Chats Title
        JLabel groupChatsTitle = new JLabel("Group Chats");
        groupChatsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(groupChatsTitle);

        // Group Chats list
        JPanel groupChatsList = new JPanel();
        groupChatsList.setLayout(new BoxLayout(groupChatsList, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) {
        	if (chat.getChatters().size() < 3) {
        		String owner = chat.getOwner().getFirstName();
            	String members = "";
            	for (AbstractUser user : chat.getChatters()) {
            		members += user.getFirstName() + ", ";
            	}
            	long lastMessageTime = chat.getMessages().get(chat.getMessages().size() - 1).getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
                JButton groupButton = new JButton("<html>" + owner + "<br>" + members + "<br>" + time + "</html>"); // Updated with information
                if (!chat.getChatters().contains(client.getThisUser())) {
                	groupButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
                groupChatsList.add(groupButton);
        	}
        }
        JScrollPane groupScroll = new JScrollPane(groupChatsList);
        groupScroll.setPreferredSize(new Dimension(200, 150));
        leftPanel.add(groupScroll);

        // Right Panel (Chat messages)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Chat messages list
        JPanel chatMessagesPanel = new JPanel();
        chatMessagesPanel.setLayout(new BoxLayout(chatMessagesPanel, BoxLayout.Y_AXIS));
        for (Chat chat : client.getChats()) { // Updated with information
        	for (Message message : chat.getMessages() ) {
        		JPanel messagePanel = new JPanel(new BorderLayout());
        		String sender = message.getSender().getFirstName();
        		long lastMessageTime = message.getCreatedAt();
            	DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            	Date date = new Date(lastMessageTime);
            	String time = dateFormat.format(date);
            	String content = message.getContent();
                JLabel messageLabel = new JLabel("<html>" + sender + " • " + time + "<br>" + content + "</html>"); // Updated with information
                messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                messagePanel.add(messageLabel, BorderLayout.CENTER);
                chatMessagesPanel.add(messagePanel);
        	}
        }
        JScrollPane chatScroll = new JScrollPane(chatMessagesPanel);
        rightPanel.add(chatScroll, BorderLayout.CENTER);

        // Message input area
        JPanel messageInputPanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        messageInputPanel.add(messageField, BorderLayout.CENTER);
        messageInputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(messageInputPanel, BorderLayout.SOUTH);

        // Split Pane (Left - Right)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250);
        frame.add(splitPane, BorderLayout.CENTER);

        // Show frame
        frame.setVisible(true);
	}
	
	private void showCreateChatDialog() {
        JDialog dialog = new JDialog(frame, "Create Chat Pane", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Create Chat", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel groupLabel = new JLabel("Add to Group:");
        JTextField searchField = new JTextField();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Example data
        String[] allUsers = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};
        for (String user : allUsers) listModel.addElement(user);

        JList<String> userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);

        // Filter logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterList(); }
            public void removeUpdate(DocumentEvent e) { filterList(); }
            public void changedUpdate(DocumentEvent e) { filterList(); }

            private void filterList() {
                String filter = searchField.getText().toLowerCase();
                listModel.clear();
                for (String user : allUsers) {
                    if (user.toLowerCase().contains(filter)) {
                        listModel.addElement(user);
                    }
                }
            }
        });
        
        JScrollPane groupScrollPane = new JScrollPane(searchField);
        groupScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        // Add to your formPanel instead
        formPanel.add(new JLabel("Search Users:"));
        formPanel.add(searchField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Select Users to Add:"));
        formPanel.add(userScrollPane);

        formPanel.add(groupLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(groupScrollPane);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(e -> dialog.dispose());

        createButton.addActionListener(e -> {
            String chatName = nameField.getText().trim();
            String groupMembers = searchField.getText().trim();
            // Handle your creation logic here
            System.out.println("Chat Name: " + chatName);
            System.out.println("Group Members: " + groupMembers);
            dialog.dispose();
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        dialog.setVisible(true);
    }

	public void update() {
		frame.revalidate();
		frame.repaint();
	}
}
