import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket= socket ;

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
        Request requestRecieved;

        while(socket.isConnected()){
            try {
                requestRecieved = (Request) objectInputStream.readObject();
                System.out.println(requestRecieved);
                broadcastMessage(requestRecieved);
            }catch (IOException | ClassNotFoundException e){
                closeEverything(socket,objectInputStream,objectOutputStream);
                break;
            }
        }

    }
    public void broadcastMessage(Request request){
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientHandler)){
                   clientHandler.objectOutputStream.writeObject(request);
                   System.out.println(request.toString());
                }
            }catch (IOException e){
                closeEverything(socket,objectInputStream,objectOutputStream);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
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


}











