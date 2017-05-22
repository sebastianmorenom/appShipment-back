package repositories;

import DAOs.PaquetesDAO;
import DAOs.PersonasDAO;
import DAOs.ServiceDAO;
import model.Service;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    private Database db;
    private final String service_created = "CR";
    private final String service_accepted = "AC";
    private final String service_finished  = "FI";

    public ServiceRepository(Database db){
        this.db = db;
    }

    public boolean createService(Service service){
        boolean success;
        success = addService(service);
        System.out.println(success);
        if(success){
            service.idService = getIdServiceAccepted(service.idUser, service.idTransporter);
            if (service.idService != -1){
                success = addPackage(service);
                return success;
            }
            else {
                return false;
            }
        }
        return success;
    }

    public boolean addService(Service service) {
        Connection conn;
        try{
            conn = db.getConnection();
            service.status = service_accepted;
            ServiceDAO serviceDAO = new ServiceDAO(db, conn);
            int result = serviceDAO.insertService(service);
            if(result == -1){
                conn.close();
                return false;
            }
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public int getIdServiceAccepted(int idUser, int idTransporter){
        int id = -1;
        Connection conn;
        try {
            conn = db.getConnection();
            ServiceDAO serviceDAO = new ServiceDAO(db, conn);
            id = serviceDAO.selectIdServiceAccepted(idUser, idTransporter);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public boolean addPackage (Service service){
        Connection conn;
        try{
            conn = db.getConnection();
            PaquetesDAO paquetesDAO = new PaquetesDAO(db,conn);
            int result = paquetesDAO.insertPackage(service);
            if(result == -1){
                conn.close();
                return false;
            }
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public List<Service> getActiveServicesById(int id){
        List<Service> services = new ArrayList<>();
        Connection conn;
        try{
            conn = db.getConnection();
            ServiceDAO serviceDAO = new ServiceDAO(db, conn);
            PaquetesDAO paquetesDAO = new PaquetesDAO(db, conn);
            PersonasDAO personasDAO = new PersonasDAO(db,conn);
            services = serviceDAO.getActiveServicesByUserId(id,service_accepted);
            if (services.size() > 0){
                services.get(0).addressee = paquetesDAO.selectAddressee(services.get(0).idService,
                    services.get(0).idUser, services.get(0).idTransporter );
                services.get(0).transporter = personasDAO.getTransportadorById(services.get(0).idTransporter);
            }
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return services;
    }
}
