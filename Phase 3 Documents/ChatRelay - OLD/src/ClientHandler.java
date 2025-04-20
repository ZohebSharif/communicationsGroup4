import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String userId;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        try {
            InputStream inStream = clientSocket.getInputStream();
            inputStream = new ObjectInputStream(inStream);

            OutputStream outStream = clientSocket.getOutputStream();
            outputStream = new ObjectOutputStream(outStream);
        } catch (IOException e) {
            e.printStackTrace(); // Better Message
        }
    }

    public void start() {}
    public void stop() {}
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

    public void sendPacket(Packet packet) {}
    
    @Override
    public void run() {
        try {
            Packet checkLogin = (Packet) inputStream.readObject();

            if (checkLogin.getActionType().equals(Packet.actionType.LOGIN)) {
                String[] args = {"Success"};
                Packet accept = new Packet(Packet.actionType.SUCCESS, args, "Client");
                System.out.println("Got: " + accept.getActionType().toString());
                outputStream.writeObject(accept);
            }
        } catch (IOException | ClassNotFoundException e) {

        }
    }
    
}
