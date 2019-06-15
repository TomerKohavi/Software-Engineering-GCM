package otherClasses;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Interface that describes is necessary for object classes to have
 * @author Ronen Cohen
 *
 */
public interface ClassMustProperties extends Serializable {
	
	/**
	 * Function that saves the object to the database
	 * 
	 * @throws SQLException if database access failed
	 */
	public void saveToDatabase() throws SQLException;

	/**
	 * Function that delete the object from the database
	 * 
	 * @throws SQLException if database access failed
	 */
	public void deleteFromDatabase() throws SQLException;

	/**
	 * Function that reloads the object temps variables from the database
	 * 
	 * @throws SQLException if database access failed
	 */
	public void reloadTempsFromDatabase() throws SQLException;
	
	/**
	 * Function that returns the id of the object
	 * @return the object id
	 */
	public int getId();
}
