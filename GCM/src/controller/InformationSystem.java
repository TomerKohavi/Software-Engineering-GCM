package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import objectClasses.Statistic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import otherClasses.Pair;

/**
 * Controller class that handle the managment of statistics
 * @author Ron Cohen
 *
 */
public final class InformationSystem
{
	
	/**
	 * Enum that describe the events can be added to statistic "counters"
	 */
	public enum Ops
	{
		OneTimePurcahse, Subscription, SubRenewal, Visit, SubDownload, VersionPublish, NumMaps
	}
	
	/**
	 * change the default constructor to private,
	 * this class cannot be created as object.
	 */
	private InformationSystem()
	{
	}

	/**
	 * Returns statistic with the city id and the date,
	 * if none was found return null.
	 * @param cityId the city id
	 * @param d the LocalDate of the statistic
	 * @return the statistic that was found
	 * @throws SQLException if the access to database failed
	 */
	public static Statistic getStatistic(int cityId, LocalDate d) throws SQLException
	{
		ArrayList<Integer> ids = Database.searchStatistic((Integer) cityId, d, null, null, null,null);
		if (ids.size() == 0)
			return null;
		Statistic s = Database._getStatisticById(ids.get(0));
		return s;
	}

	/**
	 * Calc and return a "sum" of all the statistic in the 
	 * LocalDate range and have the city id.
	 * @param cityId the city id, can be null to request all cities id
	 * @param from LocalDate range from
	 * @param end LocalDate range to
	 * @return the statistic "sum" of all the statistics in range
	 * @throws SQLException if the access to database failed
	 */
	public static Statistic getRangeSumStatistics(Integer cityId, LocalDate from, LocalDate end) throws SQLException
	{
		ArrayList<Integer> ids = Database.searchStatistic((Integer) cityId, null, from, end, null,null);
		Statistic sum = Statistic.createBlankStatistic();
		//calc sum
		for (int id : ids)
		{
			Statistic s = Database._getStatisticById(id);
			if (s == null) continue;
			sum = Statistic.addStatistics(sum, s);
		}
		//calc num maps, need to go on all the statistics :(
		ArrayList<Integer> allValidNumMapsStatisticsIds = Database.searchStatistic((Integer) cityId, null, null, end, null,true);
		ArrayList<Statistic> statisticsNumMaps = new ArrayList<Statistic>();
		for(int id:allValidNumMapsStatisticsIds)
		{
			Statistic s = Database._getStatisticById(id);
			if(s.getNumMaps() >= 0)
				statisticsNumMaps.add(s);
		}
		Collections.sort(statisticsNumMaps);
		java.util.Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Statistic s: statisticsNumMaps)
			map.put(s.getCityId(), s.getNumMaps());
		int numMaps = sumMapList(map);
		sum.setNumMaps(numMaps);
		return sum;
	}

	/**
	 * Calc and return a detailed list of the changes to num maps in the LocalDate range
	 * @param cityId the city id, can be null to request all cities id
	 * @param from LocalDate range from
	 * @param end LocalDate range to
	 * @return list of the num maps and the LocalDate range it was relevant
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Pair<Pair<LocalDate, LocalDate>, Integer>> getRangeNumMaps(Integer cityId, LocalDate from, LocalDate end) throws SQLException
	{
		ArrayList<Integer> ids = Database.searchStatistic((Integer) cityId, null, null, end, null,true);
		ArrayList<Pair<Pair<LocalDate, LocalDate>, Integer>> ans = new ArrayList<Pair<Pair<LocalDate, LocalDate>, Integer>>();
		ArrayList<Statistic> statistics = new ArrayList<Statistic>();
		for (int id : ids)
		{
			Statistic s = Database._getStatisticById(id);
			if (s != null && s.getNumMaps() >= 0)
				statistics.add(s);
		}
		Collections.sort(statistics);
		java.util.Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int lastNumMaps = -1;
		for (int i = 0; i < statistics.size(); i++)
		{
			LocalDate fromRange = statistics.get(i).getDate();
			if(fromRange.isBefore(from))
				fromRange =from;
			LocalDate toRange;
			if (i < statistics.size() - 1)
				toRange = statistics.get(i + 1).getDate();
			else
				toRange = LocalDate.now();
			if(toRange.isBefore(from)) continue; // if we are not in the range dont add the pair
			Pair<LocalDate, LocalDate> datesRange = new Pair<LocalDate, LocalDate>(fromRange, toRange);
			map.put(statistics.get(i).getCityId(), statistics.get(i).getNumMaps());
			int numMaps = sumMapList(map);
			if (lastNumMaps == numMaps)
			{
				ans.get(ans.size() - 1).a.b = toRange;
			}
			else
				ans.add(new Pair<Pair<LocalDate, LocalDate>, Integer>(datesRange, numMaps));
			lastNumMaps = numMaps;
		}
		return ans;
	}

	/**
	 * Sums the values of java.util.Map
	 * @param map the map
	 * @return the sum of the values in the map
	 */
	private static int sumMapList(java.util.Map<Integer, Integer> map)
	{
		int sum = 0;
		for (int val : map.values())
		{
			sum += val;
		}
		return sum;
	}

	/**
	 * Adds an one time purchase to the statistic of the city id and today date
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void addOneTimePurchase(int cityId) throws SQLException
	{
		LocalDate today=LocalDate.now();
		addOneTimePurchase(cityId, today);
	}

	/**
	 * Adds an one time purchase to the statistic of the city id and the date
	 * @param cityId the city id 
	 * @param d the date
	 * @throws SQLException if the access to database failed
	 */
	public static void addOneTimePurchase(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			{
			s = new Statistic(cityId, d);
			}
		s.addOneTimePurchase();
		s.saveToDatabase();
	}

	/**
	 * Set the n number maps of the statistic with city id and today date
	 * @param cityId the city id
	 * @param numMaps the number of maps
	 * @throws SQLException if the access to database failed
	 */
	public static void setNumMaps(int cityId, int numMaps) throws SQLException
	{
		setNumMaps(cityId, numMaps, LocalDate.now());
	}

	/**
	 * Set the n number maps of the statistic with city id and the date
	 * @param cityId the city id
	 * @param d the date
	 * @param numMaps the number of maps
	 * @throws SQLException if the access to database failed
	 */
	public static void setNumMaps(int cityId, int numMaps, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.setNumMaps(numMaps);
		s.saveToDatabase();
	}

	/**
	 * Adds a visit to the statistic of the city id and today date
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void addVisit(int cityId) throws SQLException
	{
		addVisit(cityId, LocalDate.now());
	}

	/**
	 * Adds a visit to the statistic of the city id and the date
	 * @param cityId the city id 
	 * @param d the date
	 * @throws SQLException if the access to database failed
	 */
	public static void addVisit(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addVisit();
		s.saveToDatabase();
	}

	/**
	 * Adds a subscription purchase to the statistic of the city id and today date
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubscription(int cityId) throws SQLException
	{
		addSubscription(cityId, LocalDate.now());
	}

	/**
	 * Adds a subscription purchase to the statistic of the city id and the date
	 * @param cityId the city id 
	 * @param d the date
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubscription(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubscription();
		s.saveToDatabase();
	}

	/**
	 * Adds a subscription renewal purchase to the statistic of the city id and today date
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubscriptionRenewal(int cityId) throws SQLException
	{
		addSubscriptionRenewal(cityId, LocalDate.now());
	}

	/**
	 * Adds a subscription renewal purchase to the statistic of the city id and the date
	 * @param cityId the city id 
	 * @param d the date
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubscriptionRenewal(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubscriptionRenewal();
		s.saveToDatabase();
	}

	/**
	 * Adds a subscription download to the statistic of the city id and today date
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubDownload(int cityId) throws SQLException
	{
		addSubDownload(cityId, LocalDate.now());
	}

	/**
	 * Adds a subscription download to the statistic of the city id and the date
	 * @param cityId the city id 
	 * @param d the date
	 * @throws SQLException if the access to database failed
	 */
	public static void addSubDownload(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubDownload();
		s.saveToDatabase();
	}

	/**
	 * Updates the statistic of the city id that a new version of this city was published today
	 * @param cityId the city id
	 * @throws SQLException if the access to database failed
	 */
	public static void newVersionWasPublished(int cityId) throws SQLException
	{
		newVersionWasPublished(cityId, LocalDate.now());
	}

	/**
	 * Updates the statistic of the city id that a new version of this city was published in the date
	 * @param cityId the city id
	 * @param d the LocalDate that was published
	 * @throws SQLException if the access to database failed
	 */
	public static void newVersionWasPublished(int cityId, LocalDate d) throws SQLException
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.newVersionWasPublished();
		s.saveToDatabase();
	}

	/**
	 * Returns list of the statistics id that with today LocalDate and describe cities that has new published version
	 * @return list of the statistics ids
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> getCitiesWithNewPublishedVersion() throws SQLException
	{
		return getCitiesWithNewPublishedVersion(LocalDate.now());
	}

	/**
	 * Returns list of the statistics id that with the LocalDate and describe cities that has new published version
	 * @param d the LocalDate of the statistics
	 * @return list of the statistics ids
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> getCitiesWithNewPublishedVersion(LocalDate d) throws SQLException
	{
		return Database.searchStatistic(null, d, null, null, true,null);
	}

	/**
	 * Return a list of all the statistics
	 * @return list of statisics
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Statistic> getAllStatistics() throws SQLException
	{
		ArrayList<Integer> ids = Database.searchStatistic(null, null, null, null, null,null);
		ArrayList<Statistic> arrList = new ArrayList<Statistic>();
		for (int id : ids)
		{
			Statistic o = Database._getStatisticById(id);
			if (o == null)
				continue;
			arrList.add(o);
		}
		Collections.sort(arrList, Collections.reverseOrder());
		return arrList;
	}
}
