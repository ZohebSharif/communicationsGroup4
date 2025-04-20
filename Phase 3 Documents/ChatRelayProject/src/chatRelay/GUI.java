package chatRelay;

import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
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

    public GUI() {
        setTitle("Chat Relay");
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            splitPane = new JSplitPane();
            leftPanel = new JPanel();
            rightPanel = new JPanel();

            nameLabel = new JLabel("First Last");
            privateChats = new JScrollPane();
            teamChats = new JScrollPane();

            chatInfo = new JLabel("Team Name - Members List");
            messagesPanel = new JPanel();
            messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
            listOfMessages = new JScrollPane(messagesPanel);

            inputPanel = new JPanel();
            textField = new JTextField();
            sendButton = new JButton("Send");

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

            String[] names = {"Zoheb", "Talhah", "Shawn", "Kenny"};
            for (String name : names) {
                JButton b = new JButton();
                b.setLayout(new GridLayout(2,2));
                JLabel label1 = new JLabel("Your");
                JLabel label2 = new JLabel("Name");
                b.add(label1);
                b.add(label2);
                buttonPanel.add(b);
            }

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
