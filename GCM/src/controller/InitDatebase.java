package controller;

import java.sql.Date;
import java.sql.Time;

import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.Location;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.OneTimePurchase;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Subscription;

public class InitDatebase {
	/* initialize the data base
	 * 
	 * @param name of the user permission
	 * @param pass of the user permission
	 */
	public static void initDatabase(String name, String pass)
	{
		try
		{
			Database.createConnection();
			// reset
			if (!Database.resetAll(name, pass))
				return;
			// start insert

			// create cities
			// 1
			{
				City c1 = new City("Haifa", "The third largest city in Israel. As of 2016, the city is a major seaport "
						+ "located on Israel's Mediterranean coastline in the Bay of Haifa covering 63.7 square kilometres.");
				CityDataVersion cdv = new CityDataVersion(c1, "1.0", 20, 100.9);
				PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "University of Haifa",
						PlaceOfInterest.PlaceType.MUSEUM,
						"A public research university on the top of Mount Carmel in Haifa, Israel. "
								+ "The university was founded in 1963 by the mayor of its host city, Abba Hushi,"
								+ " to operate under the academic auspices of the Hebrew University of Jerusalem.",
						false);
				p0.saveToDatabase();
				PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "School of Haifa",
						PlaceOfInterest.PlaceType.PUBLIC, "the best shool in the city", false);
				p1.saveToDatabase();
				PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Haifa museum of art",
						PlaceOfInterest.PlaceType.MUSEUM, "the biggest meseum in the city", false);
				p2.saveToDatabase();
				PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Vivino", PlaceOfInterest.PlaceType.RESTAURANT,
						"Vivino Haifa is located in a magical pine grove in the heart of the city, far from the city’s hustle and bustle. At Vivino Haifa you’ll find a piece of Italian tranquility under the sky in a beautiful courtyard, in our indoor garden or in an interior combining modern design with touches of the rich culture of the boot country.",
						false);
				p3.saveToDatabase();
				PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Hecht Park", PlaceOfInterest.PlaceType.PARK,
						"Hecht Park is the largest stretch of greenery within the urban area of the City of Haifa. Though distinct from its surroundings, it is a continuous layer among the landscape of beaches and municipal open areas stretching between Dado Beach",
						false);
				p4.saveToDatabase();
				PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
				cdv.addPlaceOfInterestSight(ps0);
				PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
				cdv.addPlaceOfInterestSight(ps1);
				PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv.getId(), p2);
				cdv.addPlaceOfInterestSight(ps2);
				PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv.getId(), p3);
				cdv.addPlaceOfInterestSight(ps3);
				PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv.getId(), p4);
				cdv.addPlaceOfInterestSight(ps4);
				Map m0 = new Map(c1.getId(), "Central city", "First map", "haifa1.png");
				double[] coords0 = { 121.3, 518.7 };
				Location l0 = new Location(m0, p0, coords0);
				m0.addLocation(l0);
				double[] coords1 = { 411.3, 150.2 };
				Location l1 = new Location(m0, p1, coords1);
				m0.addLocation(l1);
				m0.saveToDatabase();
				MapSight ms0 = new MapSight(cdv.getId(), m0);
				cdv.addMapSight(ms0);

				Map m1 = new Map(c1.getId(), "Mount Carmel", "Second map", "haifa2.png");
				double[] coords2 = { 412.3, 285.7 };
				Location l2 = new Location(m1, p2, coords2);
				m1.addLocation(l2);
				double[] coords3 = { 511.3, 449.2 };
				Location l3 = new Location(m1, p3, coords3);
				m1.addLocation(l3);
				double[] coords4 = { 12.3, 519.2 };
				Location l4 = new Location(m1, p4, coords4);
				m1.addLocation(l4);
				m1.saveToDatabase();
				MapSight ms1 = new MapSight(cdv.getId(), m1);
				cdv.addMapSight(ms1);
				Route r = new Route(c1.getId(), "Small route");
				RouteStop rstop1 = new RouteStop(r, p0, new Time(1, 25, 0));
				r.addRouteStop(rstop1);
				RouteStop rstop2 = new RouteStop(r, p1, new Time(0, 50, 0));
				r.addRouteStop(rstop2);
				r.saveToDatabase();
				RouteSight rs = new RouteSight(cdv.getId(), r, true);
				cdv.addRouteSight(rs);

				Route r1 = new Route(c1.getId(), "Big route");
				RouteStop rstop3 = new RouteStop(r1, p2, new Time(4, 0, 0));
				r1.addRouteStop(rstop3);
				RouteStop rstop4 = new RouteStop(r1, p3, new Time(0, 20, 0));
				r1.addRouteStop(rstop4);
				RouteStop rstop5 = new RouteStop(r1, p4, new Time(3, 30, 0));
				r1.addRouteStop(rstop5);
				r1.saveToDatabase();
				RouteSight rs1 = new RouteSight(cdv.getId(), r1, true);
				cdv.addRouteSight(rs1);

				c1.addPublishedCityDataVersion(cdv);
				c1.addUnpublishedCityDataVersion(new CityDataVersion(cdv,"2.0"));
				c1.saveToDatabase();
			}
			// 2
			{
				City c1 = new City("Tel aviv",
						"Tel Aviv-Yafo, is the second most populous city in Israel—after Jerusalem—and the most populous city in the conurbation of Gush Dan, Israel's largest metropolitan area. Located on the country's Mediterranean coastline and with a population of 443,939, it is the economic and technological center of the country.");
				CityDataVersion cdv = new CityDataVersion(c1, "1.0", 15, 100.9);
				PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "tel aviv carmel market",
						PlaceOfInterest.PlaceType.PUBLIC,
						"The Carmel Market (the Shuk Hacarmel) is the largest market, or shuk, in Tel Aviv. A vibrant marketplace where traders sell everything from clothing to spices, and fruit to electronics, visiting the Carmel Market is a fascinating thing to do in Tel Aviv.",
						false);
				p0.saveToDatabase();
				PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "Tel aviv port", PlaceOfInterest.PlaceType.PUBLIC,
						"Namal Tel Aviv, the Tel Aviv Port has recently been restored and is now one of the hottest places in town. During the day, the cafes and stores at Namal Tel Aviv (the Tel Aviv Port) the host some of the city’s richest and trendiest",
						false);
				p1.saveToDatabase();
				PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Tel Aviv Museum of Art",
						PlaceOfInterest.PlaceType.MUSEUM,
						"Tel Aviv Museum of Art is a municipal museum, one of Israel's leading artistic and cultural institutions. The museum comprises various departments",
						false);
				p2.saveToDatabase();
				PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Tel aviv university",
						PlaceOfInterest.PlaceType.PUBLIC,
						"Tel Aviv University came into being through the dedicated efforts of visionaries who foresaw the need for an additional university in Israel’s rapidly growing central region. In the 1930s, the idea was promoted by then mayor of Tel Aviv, Meir Dizengoff",
						false);
				p3.saveToDatabase();
				PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Neve Tzedek", PlaceOfInterest.PlaceType.PUBLIC,
						"The charming neighborhood of Neve Tzedek is one of the oldest in the city, filled with quaint buildings showcasing both the old Bauhaus buildings in their original form mixed with newer structures and homes.",
						false);
				p4.saveToDatabase();
				PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
				cdv.addPlaceOfInterestSight(ps0);
				PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
				cdv.addPlaceOfInterestSight(ps1);
				PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv.getId(), p2);
				cdv.addPlaceOfInterestSight(ps2);
				PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv.getId(), p3);
				cdv.addPlaceOfInterestSight(ps3);
				PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv.getId(), p4);
				cdv.addPlaceOfInterestSight(ps4);
				Map m0 = new Map(c1.getId(), "North city", "First map", "tel_aviv1.png");
				double[] coords0 = { 123.3, 354.7 };
				Location l0 = new Location(m0, p0, coords0);
				m0.addLocation(l0);
				double[] coords1 = { 17.3, 452.2 };
				Location l1 = new Location(m0, p1, coords1);
				m0.addLocation(l1);
				m0.saveToDatabase();
				MapSight ms0 = new MapSight(cdv.getId(), m0);
				cdv.addMapSight(ms0);

				Map m1 = new Map(c1.getId(), "Downtown", "Second map", "tel_aviv2.png");
				double[] coords2 = { 117.3, 435.7 };
				Location l2 = new Location(m1, p2, coords2);
				m1.addLocation(l2);
				double[] coords3 = { 212.3, 336.2 };
				Location l3 = new Location(m1, p3, coords3);
				m1.addLocation(l3);
				double[] coords4 = { 477.3, 313.2 };
				Location l4 = new Location(m1, p4, coords4);
				m1.addLocation(l4);
				m1.saveToDatabase();
				MapSight ms1 = new MapSight(cdv.getId(), m1);
				cdv.addMapSight(ms1);
				Route r = new Route(c1.getId(), "Small route");
				RouteStop rstop1 = new RouteStop(r, p0, new Time(4, 30, 0));
				r.addRouteStop(rstop1);
				RouteStop rstop2 = new RouteStop(r, p1, new Time(2, 0, 0));
				r.addRouteStop(rstop2);
				r.saveToDatabase();
				RouteSight rs = new RouteSight(cdv.getId(), r, true);
				cdv.addRouteSight(rs);

				Route r1 = new Route(c1.getId(), "Big route");
				RouteStop rstop3 = new RouteStop(r1, p2, new Time(0, 40, 0));
				r1.addRouteStop(rstop3);
				RouteStop rstop4 = new RouteStop(r1, p3, new Time(2, 20, 0));
				r1.addRouteStop(rstop4);
				RouteStop rstop5 = new RouteStop(r1, p4, new Time(4, 20, 0));
				r1.addRouteStop(rstop5);
				r1.saveToDatabase();
				RouteSight rs1 = new RouteSight(cdv.getId(), r1, true);
				cdv.addRouteSight(rs1);

				c1.addPublishedCityDataVersion(cdv);
				c1.addUnpublishedCityDataVersion(new CityDataVersion(cdv,"2.0"));
				c1.saveToDatabase();
			}
			// 3

			City c1 = new City("Jerusalem",
					"Jerusalem is a city in the Middle East, located on a plateau in the Judaean Mountains between the Mediterranean and the Dead Sea. It is one of the oldest cities in the world, and is considered holy to the three major Abrahamic religions—Judaism, Christianity, and Islam.");
			CityDataVersion cdv = new CityDataVersion(c1, "1.0", 10, 122.9);
			PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "Western Wall", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Western Wall, or “Wailing Wall”, is the most religious site in the world for the Jewish people. Located in the Old City of Jerusalem, it is the western support wall of the Temple Mount.",
					false);
			p0.saveToDatabase();
			PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "Machane Yehuda Market",
					PlaceOfInterest.PlaceType.PUBLIC,
					"The Machane Yehuda Market, or shuk, is the largest market in Jerusalem with over 250 vendors selling everything from fruit and vegetables to specialty foods, and clothing to Judaica. The market is the main ‘traditional’ marketplace of Jerusalem contrasting with the supermarkets that are found across this city, just as any other advanced city",
					false);
			p1.saveToDatabase();
			PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Tower od David", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Tower of David also known as the Jerusalem Citadel, is an ancient citadel located near the Jaffa Gate entrance to western edge of the Old City of Jerusalem.",
					false);
			p2.saveToDatabase();
			PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Jewish Quarter", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Jewish Quarter of Jerusalem’s Old City is one of the four quarters of the walled city. The quarter is home to around 2,000 people and covers about 0.1 square kilometers. It is also the location of many tens of synagogues and yeshivas (places of the study of Jewish texts) and has been almost continually home to Jews since the century 8 BCE.",
					false);
			p3.saveToDatabase();
			PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Old City of Jerusalem",
					PlaceOfInterest.PlaceType.PUBLIC,
					"The Old City of Jerusalem is one of the most intense places on Earth! At the heart of the Jewish, Islamic, and Christian religions, this walled one-kilometer area in the center of Jerusalem is beyond words and cannot be missed.",
					false);
			p4.saveToDatabase();
			PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
			cdv.addPlaceOfInterestSight(ps0);
			PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
			cdv.addPlaceOfInterestSight(ps1);
			PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv.getId(), p2);
			cdv.addPlaceOfInterestSight(ps2);
			PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv.getId(), p3);
			cdv.addPlaceOfInterestSight(ps3);
			PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv.getId(), p4);
			cdv.addPlaceOfInterestSight(ps4);
			Map m0 = new Map(c1.getId(), "West city", "First map", "jerusalem1.png");
			double[] coords0 = { 233.3, 445.7 };
			Location l0 = new Location(m0, p0, coords0);
			m0.addLocation(l0);
			double[] coords1 = { 117.3, 328.2 };
			Location l1 = new Location(m0, p1, coords1);
			m0.addLocation(l1);
			m0.saveToDatabase();
			MapSight ms0 = new MapSight(cdv.getId(), m0);
			cdv.addMapSight(ms0);

			Map m1 = new Map(c1.getId(), "East city", "Second map", "jerusalem2.png");
			double[] coords2 = { 123.3, 529.7 };
			Location l2 = new Location(m1, p2, coords2);
			m1.addLocation(l2);
			double[] coords3 = { 412.3, 216.2 };
			Location l3 = new Location(m1, p3, coords3);
			m1.addLocation(l3);
			double[] coords4 = { 372.3, 213.2 };
			Location l4 = new Location(m1, p4, coords4);
			m1.addLocation(l4);
			m1.saveToDatabase();
			MapSight ms1 = new MapSight(cdv.getId(), m1);
			cdv.addMapSight(ms1);
			Route r = new Route(c1.getId(), "Small route");
			RouteStop rstop1 = new RouteStop(r, p0, new Time(1, 0, 0));
			r.addRouteStop(rstop1);
			RouteStop rstop2 = new RouteStop(r, p1, new Time(0, 40, 0));
			r.addRouteStop(rstop2);
			r.saveToDatabase();
			RouteSight rs = new RouteSight(cdv.getId(), r, true);
			cdv.addRouteSight(rs);

			Route r1 = new Route(c1.getId(), "Big route");
			RouteStop rstop3 = new RouteStop(r1, p2, new Time(4, 0, 0));
			r1.addRouteStop(rstop3);
			RouteStop rstop4 = new RouteStop(r1, p3, new Time(2, 30, 0));
			r1.addRouteStop(rstop4);
			RouteStop rstop5 = new RouteStop(r1, p4, new Time(0, 50, 0));
			r1.addRouteStop(rstop5);
			r1.saveToDatabase();
			RouteSight rs1 = new RouteSight(cdv.getId(), r1, true);
			cdv.addRouteSight(rs1);

			c1.addPublishedCityDataVersion(cdv);
			c1.addUnpublishedCityDataVersion(new CityDataVersion(cdv,"2.0"));
			c1.saveToDatabase();

			// create Users
			// 1
			{
				Employee e = new Employee("Lior", "Lior_strong1!", "lior@gmail.com", "lior", "wiessman", "0523322726",
						Employee.Role.CEO);
				e.saveToDatabase();
				Customer cust = new Customer("yosi11", "LDCyosiiii!", "yosi@gmail.com", "yosi", "ben asser",
						"0523322123", "5495681338665894", "07/24", "896");
				Subscription sub0 = new Subscription(cust, 1, new Date(119, 5, 8), 201.8, 199.9, new Date(119, 11, 8));
				cust.addSubscription(sub0);
				Subscription sub1 = new Subscription(cust, 2, new Date(119, 8, 7), 171.8, 112.9, new Date(119, 12, 7));
				cust.addSubscription(sub1);

				OneTimePurchase otp = new OneTimePurchase(cust, 1 , new Date(119, 8, 6), 20, 19);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}
			// 2
			{
				Employee e = new Employee("adiel", "adiel1", "statman.adiel@gmail.com", "adiel", "statman",
						"0525952726", Employee.Role.REGULAR);
				e.saveToDatabase();
				Customer cust = new Customer("dan", "masterDan%", "dannyf.post@gmail.com", "dan", "feldman",
						"0523325686", "5495655558665894", "01/23", "354");
				Subscription sub0 = new Subscription(cust, 1, new Date(119, 8, 8), 211.8, 189.9, new Date(119, 11, 8));
				cust.addSubscription(sub0);
				Subscription sub1 = new Subscription(cust, 3, new Date(119, 3, 3), 131.8, 111.9, new Date(119, 8, 3));
				cust.addSubscription(sub1);

				OneTimePurchase otp = new OneTimePurchase(cust, 2, new Date(119, 8, 6), 19, 18);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}

			// 3
			{
				Employee e = new Employee("ben", "benbon&ALAA", "bengordoncshaifa@gmail.com", "ben", "musa",
						"0508322726", Employee.Role.MANAGER);
				e.saveToDatabase();
				Customer cust = new Customer("gadi", "gadiHAVIV!@", "gadi@gmail.com", "gadi", "landau", "0524867726",
						"5495123458665894", "01/25", "891");
				Subscription sub = new Subscription(cust, 1, new Date(118, 7, 8), 53.2, 50.9, new Date(118, 9, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, 2, new Date(119, 8, 6), 9, 8);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}
			// 4
			{
				Employee e = new Employee("sigal", "sigalIsNoob!", "yonatan.sigal11@gmail.com", "yonatan", "sigal",
						"0508322126", Employee.Role.REGULAR);
				e.saveToDatabase();
				Customer cust = new Customer("tomer", "IAmTomer*", "1234tomer@gmail.com", "tomer", "kohavi",
						"0524867726", "5495123458612894", "02/25", "821");
				Subscription sub = new Subscription(cust, 3, new Date(119, 7, 6), 63.2, 50.9, new Date(119, 9, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, 1, new Date(119, 7, 6), 19, 8);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}
			// 5
			{
				Employee e = new Employee("a", "a", "a@gmail.com", "aa", "aa", "0500000000",
						Employee.Role.CEO);
				e.saveToDatabase();
				Customer cust = new Customer("q", "q", "q@gmail.com", "q", "q q",
						"0523022100", "5495681338665894", "07/24", "000");
				Subscription sub0 = new Subscription(cust, 3, new Date(112, 2, 25), 201.8, 199.9, new Date(119, 5, 25));
				cust.addSubscription(sub0);
				Subscription sub1 = new Subscription(cust, 3, new Date(115, 2, 28), 171.8, 112.9, new Date(119, 12, 15));
				cust.addSubscription(sub1);

				OneTimePurchase otp = new OneTimePurchase(cust, 1 , new Date(119, 12, 13), 15, 9);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// Database.closeConnection();
		}
	}

}
