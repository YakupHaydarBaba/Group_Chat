import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    //client handlers properties
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> clientNames = new ArrayList<>();
    private Socket socket;
    private DatabaseConnection dbConn;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String clientFullName;
    //Constructor that creates input,output streams
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dbConn = new DatabaseConnection();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //adding new client to client socket list
            clientHandlers.add(this);
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }

    @Override // Thread that waiting for clients request. when it arrives it pass them to request handler.
    public void run() {
        Request requestReceived;

        while (socket.isConnected()) {
            try {
                requestReceived = (Request) objectInputStream.readObject();
                requestReceived.decryptRequest();
                requestHandler(requestReceived);
            } catch (IOException | ClassNotFoundException e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
                break;
            }
        }

    }
    //this method broadcast message to everyone
    public void broadcastMessage(Request request) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientFullName.equals(this.clientFullName)) {
                    Request broadcastRequest = request;
                    broadcastRequest.encryptRequest();
                    clientHandler.objectOutputStream.writeObject(request);
                }
            } catch (IOException e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
            }
        }
    }
    //this method forwards message to a specific person
    public void privateMessage(Request request) {
        try {
            for (ClientHandler clientHandler :
                    clientHandlers) {
                if (clientHandler.clientFullName.equals(request.getTo())) {
                    Request privateMessageRequest = request;
                    privateMessageRequest.encryptRequest();
                    clientHandler.objectOutputStream.writeObject(request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }
    //this method removes client from client list if something bad happens :/
    public void removeClientHandler() {
        clientHandlers.remove(this);
        clientNames.remove(this.clientFullName);
    }
    //This method closes everything if something bad happens :/
    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        removeClientHandler();
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();

            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //this method takes requests and does what client want
    public void requestHandler(Request request) {
        try {
            if (request.getRequest().equals("signin")) {
                String name = dbConn.signin(request.getEmail(), request.getPassword());
                Request loginRequest = new Request();
                loginRequest.setSigninAnswer("signin", name);
                loginRequest.encryptRequest();
                objectOutputStream.writeObject(loginRequest);
                if (name != null) {
                    this.clientFullName = name;
                    clientNames.add(name);
                    Request loginMessage = new Request();
                    loginMessage.setMessage("broadcast", "Everyone", name + " has entered.", "Server");
                    broadcastMessage(loginMessage);
                }
            } else if (request.getRequest().equals("signup")) {
                boolean isSuccessful = dbConn.signup(request.getFullName(), request.getEmail(), request.getPassword());
                Request signupRequest = new Request();
                signupRequest.setBoolean("signupBoolean", isSuccessful);
                signupRequest.encryptRequest();
                objectOutputStream.writeObject(signupRequest);

            } else if (request.getRequest().equals("broadcast")) {
                broadcastMessage(request);
            } else if (request.getRequest().equals("privateMessage")) {
                privateMessage(request);
            } else if (request.getRequest().equals("activeUsers")) {
                Request activeUserRequest = new Request();
                ArrayList<String> activeClients = new ArrayList<>();
                for (String name :
                        clientNames) {
                    if (name != this.clientFullName) {
                        activeClients.add(name);
                    }
                }
                activeUserRequest.setOnlineUsers("activeUsers", activeClients);
                activeUserRequest.encryptRequest();
                objectOutputStream.writeObject(activeUserRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}











