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
    public String signin(String email, String password){
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

    public boolean signup(String fullName,String email, String password){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user (full_name,email,password) VALUES (?,?,?)");
            statement.setString(1,fullName);
            statement.setString(2,email);
            statement.setString(3,password);

            int isSuccesful =statement.executeUpdate();

            if (isSuccesful > 0){
                return true;
            }
            return false;


        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }


    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        System.out.println(databaseConnection.signup("nurican","nuni","0101"));;
    }
}
