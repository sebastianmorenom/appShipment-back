package repositories;

import DAOs.PersonasDAO;
import model.Transportador;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransportadoresRepository {

    private Database db;

    public TransportadoresRepository(Database db){
        this.db = db;
    }

    public List<Transportador> getTransportadores(){

        List<Transportador> transportadores = new ArrayList<>();
        Connection conn;

        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            transportadores = personasDAO.getAllTransportadores();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transportadores;
    }
}
