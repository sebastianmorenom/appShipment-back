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
        System.out.println(json);
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            User user = Json.fromJson(json, User.class);
            if(user.username == null ) {
                return badRequest("Missing parameter [username]");
            } else if ( user.password == null ) {
                return badRequest("Missing parameter [password]");
            }else
            {
                boolean validCredentials = loginRepository.verifyCredentials(user);
                if( validCredentials ){
                    return ok("Hello " + user.username + " - "+ user.password);
                }
                else {
                    return unauthorized("Invalid Credentials");
                }
            }
        }
    }

}