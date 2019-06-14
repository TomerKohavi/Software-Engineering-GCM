package otherClasses;

import java.io.Serializable;

/**
 * Interface that describes is necessary for object classes to have
 * @author Ron Cohen
 *
 */
public interface ClassMustProperties extends Serializable {
	
	/**
	 * Function that saves the object to the database
	 */
	public void saveToDatabase();

	/**
	 * Function that delete the object from the database
	 */
	public void deleteFromDatabase();

	/**
	 * Function that reloads the object temps variables from the database
	 */
	public void reloadTempsFromDatabase();
	
	/**
	 * Function that returns the id of the object
	 * @return the object id
	 */
	public int getId();
}
