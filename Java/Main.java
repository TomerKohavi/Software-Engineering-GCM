
public class Main {
	public static void main(String [] args)
	{
		Database.createConnection();
		Customer cust = new Customer("Tal20", "11235", "a@a.com", "Tal", "Shahnov", "055");
		cust.saveToDatabase();
		System.out.println(Database.searchCustomer("Tal20", "11235"));
		cust.deleteFromDatabase();
		System.out.println(Database.searchCustomer("Tal20", "11235"));
		Database.closeConnection();
	}
}
