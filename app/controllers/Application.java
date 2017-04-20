package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import model.CarDetail;
import model.Localization;
import play.*;
import play.api.db.Database;
import play.libs.Json;
import play.mvc.*;

import repositories.LocalizationRepository;
import repositories.TransportadoresRepository;
import views.html.*;

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

    public Result getTransportadores (){
        TransportadoresRepository transportadoresRepository = new TransportadoresRepository(db);
        JsonNode json = request().body().asJson();
        if( json == null){
            return ok(Json.toJson(transportadoresRepository.getTransportadores()));
        }
        else {
            String estado = (json.has("estado"))?json.get("estado").toString():null;
            if (estado == null){
                return ok(Json.toJson(transportadoresRepository.getTransportadores()));
            }
            else {
                return ok(Json.toJson(transportadoresRepository.getTransportadoresByState(estado)));
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
            if (carDetail.model <= 1980 || carDetail.reference == null || carDetail.type == null || carDetail.id == null ){
                return badRequest("Missing parameters: Expecting object (model)");
            }
            else{
               boolean success = transportadoresRepository.addNewVehicle(carDetail,username);
               if (success == true){
                   return ok(" Vehicle Added to: (username) ");
               }
               else{
                   return ok("Vehicle cannot be Added to: (username)");
               }
            }
        }
    }
}
