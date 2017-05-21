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
    private final double MAX_DISTANCE = 3000;
    private final double EARTH_RADIUS = 6371.01;

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

    public Transportador getTransportadorById(int id){

        Transportador transportador = new Transportador();
        Connection conn;
        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            transportador = personasDAO.getTransportadorById(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transportador;
    }

    public List<Transportador> getTransportadoresByState(String estado){

        List<Transportador> transportadores = new ArrayList<>();
        Connection conn;
        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            transportadores = personasDAO.getAllTransportadoresByState(estado);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transportadores;
    }

    public List<Transportador> getTransportadoresClosed(String estado, double lat, double lng){

        List<Transportador> transportadores = new ArrayList<>();
        Connection conn;
        try {
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            transportadores = personasDAO.getAllTransportadoresByState(estado);
            transportadores = selectClosedTransporters(transportadores, lat, lng);

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
     public List<CarDetail> getVehiclesByTransporter(int id_transporter) throws SQLException {
        List<CarDetail> vehiculos = new ArrayList<>();
        Connection conn;

        try{
            conn = db.getConnection();
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            vehiculos = personasDAO.getVehiclesByTransporter(id_transporter);

        } catch(SQLException e){
            throw new SQLException(e.getMessage(), e);
        }

        return vehiculos;
    }

    public boolean updateVehicleByTransporter(CarDetail carDetail) throws SQLException {
        boolean exito =false;
        Connection conn;

        conn = db.getConnection();
        VehiculosDAO vehiculosDAO = new VehiculosDAO(db,conn);
        exito = vehiculosDAO.updateVehicleByTransporter(carDetail);
        return exito;
    }

    public List<Transportador> selectClosedTransporters(List<Transportador> transporters, double lat, double lng){
        List<Transportador> selectedTrasnporters = new ArrayList<>();
        for ( Transportador t : transporters) {

            double latDistance = Math.toRadians(t.pos.lat - lat);
            double lonDistance = Math.toRadians(t.pos.lng - lng);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(t.pos.lat))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = EARTH_RADIUS * c *1000; // convert to meters

            if (distance < MAX_DISTANCE)
                selectedTrasnporters.add(t);
        }

        return selectedTrasnporters;
    }
}
