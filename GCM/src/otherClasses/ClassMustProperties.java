package otherClasses;

import java.io.Serializable;

public interface ClassMustProperties extends Serializable {
	public void saveToDatabase();

	public void deleteFromDatabase();

	public void reloadTempsFromDatabase();
}
