import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String username;

    public Client(Socket socket,String username){
        try {
            this.socket = socket;

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.username = username;


        }catch (IOException  e){
            closeEverything(socket,objectInputStream,objectOutputStream);
        }
    }
    public void sendMessage(){
        try {
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                Request r1 = new Request("message","yakup",messageToSend);
                objectOutputStream.writeObject(r1);
            }
        }catch (IOException  e){
            e.printStackTrace();
            closeEverything(socket,objectInputStream,objectOutputStream);
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()){
                    try {
                        Request requestRecieved  = (Request) objectInputStream.readObject();
                        msgFromGroupChat = requestRecieved.toString();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException | ClassNotFoundException e){
                        closeEverything(socket,objectInputStream,objectOutputStream);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,ObjectInputStream objectInputStream,ObjectOutputStream objectOutputStream){
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

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);;
        System.out.println("Enter username for the group chat: ");
        String username = scan.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket ,"username");
        client.listenForMessage();
        client.sendMessage();


    }

}
