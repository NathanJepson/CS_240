package ServiceQ;

import DataAccess.AuthDao;
import DataAccess.DataAccessError;
import DataAccess.Database;
import DataAccess.EventDao;
import Encoding.Encoder;
import Encoding.EventLoader;
import Handler.ClientError;
import Handler.ServerError;
import Model.AuthToken;
import Model.Event;
import RequestQ.EventRequest;
import Response.EventResponse;

import java.sql.Connection;
import java.sql.SQLException;

public class EventService {

    private Database db;

    public EventService() {}


    public EventResponse getEvent(EventRequest er) throws ServerError, ClientError, DataAccessError {

        try {
            db = new Database();
            db.openConnection();
            AuthDao authDao = new AuthDao(db.getConnection());
            if (authDao.find(er.getAuthToken()) != null) {
                AuthToken auth = authDao.find(er.getAuthToken());
                EventLoader eventLoader = new EventLoader();
                EventDao eventDao = new EventDao(db.getConnection());
                Event[] tmp = eventDao.findAssociatedEvents(auth.getUserName());
                eventLoader.setData(tmp);
                //Encoder gsonEncoder = new Encoder();
                //String result = gsonEncoder.getEventResponse(eventLoader);
                EventResponse result = new EventResponse(eventLoader.getData(),true);
                db.closeConnection(true);
                db = null;
                return result;

            } else {
                throw new ClientError("Error: Not authorized.");
            }
        } catch (DataAccessError e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: Opening database. " + e.getMessage());
        }
        catch (ClientError e) {
            db.closeConnection(false);
            db = null;
            throw new ClientError("Error: Opening database. " + e.getMessage());
        }
        catch (Exception e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: Opening database. " + e.getMessage());
        }
    }


}
