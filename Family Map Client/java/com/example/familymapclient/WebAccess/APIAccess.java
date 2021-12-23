package com.example.familymapclient.WebAccess;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class APIAccess {

    private static final int TIMEOUT = 2000;

    public APIAccess() {}

    public static String proxy(String thisURL, String path, String requestBody) throws SocketTimeoutException {

        try {

            if (path.equals("/user/login")) {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                return getResponse(connection,requestBody,true);

            } else if (path.equals("/user/register")) {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                return getResponse(connection,requestBody,true);

            } else if (path.equals("/clear"))  {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("POST");
                return getResponse(connection,requestBody,false);
            } else if (path.equals("/load")) {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                return getResponse(connection,requestBody,true);
            }
            else if (path.equals("/person")) {

                throw new NoAuthToken("You must provide an auth token.");

            } else if (path.equals("/event")) {

                throw new NoAuthToken("You must provide an auth token.");

            } else {
                throw new UnknownPath("This path is unknown to the client app.");
            }
        } catch (UnknownPath | NoAuthToken e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new SocketTimeoutException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    //This second method is used to provide an auth-token
    public static String proxy(String thisURL, String path, String requestBody, String authToken) throws SocketTimeoutException {

        try {
            if (path.equals("/user/login") || path.equals("/user/register")) {
                return proxy(thisURL,path,requestBody);
            }
            else if (path.equals("/person")) {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization",authToken);
                return getResponse(connection,requestBody,false);

            } else if (path.equals("/event")) {
                URL url = new URL(thisURL + path);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization",authToken);
                return getResponse(connection,requestBody,false);

            } else {
                throw new UnknownPath("This path is unknown to the client app.");
            }
        } catch (UnknownPath | MalformedURLException | ProtocolException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new SocketTimeoutException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        }

    }


    private static String getResponse (HttpURLConnection thisConnection, String requestBody, boolean setOutput)
            throws SocketTimeoutException {

        String responseBodyData;
        thisConnection.setDoOutput(setOutput);

        try {
            if (setOutput) {
                OutputStreamWriter out = new OutputStreamWriter(thisConnection.getOutputStream());
                out.write(requestBody);
                out.close();
            }

            InputStream responseBody = thisConnection.getInputStream();

            // Read response body bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = responseBody.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            // Convert response body bytes to a string
            responseBodyData = baos.toString();
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException(e.getMessage());
        }
        catch (IOException e) {
            //thisConnection.disconnect();
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        //thisConnection.disconnect();
        return responseBodyData;
    }

    



}
