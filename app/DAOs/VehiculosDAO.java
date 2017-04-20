package DAOs;


import model.CarDetail;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculosDAO {
    private Database db;
    private Connection conn;

    public VehiculosDAO(Database db, Connection conn){
        this.db = db;
        this.conn = conn;
    }

    public int AddVehicle(CarDetail carDetail){

        return 0;
    }

    public boolean AssociateVehicleToTransporter(int id_transporter,int id_vehicle){

        return true;
    }

    public List<String> getAllVehicleTypes() throws SQLException {
        List<String> tiposVehiculo = new ArrayList<>();
        PreparedStatement preparedStatement;
        String statement = "select nombre from tipos_vehiculo";

        try {
            preparedStatement = conn.prepareStatement(statement);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                String tipo;
                tipo = result.getString("NOMBRE");
                tiposVehiculo.add(tipo);
            }
        } catch (SQLException e){
            throw new SQLException(e.getMessage(), e);
        }

        return tiposVehiculo;
    }
}
