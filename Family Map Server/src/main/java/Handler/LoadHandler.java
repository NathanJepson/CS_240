package Handler;

import Encoding.Encoder;
import RequestQ.LoadRequest;
import RequestQ.RegisterRequest;
import Response.LoadResponse;
import Response.RegisterResponse;
import ServiceQ.LoadService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //Method: POST
        boolean Success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Encoder gsonEncoder = new Encoder();
                LoadRequest lq = gsonEncoder.getLoadRequest(reqData);

                LoadService ls = new LoadService();
                LoadResponse lr = ls.getLoadResponse(lq); //This is where the magic happens.

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream os = exchange.getResponseBody();
                writeString(gsonEncoder.getLoadResponse(lr),os);
                os.close();
                Success = true;

            }
            else {
                throw new ClientError();
            }

            if (!Success) {
                throw new ClientError();
            }

        } catch (ClientError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getLoadResponse(new LoadResponse("Error: 404.",false)),os);
            os.close();
        }
        catch (ServerError e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getLoadResponse(new LoadResponse(e.getMessage(),false)),os);
            os.close();
        }

        catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            OutputStream os = exchange.getResponseBody();
            Encoder gsonEncoder = new Encoder();
            writeString(gsonEncoder.getLoadResponse(new LoadResponse(e.getMessage(),false)),os);
            os.close();
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
