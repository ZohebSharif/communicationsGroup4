package chatRelay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;

public class GUI extends JFrame implements Runnable {
    private final JSplitPane splitPane;
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private final JLabel nameLabel;
    private final JScrollPane privateChats;
    private final JScrollPane teamChats;
    private final JLabel chatInfo;
    private final JPanel messagesPanel;  // Container for chat messages
    private final JScrollPane listOfMessages;
    private final JPanel inputPanel;
    private final JTextField textField;
    private final JButton sendButton;
    private Client client;

    public GUI(Client client) {
    	this.client = client;
        setTitle("Chat Relay");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        splitPane = new JSplitPane();
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        nameLabel = new JLabel();
        privateChats = new JScrollPane();
        teamChats = new JScrollPane();

        chatInfo = new JLabel();
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        listOfMessages = new JScrollPane(messagesPanel);

        inputPanel = new JPanel();
        textField = new JTextField();
        sendButton = new JButton("Send");
    }

	@Override
	public void run() {
		loginPane();
		buildGUI();
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
	
	private void buildGUI() {
		setPreferredSize(new Dimension(1000, 1000));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setRightComponent(rightPanel);
        splitPane.setLeftComponent(leftPanel);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(nameLabel);
        nameLabel.setPreferredSize(new Dimension(200, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        privateChats.setViewportView(buttonPanel);
        leftPanel.add(privateChats);
        leftPanel.add(teamChats);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(chatInfo);
        chatInfo.setPreferredSize(new Dimension(600, 50));
        rightPanel.add(listOfMessages);
        rightPanel.add(inputPanel);

        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(textField);
        inputPanel.add(sendButton);

        pack();
	}
}
