package controller;

import java.util.ArrayList;
import java.util.Collections;

import objectClasses.Statistic;

import java.sql.Date;
import java.util.Calendar;

public final class InformationSystem
{
    private InformationSystem(){}

    //list of Statistic
    public static Statistic getStatistic(int cityId, Date d){
        ArrayList<Integer> ids=Database.searchStatistic((Integer) cityId, d, null, null,null);
        if(ids.size()!=1)
            return null;
        Statistic s=Database._getStatisticById(ids.get(0));
        if(s==null)
            return null;
        if(Database.getCityById(s.getCityId())==null)
        {
            Database._deleteStatistic(s.getId());
            return null;
        }
        return s;
    }

    public static Statistic getRangeSumStatistics(Integer cityId,Date from,Date end)
    {
        ArrayList<Integer> ids=Database.searchStatistic((Integer) cityId, null, from, end,null);
        Statistic sum=Statistic.createBlankStatistic();
        for(int id:ids)
        {
            Statistic s=Database._getStatisticById(id);
            if(s!=null)
            	sum=Statistic.addStatistics(sum,s);
        }
        return sum;
    }

    public static void addOneTimePurchase(int cityId) {
        addOneTimePurchase(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }

    public static void addOneTimePurchase(int cityId,Date d) {
        Statistic s = getStatistic(cityId,d);
        if(s == null)
            s = new Statistic(cityId,d);
        s.addOneTimePurchase();
        s.saveToDatabase();
    }

    public static void addVisit(int cityId){
        addVisit(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }

    public static void addVisit(int cityId,Date d) {
        Statistic s = getStatistic(cityId,d);
        if(s == null)
            s = new Statistic(cityId,d);
        s.addVisit();
        s.saveToDatabase();
    }

    public static void addSubscription(int cityId) {
        addSubscription(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }

    public static void addSubscription(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscription();
        s.saveToDatabase();
    }

    public static void addSubscriptionRenewal(int cityId) {
        addSubscriptionRenewal(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }

    public static void addSubscriptionRenewal(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscriptionRenewal();
        s.saveToDatabase();
    }

    public static void addSubDownload(int cityId) {
        addSubDownload(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }

    public static void addSubDownload(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubDownload();
        s.saveToDatabase();
    }
    
    public static void newVersionWasPublished(int cityId) {
    	newVersionWasPublished(cityId,new Date(Calendar.getInstance().getTime().getTime()));
    }
    
    public static void newVersionWasPublished(int cityId,Date d) {
    	Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.newVersionWasPublished();
        s.saveToDatabase();
    }
    
    public static ArrayList<Integer> getCitiesWithNewPublishedVersion() {
		return getCitiesWithNewPublishedVersion(new Date(Calendar.getInstance().getTime().getTime()));
    }
    
    public static ArrayList<Integer> getCitiesWithNewPublishedVersion(Date d) {
    	return Database.searchStatistic(null, d, null, null, true);
    }
    
    public static ArrayList<Statistic> getAllStatistics() {
        ArrayList<Integer> ids= Database.searchStatistic(null, null, null, null,null);
        ArrayList<Statistic> arrList=new ArrayList<Statistic>();
        for(int id : ids)
        {
            Statistic o=Database._getStatisticById(id);
            if(o==null)
                continue;
            if(Database.getCityById(o.getCityId())==null)
                Database._deleteStatistic(id);
            else
                arrList.add(o);
        }
        Collections.sort(arrList,Collections.reverseOrder());
        return arrList;
    }
}
