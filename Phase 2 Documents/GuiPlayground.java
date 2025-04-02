import javax.swing.*;
import java.awt.*;

public class GuiPlayground extends javax.swing.JFrame {
    private final JSplitPane splitPane;
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private final JLabel nameLabel;
    private final JList chats;
    private final JScrollPane privateChats;
    private final JScrollPane teamChats;
    private final JLabel chatInfo;
    private final JList messages;
    private final JScrollPane listOfMessages;
    private final JPanel inputPanel;
    private final JTextField textField;
    private final JButton sendButton;

    public GuiPlayground() {
        setTitle("Chat Relay");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        splitPane = new JSplitPane();

        leftPanel = new JPanel();
        rightPanel = new JPanel();

        nameLabel = new JLabel("First Last");
        chats = new JList<>();
        privateChats = new JScrollPane();
        teamChats = new JScrollPane();

        chatInfo = new JLabel("Team Name - Members List");
        messages = new JList<>();
        listOfMessages = new JScrollPane();

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
        leftPanel.add(privateChats);
        privateChats.setViewportView(chats);
        leftPanel.add(teamChats);
        teamChats.setViewportView(chats);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(chatInfo);
        chatInfo.setPreferredSize(new Dimension(600, 50));
        rightPanel.add(listOfMessages);
        listOfMessages.setViewportView(messages);
        rightPanel.add(inputPanel);

        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        inputPanel.add(textField);
        inputPanel.add(sendButton);

        pack();
    }

    public static void main(String args[]){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                new GuiPlayground().setVisible(true);
            }
        });
    }
}