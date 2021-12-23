package ServiceQ;

import DataAccess.AuthDao;
import DataAccess.DataAccessError;
import DataAccess.Database;
import DataAccess.PersonDao;
import Handler.ClientError;
import Handler.ServerError;
import Model.AuthToken;
import Model.Person;
import RequestQ.PersonRequest;
import Response.OnePersonResponse;

import java.sql.Connection;
import java.sql.SQLException;

public class OnePersonService {


    private Database db;
    public OnePersonService() {}


    public Person getResponse (PersonRequest pr) throws ServerError, ClientError, DataAccessError {
        try {
            db = new Database();
            db.openConnection();
            AuthDao authDao = new AuthDao(db.getConnection());

            if (authDao.find(pr.getAuthToken()) != null) {
                AuthToken auth = authDao.find(pr.getAuthToken());

                PersonDao personDao = new PersonDao(db.getConnection());
                Person relevantPerson = personDao.find(pr.getPersonID());
                if (relevantPerson == null) {
                    db.closeConnection(false);
                    db = null;
                    throw new ClientError("Error: Person doesn't exit, my child.");
                }
                if (!relevantPerson.getAssociatedUsername().equals(auth.getUserName())) {

                    db.closeConnection(false);
                    throw new ClientError("Error: You do not have permission.");
                } else {
                    db.closeConnection(true);
                    db = null;
                    return relevantPerson;
                }
            }
            else {
                db.closeConnection(false);
                db = null;
                throw new ClientError("Error: Not authorized.");
            }

            } catch (DataAccessError e) {
            if (db != null) {
                db.closeConnection(false);
                db = null;
            }
            throw new ServerError("Error: " + e.getMessage());
        }


    }


}
