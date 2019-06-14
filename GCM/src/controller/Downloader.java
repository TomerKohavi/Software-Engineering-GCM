package controller;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.Customer;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Subscription;
import otherClasses.Pair;

/**
 * Class that handles all the downloads
 * 
 * @author Ron Cohen
 *
 */
public final class Downloader
{
	/**
	 * change the default constructor to private, this class cannot be created as
	 * object.
	 */
	private Downloader()
	{
	}

	/**
	 * Downloads the places of interest
	 * 
	 * @param listPS the list of places of interest
	 * @param path the path to download
	 * @return if the download succeeded
	 */
	public static boolean downloadPOIs(ArrayList<PlaceOfInterestSight> listPS, String path)
	{
		if (listPS == null)
			return false;
		try
		{
			FileWriter fileWriter = new FileWriter(path);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("-----Places Of Interest-----");
			printWriter.println("");
			for (PlaceOfInterestSight ps : listPS)
			{
				PlaceOfInterest p = ps.getCopyPlace();
				if (p == null)
					continue;
				printWriter.println(p);
			}
			printWriter.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Problem writing to file- downloadPOIs");
			return false;
		}
	}

	/**
	 * Downloads reports of update cities versions that intended to and classified
	 * by customers today
	 * 
	 * @param path the path to download the report
	 * @return if the download succeeded
	 */
	public static boolean downloadReportsUpdateVersions(String path)
	{
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		ArrayList<Integer> citiesIds = InformationSystem.getCitiesWithNewPublishedVersion();
		if (citiesIds == null)
			return false;
		ArrayList<City> cities = new ArrayList<City>();
		for (int cityId : citiesIds)
		{
			City c = Database.getCityById(cityId);
			if (c == null)
				continue;
			cities.add(c);
		}
		ArrayList<Integer> subsIds = new ArrayList<Integer>();
		// find relevant subs
		for (int cityId : citiesIds)
		{
			subsIds.addAll(Database.searchSubscription(null, cityId, today, true));
		}
		// find relevant users
		Set<Integer> customersIds = new HashSet<Integer>();
		for (int subsId : subsIds)
		{
			Subscription s = Database._getSubscriptionById(subsId);
			if (s == null)
				continue;
			customersIds.add(s.getUserId());
		}
		// find which cities for each customer
		ArrayList<Pair<Customer, ArrayList<City>>> custCities = new ArrayList<>();
		for (int custId : customersIds)
		{
			Customer cust = Database.getCustomerById(custId);
			if (cust == null)
				continue;
			ArrayList<City> citiesOfCust = new ArrayList<City>();
			for (City city : cities)
			{
				if (cust.canViewCityWithSubscription(city.getId()))
					citiesOfCust.add(city);
			}
			if (citiesOfCust.size() == 0)
				continue;
			Pair<Customer, ArrayList<City>> p = new Pair<Customer, ArrayList<City>>(cust, citiesOfCust);
			custCities.add(p);
		}
		// write the report
		try
		{
			FileWriter fileWriter = new FileWriter(path);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("-----Reports of Update Version (for customers)-----");
			printWriter.println("");
			for (Pair<Customer, ArrayList<City>> p : custCities)
			{
				Customer c = p.a;
				printWriter.println("The Customer:");
				printWriter.println("- Id: " + p.a.getId());
				printWriter.println("- Full name: " + p.a.getFirstName() + " " + p.a.getLastName());
				printWriter.println("- Phone number: " + p.a.getPhoneNumber());
				printWriter.println("- Email: " + p.a.getEmail());
				printWriter.println("The Message:");
				String space = "   ";
				printWriter.println(space + "Hello loyal customer,");
				printWriter.println(
						space + "Our dedicated crew works hard every day just to improve your viewing content.");
				printWriter.println(
						space + "After many hours we finally managed to create new versions of several cities.");
				printWriter.println(space + "As of today the following cities will be updated:");
				for (City city : p.b)
				{
					printWriter.println(space + "- The city: \"" + city.getCityName() + "\", version: \""
							+ city.getCopyPublishedVersion().getVersionName() + "\".");
				}
				printWriter.println(space + "That is all for now,");
				printWriter.println(space + "Thanks again for your hard support.");
				printWriter.println("");
				printWriter.println("");
			}
			printWriter.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Problem writing to file- downloadReportsUpdateVersions");
			return false;
		}
	}
}
