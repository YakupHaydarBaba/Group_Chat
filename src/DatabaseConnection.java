import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DatabaseConnection {
    Connection connection ;

    public DatabaseConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/groupchat","root","1234");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String login(String email, String password){
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT full_name FROM user WHERE email = ? AND password = ? ");
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()){
                return resultSet.getString("full_name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

    }
}
