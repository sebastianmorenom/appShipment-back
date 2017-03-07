package controllers;

import play.*;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Login extends Controller {

    public Result login(String username){

        return ok(Json.toJson("Hello!, "+username));
    }

}