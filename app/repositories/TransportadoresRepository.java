package repositories;

import DAOs.PersonasDAO;
import model.CarDetail;
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

    public boolean addNewVehicle(CarDetail carDetail, String username){
        Connection conn;
        boolean request = false;
        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db, conn);
            int transportador = personasDAO.getPersonaIdByUsername(username);
            request = personasDAO.addNewVehicle(carDetail,transportador);

        } catch (SQLException e) {
            e.printStackTrace();
        }

       return request;
    }
}
