package DataAccess;

import Model.User;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class will interact with the database, using a particular connection variable,
 * which will allow us to SELECT or INSERT from and into the user table respectively.
 */
public class UserDao
{
    private Connection conn;

    /**
     * @param conn where conn is the database connection passed by the relevant service class.
     */
    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     *
     * @param user where user is the user object containing all relevant information
     *             that we'll want to INSERT into the user table
     */
    public void insert(User user) throws DataAccessError
    {
        String sql = "INSERT INTO user (UserName, Password, Email, FirstName, LastName, " +
                "Gender, person_id) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3,user.getEmail());
            stmt.setString(4,user.getFirstName());
            stmt.setString(5,user.getLastName());
            stmt.setString(6,user.getGender());
            stmt.setString(7,user.getPersonId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessError("Error encountered while inserting into the database");
        }
    }

    /**
     * @param username where username is the unique string identifier (like a net id) which
     *                 will help use find the relevant user row from the user table using SELECT
     * @return a user object containing all relevant information of the user we were looking for (personID,
     * gender, password, etc.)
     */
    public User find(String username) throws DataAccessError {
        User result;

        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE UserName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("person_id"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error encountered while finding user");
        } finally {
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

    public User findWithPassword(String username, String password) throws DataAccessError {

        User result;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE UserName=? AND Password=?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2,password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("person_id"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error encountered while finding user");
        } finally {
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
        String sql = "DELETE * from user";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate(sql);
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessError("Could not delete user table");
        }
    }

    public void deleteOneUser(String username) throws DataAccessError {
        String sql = "DELETE * FROM user WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,username);
            stmt.executeUpdate( );

        } catch (SQLException e ) {
            throw new DataAccessError("Error: Could not delete specific user.");
        }

    }

    public void setPersonId(String userName, String personID) throws DataAccessError {
        String sql = "UPDATE user SET person_id = ? WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,personID);
            stmt.setString(2,userName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessError("Error: Trouble with setting person id.");
        }
    }

}
