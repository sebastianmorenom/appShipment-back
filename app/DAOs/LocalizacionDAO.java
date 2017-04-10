package DAOs;

import model.Localization;
import org.joda.time.DateTime;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalizacionDAO {
    private Database db;
    private Connection conn;

    public LocalizacionDAO(Database db, Connection conn){
        this.db = db;
        this.conn = conn;
    }

    public boolean insertLocalizacion (Localization localization) throws SQLException{

        boolean exito=false;
        PreparedStatement preparedStatement;
        String statement = "INSERT INTO LOCALIZACIONES (ID_LOCALIZACION, LATITUD, LONGITUD, FECHA_ACTUALIZACION) VALUES (?,?,?,?)";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,localization.id);
            preparedStatement.setDouble(2,localization.lat);
            preparedStatement.setDouble(3,localization.lng);
            preparedStatement.setDate(4,new java.sql.Date(localization.fecha_actualizacion.getMillis()));

            int result = preparedStatement.executeUpdate();
            if(result ==1 ){
                exito=true;
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage(), e);
        }
        finally {
        }
        return exito;
    }

    public boolean updateLocalizacion (Localization localization) throws SQLException{

        boolean exito=false;
        PreparedStatement preparedStatement;
        String statement = "UPDATE LOCALIZACIONES SET LATITUD=?, LONGITUD=?, FECHA_ACTUALIZACION=? WHERE ID_LOCALIZACION=?";


        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setDouble(1,localization.lat);
            preparedStatement.setDouble(2,localization.lng);
            preparedStatement.setDate(3,new java.sql.Date(localization.fecha_actualizacion.getMillis()));
            preparedStatement.setInt(4,localization.id);

            int result = preparedStatement.executeUpdate();
            if(result ==1 ){
                exito=true;
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage(), e);
        }
        finally {
        }

        return exito;
    }

    public Localization getLocalizacionById(int id) throws SQLException{

        Localization localization = null;
        PreparedStatement preparedStatement;
        String statement = "SELECT * FROM localizaciones WHERE id_localizacion=?";

        try{
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,id);

            ResultSet result =  preparedStatement.executeQuery();

            while (result.next()){
                localization = new Localization();
                localization.id = result.getInt("ID_LOCALIZACION");
                localization.lat = result.getDouble("LATITUD");
                localization.lng = result.getDouble("LONGITUD");
                localization.fecha_actualizacion = new DateTime(result.getDate("FECHA_ACTUALIZACION"));
            }

        }
        catch (SQLException e){
            throw new SQLException(e.getMessage(), e);
        }

        return localization;
    }
}
