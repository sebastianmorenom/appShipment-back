package DAOs;

import model.Addressee;
import model.Service;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaquetesDAO {

    private Database db;
    private Connection conn;

    public PaquetesDAO(Database db, Connection conn) throws SQLException {
        this.db = db;
        this.conn = conn;
    }

    public int insertPackage(Service service) {
        int result=-1;
        PreparedStatement preparedStatement;
        String statement = "INSERT INTO PAQUETES (ID_SERVICIO, ID_USUARIO, ID_TRANSPORTADOR, NOMBRE_DESTINATARIO, " +
                "TIPO_ID_DESTINATARIO, NM_ID_DESTINATARIO, TEL_DESTINATARIO, DECLARACION_CONTENIDO, VALOR_DECLARADO," +
                "PESO, ID_DIMENSIONES) \n" +
                "VALUES (?,?,?,?,?,?,?,?,?,null,?)";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,service.idService);
            preparedStatement.setInt(2,service.idUser);
            preparedStatement.setInt(3,service.idTransporter);
            preparedStatement.setString(4,service.addressee.name);
            preparedStatement.setString(5,service.addressee.idType);
            preparedStatement.setDouble(6,service.addressee.id);
            preparedStatement.setDouble(7,service.addressee.numTel);
            preparedStatement.setString(8,service.addressee.contentDeclaration);
            preparedStatement.setDouble(9,service.addressee.valueDeclaration);
            preparedStatement.setInt(10,service.addressee.dimension);

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return result;
    }

    public Addressee selectAddressee(int idService, int idUser, int idTransporter){
        Addressee addressee = new Addressee();

        PreparedStatement preparedStatement;
        String statement = "SELECT * FROM PAQUETES WHERE ID_SERVICIO = ? " +
                "AND ID_USUARIO = ? AND ID_TRANSPORTADOR = ? ";

        try {
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,idService);
            preparedStatement.setInt(2,idUser);
            preparedStatement.setInt(3,idTransporter);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                addressee.name = result.getString("NOMBRE_DESTINATARIO");
                addressee.idType = result.getString("TIPO_ID_DESTINATARIO");
                addressee.id = result.getInt("NM_ID_DESTINATARIO");
                addressee.numTel = result.getLong("TEL_DESTINATARIO");
                addressee.contentDeclaration = result.getString("DECLARACION_CONTENIDO");
                addressee.valueDeclaration = result.getLong("VALOR_DECLARADO");
                addressee.dimension = result.getInt("ID_DIMENSIONES");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressee;
    }

}
