public class Employee extends User {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}