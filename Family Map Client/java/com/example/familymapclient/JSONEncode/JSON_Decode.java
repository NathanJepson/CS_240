package com.example.familymapclient.JSONEncode;

import com.example.familymapclient.Model.Person;
import com.example.familymapclient.Request.LoadRequest;
import com.example.familymapclient.Request.LoginRequest;
import com.example.familymapclient.Response.EventResponse;
import com.example.familymapclient.Response.LoginResponse;
import com.example.familymapclient.Response.PersonResponse;
import com.example.familymapclient.Response.RegisterResponse;
import com.google.android.gms.common.api.Response;
import com.google.gson.Gson;

public class JSON_Decode {


    public JSON_Decode( ){}

    public static LoginResponse getLoginResponse(String str) {
        Gson gson = new Gson();
        LoginResponse result = gson.fromJson(str, LoginResponse.class);
        return result;
    }

    public static RegisterResponse getRegisterResponse(String str) {
        Gson gson = new Gson();
        RegisterResponse result = gson.fromJson(str, RegisterResponse.class);
        return result;
    }

    public static PersonResponse getPersonResponse(String str) {
        Gson gson = new Gson(   );
        PersonResponse result = gson.fromJson(str, PersonResponse.class);
        return result;
    }

    public static EventResponse getEventResponse(String str) {
        Gson gson = new Gson(   );
        EventResponse result = gson.fromJson(str, EventResponse.class);
        return result;
    }


}
