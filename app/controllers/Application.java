package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
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
        return ok(Json.toJson(transportadoresRepository.getTransportadores()));
    }
}
