package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import model.User;
import play.api.db.Database;
import play.mvc.*;
import play.libs.Json;
import repositories.LoginRepository;

public class Login extends Controller {

    private Database db;

    @Inject
    public Login(Database db) {
        this.db = db;
    }

    public Result login(){

        LoginRepository loginRepository = new LoginRepository(db);
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            User user = Json.fromJson(json, User.class);
            if(user.username == null ||  user.password == null ) {
                return badRequest("Missing parameters: Expecting object {username, password}");
            }
            else {

                boolean validCredentials = loginRepository.verifyCredentials(user);
                if( validCredentials ){
                    String response = "Hello " + user.username + " - "+ user.password;
                    return ok(Json.toJson(response));
                }
                else {
                    return unauthorized(Json.toJson("Invalid Credential!"));
                }
            }
        }
    }
}