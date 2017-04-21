package repositories;

import DAOs.PersonasDAO;
import DAOs.VehiculosDAO;
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
        boolean exito = false;
        int id_vehicle = 0;
        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db, conn);
            VehiculosDAO vehiculosDAO = new VehiculosDAO(db,conn);
            //int transportador = personasDAO.getPersonaIdByUsername(username);
            id_vehicle = vehiculosDAO.AddVehicle(carDetail);
            if(id_vehicle != -1){
               exito = vehiculosDAO.AssociateVehicleToTransporter(1002,id_vehicle);
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       return exito;
    }
 /*
    public List<CarDetail> getVehiclesByTransporter() throws SQLException {
        boolean exito = false;
        List<CarDetail> vehiculos = new ArrayList<>();
        Connection conn;

        try{
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            vehiculos = personasDAO.getVehiclesByTransporter();

        } catch(SQLException e){
            throw new SQLException(e.getMessage(), e);
        }

        return vehiculos;
    }
*/
}
