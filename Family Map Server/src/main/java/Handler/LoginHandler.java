package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import RequestQ.LoginRequest;
import RequestQ.RegisterRequest;
import Response.LoadResponse;
import Response.LoginResponse;
import Response.RegisterResponse;
import ServiceQ.LoginService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;


public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //HTTP Method: POST
        boolean Success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Encoder gsonEncoder = new Encoder();
                LoginRequest lrq = gsonEncoder.getLoginRequest(reqData);

                LoginService ls = new LoginService();
                LoginResponse lr = ls.login(lrq);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream os = exchange.getResponseBody();
                String resultant = gsonEncoder.getLoginResponse(lr);
                writeString(resultant,os);
                os.close();
                Success = true;

            } else {
                throw new ClientError("Error: Didn't use POST.");
            }

            if (!Success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                OutputStream os = exchange.getResponseBody();
                Encoder gsonEncoder = new Encoder();
                writeString(gsonEncoder.getLoginResponse(new LoginResponse("Error: Bad request.",false)),os);
                os.close();
            }
        } catch (ClientError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getLoginResponse(new LoginResponse(e.getMessage(),false)),os);
            os.close();

        } catch (ServerError | DataAccessError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getLoginResponse(new LoginResponse(e.getMessage(),false)),os);
            os.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
