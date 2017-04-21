package DAOs;

import model.CarDetail;
import model.Transportador;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonasDAO {

    private Database db;
    private Connection conn;

    public PersonasDAO(Database db, Connection conn){
        this.db = db;
        this.conn = conn;
    }

    public int getPersonaIdByUsername (String username) throws SQLException{

        int id=-1;
        PreparedStatement preparedStatementId;
        String idStatement = "select ID_PERSONA from PERSONAS where EMAIL=?";

        try {
            preparedStatementId = conn.prepareStatement(idStatement);
            preparedStatementId.setString(1, username);

            ResultSet result = preparedStatementId.executeQuery();

            while (result.next()){
               id = result.getInt("ID_PERSONA");
            }

        } catch (SQLException e) {
            return -1;
        }

        return id;
    }

    public List<Transportador> getAllTransportadores() throws SQLException{
        List<Transportador> transportadores = new ArrayList<>();
        PreparedStatement preparedStatement;
        String statement = "select p.nombre, p.apellido, p.calificacion, t.ESTADO, l.LATITUD, l.LONGITUD,l.FECHA_ACTUALIZACION, v.MARCA,v.REFERENCIA, v.MODELO, v.PLACA, vt.NOMBRE as tipo \n" +
                "from personas p \n" +
                "INNER JOIN Localizaciones l on p.ID_PERSONA = l.ID_LOCALIZACION\n" +
                "INNER JOIN TRANSPORTADORES t on p.ID_PERSONA = t.ID_TRANSPORTADOR \n" +
                "INNER JOIN TRANSP_VEHICULOS tv on t.ID_TRANSPORTADOR=tv.ID_TRANSP \n" +
                "INNER JOIN vehiculos v on tv.ID_VEHICULO=v.ID_VEHICULO\n" +
                "INNER JOIN TIPOS_VEHICULO vt on v.TIPO=vt.ID_TIPO";

        try {
            preparedStatement = conn.prepareStatement(statement);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                System.out.println(" entrasmos");
                Transportador trans = new Transportador();
                trans.name = result.getString("NOMBRE") + result.getString("APELLIDO");
                trans.calificacion = result.getDouble("CALIFICACION");
                trans.pos.lat = result.getDouble("LATITUD");
                trans.pos.lng = result.getDouble("LONGITUD");
                trans.carDetail.type = result.getInt("TIPO");
                trans.carDetail.placa = result.getString("PLACA");
                trans.carDetail.model = result.getInt("MODELO");
                trans.carDetail.reference = result.getString("REFERENCIA");

                transportadores.add(trans);

            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage(), e);
        }

        return transportadores;
    }

    /*public List<CarDetail> getVehiclesByTransporter(){
        List<CarDetail> vehiculos = new ArrayList<>();

        return vehiculos;
    }*/
}
