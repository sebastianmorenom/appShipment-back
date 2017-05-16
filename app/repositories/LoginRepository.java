package repositories;

import com.google.inject.Inject;
import model.User;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginRepository {

    private Database db;

    public LoginRepository (Database db){
        this.db = db;
    }


    public boolean verifyCredentials (User user ){

        boolean response;
        PreparedStatement preparedStatement = null;
        Connection conn =null;

        String statement = "select * from personas where email = ? and password = ?";

        try {
            conn = db.getConnection();
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, user.username);
            preparedStatement.setString(2, user.password);

            ResultSet result = preparedStatement.executeQuery();

            response = result.next();
            if(response){
                user.id = result.getInt("ID_PERSONA");
            }
            conn.close();

        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        catch (Exception ex){
            return false;
        }

        return response;

    }

}
