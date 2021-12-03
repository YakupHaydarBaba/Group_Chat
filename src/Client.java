import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String fullName;
    private Scanner scanner;
    private ArrayList<String> activeUsers;

    public Client(Socket socket){
        try {
            this.socket = socket;
            scanner = new Scanner(System.in);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            login();
            this.activeUsers.add("Everyone");


        }catch (IOException  e){
            closeEverything(socket,objectInputStream,objectOutputStream);
        }
    }
    public void sendMessage(){
        try {

            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                Request messageRequest = new Request();
                messageRequest.setMessage("message","yakup",messageToSend);
                objectOutputStream.writeObject(messageRequest);
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

                while (socket.isConnected()){
                    try {
                        Request requestReceived  = (Request) objectInputStream.readObject();
                        requestHandler(requestReceived);
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

    private void login() throws IOException {
        System.out.println("enter email");
        String email = scanner.nextLine();
        System.out.println("enter password");
        String password = scanner.nextLine();
        System.out.println(email+password);
        Request loginRequest = new Request();
        loginRequest.setSignin("signin",email,password);
        System.out.println(loginRequest);
        objectOutputStream.writeObject(loginRequest);
    }

    private void requestHandler(Request request){
        if (request.getRequest().equals("Message")){
            System.out.println(request.getMessage());
        }else if (request.getRequest().equals("activeUsers")){
            this.activeUsers.clear();
            this.activeUsers.add("Everyone");
            this.activeUsers.addAll(request.getOnlineUsers());
        }else if (request.getRequest().equals("sigin")){
            this.fullName = request.getFullName();
        }

    }


    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);;
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket);
        client.listenForMessage();
        client.sendMessage();


    }


}
