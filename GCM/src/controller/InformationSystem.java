package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import objectClasses.Statistic;

import java.sql.Date;
import java.util.Calendar;
import otherClasses.Pair;

public final class InformationSystem
{
	
	/**
	 * Enum that describe the events can be added to statistic "counters"
	 */
	public enum Ops
	{
		OneTimePurcahse, Subscription, SubRenewal, Visit, SubDownload, VersionPublish
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
	 * @param d the date of the statistic
	 * @return the statistic that was found
	 */
	public static Statistic getStatistic(int cityId, Date d)
	{
		ArrayList<Integer> ids = Database.searchStatistic((Integer) cityId, d, null, null, null);
		if (ids.size() != 1)
			return null;
		Statistic s = Database._getStatisticById(ids.get(0));
		return s;
	}

	/**
	 * Calc and return a "sum" of all the statistic in the 
	 * date range and have the city id.
	 * @param cityId the city id, can be null to request all cities id
	 * @param from date range from
	 * @param end date range to
	 * @return the statistic "sum" of all the statistics in range
	 */
	public static Statistic getRangeSumStatistics(Integer cityId, Date from, Date end)
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

	public static ArrayList<Pair<Pair<Date, Date>, Integer>> getRangeNumMaps(Integer cityId, Date from, Date end)
	{
		ArrayList<Integer> ids = Database.searchStatistic((Integer) cityId, null, null, end, null,true);
		ArrayList<Pair<Pair<Date, Date>, Integer>> ans = new ArrayList<Pair<Pair<Date, Date>, Integer>>();
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
			Date fromRange = statistics.get(i).getDate();
			if(fromRange.before(from))
				fromRange =from;
			Date toRange;
			if (i < statistics.size() - 1)
				toRange = statistics.get(i + 1).getDate();
			else
				toRange = new Date(Calendar.getInstance().getTime().getTime());
			if(toRange.before(from)) continue; // if we are not in the range dont add the pair
			Pair<Date, Date> datesRange = new Pair<Date, Date>(fromRange, toRange);
			map.put(statistics.get(i).getCityId(), statistics.get(i).getNumMaps());
			int numMaps = sumMapList(map);
			if (lastNumMaps == numMaps)
			{
				ans.get(ans.size() - 1).a.b = toRange;
			}
			else
				ans.add(new Pair<Pair<Date, Date>, Integer>(datesRange, numMaps));
			lastNumMaps = numMaps;
		}
		return ans;
	}

	private static int sumMapList(java.util.Map<Integer, Integer> map)
	{
		int sum = 0;
		for (int val : map.values())
		{
			sum += val;
		}
		return sum;
	}

	public static void addOneTimePurchase(int cityId)
	{
		addOneTimePurchase(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void addOneTimePurchase(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addOneTimePurchase();
		s.saveToDatabase();
	}

	public static void setNumMaps(int cityId, int numMaps)
	{
		setNumMaps(cityId, numMaps, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void setNumMaps(int cityId, int numMaps, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.setNumMaps(numMaps);
		s.saveToDatabase();
	}

	public static void addVisit(int cityId)
	{
		addVisit(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void addVisit(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addVisit();
		s.saveToDatabase();
	}

	public static void addSubscription(int cityId)
	{
		addSubscription(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void addSubscription(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubscription();
		s.saveToDatabase();
	}

	public static void addSubscriptionRenewal(int cityId)
	{
		addSubscriptionRenewal(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void addSubscriptionRenewal(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubscriptionRenewal();
		s.saveToDatabase();
	}

	public static void addSubDownload(int cityId)
	{
		addSubDownload(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void addSubDownload(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.addSubDownload();
		s.saveToDatabase();
	}

	public static void newVersionWasPublished(int cityId)
	{
		newVersionWasPublished(cityId, new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static void newVersionWasPublished(int cityId, Date d)
	{
		Statistic s = getStatistic(cityId, d);
		if (s == null)
			s = new Statistic(cityId, d);
		s.newVersionWasPublished();
		s.saveToDatabase();
	}

	public static ArrayList<Integer> getCitiesWithNewPublishedVersion()
	{
		return getCitiesWithNewPublishedVersion(new Date(Calendar.getInstance().getTime().getTime()));
	}

	public static ArrayList<Integer> getCitiesWithNewPublishedVersion(Date d)
	{
		return Database.searchStatistic(null, d, null, null, true);
	}

	public static ArrayList<Statistic> getAllStatistics()
	{
		ArrayList<Integer> ids = Database.searchStatistic(null, null, null, null, null);
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
