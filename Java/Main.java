
public class Main {
	public static void main(String [] args)
	{
		Database.createConnection();
		Customer cust = new Customer("Tal20", "11235", "a@a.com", "Tal", "Shahnov", "055");
		cust.saveToDatabase();
		Customer cust2 = Database.getCustomerById(cust.getId());
		System.out.println(cust2.getEmail());
		Database.closeConnection();
	}
}
