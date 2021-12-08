import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements ActionListener, KeyListener {
    private Socket socket;
    //properties
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String fullName;
    private Scanner scanner;
    private ArrayList<String> activeUsers;
    private String messages = "";


    public JFrame startScreen = new JFrame("Welcome");
    public JFrame signin = new JFrame("Sign-in");
    public JFrame signup = new JFrame("Sign-up");
    public JFrame chatScreen = new JFrame("Chat");
    public JButton signinButton, signupButton, inButton, upButton, chatSendButton, inCancel, upCancel;
    public JTextField inEmail, upEmail, upFullname, messageField;
    public JTextArea messageArea;
    public JComboBox<String> activeUsersBox;
    public JScrollBar scrollBar;
    public JPasswordField inPassword, upPassword, upPasswordRe;

    // setting screens and input output streams
    public Client(Socket socket) {
        try {
            setChatScreen();
            setSigninScreen();
            setSignupScreen();
            setStartScreen();
            activeUsers = new ArrayList<>();
            this.socket = socket;
            scanner = new Scanner(System.in);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());


            this.activeUsers.add("Everyone");


        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }
    // sending message in the message field
    public void sendMessage() {
        try {

            if (socket.isConnected()) {
                String messageToSend = this.messageField.getText();
                Request messageRequest = new Request();
                int index = activeUsersBox.getSelectedIndex();
                if (activeUsers.get(index).equals("Everyone")) {
                    messageRequest.setMessage("broadcast", activeUsers.get(index), messageToSend, this.fullName);
                    messageRequest.encryptRequest();
                    objectOutputStream.writeObject(messageRequest);
                    messages += ("You: " + messageToSend + "\n");
                    this.messageArea.setText(messages);
                    this.messageField.setText("");
                } else {
                    messageRequest.setMessage("privateMessage", activeUsers.get(index), messageToSend, this.fullName);
                    messageRequest.encryptRequest();
                    objectOutputStream.writeObject(messageRequest);
                    messages += ("You : " + messageToSend + "(Sended to " + activeUsers.get(index) + ")\n");
                    this.messageArea.setText(messages);
                    this.messageField.setText("");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }
    //listens for requests coming from server
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()) {
                    try {
                        Request requestReceived = (Request) objectInputStream.readObject();
                        requestReceived.decryptRequest();
                        requestHandler(requestReceived);
                    } catch (IOException | ClassNotFoundException e) {
                        closeEverything(socket, objectInputStream, objectOutputStream);
                    }
                }
            }
        }).start();
    }
    //This method closes everything if something bad happens :/
    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
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
    // sending login request
    private void login() {
        try {
            String email = inEmail.getText();

            String password = String.valueOf(inPassword.getPassword());

            Request loginRequest = new Request();
            loginRequest.setSignin("signin", email, password);
            loginRequest.encryptRequest();
            objectOutputStream.writeObject(loginRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //sending signup request
    private void signup() {
        String password = String.valueOf(upPassword.getPassword());
        String password2 = String.valueOf(upPasswordRe.getPassword());
        if (password.equals(password2)) {
            try {
                String fullname = upFullname.getText();
                String email = upEmail.getText();

                Request signupRequest = new Request();
                signupRequest.setSignup("signup", fullname, email, password);
                signupRequest.encryptRequest();
                objectOutputStream.writeObject(signupRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this.signup,
                    "Passwords must be same",
                    "Warning",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    // takes requests and handle them
    private void requestHandler(Request request) {

        if (request.getRequest().equals("privateMessage")) {
            messages += (request.getFullName() + ": " + request.getMessage() + " (private for you)\n");
            this.messageArea.setText(messages);
        } else if (request.getRequest().equals("broadcast")) {
            messages += (request.getFullName() + ": " + request.getMessage() + " \n");
            this.messageArea.setText(messages);
            if (request.getFullName().equals("Server")) {
                refreshActiveUsers();
            }
        } else if (request.getRequest().equals("activeUsers")) {
            this.activeUsers.clear();
            this.activeUsers.add("Everyone");
            this.activeUsers.addAll(request.getOnlineUsers());
            populateComboBox();
        } else if (request.getRequest().equals("signin")) {
            if (request.getFullName() != null) {
                this.fullName = request.getFullName();
                this.signin.setVisible(false);
                this.chatScreen.setVisible(true);
                refreshActiveUsers();
            } else if (request.getFullName() == null) {
                JOptionPane.showMessageDialog(this.signin,
                        "Email or password is incorrect. Try again.",
                        "Can't signin",
                        JOptionPane.ERROR_MESSAGE);
                inEmail.setText("");
                inPassword.setText("");
            }
        } else if (request.getRequest().equals("signupBoolean")) {
            if (request.isData()) {
                JOptionPane.showMessageDialog(this.signup,
                        "Signup succesfull");

                signup.setVisible(false);
                signin.setVisible(true);
            } else if (!request.isData()) {
                JOptionPane.showMessageDialog(this.signup,
                        "Can't sign up try another email",
                        "Error occurred",
                        JOptionPane.ERROR_MESSAGE);

                this.upEmail.setText("");
            }
        }

    }
    //sending active user request to server
    public void refreshActiveUsers() {
        Request activeUsersRequest = new Request();
        activeUsersRequest.setRequest("activeUsers");
        try {
            activeUsersRequest.encryptRequest();
            objectOutputStream.writeObject(activeUsersRequest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //populates combobox with user list
    public void populateComboBox() {
        activeUsersBox.removeAllItems();
        for (String user :
                activeUsers) {

            activeUsersBox.addItem(user);
        }
    }
    //setting start screen
    public void setStartScreen() {
        this.startScreen.setVisible(true);
        this.startScreen.setSize(300, 250);
        this.startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.startScreen.setLayout(null);

        this.signupButton = new JButton("Sign-up");
        this.signupButton.setBounds(100, 100, 100, 30);

        this.signinButton = new JButton("Sign-in");
        this.signinButton.setBounds(100, 50, 100, 30);

        this.signinButton.addActionListener(this);
        this.signupButton.addActionListener(this);

        this.startScreen.add(signinButton);
        this.startScreen.add(signupButton);

        this.startScreen.setResizable(false);
    }
    // setting sign in screen
    public void setSigninScreen() {

        this.signin.setSize(300, 400);
        this.signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.signin.setLayout(null);

        this.inCancel = new JButton("Cancel");
        this.inCancel.setBounds(100, 300, 100, 30);

        this.inEmail = new JTextField();
        this.inEmail.setBounds(50, 50, 200, 50);


        this.inPassword = new JPasswordField();
        this.inPassword.setBounds(50, 150, 200, 50);

        JLabel inEmailLabel = new JLabel("Email");
        inEmailLabel.setBounds(50, 20, 100, 20);

        JLabel inPasswordLabel = new JLabel("Password");
        inPasswordLabel.setBounds(50, 120, 100, 20);

        this.inButton = new JButton("Sign-in");
        this.inButton.setBounds(100, 250, 100, 30);
        this.inButton.addActionListener(this);

        this.signin.add(inCancel);
        this.signin.add(inEmail);
        this.signin.add(inPassword);
        this.signin.add(inButton);
        this.signin.add(inEmailLabel);
        this.signin.add(inPasswordLabel);
        this.signin.setVisible(false);

        this.signin.setResizable(false);

        inPassword.addKeyListener(this);
        inEmail.addKeyListener(this);
        inCancel.addActionListener(this);
    }
    // setting sign up screen
    public void setSignupScreen() {
        this.signup.setSize(300, 500);
        this.signup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.signup.setLayout(null);


        JLabel upPasswordLabel = new JLabel("Password");
        JLabel upPasswordReLabel = new JLabel("Re Password");
        JLabel upEmailLabel = new JLabel("Email");
        JLabel upFullnameLabel = new JLabel("Full Name");

        this.upCancel = new JButton("Cancel");
        this.upFullname = new JTextField();
        this.upEmail = new JTextField();
        this.upPassword = new JPasswordField();
        this.upPasswordRe = new JPasswordField();

        this.upButton = new JButton("Sign-Up");

        upPasswordLabel.setBounds(50, 240, 100, 30);
        upPasswordReLabel.setBounds(50, 170, 100, 30);
        upEmailLabel.setBounds(50, 100, 100, 30);
        upFullnameLabel.setBounds(50, 30, 100, 30);

        this.upCancel.setBounds(100, 400, 100, 30);
        this.upPassword.setBounds(50, 270, 200, 30);
        this.upPasswordRe.setBounds(50, 200, 200, 30);
        this.upEmail.setBounds(50, 130, 200, 30);
        this.upFullname.setBounds(50, 60, 200, 30);

        this.upButton.setBounds(100, 350, 100, 30);
        this.upButton.addActionListener(this);

        this.signup.add(upCancel);
        this.signup.add(upFullname);
        this.signup.add(upEmail);
        this.signup.add(upPasswordRe);
        this.signup.add(upPassword);
        this.signup.add(upPasswordLabel);
        this.signup.add(upFullnameLabel);
        this.signup.add(upPasswordReLabel);
        this.signup.add(upEmailLabel);
        this.signup.add(upButton);

        this.signup.setVisible(false);
        this.signup.setResizable(false);

        this.upPassword.addKeyListener(this);
        this.upPasswordRe.addKeyListener(this);
        this.upEmail.addKeyListener(this);
        this.upFullname.addKeyListener(this);
        upCancel.addActionListener(this);
    }
    //setting chat screen
    public void setChatScreen() {
        chatScreen.setSize(700, 450);
        chatScreen.setLayout(null);
        chatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.messageArea = new JTextArea();
        this.messageField = new JTextField();
        this.chatSendButton = new JButton("Send");
        this.activeUsersBox = new JComboBox<>();
        this.scrollBar = new JScrollBar();


        this.messageArea.setBounds(10, 10, 410, 300);
        this.messageField.setBounds(10, 320, 300, 30);
        this.chatSendButton.setBounds(320, 320, 100, 30);
        this.activeUsersBox.setBounds(450, 10, 200, 20);



        this.messageArea.setEnabled(false);
        activeUsersBox.addItem("Everyone");

        this.chatScreen.add(messageArea);
        this.chatScreen.add(messageField);
        this.chatScreen.add(chatSendButton);
        this.chatScreen.add(activeUsersBox);
        this.chatSendButton.addActionListener(this);

        this.chatScreen.setVisible(false);
        this.chatScreen.setResizable(false);

        messageField.addKeyListener(this);

    }
    //main
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        ;
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket);
        client.listenForMessage();


    }

    @Override// actionlistener for buttons
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(signinButton)) {
            startScreen.setVisible(false);
            signin.setVisible(true);
        }
        if (e.getSource().equals(signupButton)) {
            startScreen.setVisible(false);
            signup.setVisible(true);
        }
        if (e.getSource().equals(inButton)) {
            if (!inEmail.getText().equals("") && !String.valueOf(inPassword.getPassword()).equals("")) {
                login();
            } else if (inEmail.getText().equals("") || String.valueOf(inPassword.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(this.signin,
                        "Please fill in all blank fields.",
                        "Can't sign-in",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
        if (e.getSource().equals(upButton)) {
            if (!upEmail.getText().equals("") && !upFullname.getText().equals("") && !String.valueOf(upPassword.getPassword()).equals("") && !String.valueOf(upPasswordRe.getPassword()).equals(""))
                signup();
            else if (upEmail.getText().equals("") || upFullname.getText().equals("") || String.valueOf(upPassword.getPassword()).equals("") || String.valueOf(upPasswordRe.getPassword()).equals(""))
                JOptionPane.showMessageDialog(this.signup,
                        "Please fill in all blank fields.",
                        "Can't sign-up",
                        JOptionPane.ERROR_MESSAGE);
        }
        if (e.getSource().equals(chatSendButton)) {
            if (!this.messageField.getText().equals("")) {
                sendMessage();
            }
        }
        if (e.getSource().equals(inCancel)) {
            signin.setVisible(false);
            startScreen.setVisible(true);
        }
        if (e.getSource().equals(upCancel)) {
            signup.setVisible(false);
            startScreen.setVisible(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override//keylistener for sending with enter
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (chatScreen.isVisible()) {
                chatSendButton.doClick();
            } else if (signin.isVisible()) {
                inButton.doClick();
            } else if (signup.isActive()) {
                upButton.doClick();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
