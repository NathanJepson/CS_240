package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import RequestQ.EventRequest;
import Response.EventResponse;
import Response.FillResponse;
import Response.OneEventResponse;
import ServiceQ.EventService;
import ServiceQ.OneEventService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    //METHOD: GET

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String authToken = exchange.getRequestHeaders().getFirst("Authorization");

                if (authToken == null) {
                    throw new ClientError("Error: Didn't use auth-token.");
                }
                else if (authToken.equals("")) {
                    throw new ClientError("Error: Didn't use auth-token.");
                }

                URI thisURI = exchange.getRequestURI();
                String path = thisURI.getPath();
                String[] tokens =  path.split("/");

                if (tokens.length == 2 && tokens[1].equals("event")) {
                    EventService eventService = new EventService();
                    EventRequest er = new EventRequest(authToken);
                    EventResponse eventResponse = eventService.getEvent(er);

                    Encoder gsonEncoder = new Encoder();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    writeString( (gsonEncoder.getEventResponse_two(eventResponse)), os);
                    os.close();

                } else if (tokens.length == 3 && tokens[1].equals("event") && !tokens[2].equals("")) {
                    OneEventService eventService = new OneEventService();
                    EventRequest er = new EventRequest(authToken, tokens[2]);
                    OneEventResponse eventResponse = eventService.getResponse(er);

                    Encoder gsonEncoder = new Encoder();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    writeString(gsonEncoder.getEventResponse_three(eventResponse), os);
                    os.close();

                } else {
                    throw new ClientError("Error: Bad request.");
                }

            } else {
                throw new ClientError("Error: You didn't use GET.");
            }

        } catch (ClientError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getEventResponse_two(new EventResponse(e.getMessage(),false)),os);
            os.close();

        } catch (ServerError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getEventResponse_two(new EventResponse("Error: " + e.getMessage(),false)),os);
            os.close();

        } catch (IOException | DataAccessError e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getEventResponse_two(new EventResponse("Error: " + e.getMessage(),false)),os);
            os.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getEventResponse_two(new EventResponse("Error: " + e.getMessage(),false)),os);
            os.close();
        }
    }


    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
