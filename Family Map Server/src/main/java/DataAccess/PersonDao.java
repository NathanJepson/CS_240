package DataAccess;

import Handler.ServerError;
import Model.Event;
import Model.Person;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * This class will interact with the database to SELECT from the person table, or INSERT into the person table,
 * and will transform SQL results into person objects (or use person objects to insert into the table)
 */
public class PersonDao
{
    private Connection conn;

    /**
     *
     * @param conn where conn is the connection variable passed-in by the relevant service class
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     *
     * @param person where person is the person object, containing all relevant information for
     *               a person row that we will insert into the person table (SQLite)
     */
    public void insert(Person person) throws DataAccessError {

        String sql = "INSERT INTO person (person_id, AssociatedUserName, FirstName, LastName, " +
                "Gender, father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonId());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3,person.getFirstName());
            stmt.setString(4,person.getLastName());
            stmt.setString(5,person.getGender());
            stmt.setString(6,person.getFatherID());
            stmt.setString(7,person.getMotherID());
            stmt.setString(8,person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessError("Error encountered while inserting into the database " );
        }
    }

    /**
     *
     * @param personID where personID is the unique identifier of the person row were are looking for
     * @return a Person object containing all relevant information of the person we found using the
     * ID (in other words, username, firstName, lastName, etc.).
     */
    public Person find (String personID) throws DataAccessError
    {
        Person result;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE person_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Person(rs.getString("person_id"), rs.getString("AssociatedUserName"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("father_id"),
                        rs.getString("mother_id"), rs.getString("spouse_id"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Error encountered while finding person");
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


    public Person[] findAssociatedPersons(String userName) throws ServerError //INPUT: Specific User
    {
        Vector<Person> result = new Vector<Person>();
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE AssociatedUserName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Person tmp = new Person(rs.getString("person_id"), rs.getString("AssociatedUserName"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("father_id"),
                        rs.getString("mother_id"), rs.getString("spouse_id"));
                result.add(tmp);
            }
            Object[] finalResult = result.toArray();
            Person[] finalResultForReals = new Person[finalResult.length];
            for (int i=0; i < finalResult.length; i++) {
                finalResultForReals[i] = (Person)finalResult[i];
            }
            return finalResultForReals;

        } catch (SQLException e) {
            throw new ServerError("Error: Internal server " + e.getMessage());
        }
    }

    public void deleteAssociatedPersons(String userName) throws DataAccessError //INPUT: Specific User
    {
        String sql = "DELETE FROM person WHERE AssociatedUserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessError("Error: Could not delete associated persons. " + e.getMessage());
        }
    }

    public void delete() throws DataAccessError {
        String sql = "DELETE FROM person";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessError("Could not delete person table");
        }
    }



}
