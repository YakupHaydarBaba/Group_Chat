import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String request, to,message, email,fullName,password;
    private ArrayList<String> onlineUsers;

    public Request(String request, String to, String message) {
        this.request = request;
        this.to = to;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Request{" +
                "request='" + request + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
