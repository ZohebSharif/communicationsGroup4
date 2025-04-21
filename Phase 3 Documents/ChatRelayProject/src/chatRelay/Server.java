package chatRelay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    //private HashMap<userId, ClientHandler> clients;
    private ArrayList<ClientHandler> clients; // Testing purposes
    private DBManager dbManager;
    private int port;
    private String IP;

    public Server(int port, String IP) {
        this.port = port;
        this.IP = IP;
    }

    public void connect() {
        ServerSocket server;

        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);

            while(true) {
                Socket client = server.accept();

                ClientHandler clientSock = new ClientHandler(client, this);

                //clients.add(clientSock);

                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            
        }
    }
    public void disconnect() {}
    public void recievePacket(String clientId, Packet packet) {}
    public void sendErrorMessage(String userId, String errorMessage) {}
    public void sendSuccessMessage(String userId, String successMessage) {}
    public void sendPacketToUsers(Packet packet, String[] userIds) {}
}
