package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean Success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                System.out.println(reqHeaders.toString());

                URI thisURI = exchange.getRequestURI();
                String path = thisURI.getPath();
                System.out.println(path); //FIXME
                //Headers respHeaders = exchange.getResponseHeaders(); //NEVER USED?
                OutputStream os = exchange.getResponseBody();

                String newPath;
                if (path.length() == 0 || path.equals("/") || path.equals("")) {
                    newPath = "web" + path + "index.html";
                }
                else {
                    newPath = "web" + path;
                }
                    File file = new File(newPath);
                    if (file.exists()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                        os.write(Files.readAllBytes(file.toPath()));
                        os.close();
                        Success = true;
                    }
                    else {
                        String path404 = "web/HTML/404.html";
                        File file404 = new File(path404);

                        //System.err.println("Error: File not found: " + path);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                        // We are not sending a response body, so close the response body
                        // output stream, indicating that the response is complete.
                        os.write(Files.readAllBytes(file404.toPath()));
                        os.close();
                        //os.write("E404 File not found.".getBytes()); //FIXME possibly
                    }
                }
            else
            {
                throw new WrongMethodException("Error: Use GET. You didn't");
            }

            /*
            if (!Success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().flush();
                exchange.getResponseBody().close();
            }

             */
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        catch (WrongMethodException e ) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();
        }
    }

}

