
public class Main {

	  public static void main(String[] args) {
	        // Prints "Hello, World" to the terminal window.
		  PlaceOfInterest p=PlaceOfInterest._createPlaceOfInterest(5, 3, "lior_test", PlaceOfInterest.PlaceType.CINEMA, "lior:)", true);
	      System.out.println(p.getId());
	      //DataBaseAccess.savePlaceOfInterest(p);
//	      DataBaseAccess.Build();
	      //DataBaseAccess.savePlaceOfInterest(p);
	      System.out.println("GOOD");
	    }

}
}
