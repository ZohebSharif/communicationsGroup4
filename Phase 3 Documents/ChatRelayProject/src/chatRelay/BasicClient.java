package chatRelay;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BasicClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public BasicClient(String ip, int port) {
        System.out.println("BasicClient constructor fired");
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server at " + ip + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
        System.out.println("BasicClient.login() fired");
        try {
            Packet loginPacket = new Packet(actionType.LOGIN, new String[]{username, password}, "tempClient");
            out.writeObject(loginPacket);
            out.flush();

            Packet response = (Packet) in.readObject();
            System.out.println("Login Response: " + response.getActionType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String authorId, String chatId, String content) {
        System.out.println("BasicClient.sendMessage() fired");
        try {
            Packet messagePacket = new Packet(actionType.SEND_MESSAGE, new String[]{authorId, chatId, content}, authorId);
            out.writeObject(messagePacket);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(String userId) {
        try {
            Packet logoutPacket = new Packet(actionType.LOGOUT, new String[]{}, userId);
            out.writeObject(logoutPacket);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void listen() {


		System.out.println("in listen()  loop");
        try {
            while (true) {
                Packet incoming = (Packet) in.readObject();
                System.out.println("Received from server: " + incoming.getActionType());

                switch (incoming.getActionType()) {
                    case SUCCESS -> {
                        System.out.println("Action successful.");
                    }
                    case SEND_MESSAGE -> {
                        System.out.println("Message received: " + incoming.getActionArguments()[0]);
                    }
                    case ERROR -> {
                        System.out.println("Error from server: " + incoming.getActionArguments()[0]);
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

    public static void main(String[] args) {
        BasicClient client = new BasicClient("127.0.0.1", 1337);


        client.login("chrsmi", "asdf");
        client.listen();
    }
}
