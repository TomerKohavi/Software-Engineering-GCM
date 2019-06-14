package common;

public class Console implements ChatIF {

	@Override
	public void display(Object message) {
		System.out.println(message);
	}
	
}
