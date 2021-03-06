package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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

/**
 * Class that in charge of initialize the data base
 * 
 *
 * @author Ron Cohen
 * @author Lior Weissman
 * @author Tal Shachnovskieee
 *
 */

public class InitDatebase {
	
	/**
	 * initialize the data base
	 * 
	 * @param name of the user permission
	 * 
	 * @param pass of the user permission
	 */
	public static void initDatabase(String name, String pass) {
		try {
			System.out.println("Starting init database:");
			Database.createConnection();
			// reset
			if (!Database.resetAll(name, pass))
				return;

			// create cities, POIs, Routes
			System.out.println("Creating cities...");
			haifa();
			telAviv();
			jerusalem();
			rome();

			// create Users, subscriptions, purchases, downloads
			System.out.println("Creating users...");
			liorAndYosi();
			danAndAdiel();
			benAndGadi();
			sigalAndTomer();
			aAndB();

			// create statistics
			System.out.println("Creating statistics...");
			initStatistics();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Database.closeConnection();
		}
		System.out.println("Finish init database.");
	}

	/**
	 * Initializing Haifa city in database
	 * @throws SQLException if the access to database failed
	 */
	private static void haifa() throws SQLException {
		City c1 = new City("Haifa", "The third largest city in Israel. As of 2016, the city is a major seaport "
				+ "located on Israel's Mediterranean coastline in the Bay of Haifa covering 63.7 square kilometres.", 20, 103);
		PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "University of Haifa", PlaceOfInterest.PlaceType.PUBLIC,
				"A public research university on the top of Mount Carmel in Haifa, Israel. "
						+ "The university was founded in 1963 by the mayor of its host city, Abba Hushi,"
						+ " to operate under the academic auspices of the Hebrew University of Jerusalem.",
				false);
		p0.saveToDatabase();
		PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "School of Haifa", PlaceOfInterest.PlaceType.PUBLIC,
				"the best shool in the city", false);
		p1.saveToDatabase();
		PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Haifa museum of art", PlaceOfInterest.PlaceType.MUSEUM,
				"the biggest meseum in the city", false);
		p2.saveToDatabase();
		PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Vivino", PlaceOfInterest.PlaceType.RESTAURANT,
				"Vivino Haifa is located in a magical pine grove in the heart of the city, far from the city�s hustle and bustle. At Vivino Haifa you�ll find a piece of Italian tranquility under the sky in a beautiful courtyard, in our indoor garden or in an interior combining modern design with touches of the rich culture of the boot country.",
				false);
		p3.saveToDatabase();
		PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Hecht Park", PlaceOfInterest.PlaceType.PARK,
				"Hecht Park is the largest stretch of greenery within the urban area of the City of Haifa. Though distinct from its surroundings, it is a continuous layer among the landscape of beaches and municipal open areas stretching between Dado Beach",
				false);

		// cdv
		CityDataVersion cdv = new CityDataVersion(c1, "1.2");

		p4.saveToDatabase();
		PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
		ps1.saveToDatabase();
		PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv.getId(), p2);
		ps2.saveToDatabase();
		PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv.getId(), p3);
		ps3.saveToDatabase();
		PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv.getId(), p4);
		ps4.saveToDatabase();

		Map m0 = new Map(c1.getId(), "Central city", "General maps that shows most of the city.", "haifa1.png");
		double[] coords0 = { 121.3, 518.7 };
		Location l0 = new Location(m0, p0, coords0);
		m0.addLocation(l0);
		double[] coords1 = { 411.3, 150.2 };
		Location l1 = new Location(m0, p1, coords1);
		m0.addLocation(l1);
		double[] coords2 = { 43, 350 };
		Location l2 = new Location(m0, p3, coords2);
		m0.addLocation(l2);
		m0.saveToDatabase();
		MapSight ms0 = new MapSight(cdv.getId(), m0);
		ms0.saveToDatabase();

		Map m1 = new Map(c1.getId(), "Mount Carmel", "A map tha focus of Mount Carmer. Scale of 1-50 in real life.", "haifa2.png");
		double[] coords5 = { 412.3, 285.7 };
		Location l5 = new Location(m1, p2, coords5);
		m1.addLocation(l5);
		double[] coords3 = { 511.3, 449.2 };
		Location l3 = new Location(m1, p3, coords3);
		m1.addLocation(l3);
		double[] coords4 = { 12.3, 519.2 };
		Location l4 = new Location(m1, p4, coords4);
		m1.addLocation(l4);
		m1.saveToDatabase();
		MapSight ms1 = new MapSight(cdv.getId(), m1);
		ms1.saveToDatabase();

		Route r = new Route(c1.getId(), "Education route", "A route that passes through the best academic institutions in Haifa.",true);
		RouteStop rstop1 = new RouteStop(r, p0, LocalTime.of(1, 25, 0));
		r.addRouteStop(rstop1);
		RouteStop rstop2 = new RouteStop(r, p1, LocalTime.of(0, 50, 0));
		r.addRouteStop(rstop2);
		r.saveToDatabase();
		RouteSight rs = new RouteSight(cdv.getId(), r);
		rs.saveToDatabase();

		Route r1 = new Route(c1.getId(), "Nice family trip","A route that designed for a vatction day for all ages.",false);
		RouteStop rstop3 = new RouteStop(r1, p2, LocalTime.of(4, 0, 0));
		r1.addRouteStop(rstop3);
		RouteStop rstop4 = new RouteStop(r1, p3, LocalTime.of(0, 20, 0));
		r1.addRouteStop(rstop4);
		RouteStop rstop5 = new RouteStop(r1, p4, LocalTime.of(3, 30, 0));
		r1.addRouteStop(rstop5);
		r1.saveToDatabase();
		RouteSight rs1 = new RouteSight(cdv.getId(), r1);
		rs1.saveToDatabase();

		cdv.reloadTempsFromDatabase();
		c1._addPublishedCityDataVersion(cdv);
		//c1.addUnpublishedCityDataVersion(new CityDataVersion(cdv, "3.1"));
		c1.saveToDatabase();
	}
	
	/**
	 * Initializing Tel Aviv city in database
	 * @throws SQLException if the access to database failed
	 */
	private static void telAviv() throws SQLException {

		City c1 = new City("Tel aviv",
				"Tel Aviv-Yafo, is the second most populous city in Israel�after Jerusalem�and the most populous city in the conurbation of Gush Dan, Israel's largest metropolitan area. Located on the country's Mediterranean "
				+ "coastline and with a population of 443,939, it is the economic and technological center of the country.",15, 100);
		PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "tel aviv carmel market", PlaceOfInterest.PlaceType.PUBLIC,
				"The Carmel Market (the Shuk Hacarmel) is the largest market, or shuk, in Tel Aviv. A vibrant marketplace where traders sell everything from clothing to spices, and fruit to electronics, visiting the Carmel Market is a fascinating thing to do in Tel Aviv.",
				false);
		p0.saveToDatabase();
		PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "Tel aviv port", PlaceOfInterest.PlaceType.PUBLIC,
				"Namal Tel Aviv, the Tel Aviv Port has recently been restored and is now one of the hottest places in town. During the day, the cafes and stores at Namal Tel Aviv (the Tel Aviv Port) the host some of the city�s richest and trendiest",
				false);
		p1.saveToDatabase();
		PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Tel Aviv Museum of Art", PlaceOfInterest.PlaceType.MUSEUM,
				"Tel Aviv Museum of Art is a municipal museum, one of Israel's leading artistic and cultural institutions. The museum comprises various departments",
				false);
		p2.saveToDatabase();
		PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Tel aviv university", PlaceOfInterest.PlaceType.PUBLIC,
				"Tel Aviv University came into being through the dedicated efforts of visionaries who foresaw the need for an additional university in Israel�s rapidly growing central region. In the 1930s, the idea was promoted by then mayor of Tel Aviv, Meir Dizengoff",
				false);
		p3.saveToDatabase();
		PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Neve Tzedek", PlaceOfInterest.PlaceType.PUBLIC,
				"The charming neighborhood of Neve Tzedek is one of the oldest in the city, filled with quaint buildings showcasing both the old Bauhaus buildings in their original form mixed with newer structures and homes.",
				false);

		// cdv
		CityDataVersion cdv = new CityDataVersion(c1, "2.1");
		p4.saveToDatabase();
		PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
		ps1.saveToDatabase();
		PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv.getId(), p2);
		ps2.saveToDatabase();
		PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv.getId(), p3);
		ps3.saveToDatabase();
		PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv.getId(), p4);
		ps4.saveToDatabase();
		Map m0 = new Map(c1.getId(), "North city", "Map that focus on the north part of the city, roads and streats.", "tel_aviv1.png");
		double[] coords0 = { 123.3, 354.7 };
		Location l0 = new Location(m0, p0, coords0);
		m0.addLocation(l0);
		double[] coords1 = { 17.3, 452.2 };
		Location l1 = new Location(m0, p1, coords1);
		m0.addLocation(l1);
		m0.saveToDatabase();
		MapSight ms0 = new MapSight(cdv.getId(), m0);
		ms0.saveToDatabase();

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
		ms1.saveToDatabase();
		Route r = new Route(c1.getId(), "Shopping and resturant tour","A tour that pass throught the best shopping attractions in town.",false);
		RouteStop rstop1 = new RouteStop(r, p0, LocalTime.of(4, 30, 0));
		r.addRouteStop(rstop1);
		RouteStop rstop2 = new RouteStop(r, p1, LocalTime.of(2, 0, 0));
		r.addRouteStop(rstop2);
		r.saveToDatabase();
		RouteSight rs = new RouteSight(cdv.getId(), r);
		rs.saveToDatabase();

		Route r1 = new Route(c1.getId(), "Random route","A route with a wide variety of attractions types.",true);
		RouteStop rstop3 = new RouteStop(r1, p2, LocalTime.of(0, 40, 0));
		r1.addRouteStop(rstop3);
		RouteStop rstop4 = new RouteStop(r1, p3, LocalTime.of(2, 20, 0));
		r1.addRouteStop(rstop4);
		RouteStop rstop5 = new RouteStop(r1, p4, LocalTime.of(4, 20, 0));
		r1.addRouteStop(rstop5);
		r1.saveToDatabase();
		RouteSight rs1 = new RouteSight(cdv.getId(), r1);
		rs1.saveToDatabase();

		cdv.reloadTempsFromDatabase();
		c1._addPublishedCityDataVersion(cdv);
		//c1.addUnpublishedCityDataVersion(new CityDataVersion(cdv, "6.888"));
		c1.saveToDatabase();

	}
	
	/**
	 * Initializing Jerusalem city in database
	 * @throws SQLException if the access to database failed
	 */
	private static void jerusalem() throws SQLException {
		City city = new City("Jerusalem", 
				"Jerusalem is a city in the Middle East, located on a plateau in the Judaean Mountains between the Mediterranean and the Dead Sea. "
						+ "It is one of the oldest cities in the world, and is considered holy to the three major Abrahamic religions�Judaism, Christianity, and Islam.",40, 182);
		PlaceOfInterest p0 = new PlaceOfInterest(city.getId(), "Western Wall", PlaceOfInterest.PlaceType.HISTORICAL,
				"The Western Wall, or �Wailing Wall�, is the most religious site in the world for the Jewish people. Located in the Old City of Jerusalem, it is the western support wall of the Temple Mount.",
				false);
		p0.saveToDatabase();
		PlaceOfInterest p1 = new PlaceOfInterest(city.getId(), "Machane Yehuda Market", PlaceOfInterest.PlaceType.STORE,
				"The Machane Yehuda Market, or shuk, is the largest market in Jerusalem with over 250 vendors selling everything from fruit and vegetables to specialty foods, and clothing to Judaica. The market is the main �traditional� marketplace of Jerusalem contrasting with the supermarkets that are found across this city, just as any other advanced city",
				false);
		p1.saveToDatabase();
		PlaceOfInterest p2 = new PlaceOfInterest(city.getId(), "Tower od David", PlaceOfInterest.PlaceType.HISTORICAL,
				"The Tower of David also known as the Jerusalem Citadel, is an ancient citadel located near the Jaffa Gate entrance to western edge of the Old City of Jerusalem.",
				false);
		p2.saveToDatabase();
		PlaceOfInterest p3 = new PlaceOfInterest(city.getId(), "Jewish Quarter", PlaceOfInterest.PlaceType.HISTORICAL,
				"The Jewish Quarter of Jerusalem�s Old City is one of the four quarters of the walled city. The quarter is home to around 2,000 people and covers about 0.1 square kilometers. It is also the location of many tens of synagogues and yeshivas (places of the study of Jewish texts) and has been almost continually home to Jews since the century 8 BCE.",
				false);
		p3.saveToDatabase();
		PlaceOfInterest p4 = new PlaceOfInterest(city.getId(), "Old City of Jerusalem", PlaceOfInterest.PlaceType.PUBLIC,
				"The Old City of Jerusalem is one of the most intense places on Earth! At the heart of the Jewish, Islamic, and Christian religions, this walled one-kilometer area in the center of Jerusalem is beyond words and cannot be missed.",
				false);
		PlaceOfInterest p5 = new PlaceOfInterest(city.getId(), "Israel Museum", PlaceOfInterest.PlaceType.MUSEUM,
				"The Israel Museum was established in 1965 as Israel's foremost cultural institution and one of the world�s leading encyclopaedic museums. It is situated on a hill in the Givat Ram neighbourhood of Jerusalem, adjacent to the Bible Lands Museum, the Knesset, the Israeli Supreme Court, and the Hebrew University of Jerusalem..",
				true);
		PlaceOfInterest p6 = new PlaceOfInterest(city.getId(), "iTravelJerusalem", PlaceOfInterest.PlaceType.MUSEUM,
				"180 years ago, there were no supermarkets in Jerusalem, no baking ovens in the houses. Anyone who wanted to eat a slice of bread would turn himself a whole day\r\n" + 
				"Comes with a sack of wheat to the windmill in Mishkenot Sha'ananim and together with other Jerusalemites they would slowly turn the millstone together\r\n" + 
				"Then you sift the flour and bake together in the communal tabun (which still exists at the entrance gates to Mishkenot Sha'ananim), and then sit together and eat and enjoy fresh bread and the satisfaction of having a significant, slow and common effort.",
				true);
		// cdv
		CityDataVersion cdv = new CityDataVersion(city, "3.3");
		PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
		ps1.saveToDatabase();
		PlaceOfInterestSight ps5 = new PlaceOfInterestSight(cdv.getId(), p5);
		ps5.saveToDatabase();

		Map m0 = new Map(city.getId(), "All Jerusalem", "general map that shows almost all the city.", "general map jerusalem.png");
		double[] coords0 = { 400, 200 };
		Location l0 = new Location(m0, p0, coords0);
		m0.addLocation(l0);
		double[] coords1 = { 200, 200 };
		Location l1 = new Location(m0, p1, coords1);
		m0.addLocation(l1);
		double[] coords5 = { 100, 100 };
		Location l5 = new Location(m0, p5, coords5);
		m0.addLocation(l5);
		m0.saveToDatabase();

		MapSight ms0 = new MapSight(cdv.getId(), m0);
		ms0.saveToDatabase();

		Route r1 = new Route(city.getId(), "Historical attractions","A route that pass throught the best historical attractions in town.",true);
		RouteStop rstop1 = new RouteStop(r1, p0, LocalTime.of(0, 40, 0));
		r1.addRouteStop(rstop1);
		RouteStop rstop2 = new RouteStop(r1, p2, LocalTime.of(1, 30, 0));
		r1.addRouteStop(rstop2);
		r1.saveToDatabase();

		RouteSight rs1 = new RouteSight(cdv.getId(), r1);
		rs1.saveToDatabase();

		
		cdv.reloadTempsFromDatabase();
		city._addPublishedCityDataVersion(cdv);
		// cdv2
		CityDataVersion cdv2 = city.getCopyUnpublishedVersions().get(0);
		PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv2.getId(), p2);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv2.getId(), p3);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv2.getId(), p4);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps6 = new PlaceOfInterestSight(cdv2.getId(), p6);
		ps0.saveToDatabase();

		Map m1 = null;
		ArrayList<Integer> idsM1 = Database.searchMap(m0.getCityId(), m0.getName(), m0.getInfo(), m0.getImgURL());
		for (int id : idsM1) {
			if (id != m0.getId()) {
				m1 = Database.getMapById(id);
			}
		}
		if (m1 != null) {
			double[] coords2 = { 100, 350 };
			Location l2 = new Location(m1, p2, coords2);
			m1.addLocation(l2);
			double[] coords3 = { 70, 420 };
			Location l3 = new Location(m1, p3, coords3);
			m1.addLocation(l3);
			m1.saveToDatabase();
		} else
			System.out.println("Something weird is happening :( check rome() in InitDatabase");

		Map m2 = new Map(city.getId(), "Jerusalem the Old City", "A map that describes the old city in it's glory.",
				"jerusalem-old-city-map.jpg");
		double[] coords20 = { 100, 300 };
		Location l20 = new Location(m2, p0, coords20);
		m2.addLocation(l20);
		double[] coords25 = { 350, 180 };
		Location l25 = new Location(m2, p5, coords25);
		m2.addLocation(l25);
		m0.saveToDatabase();
		double[] coords24 = { 270, 420 };
		Location l24 = new Location(m2, p4, coords24);
		m2.addLocation(l24);
		m0.saveToDatabase();
		double[] coords26 = { 200, 100 };
		Location l26 = new Location(m2, p6, coords26);
		m2.addLocation(l26);
		m0.saveToDatabase();

		MapSight ms2 = new MapSight(cdv2.getId(), m2);
		ms2.saveToDatabase();

		Route r2 = null;
		ArrayList<Integer> idsR2 = Database.searchRoute(r1.getCityId(), r1.getInfo());
		for (int id : idsR2) {
			if (id != r1.getId()) {
				r2 = Database.getRouteById(id);
			}
		}
		if (r2 != null) {
			RouteStop rstop3 = new RouteStop(r2, p3, LocalTime.of(2, 0, 0));
			r2.addRouteStop(rstop3);
			r2.saveToDatabase();
		} else
			System.out.println("Something weird is happening :( check rome() in InitDatabase");

		Route r3 = new Route(city.getId(), "Museums are amazing!!","Best museums in the city can be found here.",false);
		RouteStop rstop25 = new RouteStop(r3, p5, LocalTime.of(2, 27, 0));
		r3.addRouteStop(rstop25);
		RouteStop rstop24 = new RouteStop(r3, p6, LocalTime.of(0, 53, 0));
		r3.addRouteStop(rstop24);
		r3.saveToDatabase();

		RouteSight rs2 = new RouteSight(cdv2.getId(), r3);
		rs2.saveToDatabase();
		
		cdv2.saveToDatabase();
		city.saveToDatabase();
	}

	/**
	 * Initializing Rome city in database
	 * @throws SQLException if the access to database failed
	 */
	private static void rome() throws SQLException {
		City city = new City("Rome",
				"Rome (Latin and Italian: Roma (About this soundlisten)) is the capital city and a special comune of Italy "
						+ "(named Comune di Roma Capitale). Rome also serves as the capital of the Lazio region. With 2,872,800 residents in 1,285 km "
						+ " it is also the country's most populated comune. "
						+ "It is the fourth most populous city in the European Union by population within city limits. ",40, 135);
		PlaceOfInterest p0 = new PlaceOfInterest(city.getId(), "Vatican", PlaceOfInterest.PlaceType.HISTORICAL,
				"Vatican City, " + "officially Vatican City State "
						+ "is an independent city-state enclaved within Rome, Italy. Established with the Lateran Treaty (1929), "
						+ "it is distinct from yet under \"full ownership, exclusive dominion, and sovereign authority and jurisdiction\" of the Holy See (Latin: Sancta Sedes)."
						+ " With an area of 44 hectares (110 acres), "
						+ "and a population of about 1,000, it is the smallest sovereign state in the world by both area and population.",
				true);
		p0.saveToDatabase();
		PlaceOfInterest p1 = new PlaceOfInterest(city.getId(), "Colosseum", PlaceOfInterest.PlaceType.HISTORICAL,
				"The Colosseum or Coliseum, also known as the Flavian Amphitheatre (Latin: "
						+ "Amphitheatrum Flavium; Italian: Anfiteatro Flavio,"
						+ " is an oval amphitheatre in the centre of the city of Rome, Italy.",
				false);
		p1.saveToDatabase();
		PlaceOfInterest p2 = new PlaceOfInterest(city.getId(), "Pantheon", PlaceOfInterest.PlaceType.HISTORICAL,
				"The Pantheon of Agrippa, also known as the Roman Pantheon, is one of the architectural masterpieces of the Italian capital. It is the best preserved building from ancient Rome.",
				true);
		p2.saveToDatabase();
		PlaceOfInterest p3 = new PlaceOfInterest(city.getId(), "EMPRESA", PlaceOfInterest.PlaceType.STORE,
				"Some of the sexiest tailored but cool menswear in the city. If you�re in the market for a jacket, trench, high collared structured cardigan or hand crafted leather shoes with an edge this is a place where even boys who hate shopping will get a little bit excited. It�s the perfect option for those who want beautiful Italian clothes but nothing too formal and nothing too mass-market-and-available-back-home.",
				false);
		p3.saveToDatabase();
		PlaceOfInterest p4 = new PlaceOfInterest(city.getId(), "Multisala Lux", PlaceOfInterest.PlaceType.CINEMA,
				"Multiscreen cinema. Several rooms. Blockbuster movies. Sometimes in the original language Molte sale. Film di successo. A volte in lingua originale",
				true);
		p4.saveToDatabase();
		PlaceOfInterest p5 = new PlaceOfInterest(city.getId(), "PIERLUIGI", PlaceOfInterest.PlaceType.RESTAURANT,
				"If you were visiting a well-heeled, globetrotting uncle with a pied a terre somewhere near Piazza Navona (you are?), he�d probably take you for lunch at Pierluigi. The ultimate �posh� Roman trattoria, it was chosen by Mark Zuckerberg and Priscilla Chan for a honeymoon dinner in May 2012, and Barack Obama, Hillary Clinton and John Kerry have all eaten here while in Rome on official or informal visits � it�s that kind of place. Occupying one side of pretty, cobbled piazza, it�s a delightful venue for an al-fresco lunch, and could very well rest on its comfortable laurels.",
				true);
		p5.saveToDatabase();
		PlaceOfInterest p6 = new PlaceOfInterest(city.getId(), "Villa Spalletti Trivelli ",
				PlaceOfInterest.PlaceType.HOTEL,
				"A stately mansion set in the middle of Rome, this is an oasis of old-fashioned charm and calm. It�s a picture of classic elegance with antique furniture, polished wood and traditionally furnished rooms. Outside, the manicured gardens are a lovely place to relax.",
				true);
		p6.saveToDatabase();
		PlaceOfInterest p7 = new PlaceOfInterest(city.getId(), "Stadio Olimpico ",
				PlaceOfInterest.PlaceType.PUBLIC,
				"The Stadio Olimpico is the main and largest sports facility of Rome, Italy."
				+ " It is located within the Foro Italico sports complex, north of the city.",
				true);
		p7.saveToDatabase();

		// cdv
		CityDataVersion cdv = new CityDataVersion(city, "6.2");
		PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv.getId(), p0);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv.getId(), p1);
		ps1.saveToDatabase();
		PlaceOfInterestSight ps5 = new PlaceOfInterestSight(cdv.getId(), p5);
		ps5.saveToDatabase();
		PlaceOfInterestSight ps7 = new PlaceOfInterestSight(cdv.getId(), p7);
		ps7.saveToDatabase();

		Map m0 = new Map(city.getId(), "All Rome", "general map of all the city.", "roma.png");
		double[] coords0 = { 400, 200 };
		Location l0 = new Location(m0, p0, coords0);
		m0.addLocation(l0);
		double[] coords1 = { 200, 200 };
		Location l1 = new Location(m0, p1, coords1);
		m0.addLocation(l1);
		double[] coords5 = { 100, 100 };
		Location l5 = new Location(m0, p5, coords5);
		m0.addLocation(l5);
		m0.saveToDatabase();

		MapSight ms0 = new MapSight(cdv.getId(), m0);
		ms0.saveToDatabase();

		Route r1 = new Route(city.getId(), "Historical trip","A trip that emphasis the historical attraction in the city.",true);
		RouteStop rstop1 = new RouteStop(r1, p0, LocalTime.of(0, 40, 0));
		r1.addRouteStop(rstop1);
		RouteStop rstop2 = new RouteStop(r1, p1, LocalTime.of(1, 30, 0));
		r1.addRouteStop(rstop2);
		r1.saveToDatabase();

		RouteSight rs1 = new RouteSight(cdv.getId(), r1);
		rs1.saveToDatabase();

		cdv.reloadTempsFromDatabase();
		city._addPublishedCityDataVersion(cdv);
		// cdv2
		CityDataVersion cdv2 = city.getCopyUnpublishedVersions().get(0);
		PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv2.getId(), p2);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv2.getId(), p3);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv2.getId(), p4);
		ps0.saveToDatabase();
		PlaceOfInterestSight ps6 = new PlaceOfInterestSight(cdv2.getId(), p6);
		ps0.saveToDatabase();

		Map m1 = null;
		ArrayList<Integer> idsM1 = Database.searchMap(m0.getCityId(), m0.getName(), m0.getInfo(), m0.getImgURL());
		for (int id : idsM1) {
			if (id != m0.getId()) {
				m1 = Database.getMapById(id);
			}
		}
		if (m1 != null) {
			double[] coords2 = { 100, 350 };
			Location l2 = new Location(m1, p2, coords2);
			m1.addLocation(l2);
			double[] coords3 = { 70, 420 };
			Location l3 = new Location(m1, p3, coords3);
			m1.addLocation(l3);
			m1.saveToDatabase();
		} else
			System.out.println("Something weird is happening :( check rome() in InitDatabase");

		Map m2 = new Map(city.getId(), "Rome Geografic", "Wide range map that shows all the city with satellite imgs.",
				"rome_terrain.png");
		double[] coords20 = { 100, 300 };
		Location l20 = new Location(m2, p0, coords20);
		m2.addLocation(l20);
		double[] coords25 = { 350, 180 };
		Location l25 = new Location(m2, p5, coords25);
		m2.addLocation(l25);
		m0.saveToDatabase();
		double[] coords24 = { 270, 420 };
		Location l24 = new Location(m2, p4, coords24);
		m2.addLocation(l24);
		m0.saveToDatabase();
		double[] coords26 = { 200, 100 };
		Location l26 = new Location(m2, p6, coords26);
		m2.addLocation(l26);
		m0.saveToDatabase();

		MapSight ms2 = new MapSight(cdv2.getId(), m2);
		ms2.saveToDatabase();

		Route r2 = null;
		ArrayList<Integer> idsR2 = Database.searchRoute(r1.getCityId(), r1.getInfo());
		for (int id : idsR2) {
			if (id != r1.getId()) {
				r2 = Database.getRouteById(id);
			}
		}
		if (r2 != null) {
			r2.setInfo("Historical trip - Upgraded");
			RouteStop rstop3 = new RouteStop(r2, p2, LocalTime.of(2, 0, 0));
			r2.addRouteStop(rstop3);
			r2.saveToDatabase();
		} else
			System.out.println("Something weird is happening :( check rome() in InitDatabase");

		Route r3 = new Route(city.getId(), "Fun trip not boring :-)","Trip that is best suited for ages 10-20.",false);
		RouteStop rstop23 = new RouteStop(r3, p3, LocalTime.of(0, 40, 0));
		r3.addRouteStop(rstop23);
		RouteStop rstop25 = new RouteStop(r3, p5, LocalTime.of(2, 27, 0));
		r3.addRouteStop(rstop25);
		RouteStop rstop24 = new RouteStop(r3, p4, LocalTime.of(0, 53, 0));
		r3.addRouteStop(rstop24);
		r3.saveToDatabase();

		RouteSight rs2 = new RouteSight(cdv2.getId(), r3);
		rs2.saveToDatabase();

		cdv2.saveToDatabase();
		city.saveToDatabase();
	}

	/**
	 * Initializing Lior and Yosi users in database
	 * @throws SQLException if the access to database failed
	 */
	private static void liorAndYosi() throws SQLException {
		Employee e = new Employee("Lior", "Lior_strong1!", "lior@gmail.com", "lior", "wiessman", "0523322726",
				Employee.Role.CEO);
		e.saveToDatabase();
		Customer cust = new Customer("yosi11", "LDCyosiiii!", "yosi@gmail.com", "yosi", "ben asser", "0523322123",
				"5495681338665894", "07/24", "896");
		Subscription sub0 = new Subscription(cust, 1, LocalDate.of(2019, 2, 8), 201.8, 199.9, LocalDate.of(2019, 4, 8));
		cust.addSubscription(sub0);
		InformationSystem.addSubscription(1, LocalDate.of(2019, 2, 8));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 2, 21));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 3, 15));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 4, 6));
		
		for(int i=0;i<11;i++)
			InformationSystem.addVisit(1, LocalDate.of(2019, 2, 8).plusDays(i*4));
		
		Subscription sub1 = new Subscription(cust, 2, LocalDate.of(2019, 5, 7), 171.8, 112.9, LocalDate.of(2019, 11, 7));
		cust.addSubscription(sub1);
		InformationSystem.addSubscription(2, LocalDate.of(2019, 5, 7));
		InformationSystem.addSubDownload(2,LocalDate.of(2019, 5, 20));

		for(int i=0;i<11;i++)
			InformationSystem.addVisit(2, LocalDate.of(2019, 5, 7).plusDays(i*3));
		
		OneTimePurchase otp = new OneTimePurchase(cust, 1, LocalDate.of(2019, 5, 17), 20, 19);
		otp.updateToWasDownload();
		InformationSystem.addVisit(1, LocalDate.of(2019, 5, 17));
		InformationSystem.addOneTimePurchase(1, LocalDate.of(2019, 5, 17));
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
	}
	
	/**
	 * Initializing Dan and adiel users in database
	 * @throws SQLException if the access to database failed
	 */
	private static void danAndAdiel() throws SQLException {
		Employee e = new Employee("adiel", "adiel1", "statman.adiel@gmail.com", "adiel", "statman", "0525952726",
				Employee.Role.REGULAR);
		e.saveToDatabase();
		Customer cust = new Customer("dan", "masterDan#1", "dannyf.post@gmail.com", "dan", "feldman", "0523325686",
				"5495655558665894", "01/23", "354");
		Subscription sub0 = new Subscription(cust, 1, LocalDate.of(2019, 3, 17), 211.8, 189.9, LocalDate.of(2019, 6, 17));
		cust.addSubscription(sub0);
		InformationSystem.addSubscription(1, LocalDate.of(2019, 3, 17));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 4, 30));
		
		for(int i=0;i<2;i++)
			InformationSystem.addVisit(1, LocalDate.of(2019, 6, 7).plusDays(i*3));
		
		Subscription sub1 = new Subscription(cust, 4, LocalDate.of(2019, 2, 3), 131.8, 111.9, LocalDate.of(2019, 5, 3));
		cust.addSubscription(sub1);
		InformationSystem.addSubscription(4, LocalDate.of(2019, 2, 3));
		
		Subscription sub2 = new Subscription(cust, 4, LocalDate.of(2019, 5, 4), 131.8, 100.7, LocalDate.of(2019, 8, 4));
		cust.addSubscription(sub2);
		InformationSystem.addSubscriptionRenewal(4,LocalDate.of(2019, 5, 4));
		InformationSystem.addSubDownload(4,LocalDate.of(2019, 5, 20));
		
		for(int i=0;i<11;i++)
			InformationSystem.addVisit(4, LocalDate.of(2019, 2, 3).plusDays(i*5));

		OneTimePurchase otp = new OneTimePurchase(cust, 2, LocalDate.of(2019, 6, 10), 19, 18);
		otp.updateToWasDownload();
		InformationSystem.addVisit(2, LocalDate.of(2019, 6, 10));
		InformationSystem.addOneTimePurchase(2, LocalDate.of(2019, 6, 10));
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
	}

	/**
	 * Initializing Ben and Gadi users in database
	 * @throws SQLException if the access to database failed
	 */
	private static void benAndGadi() throws SQLException {
		Employee e = new Employee("ben", "benbon&ALAA", "bengordoncshaifa@gmail.com", "ben", "musa", "0508322726",
				Employee.Role.MANAGER);
		e.saveToDatabase();
		Customer cust = new Customer("gadi", "gadi11@", "gadi@gmail.com", "gadi", "landau", "0524867726",
				"5495123458665894", "01/25", "891");
		Subscription sub = new Subscription(cust, 1, LocalDate.of(118, 6, 8), 53.2, 50.9, LocalDate.of(118, 8, 8));
		cust.addSubscription(sub);
		InformationSystem.addSubscription(1, LocalDate.of(2019, 6, 8));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 6, 11));

		for(int i=0;i<3;i++)
			InformationSystem.addVisit(1, LocalDate.of(2019, 6, 8).plusDays(i*2));
		
		OneTimePurchase otp = new OneTimePurchase(cust, 2, LocalDate.of(2019, 4, 6), 9, 8);
		otp.updateToWasDownload();
		InformationSystem.addVisit(2, LocalDate.of(2019, 4, 6));
		InformationSystem.addOneTimePurchase(2, LocalDate.of(2019, 4, 6));
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
	}

	/**
	 * Initializing Sigal and Tomer employees in database
	 * @throws SQLException if the access to database failed
	 */
	private static void sigalAndTomer() throws SQLException {
		Employee e = new Employee("sigal", "sigalIsNoob!", "yonatan.sigal11@gmail.com", "yonatan", "sigal",
				"0508322126", Employee.Role.REGULAR);
		e.saveToDatabase();
		Customer cust = new Customer("tomer", "IAmTomer*", "1234tomer@gmail.com", "tomer", "kohavi", "0524867726",
				"5495123458612894", "02/25", "821");
		Subscription sub = new Subscription(cust, 3, LocalDate.of(2019, 6, 6), 63.2, 50.9, LocalDate.of(2019, 8, 8));
		cust.addSubscription(sub);
		InformationSystem.addSubscription(3, LocalDate.of(2019, 6, 6));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 6, 10));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 6, 13));
		
		for(int i=0;i<2;i++)
			InformationSystem.addVisit(3, LocalDate.of(2019, 6, 6).plusDays(i*2));

		OneTimePurchase otp = new OneTimePurchase(cust, 1, LocalDate.of(2019, 6, 6), 19, 8);
		otp.updateToWasDownload();
		InformationSystem.addVisit(1, LocalDate.of(2019, 6, 6));
		InformationSystem.addOneTimePurchase(1, LocalDate.of(2019, 6, 6));
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
	}

	/**
	 * Initializing A and B employees in database
	 * @throws SQLException if the access to database failed
	 */
	private static void aAndB() throws SQLException {
		Employee e = new Employee("a", "a", "a@gmail.com", "aa", "aa", "0500000000", Employee.Role.CEO);
		e.saveToDatabase();
		Customer cust = new Customer("q", "q", "q@gmail.com", "q", "q q", "0523022100", "5495681338665894", "07/24",
				"000");
		Subscription sub0 = new Subscription(cust, 3, LocalDate.of(2019, 1, 25), 201.8, 199.9, LocalDate.of(2019, 3, 25));
		cust.addSubscription(sub0);
		InformationSystem.addSubscription(3, LocalDate.of(2019, 1, 25));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 1, 29));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 2, 17));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 2, 21));
		Subscription sub2 = new Subscription(cust, 3, LocalDate.of(2019, 3, 18), 201.8, 179.9, LocalDate.of(2019, 6, 18));
		cust.addSubscription(sub2);
		InformationSystem.addSubscriptionRenewal(3,LocalDate.of(2019, 3, 18));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 4, 11));
		InformationSystem.addSubDownload(3,LocalDate.of(2019, 5, 20));
		
		for(int i=0;i<27;i++)
			InformationSystem.addVisit(3, LocalDate.of(2019, 1, 25).plusDays(i*4));
		
		Subscription sub1 = new Subscription(cust, 1, LocalDate.of(2019, 1, 28), 171.8, 112.9, LocalDate.of(2019, 5, 15));
		cust.addSubscription(sub1);
		InformationSystem.addSubscription(1, LocalDate.of(2019, 1, 28));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 2, 20));
		InformationSystem.addSubDownload(1,LocalDate.of(2019, 4, 17));
		
		for(int i=0;i<20;i++)
			InformationSystem.addVisit(1, LocalDate.of(2019, 1, 28).plusDays(i*4));

		OneTimePurchase otp = new OneTimePurchase(cust, 4, LocalDate.of(2019, 4, 20), 15, 9);
		otp.updateToWasDownload();
		InformationSystem.addVisit(4, LocalDate.of(2019, 4, 20));
		InformationSystem.addOneTimePurchase(4, LocalDate.of(2019, 4, 20));
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
	}

	
	/**
	 * Return the number of maps in the city
	 * @param cityId the city id
	 * @return the number of maps
	 * @throws SQLException if the access to database failed
	 */
	private static int getNumMapsOfCitiy(int cityId) throws SQLException {
		City c=Database.getCityById(cityId);
		if(c==null) return 0;
		CityDataVersion cdv=c.getCopyPublishedVersion();
		if(cdv==null) return 0;
		return cdv.getNumMapSights();	
	}
	
	/**
	 * Initializing some of the statistics in database
	 * @throws SQLException if the access to database failed
	 */
	private static void initStatistics() throws SQLException {		
		//city 1
		InformationSystem.setNumMaps(1, 0 ,LocalDate.of(2019, 1, 10));
		InformationSystem.newVersionWasPublished(1, LocalDate.of(2019, 1, 25));
		InformationSystem.setNumMaps(1, 1,LocalDate.of(2019, 1, 25));
		InformationSystem.newVersionWasPublished(1, LocalDate.of(2019, 4, 30));
		InformationSystem.setNumMaps(1, getNumMapsOfCitiy(1),LocalDate.of(2019, 4, 30));
		
		//city 2
		InformationSystem.setNumMaps(2, 0 ,LocalDate.of(2019, 4, 1));
		InformationSystem.newVersionWasPublished(2, LocalDate.of(2019, 4, 5));
		InformationSystem.setNumMaps(2, 1,LocalDate.of(2019, 4, 5));
		InformationSystem.newVersionWasPublished(2, LocalDate.of(2019, 5, 30));
		InformationSystem.setNumMaps(2, getNumMapsOfCitiy(2),LocalDate.of(2019, 4, 30));
		
		//city 3
		InformationSystem.setNumMaps(3, 0 ,LocalDate.of(2019, 1, 10));
		InformationSystem.newVersionWasPublished(3, LocalDate.of(2019, 1, 20));
		InformationSystem.setNumMaps(3, getNumMapsOfCitiy(3),LocalDate.of(2019, 4 , 30));
		
		//city 4
		InformationSystem.setNumMaps(4, 0 ,LocalDate.of(2019, 1, 1));
		InformationSystem.newVersionWasPublished(4, LocalDate.of(2019, 1, 27));
		InformationSystem.setNumMaps(4, getNumMapsOfCitiy(4),LocalDate.of(2019, 1, 27));
	}

}
