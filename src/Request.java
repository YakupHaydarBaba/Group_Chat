import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String request, to,message, email,fullName,password;
    private ArrayList<String> onlineUsers;
    private boolean data;



    public void setMessage(String request, String to, String message){
        this.request = request;
        this.to = to;
        this.message = message;
    }

    public void setSignup(String request, String fullName, String email,String password){
        this.request =request;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public void setSignin(String request, String email ,String password){
        this.request= request;
        this.email = email;
        this.password = password;
    }

    public void setSigninAnswer(String request, String fullName){
        this.request= request;
        this.fullName = fullName;
    }

    public void setBoolean(String request , Boolean data){
        this.request = request;
        this.data = data;
    }

    public void setRequest(String request){
        this.request= request;
    }

    public void setOnlineUsers(String request,ArrayList<String> onlineUsers) {
        this.request= request;
        this.onlineUsers = onlineUsers;
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

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }
}
