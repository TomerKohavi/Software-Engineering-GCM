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

public final class Downloader
{
    public static boolean downloadPOIs(CityDataVersion cdv,String path)
    {
        if(cdv==null) return false;
        ArrayList<PlaceOfInterestSight> listPS=cdv.getCopyPlaceSights();
        if(listPS==null) return false;
        try{
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("-----Places Of Interest-----\n");
            for(PlaceOfInterestSight ps: listPS)
            {
                PlaceOfInterest p=ps.getCopyPlace();
                if(p==null) continue;
                printWriter.print(p);
            }
            printWriter.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    
    public static boolean downloadReportsUpdateVersions(String path)
    {
    	Date today=new Date(Calendar.getInstance().getTime().getTime());
    	ArrayList<Integer> citiesIds=InformationSystem.getCitiesWithNewPublishedVersion();
    	if(citiesIds==null) return false;
    	ArrayList<City> cities=new ArrayList<City>();
        for(int cityId:citiesIds) {
        	City c=Database.getCityById(cityId);
        	if(c==null) continue;
        	cities.add(c);
        }
        ArrayList<Integer> subsIds=new ArrayList<Integer>();
        //find relevant subs
        for(int cityId:citiesIds) {
        	subsIds.addAll(Database.searchSubscription(null, cityId, today, true));
        }
        //find relevant users
        Set<Integer> customersIds=new HashSet<Integer>(); 
        for(int subsId:subsIds) {
        	Subscription s=Database._getSubscriptionById(subsId);
        	if(s==null) continue;
        	customersIds.add(s.getUserId());
        }
        //find which cities for each customer
        ArrayList<Pair<Customer,ArrayList<City>>> custCities=new ArrayList<>();
        for(int custId:customersIds) {
        	Customer cust=Database.getCustomerById(custId);
        	if(cust==null) continue;
        	ArrayList<City> citiesOfCust=new ArrayList<City>();
        	for(City city:cities)
        	{
        		if(cust.canViewCityWithSubscription(city.getId()))
        			citiesOfCust.add(city);
        	}
        	if(citiesOfCust.size()==0) continue;
        	Pair<Customer,ArrayList<City>> p=new Pair<Customer, ArrayList<City>>(cust, citiesOfCust);
        	custCities.add(p);
        }
        //write the report
        try{
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("-----Reports of Update Version (for customers)-----\n");
            for(Pair<Customer,ArrayList<City>> p:custCities)
            {
            	Customer c=p.a;
            	printWriter.print("The Customer:");
            	printWriter.print("- Id: "+p.a.getId());
            	printWriter.print("- Full name: "+p.a.getFirstName()+" "+p.a.getLastName());
            	printWriter.print("- Phone number: "+p.a.getPhoneNumber());
            	printWriter.print("- Email: "+p.a.getEmail());
            	printWriter.print("The Message:");
            	String space="   ";
            	printWriter.print(space+"Hello loyal customer,");
            	printWriter.print(space+"Our dedicated crew works hard everyday just to improve your viewing content.");
            	printWriter.print(space+"After many hours we finally managed to create new versions of several cities.");
            	printWriter.print(space+"As of today the following cities will be updated:");
            	for(City city:p.b)
            	{
            		printWriter.print(space+"- The city: \""+city.getCityName()+"\", version: \""+city.getCopyPublishedVersion().getVersionName()+"\".");
            	}
            	printWriter.print(space+"That is all for now,");
            	printWriter.print(space+"Thanks again for your hard support.");
            	printWriter.print("\n");
            }
            printWriter.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
