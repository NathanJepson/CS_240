package ServiceQ;

import DataAccess.AuthDao;
import DataAccess.DataAccessError;
import DataAccess.Database;
import DataAccess.PersonDao;
import Encoding.PersonLoader;
import Handler.ClientError;
import Handler.ServerError;
import Model.AuthToken;
import Model.Person;
import RequestQ.PersonRequest;
import Response.PersonResponse;

import java.sql.Connection;
import java.sql.SQLException;

public class PersonService {

    private Database db;

    public PersonService() {}

    public PersonResponse servicePerson(PersonRequest pr) throws ServerError, ClientError {

        try {
            db = new Database();
            db.openConnection();
            AuthDao authDao = new AuthDao(db.getConnection());
            if (authDao.find(pr.getAuthToken()) != null) {
                AuthToken auth = authDao.find(pr.getAuthToken());
                PersonLoader personLoader = new PersonLoader();
                PersonDao personDao = new PersonDao(db.getConnection());
                Person[] tmp = personDao.findAssociatedPersons(auth.getUserName());

                personLoader.setData(tmp);
                PersonResponse result = new PersonResponse(personLoader.getData(),true);
                db.closeConnection(true);
                db = null;
                return result;

            } else {
                db.closeConnection(false); //Without this line of code, many things break!
                db = null;
                throw new ClientError("Error: Not authorized.");
            }

            } catch (DataAccessError e) {
            throw new ServerError("Error: Opening database. " + e.getMessage());
        }
    }
}
