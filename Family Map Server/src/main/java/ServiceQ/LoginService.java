package ServiceQ;

import DataAccess.AuthDao;
import DataAccess.DataAccessError;
import DataAccess.Database;
import DataAccess.UserDao;
import Handler.ClientError;
import Handler.ServerError;
import Model.AuthToken;
import Model.User;
import RequestQ.LoginRequest;
import Response.LoginResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

/**
 * This class interacts with the database to engage in logging-in users (getting relevant authtokens)
 */
public class LoginService
{
    private Database db;

    /**
     * Constructor
     */
    public LoginService()
    { }

    /**
     *This function will attempt to log-in the user
     * @param r This is a loginrequest object containing the username and password
     * @return A LoginResponse object signifying whether the user was able to be logged-on or not
     */
    public LoginResponse login(LoginRequest r) throws ServerError, ClientError, DataAccessError {
        try {
            db = new Database();
            db.openConnection();

            UserDao userDao = new UserDao(db.getConnection());
            if (userDao.find(r.getUserName()) == null) {
                db.closeConnection(false);
                throw new ClientError("Error: Incorrect information.");
            }
            if (userDao.findWithPassword(r.getUserName(), r.getPassword()) == null) {
                db.closeConnection(false);
                throw new ClientError("Error: Incorrect password.");
            } else {
                AuthDao authDao = new AuthDao(db.getConnection());
                String thisToken = randomString();
                while (authDao.find(thisToken) != null) {
                    thisToken = randomString();
                }
                AuthToken authToken = new AuthToken(thisToken,r.getUserName());
                authDao.insert(authToken);

                User user = userDao.findWithPassword(r.getUserName(),r.getPassword());
                db.closeConnection(true);
                return new LoginResponse(thisToken,user.getUsername(),user.getPersonId(),true);

            }
        } catch (DataAccessError e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: Internal server error.");
        } catch (Exception e) {
            e.getMessage();
            throw new ClientError("Error: " + e.getMessage()    );
        }

    }

    /**
     * Source: https://www.baeldung.com/java-random-string
     * Gives random alpha-numeric string.
     */
    public String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 14;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
