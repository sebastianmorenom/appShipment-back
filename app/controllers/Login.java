package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.mvc.*;
import play.libs.Json;

public class Login extends Controller {

    public Result login(){

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
                return ok("Hello " + user.username + " - "+ user.password);
            }
        }
    }

}