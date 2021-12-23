package DataAccess;

import Handler.ServerError;
import Model.*;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * This class will interact with the database to SELECT or INSERT into the Event table.
 * find will make use of SELECT to grab the relevant Event object based on the event ID
 * insert will make use of INSERT to add new rows to the Event table in the SQLite database
 */
public class EventDao
{
    private Connection conn;

    /**
     *
     * @param conn where conn is the connection variable passed-in by the relevant service class
     */
    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     *
     * @param event where Event is the event object which we will use to INSERT an event into the event table
     */
    public void insert(Event event) throws DataAccessError
    {
        String sql = "INSERT INTO event (event_id, AssociatedUserName, person_id, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,event.getEventID());
            stmt.setString(2,event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4,event.getLatitude());
            stmt.setDouble(5,event.getLongitude());
            stmt.setString(6,event.getCountry());
            stmt.setString(7,event.getCity());
            stmt.setString(8,event.getEventType());
            stmt.setInt(9,event.getYear());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessError("Error encountered while inserting into the event table " + e.getMessage());
        }

    }

    /**
     *
     * @param eventID where eventID is the particular string identifier of the event we are looking for
     * @return an event object which will contain all relevant information (like geographic location,
     * year, etc.)
     */
    public Event find(String eventID) throws DataAccessError
    {
        Event result;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE event_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Event(rs.getString("event_id"),rs.getString("AssociatedUserName"),
                        rs.getString("person_id"),rs.getDouble("Latitude"),
                        rs.getDouble("Longitude"),rs.getString("Country"),
                        rs.getString("City"),rs.getString("EventType"),rs.getInt("Year"));

                return result;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error while finding event.");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();;
                }
            }
        }
        return null;
    }

    public Event findAssociatedMarriage(String personID) throws DataAccessError { //Input: PersonID
        Event result;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE EventType = \'Marriage\' AND person_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Event(rs.getString("event_id"),rs.getString("AssociatedUserName"),
                        rs.getString("person_id"),rs.getDouble("Latitude"),
                        rs.getDouble("Longitude"),rs.getString("Country"),
                        rs.getString("City"),rs.getString("EventType"),rs.getInt("Year"));

                return result;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error while finding marriage. SAD! " + e.getMessage());
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();;
                }
            }
        }
        return null;
    }


    public Event[] findAssociatedEvents(String userName) throws ServerError //INPUT: Specific User
    {
        Vector<Event> result = new Vector<Event>();
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE AssociatedUserName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Event tmp = new Event(rs.getString("event_id"),rs.getString("AssociatedUserName"),
                        rs.getString("person_id"),rs.getDouble("Latitude"),
                        rs.getDouble("Longitude"),rs.getString("Country"),
                        rs.getString("City"),rs.getString("EventType"),rs.getInt("Year"));
                result.add(tmp);
            }
            Object[] finalResult = result.toArray();

            Event[] finalResultForReals = new Event[finalResult.length];
            for (int i=0; i < finalResult.length; i++) {
                finalResultForReals[i] = (Event)finalResult[i];
            }
            return finalResultForReals;

        } catch (SQLException e) {
            throw new ServerError("Error: Internal server " + e.getMessage());
        }
    }

    public void deleteAssociatedEvents(String userName) throws DataAccessError//INPUT: Specific User
    {
        String sql = "DELETE FROM event WHERE AssociatedUserName = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessError("Error: Could not delete associated events. " + e.getMessage());
            }
    }


    public void delete() throws DataAccessError {
        String sql = "DELETE FROM event";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessError("Could not delete event table");
        }
    }
}
