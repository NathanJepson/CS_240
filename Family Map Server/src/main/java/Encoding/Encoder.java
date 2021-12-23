package Encoding;

import Model.Person;
import RequestQ.EventRequest;
import RequestQ.LoadRequest;
import RequestQ.LoginRequest;
import RequestQ.RegisterRequest;
import Response.*;
import com.google.gson.Gson;

public class Encoder {

    public Encoder() {

    }

    public RegisterRequest getRegisterRequest(String str) {
        Gson gson = new Gson();
        RegisterRequest result = gson.fromJson(str,RegisterRequest.class);
        return result;
    }

    public LoginRequest getLoginRequest(String str) {
        Gson gson = new Gson();
        LoginRequest result = gson.fromJson(str, LoginRequest.class);
        return result;
    }

    public LoadRequest getLoadRequest(String str) {
        Gson gson = new Gson();
        LoadRequest result = gson.fromJson(str, LoadRequest.class);
        return result;
    }


    //////////////////////////////////////////////////////////////////////////////////

    public String getRegisterReponse(RegisterResponse rr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(rr);
        return jsonString;
    }

    public String getLoginResponse(LoginResponse lr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(lr);
        return jsonString;
    }

    public String getLoadResponse(LoadResponse lr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(lr);
        return jsonString;
    }

    public String getClearResponse(ClearResponse cr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(cr);
        return jsonString;
    }

    public String getFillResponse(FillResponse fr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(fr);
        return jsonString;
    }

    public String getEventResponse(EventLoader el) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(el);
        return jsonString;
    }

    public String getEventResponse_two(EventResponse er) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(er);
        return jsonString;
    }

    public String getEventResponse_three(OneEventResponse oer) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(oer);
        return jsonString;
    }

    public String getOnePersonResponse(OnePersonResponse opr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(opr);
        return jsonString;
    }

    public String getOnePersonResponse_two (Person person) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(person);
        return jsonString;
    }

    public String getPersonResponse(PersonResponse pr) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(pr);
        return jsonString;
    }


}
