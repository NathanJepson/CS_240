package DataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private Connection conn;

    public Connection openConnection() throws DataAccessError {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:DatabaseFMS.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Unable to Establish connection with that database");
        }
        return conn;
    }


    public Connection getConnection() throws DataAccessError {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }


    public void closeConnection(boolean commit) throws DataAccessError {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Unable to close database connection " + e.getMessage());
        }
    }

    public void clearTables() throws DataAccessError
    {

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
            String sql2 = "DELETE FROM person";
            stmt.executeUpdate(sql2);
            String sql3 = "DELETE FROM auth_token";
            stmt.executeUpdate(sql3);
            String sql4 = "DELETE FROM event";
            stmt.executeUpdate(sql4);

        } catch (SQLException e) {
            throw new DataAccessError("SQL Error encountered while clearing tables");
        }
    }
}
