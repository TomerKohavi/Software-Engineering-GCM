import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
 
@RunWith(Parameterized.class)
public class CitySearchTestTable {
	
			@Parameters(name = "{index}: fib({0})={1}")
		    public static Iterable<Object[]> data() {
		    	System.out.println("parameters");
		        return Arrays.asList(new Object[][] { 
		                 { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
		           });
		    }
		    
		    @Parameter(0) 
		    public int fInput;

		    @Parameter(1)
		    public int fExpected;
          
            @BeforeClass
            public static void initDatabaseConnection() {
            	System.out.println("openDatabase");
            }
            
            @Test
        	public void testSearch() {
        		System.out.println("test");
        	}
            
            @AfterClass 
            public static void closeDatabaseConnection() {
            	System.out.println("closeDatabase");
          }
 
}