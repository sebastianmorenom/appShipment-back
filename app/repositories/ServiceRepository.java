package repositories;

import DAOs.PaquetesDAO;
import DAOs.ServiceDAO;
import model.Service;
import play.api.db.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class ServiceRepository {
    private Database db;

    public ServiceRepository(Database db){
        this.db = db;
    }

    public boolean createService(Service service){
        boolean success;
        System.out.println("Adding service...");
        success = addService(service);
        System.out.println(success);
        if(success){
            System.out.println("Service added\n Getting ID...");
            service.idService = getIdServiceAccepted(service.idUser, service.idTransporter);
            System.out.println("Service id: "+ service.idService);
            if (service.idService != -1){
                System.out.println("Adding package");
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
            service.status = "AC";
            ServiceDAO serviceDAO = new ServiceDAO(db, conn);
            int result = serviceDAO.insertService(service);
            if(result == -1){
                return false;
            }
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
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
