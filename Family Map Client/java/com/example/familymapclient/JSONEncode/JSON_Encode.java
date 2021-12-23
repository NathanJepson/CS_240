package com.example.familymapclient.JSONEncode;

import com.example.familymapclient.Request.LoginRequest;
import com.example.familymapclient.Request.RegisterRequest;
import com.google.gson.Gson;

public class JSON_Encode {

    public JSON_Encode(){}

    public static String getRegisterRequest(RegisterRequest rr){

        Gson gson = new Gson( );
        String jsonString = gson.toJson(rr);
        return jsonString;
    }


    public static String getLoginRequest(LoginRequest lr){
        Gson gson = new Gson();
        String jsonString = gson.toJson(lr);
        return  jsonString;
    }


}
