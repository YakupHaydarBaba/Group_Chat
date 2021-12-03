import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String request, to,message, email,fullName,password;
    private ArrayList<String> onlineUsers;

    public Request(String request, String to, String message,String from) {
        this.request = request;
        this.to = to;
        this.message = message;
    }
    public Request (String request,String email,String password){
        this.request = request;
        this.email =email;
        this.password = password;
    }
    public Request (String request,String fullname){
        this.request = request;
        this.fullName = fullname;
    }
    @Override
    public String toString() {
        return "Request{" +
                "request='" + request + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
