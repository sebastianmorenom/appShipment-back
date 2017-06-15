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

    public List<Service> getActiveServicesByUserId(int id, String userType, String serviceStatus){
        List<Service> services = new ArrayList<>();
        String user_key;
        if(userType == "user")
            user_key="ID_USUARIO";
        else
            user_key="ID_TRANSPORTADOR";
        PreparedStatement preparedStatement;
        String statement = "SELECT * FROM SERVICIOS WHERE "+user_key+" = ? AND (ESTADO = ? OR ESTADO = ?)";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,serviceStatus);
            preparedStatement.setString(3,"ST");

            ResultSet result = preparedStatement.executeQuery();
            services = listServices(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public List<Service> getActiveServicesById(int idService, int idUser, int idTrans){
        List<Service> services = new ArrayList<>();
        PreparedStatement preparedStatement;
        String statement = "SELECT * FROM SERVICIOS WHERE ID_SERVICIO = ? AND " +
                "ID_USUARIO = ? AND ID_TRANSPORTADOR = ?";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,idService);
            preparedStatement.setInt(2,idUser);
            preparedStatement.setInt(3,idTrans);

            ResultSet result = preparedStatement.executeQuery();
            services = listServices(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public boolean changeServiceState(int idService, int idUser, int idTrans, String state){
        boolean exito=false;
        PreparedStatement preparedStatement;
        String statement = "UPDATE SERVICIOS SET ESTADO=? WHERE ID_SERVICIO = ? AND " +
                "ID_USUARIO = ? AND ID_TRANSPORTADOR = ?";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,state);
            preparedStatement.setInt(2,idService);
            preparedStatement.setInt(3,idUser);
            preparedStatement.setInt(4,idTrans);

            int result = preparedStatement.executeUpdate();
            if(result ==1 ){
                exito=true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exito;
    }

    private List<Service> listServices (ResultSet result) throws SQLException{
        List<Service> services = new ArrayList<>();
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
        orderServices(services);
        return services;
    }

    private void orderServices (List<Service> services){
        for (int i=0; i<services.size()-1; i++){
            for (int j=i+1; i<services.size(); j++){
                if (services.get(i).idService < services.get(j).idService) {
                    Service aux_service = services.get(i);
                    services.set(i, services.get(j));
                    services.set(j, aux_service);
                }
            }
        }
    }
}
