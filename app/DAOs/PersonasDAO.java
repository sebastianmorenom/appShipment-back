package DAOs;

import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonasDAO {

    private Database db;
    private Connection conn;

    public PersonasDAO(Database db, Connection conn){
        this.db = db;
        this.conn = conn;
    }

    public int getPersonaIdByUsername (String username) throws SQLException{

        int id=-1;
        PreparedStatement preparedStatementId;
        String idStatement = "select ID_PERSONA from PERSONAS where EMAIL=?";

        try {
            preparedStatementId = conn.prepareStatement(idStatement);
            preparedStatementId.setString(1, username);

            ResultSet result = preparedStatementId.executeQuery();

            while (result.next()){
               id = result.getInt("ID_PERSONA");
            }

        } catch (SQLException e) {
            return -1;
        }

        return id;
    }

}
