package DataAccess;

import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class will interact with the database to either SELECT an authtoken row by the value of the AuthToken,
 * or by inserting a new AuthToken row into the database (including with an associated personID attached to it)
 */
public class AuthDao
{
    private Connection conn;

    /**
     * @param conn Where conn is the connection to the database passed by the relevant service class
     */
    public AuthDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * @param auth where auth is the specific AuthToken object which contains the AuthToken string and the personID
     */
    public void insert(AuthToken auth) throws DataAccessError
    {
        String sql = "INSERT INTO auth_token (AuthToken,UserName) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,auth.getTheToken());
            stmt.setString(2,auth.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessError("Error while inserting into auth_token table");
        }
    }

    /**
     * @param authtoken Where authtoken is a specific authtoken string, which will be used to check the database
     *          for the right authtoken row which will have a personID attached to it
     * @return the authtoken object containing the authtoken and the relevant personID
     */
    public AuthToken find(String authtoken) throws DataAccessError
    {
        AuthToken result;
        ResultSet rs = null;
        String sql = "SELECT * FROM auth_token WHERE AuthToken = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,authtoken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new AuthToken(rs.getString("AuthToken"),rs.getString("UserName"));
                return result;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error while finding AuthToken");
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void delete() throws DataAccessError {
        String sql = "DELETE * from auth_token";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessError("Could not delete auth_token table");
        }
    }

}
