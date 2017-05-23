package DAOs;

import model.Service;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private Database db;
    private Connection conn;

    public ServiceDAO(Database db, Connection conn) throws SQLException {
        this.db = db;
        this.conn = conn;
    }

    public int insertService(Service service){
        int result;
        PreparedStatement preparedStatement;
        String statement = "INSERT INTO SERVICIOS (ID_USUARIO, ID_TRANSPORTADOR, ORIGEN_LAT,ORIGEN_LNG, DESTINO_LAT, " +
                "DESTINO_LNG, ESTADO, FECHA_INICIO, FECHA_FIN, TIPO_SERVICIO) \n" +
                "VALUES (?,?,?,?,?,?,?,?,null,null)";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,service.idUser);
            preparedStatement.setInt(2,service.idTransporter);
            preparedStatement.setDouble(3,service.origen.lat);
            preparedStatement.setDouble(4,service.origen.lng);
            preparedStatement.setDouble(5,service.destino.lat);
            preparedStatement.setDouble(6,service.destino.lng);
            preparedStatement.setString(7,service.status);
            preparedStatement.setDate(8,new java.sql.Date(service.fecha_inicio.getMillis()));

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return result;
    }

    public int selectIdServiceAccepted(int idUser, int idTransporter) {
        int id=-1;
        PreparedStatement preparedStatement;
        String statement = "SELECT ID_SERVICIO FROM SERVICIOS WHERE ID_USUARIO = ? AND ID_TRANSPORTADOR=?" +
                "AND ESTADO=?";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,idUser);
            preparedStatement.setInt(2,idTransporter);
            preparedStatement.setString(3,"AC");

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                id = result.getInt("ID_SERVICIO");
            }

        } catch (SQLException e) {
            return -1;
        }

        return id;
    }

    public List<Service> getActiveServicesByUserId(int idTrans, String serviceStatus){
        List<Service> services = new ArrayList<>();
        PreparedStatement preparedStatement;
        String statement = "SELECT * FROM SERVICIOS WHERE ID_USUARIO = ? AND ESTADO = ?";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,idTrans);
            preparedStatement.setString(2,serviceStatus);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                Service service = new Service();
                service.idService = result.getInt("ID_SERVICIO");
                service.idUser = result.getInt("ID_USUARIO");
                service.idTransporter = result.getInt("ID_TRANSPORTADOR");
                service.origen.lat = result.getDouble("ORIGEN_LAT");
                service.origen.lng = result.getDouble("ORIGEN_LNG");
                service.destino.lat = result.getDouble("DESTINO_LAT");
                service.destino.lng = result.getDouble("DESTINO_LNG");
                service.status = result.getString("ESTADO");
                services.add(service);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }
}
