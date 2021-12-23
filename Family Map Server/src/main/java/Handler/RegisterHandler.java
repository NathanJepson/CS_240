package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import RequestQ.FillRequest;
import RequestQ.RegisterRequest;
import Response.RegisterResponse;
import ServiceQ.FillService;
import ServiceQ.RegisterService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;


//Method: POST
public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    //Method: POST
        boolean Success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Encoder gsonEncoder = new Encoder();
                RegisterRequest rq = gsonEncoder.getRegisterRequest(reqData);

                RegisterService rs = new RegisterService();
                RegisterResponse rr = rs.register(rq);

                FillService fillService = new FillService();

                try {

                    fillService.fillFuntimes(new FillRequest(rq.getUserName(),4));

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    writeString(gsonEncoder.getRegisterReponse(rr),os);
                    os.close();
                    Success = true;

                } catch (ClientError clientError) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    // We are not sending a response body, so close the response body
                    // output stream, indicating that the response is complete.
                    OutputStream os = exchange.getResponseBody();
                    Encoder gsonEncoder3 = new Encoder();
                    writeString(gsonEncoder3.getRegisterReponse(new RegisterResponse("Error: Bad request. " +
                            clientError.getMessage(),false)),os);
                    os.close();

                } catch (ServerError serverError) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                    OutputStream os = exchange.getResponseBody();
                    Encoder gsonEncoder2 = new Encoder();
                    writeString(gsonEncoder2.getRegisterReponse(new RegisterResponse("Internal server error." + serverError.getMessage(),false)),os);
                    os.close();

                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                OutputStream os = exchange.getResponseBody();
                Encoder gsonEncoder = new Encoder();
                writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Error: Didn't use POST.",false)),os);
                os.close();
                //throw new WrongMethodException("Use POST, no other HTTP methods!"); //FIXME: Is needed?
            }

            if (!Success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                OutputStream os = exchange.getResponseBody();
                Encoder gsonEncoder = new Encoder();
                writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Error: Bad request.",false)),os);
                os.close();
                //exchange.getResponseBody().close();
            }
        }
        catch (IOException | DataAccessError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Internal server error.",false)),os);
            os.close();
            e.printStackTrace();
        } catch (ClientError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Error: " + e.getMessage(),false)),os);
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
