import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> clientNames = new ArrayList<>();
    private Socket socket;
    private DatabaseConnection dbConn;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String clientFullName;

    public ClientHandler(Socket socket) {
        try {
            this.socket= socket ;
            this.dbConn = new DatabaseConnection();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            clientHandlers.add(this);

            //broadcastMessage("SERVER: "+ clientUsername+ " has entered the chat ");


        }catch (IOException  e){
            e.printStackTrace();
            closeEverything(socket,objectInputStream,objectOutputStream);
        }
    }

    @Override
    public void run() {
        Request requestReceived;

        while(socket.isConnected()){
            try {
                requestReceived = (Request) objectInputStream.readObject();
                requestHandler(requestReceived);
            }catch (IOException | ClassNotFoundException e){
                closeEverything(socket,objectInputStream,objectOutputStream);
                break;
            }
        }

    }

    public void broadcastMessage(Request request){
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if (!clientHandler.clientFullName.equals(clientHandler)){
                   clientHandler.objectOutputStream.writeObject(request);
                }
            }catch (IOException e){
                closeEverything(socket,objectInputStream,objectOutputStream);
            }
        }
    }

    public void privateMessage(Request request) {
        try {
            for (ClientHandler clientHandler :
                    clientHandlers) {
                if (clientHandler.clientFullName.equals(request.getTo())) {
                    clientHandler.objectOutputStream.writeObject(request);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,objectInputStream,objectOutputStream);
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        clientNames.remove(this.clientFullName);
    }

    public void closeEverything(Socket socket,ObjectInputStream objectInputStream,ObjectOutputStream objectOutputStream){
        removeClientHandler();
        try {
            if (objectInputStream != null){
                objectInputStream.close();
            }
            if (objectOutputStream != null){
                objectOutputStream.close();

            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public void requestHandler(Request request){
        if (request.getRequest().equals("signin")){
            String name =dbConn.signin(request.getEmail(), request.getPassword());
            if (name != null){
                this.clientFullName = name;
                clientNames.add(name);
                Request loginRequest = new Request();
                loginRequest.setSigninAnswer("signin",name);
            }

        }else if (request.getRequest().equals("signup")){
            boolean isSuccessful = dbConn.signup(request.getFullName(),request.getEmail(),request.getPassword());
            Request signupRequest = new Request();
            signupRequest.setBoolean("signupBoolean",isSuccessful);

        }else if (request.getRequest().equals("broadcast")){
            broadcastMessage(request);
        }else if (request.getRequest().equals("privateMessage")){
            privateMessage(request);
        }else if (request.getRequest().equals("activeUsers")){
            Request activeUserRequest = new Request();
            activeUserRequest.setOnlineUsers("activeUsers",clientNames);
        }
    }

}











