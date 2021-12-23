package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import Response.ClearResponse;
import Response.RegisterResponse;
import ServiceQ.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean Success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                ClearService clearService = new ClearService();
                ClearResponse clearResponse = clearService.clear();

                Encoder gsonEncoder = new Encoder();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream os = exchange.getResponseBody();
                writeString(gsonEncoder.getClearResponse(clearResponse),os);
                os.close();
                Success = true;

            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                OutputStream os = exchange.getResponseBody();
                Encoder gsonEncoder = new Encoder();
                writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Error: Didn't use POST.",false)),os);
                os.close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getRegisterReponse(new RegisterResponse("Internal server error.",false)),os);
            os.close();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
