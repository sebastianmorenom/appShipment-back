package controllers;

import play.mvc.*;
import play.libs.Json;

public class Login extends Controller {

    public Result login(String username){

        return ok(Json.toJson("Hello!, "+username));
    }

}