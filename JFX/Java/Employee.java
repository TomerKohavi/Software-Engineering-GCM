public class Employee extends User {
    public enum Role
    {
        MANGER,
        REGULAR
    }
    private Role role;

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