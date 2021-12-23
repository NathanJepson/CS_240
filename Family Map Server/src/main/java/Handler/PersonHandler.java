package Handler;

import DataAccess.DataAccessError;
import Encoding.Encoder;
import Model.Person;
import RequestQ.PersonRequest;
import Response.EventResponse;
import Response.OnePersonResponse;
import Response.PersonResponse;
import ServiceQ.OnePersonService;
import ServiceQ.PersonService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;

public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    //Method: GET

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

                if (tokens.length == 2 && tokens[1].equals("person")) {
                    PersonService ps = new PersonService();
                    PersonRequest pr = new PersonRequest(authToken);
                    PersonResponse presp = ps.servicePerson(pr);

                    Encoder gsonEncoder = new Encoder();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    String resultant = gsonEncoder.getPersonResponse(presp);
                    writeString( resultant, os);
                    os.close();

                } else if (tokens.length == 3 && tokens[1].equals("person") && !tokens[2].equals("")){
                    OnePersonService onePersonService = new OnePersonService();
                    PersonRequest pr = new PersonRequest(authToken,tokens[2]);
                    Person opr = onePersonService.getResponse(pr);

                    Encoder gsonEncoder = new Encoder();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream os = exchange.getResponseBody();
                    writeString(gsonEncoder.getOnePersonResponse_two(opr), os);
                    os.close();

                } else {
                    throw new ClientError("Error: Bad request.");
                }
            }
            else {
                throw new ClientError("Error: Use GET.");
            }

            } catch (ClientError e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getPersonResponse(new PersonResponse(e.getMessage(),false)),os);
            os.close();

            } catch (ServerError | DataAccessError e ) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getPersonResponse(new PersonResponse("Error: " + e.getMessage(),false)),os);
            os.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
