package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import model.CarDetail;
import model.Localization;
import org.springframework.util.SocketUtils;
import play.*;
import play.api.db.Database;
import play.libs.Json;
import play.mvc.*;

import repositories.LocalizationRepository;
import repositories.TransportadoresRepository;
import repositories.VehiculosRepository;
import views.html.*;

import java.sql.SQLException;
import java.util.List;

public class Application extends Controller {

    private Database db;

    @Inject
    public Application(Database db) {
        this.db = db;
    }

    public Result updateLatLong() {
        LocalizationRepository localizationRepository = new LocalizationRepository(db);
        JsonNode json = request().body().asJson();

        if(json == null){
            return badRequest("Expecting Json data");
        }
        else {
            Localization localization = Json.fromJson(json, Localization.class);
            if(localization.username == null || localization.lat == null || localization.lng == null){
                return badRequest("Missing parameters: Expecting object {username, lat, lng}");
            }
            else {
                boolean updated = localizationRepository.updateLatLng(localization);
                if (updated){
                    return ok("Updated localization: " + localization.lat+", "+localization.lng + ", " + localization.fecha_actualizacion);
                }
                else{
                    return internalServerError("Localization - not updated");
                }
            }
        }

    }

    public Result getCurrentPosByUsername() {
        LocalizationRepository localizationRepository = new LocalizationRepository(db);
        JsonNode json = request().body().asJson();

        if(json == null){
            return badRequest("Expecting Json data");
        }
        else {
            Localization localization = Json.fromJson(json, Localization.class);
            if(localization.username == null){
                return badRequest("Missing parameters: Expecting object {username}");
            }
            else {
                localization = localizationRepository.getCurrentPosByUsername(localization.username);
                if(localization != null)
                    return ok(Json.toJson(localization));
                else
                    return ok("Localization doesnt exist");
            }
        }
    }

    public Result getTransporter (){
        TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
        JsonNode json = request().body().asJson();
        if( json == null){
            return ok(Json.toJson(transportadoresRepository.getTransportadores()));
        }
        else {
            String estado = (json.has("estado"))?json.get("estado").asText():null;
            if (estado == null){
                return ok(Json.toJson(transportadoresRepository.getTransportadores()));
            }
            else {
                return ok(Json.toJson(transportadoresRepository.getTransportadoresByState(estado)));
            }
        }
    }

    public Result getTransportersClosed (){
        TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
        JsonNode json = request().body().asJson();
        if( json == null){
            return badRequest(Json.toJson("Data not found"));
        }
        else {
            String estado = (json.has("estado"))?json.get("estado").asText():null;
            double lat = (json.has("lat"))?json.get("lat").asDouble():0;
            double lng = (json.has("lat"))?json.get("lat").asDouble():0;
            if (estado == null ){
                return badRequest(Json.toJson("Wrong client's lat and lng "));
            }
            else {
                return ok(Json.toJson(transportadoresRepository.getTransportadoresClosed(estado, lat, lng)));
            }
        }
    }

    // Funcion para instanciar y añadir un vehiculo a un usuario dado su username

    public Result addVehicle(){

        TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
        JsonNode json = request().body().asJson();
        String username = json.get("username").toString();
        if(json == null){
            return badRequest("Expecting Json data");
        }
        else{
            CarDetail carDetail = Json.fromJson(json, CarDetail.class);
            if (carDetail.model <= 1980 || carDetail.reference == null || carDetail.type == -1 || carDetail.placa == null || carDetail.marca == null ){
                return badRequest("Error in parameters: "+ carDetail.model + " "+ carDetail.reference+" "+carDetail.type+" "+carDetail.placa+" "+carDetail.marca);
            }
            else{
               boolean success = transportadoresRepository.addNewVehicle(carDetail,username);
               if (success == true){
                   return ok(" Vehicle Added to: "+username);
               }
               else{
                   return ok("Vehicle cannot be Added to: "+username);
               }
            }
        }
    }

    public Result getAllVehicleTypes(){
        VehiculosRepository vehiculosRepository = new VehiculosRepository(db);
        return ok(Json.toJson(vehiculosRepository.getAllVehiclesType()));
    }

    public Result getVehiclesByTransporter() throws SQLException {
        TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
        JsonNode json = request().body().asJson();
        int id_transporter = json.get("transporter").asInt();

        return ok("Vehicles for id "+id_transporter+" "+Json.toJson(transportadoresRepository.getVehiclesByTransporter(id_transporter)));
    }

    public Result updateVehicleByTransporter() throws SQLException {
      TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
      JsonNode json = request().body().asJson();
      CarDetail carDetail = Json.fromJson(json,CarDetail.class);
      if (carDetail.marca == null || carDetail.model == -1 || carDetail.type == -1 || carDetail.reference == null || carDetail.placa == null || carDetail.id == -1){
          return badRequest("Error in parameters: "+carDetail.model + " "+ carDetail.reference+" "+carDetail.type+" "+carDetail.placa+" "+carDetail.marca +" "+carDetail.id);
      }
      else{
          boolean updated = transportadoresRepository.updateVehicleByTransporter(carDetail);
          if (updated){
              return ok("Updated vehicle");
          }
          else{
              return internalServerError("Vehicle - not updated");
          }
      }
    }
}
