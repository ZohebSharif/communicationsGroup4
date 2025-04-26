package chatRelay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;

public class GUI extends JFrame implements Runnable {
    private Client client;

    public GUI(Client client) {
    	this.client = client;
    }

	@Override
	public void run() {
		loginPane();
		if (client.getAdminStatus()) {
			buildITGUI();
		} else {
			buildUserGUI();
		}
	}
	
	private void loginPane() {
		JFrame frame = new JFrame("Login Pane");
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
        JFrame frame = new JFrame("Chat Application");
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
        for (int i = 0; i < 4; i++) {
            JButton chatButton = new JButton("<html>Name<br>Members List<br>Last Message Time</html>"); // Updated with information
            privateChatsList.add(chatButton);
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
        for (int i = 0; i < 3; i++) {
            JButton groupButton = new JButton("<html>Name<br>Members List<br>Last Message Time</html>"); // Updated with information
            groupChatsList.add(groupButton);
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
        for (int i = 0; i < 7; i++) { // Updated with information
            JPanel messagePanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("<html>Name • Time<br>Message</html>"); // Updated with information
            messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            messagePanel.add(messageLabel, BorderLayout.CENTER);
            chatMessagesPanel.add(messagePanel);
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
	
	public void buildITGUI() {
		JFrame frame = new JFrame("Chat Application - IT Admin View");
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
        for (int i = 0; i < 6; i++) {
            JButton chatButton = new JButton("<html>Name<br>Members List<br>Last Message Time</html>");
            if (i == 2 || i == 3) { // Access to all users - special
                chatButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }
            privateChatsList.add(chatButton);
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
        for (int i = 0; i < 3; i++) {
            JButton groupButton = new JButton("<html>Name<br>Members List<br>Last Message Time</html>");
            if (i == 2) { // Access to all chats - special
                groupButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }
            groupChatsList.add(groupButton);
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
        for (int i = 0; i < 7; i++) {
            JPanel messagePanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("<html>Name • Time<br>Message</html>");
            messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            messagePanel.add(messageLabel, BorderLayout.CENTER);
            chatMessagesPanel.add(messagePanel);
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
}
