import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatScreen extends JFrame implements ActionListener {
    public JFrame startScreen = new JFrame("Welcome");
    public JFrame signin = new JFrame("Sign-in");
    public JFrame signup = new JFrame("Sign-up");
    public JFrame chatScreen = new JFrame("Chat");
    public JButton signinButton, signupButton, inButton, upButton, chatSendButton;
    public JTextField inEmail, upEmail, upFullname, messageField;
    public JTextArea messageArea;
    public JComboBox<String> activeUsers;
    public JScrollBar scrollBar;
    public JPasswordField inPassword, upPassword, upPasswordRe;


    public ChatScreen() {
        setStartScreen();
        setSigninScreen();
        setSignupScreen();
        setChatScreen();
    }

    public void setStartScreen() {
        this.startScreen.setVisible(true);
        this.startScreen.setSize(300, 250);
        this.startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.startScreen.setLayout(null);

        this.signupButton = new JButton("Sign-up");
        this.signupButton.setBounds(100, 50, 100, 30);

        this.signinButton = new JButton("Sign-in");
        this.signinButton.setBounds(100, 100, 100, 30);

        this.signinButton.addActionListener(this);
        this.signupButton.addActionListener(this);

        this.startScreen.add(signinButton);
        this.startScreen.add(signupButton);
    }

    public void setSigninScreen() {

        this.signin.setSize(300, 400);
        this.signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.signin.setLayout(null);

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

        this.signin.add(inEmail);
        this.signin.add(inPassword);
        this.signin.add(inButton);
        this.signin.add(inEmailLabel);
        this.signin.add(inPasswordLabel);
        this.signin.setVisible(false);
    }

    public void setSignupScreen() {
        this.signup.setSize(300, 500);
        this.signup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.signup.setLayout(null);


        JLabel upPasswordLabel = new JLabel("Password");
        JLabel upPasswordReLabel = new JLabel("Re Password");
        JLabel upEmailLabel = new JLabel("Email");
        JLabel upFullnameLabel = new JLabel("Full Name");


        this.upFullname = new JTextField();
        this.upEmail = new JTextField();
        this.upPassword = new JPasswordField();
        this.upPasswordRe = new JPasswordField();

        this.upButton = new JButton("Sign-Up");

        upPasswordLabel.setBounds(50, 240, 100, 30);
        upPasswordReLabel.setBounds(50, 170, 100, 30);
        upEmailLabel.setBounds(50, 100, 100, 30);
        upFullnameLabel.setBounds(50, 30, 100, 30);

        this.upPassword.setBounds(50, 270, 200, 30);
        this.upPasswordRe.setBounds(50, 200, 200, 30);
        this.upEmail.setBounds(50, 130, 200, 30);
        this.upFullname.setBounds(50, 60, 200, 30);

        this.upButton.setBounds(100, 350, 100, 30);

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


    }


    public void setChatScreen() {
        chatScreen.setSize(700, 450);
        chatScreen.setLayout(null);
        chatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.messageArea = new JTextArea();
        this.messageField = new JTextField();
        this.chatSendButton = new JButton("Send");
        this.activeUsers = new JComboBox<>();
        this.scrollBar = new JScrollBar();



        this.messageArea.setBounds(10,10,410,300);
        this.messageField.setBounds(10,320,300,30);
        this.chatSendButton.setBounds(320,320,100,30);
        this.activeUsers.setBounds(450,10,200,20);


        this.messageArea.setEnabled(false);
        activeUsers.addItem("everyone");

        this.chatScreen.add(messageArea);
        this.chatScreen.add(messageField);
        this.chatScreen.add(chatSendButton);
        this.chatScreen.add(activeUsers);

        this.chatScreen.setVisible(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(signinButton)){
            startScreen.setVisible(false);
            signin.setVisible(true);
        }
        if (e.getSource().equals(signupButton)){
            startScreen.setVisible(false);
            signup.setVisible(true);
        }

    }

    public static void main(String[] args) {
        ChatScreen cs = new ChatScreen();

    }
}
