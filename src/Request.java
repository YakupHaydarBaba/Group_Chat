import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    //properties of request class
    private String request, to, message, email, fullName, password;
    private ArrayList<String> onlineUsers;
    private boolean data;
    Encryption encr = new Encryption();


    @Override
    public String toString() {
        return "Request{" +
                "request='" + request + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", onlineUsers=" + onlineUsers +
                ", data=" + data +
                '}';
    }

    public void setMessage(String request, String to, String message, String fullName) {
        this.request = request;
        this.to = to;
        this.message = message;
        this.fullName = fullName;
    }

    public void setSignup(String request, String fullName, String email, String password) {
        this.request = request;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public void setSignin(String request, String email, String password) {
        this.request = request;
        this.email = email;
        this.password = password;
    }

    public void setSigninAnswer(String request, String fullName) {
        this.request = request;
        this.fullName = fullName;
    }

    public void setBoolean(String request, Boolean data) {
        this.request = request;
        this.data = data;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setOnlineUsers(String request, ArrayList<String> onlineUsers) {
        this.request = request;
        this.onlineUsers = onlineUsers;
    }

    public String getRequest() {
        return request;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isData() {
        return data;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void encryptRequest(){
        this.request = encr.encrypt(request);
        this.to = encr.encrypt(to);
        this.message = encr.encrypt(message);
        this.email = encr.encrypt(email);
        this.fullName = encr.encrypt(fullName);
        this.password = encr.encrypt(password);
    }
    public void decryptRequest(){
        this.request = encr.decrypt(request);
        this.to = encr.decrypt(to);
        this.message = encr.decrypt(message);
        this.email = encr.decrypt(email);
        this.fullName = encr.decrypt(fullName);
        this.password = encr.decrypt(password);
    }
}
