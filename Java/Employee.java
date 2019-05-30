import java.io.Serializable;

public class Employee extends User implements ClassMustProperties, Serializable {
    public enum Role
    {
        MANAGER(0),
        REGULAR(1);

    	private final int value;
        
        Role(final int nv)
        {
        	value = nv;
        }
        
        public int getValue()
        {
        	return value;
        }
    }
    private Role role;

    private Employee(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, Role role) {
        super(id, userName, password, email, firstName, lastName, phoneNumber);
        this.role = role;
    }

    public static Employee _createEmployee(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, Role role){ //friend to Database
        return new Employee( id,  userName,  password,  email,  firstName,  lastName,  phoneNumber,  role);
    }

    public Employee(String userName, String password, String email, String firstName, String lastName, String phoneNumber, Role role) {
        super(userName, password, email, firstName, lastName, phoneNumber);
        this.role = role;
    }

    protected Employee(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber) {
        super(id, userName, password, email, firstName, lastName, phoneNumber);
    }

    public void saveToDatabase() {
        Database.saveEmployee(this);
    }

    public void deleteFromDatabase() {
        Database.deleteEmployee(super.getId());
    }

    public void reloadTempsFromDatabase() {}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Employee && ((Employee) o).getId()==this.getId();
    }
}