import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {//Server that accept clients.

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {

        this.serverSocket = serverSocket;
    }
    //Server starting
    public void startServer() {
        try {
            //In this loop server accepts client and create a new client handler and start it as a thread
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }
    // In this method server stops accepting client and closes the socket
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //main
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
