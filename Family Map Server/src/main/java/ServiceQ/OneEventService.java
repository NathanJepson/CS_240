package ServiceQ;

import DataAccess.AuthDao;
import DataAccess.DataAccessError;
import DataAccess.Database;
import DataAccess.EventDao;
import Handler.ClientError;
import Handler.ServerError;
import Model.AuthToken;
import Model.Event;
import RequestQ.EventRequest;
import Response.OneEventResponse;

import java.sql.Connection;
import java.sql.SQLException;

public class OneEventService {
    private Database db;
    public OneEventService() {}


    public OneEventResponse getResponse(EventRequest er) throws ServerError, ClientError, DataAccessError {
        try {
            db = new Database();
            db.openConnection();
            AuthDao authDao = new AuthDao(db.getConnection());
            if (authDao.find(er.getAuthToken()) != null) {
                AuthToken auth = authDao.find(er.getAuthToken());

                EventDao eventDao = new EventDao(db.getConnection());
                Event relevantEvent = eventDao.find(er.getEventID());
                if (!relevantEvent.getAssociatedUsername().equals(auth.getUserName())) {

                    throw new ClientError("Error: You do not have permission.");
                }
                else if (relevantEvent == null) {
                    throw new ClientError("Event doesn't exist.");
                }
                else {
                    OneEventResponse result = new OneEventResponse(relevantEvent.getAssociatedUsername(),
                            relevantEvent.getEventID(),relevantEvent.getPersonID(),relevantEvent.getLatitude(),
                            relevantEvent.getLongitude(),relevantEvent.getCountry(),relevantEvent.getCity(),
                            relevantEvent.getEventType(),relevantEvent.getYear(),true );
                    db.closeConnection(true);
                    db = null;
                    return result;
                }
            }
                else {
                    throw new ClientError("Error: No authentication.");
                }
            } catch (DataAccessError e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: " + e.getMessage());
        }
        catch (ClientError e) {
            db.closeConnection(false);
            db = null;
            throw new ClientError("Error: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
