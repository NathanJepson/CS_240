package ServiceQ;

import DataAccess.DataAccessError;
import DataAccess.Database;
import Response.ClearResponse;

import java.sql.Connection;

/**
 * Basically, the ClearService class will talk back and forth with the database (through the associated DAO class)
 * to see whether the relevant database can be cleared of data.
 */
public class ClearService
{

    private Database db;
    /**
     * Constructor
     */
    public ClearService() { }

    /**
     *
     * @return This will return a ClearResponse object, signifying whether the database was able to be cleared or not
     */
    public ClearResponse clear() {
        try {
            db = new Database();
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            db = null;
            return new ClearResponse("Clear succeeded.", true);
        }
        catch (DataAccessError e) {
            try {
                db.closeConnection(false);
                db = null;
                return new ClearResponse("Error: Could not clear tables.", false);
            } catch (DataAccessError eb) {
                return new ClearResponse("Error: could not close database.",false);
            }
        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessError accessError){
                return new ClearResponse("Error: could not close database.",false);
            }
            db = null;
            return new ClearResponse("Error: Could not clear tables.", false);
            //return new ClearResponse("Could not clear tables.", false);
        }
    }
}
