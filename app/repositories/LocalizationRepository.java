package repositories;

import DAOs.LocalizacionDAO;
import DAOs.PersonasDAO;
import model.Localization;
import org.joda.time.DateTime;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class LocalizationRepository {

    private Database db;

    public LocalizationRepository(Database db){
        this.db = db;
    }

    public boolean updateLatLng(Localization localization){

        boolean response;
        Localization currentPos;
        Connection conn;

        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db, conn);
            LocalizacionDAO localizacionDAO = new LocalizacionDAO(db, conn);
            localization.id = personasDAO.getPersonaIdByUsername(localization.username);
            currentPos = localizacionDAO.getLocalizacionById( localization.id);
            localization.fecha_actualizacion = new DateTime();

            if (currentPos != null){
                response = localizacionDAO.updateLocalizacion(localization);
            }
            else{
                response = localizacionDAO.insertLocalizacion(localization);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return response;
    }

    public Localization getCurrentPosByUsername (String username){

        Localization currentPos = new Localization();
        Connection conn;
        int id;

        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db, conn);
            LocalizacionDAO localizacionDAO = new LocalizacionDAO(db, conn);
            id = personasDAO.getPersonaIdByUsername(username);
            currentPos = localizacionDAO.getLocalizacionById( id);
            if(currentPos != null)
                currentPos.fecha_actualizacion = new DateTime();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return currentPos;
    }

}
