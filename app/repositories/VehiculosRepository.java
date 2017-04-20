package repositories;


import DAOs.VehiculosDAO;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculosRepository {

    private Database db;

    public VehiculosRepository(Database db){
        this.db = db;
    }

    public List<String> getAllVehiclesType(){

        List<String> tiposVehiculo = new ArrayList<>();
        Connection conn;

        try {
            conn = db.getConnection();
            VehiculosDAO vehiculosDAO = new VehiculosDAO(db,conn);
            tiposVehiculo = vehiculosDAO.getAllVehicleTypes();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tiposVehiculo;
    }
}
