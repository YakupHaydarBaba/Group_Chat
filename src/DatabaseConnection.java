import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DatabaseConnection {
    Encryption encr = new Encryption();
    Connection connection;
    //setting database connection
    public DatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/groupchat", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //checking sign in informations
    public String signin(String email, String password) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT full_name FROM user WHERE email = ? AND password = ? ");
            statement.setString(1, encr.encrypt(email));
            statement.setString(2, encr.encrypt(password));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return encr.decrypt(resultSet.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    //signing up new user
    public boolean signup(String fullName, String email, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user (full_name,email,password) VALUES (?,?,?)");
            statement.setString(1, encr.encrypt(fullName));
            statement.setString(2, encr.encrypt(email));
            statement.setString(3, encr.encrypt(password));

            int isSuccesful = statement.executeUpdate();

            if (isSuccesful > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
