package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import RequestQ.FillRequest;
import Response.FillResponse;
import Response.LoginResponse;
import ServiceQ.FillService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    //HTTP Method: POST

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                URI thisURI = exchange.getRequestURI();
                String path = thisURI.getPath();
                String[] tokens =  path.split("/");
                FillService fillService = new FillService();
                Encoder gsonEncoder = new Encoder();

               // System.out.println("Yes.");

                if (tokens[0].equals("") && tokens[1].equals("fill") && tokens.length == 4) {
                    if (!isNumeric(tokens[3])) {
                        throw new ClientError("Error: Incorrect parameters.");
                    } else {
                        FillResponse fr = fillService.fillFuntimes(new FillRequest(tokens[2], Integer.parseInt(tokens[3])));
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream os = exchange.getResponseBody();
                        writeString(gsonEncoder.getFillResponse(fr),os);
                        os.close();
                    }

                } else if (tokens[0].equals("") && tokens[1].equals("fill") && tokens.length == 3) {
                    FillResponse fr = fillService.fillFuntimes(new FillRequest(tokens[2],4));
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    writeString(gsonEncoder.getFillResponse(fr),os);
                    os.close();
                } else {
                    throw new ClientError("Error: Invalid method arguments.");
                }

            } else {
                throw new ClientError("Error: Should have used POST.");
            }
        } catch (ClientError  e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getFillResponse(new FillResponse(e.getMessage(),false)),os);
            os.close();

        } catch (ServerError | DataAccessError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getFillResponse(new FillResponse("Error: " + e.getMessage(),false)),os);
            os.close();
        }
        catch (Exception e) {
            e.getMessage();
        }

    }


    /*From https://www.baeldung.com/java-check-string-number */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
