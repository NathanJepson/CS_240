package ServiceQ;

import DataAccess.*;
import Handler.ClientError;
import Model.AuthToken;
import Model.Person;
import Model.User;
import RequestQ.RegisterRequest;
import Response.RegisterResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * This class interacts with the database (through the DAO classes) to add user accounts to the database
 */
public class RegisterService
{
    private Database db;
    /**
     * Constructor
     */
    public RegisterService() throws DataAccessError
    {

    }

    /**
     * This function will attempt to help create the relevant user account
     * @param r This is a RegisterRequest object containing the desired username, password, etc.
     * @return Return a RegisterResponse object which will signify either the success or failure of
     * the register account request
     */
    public RegisterResponse register (RegisterRequest r) throws DataAccessError, ClientError
    {

        try {
        //int incrementor = 0;
        db = new Database();
        db.openConnection();
        PersonDao personDao = new PersonDao(db.getConnection());

        String thisID = randomString();

        UserDao userDao = new UserDao(db.getConnection());

        if (userDao.find(r.getUserName()) != null) {
            throw new ClientError("Error: Username already taken.");
        }

            User toInsert = new User(r.getUserName(),r.getPassword(),r.getEmail(),r.getFirstName(),r.getLastName(),
                    r.getGender(),thisID);
            userDao.insert(toInsert);

            Person toInsertP = new Person(thisID,r.getUserName(),r.getFirstName(),r.getLastName(),
                    r.getGender(), null,null,null);
            personDao.insert(toInsertP);

            AuthDao authDao = new AuthDao(db.getConnection());
            String token = randomString();
            while (authDao.find(token) != null) {
                token = randomString();
            }
            AuthToken insertAuth = new AuthToken(token,r.getUserName());
            authDao.insert(insertAuth);

            db.closeConnection(true);
            db = null;
            return new RegisterResponse(token, r.getUserName(),thisID, true);

        }
        catch (DataAccessError e) {
            e.printStackTrace();
            db.closeConnection(false);
            db = null;
            return constructFailure("There was an error inserting the account." );
        } catch (ClientError e) {
            db.closeConnection(false);
            db = null;
            throw new ClientError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            db = null;
            return constructFailure("There was an error inserting the account. " + e.getMessage());
        }
    }

    public RegisterResponse constructFailure(String message) {
        RegisterResponse result = new RegisterResponse(message, false);
        return result;
    }

    /**
     * Source: https://www.baeldung.com/java-random-string
     * Gives random alpha-numeric string.
     */
    public String randomString() {
        /*
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 14;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString; */
        return UUID.randomUUID().toString();
    }
}
