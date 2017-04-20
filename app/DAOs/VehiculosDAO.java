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

    public int AddVehicle(CarDetail carDetail) throws SQLException {
        PreparedStatement preparedStatement;
        String statement = "INSERT INTO vehiculos(TIPO,MARCA,REFERENCIA,PLACA,MODELO) VALUES (?,?,?,?,?)";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,carDetail.type);
            preparedStatement.setString(2,carDetail.marca);
            preparedStatement.setString(3,carDetail.reference);
            preparedStatement.setString(4,carDetail.placa);
            preparedStatement.setInt(5,carDetail.model);
            preparedStatement.executeQuery();

        } catch (SQLException e){
            throw new SQLException(e.getMessage(), e);
        }
        return 1;
    }

    public boolean AssociateVehicleToTransporter(int id_transporter,int id_vehicle) throws SQLException {
        PreparedStatement preparedStatement;
        String statement = "INSERT INTO transp_vehiculos(ID_TRANSP,ID_VEHICULO) VALUES (?,?)";
        boolean exito = false;

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,id_transporter);
            preparedStatement.setInt(2,id_vehicle);
            preparedStatement.executeQuery();
            exito = true;
        } catch (SQLException e){
            throw new SQLException(e.getMessage(), e);
        }
        return exito;
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
