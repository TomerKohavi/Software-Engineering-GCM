import javax.xml.crypto.Data;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

public class Route
{
    private int id;
    private String info;
    private boolean acceptabilityToDisabled;
    private int numStops;
    //list of RouteStop

    public Route(String info, boolean acceptabilityToDisabled)
    {
        this.id=Database.generateIdRoute();
        this.info = info;
        this.acceptabilityToDisabled = acceptabilityToDisabled;
        numStops=0;
    }

    public ArrayList<RouteStop> getAllRouteStops() {
        int[] routeStopsIds= Database.searchRouteStop(this.id,-1,-1);
        ArrayList<RouteStop> arrList=new ArrayList<RouteStop>();
        for(int rdId : routeStopsIds)
            arrList.add(Database.getRouteStopById(rdId));
        Collections.sort(arrList);
        return arrList;
    }

    public RouteStop getRouteStopByPlaceId(int placeId)
    {
        int[] rsIds= Database.searchRouteStop(this.id,placeId,-1);
        if(rsIds.length!=1)
            return null;
        return Database.getRouteStopById(rsIds[0]);
    }

    public RouteStop getRouteStopAtNum(int num)
    {
        int[] rsIds= Database.searchRouteStop(this.id,-1,num);
        if(rsIds.length!=1)
            return null;
        return Database.getRouteStopById(rsIds[0]);
    }

    public RouteStop getRouteStopById(int rsId)
    {
        RouteStop rs=Database.getRouteStopById(rsId);
        if(rs==null || rs.getRouteId()!=this.id)
            return null;
        return rs;
    }

    public RouteStop addRouteStop(int placeId,Time recommendedTime)
    {
        int index=this.numStops;
        this.numStops++;
        RouteStop rs=new RouteStop(placeId,index,recommendedTime);
        Database.saveRouteStop(rs);
        return rs;
    }

    public RouteStop addRouteStop(int placeId,Time recommendedTime,int index)
    {
        if(index>this.numStops)
            return null;
        RouteStop rs=new RouteStop(placeId,index,recommendedTime);
        ArrayList<RouteStop> listRs=getAllRouteStops();
        for(int i=index;i<listRs.size();i++)
        {
            RouteStop temp=listRs.get(i);
            temp.setNumStop(i+1);
            Database.saveRouteStop(temp);
        }
        Database.saveRouteStop(rs);
        this.numStops++;
        return rs;
    }

    public RouteStop removeRouteStopAtIndex(int index)
    {
        if(index>=this.numStops)
            return null;
        ArrayList<RouteStop> listRs=getAllRouteStops();
        RouteStop rs=listRs.get(index);
        for(int i=index+1;i<listRs.size();i++)
        {
            RouteStop temp=listRs.get(i);
            temp.setNumStop(i-1);
            Database.saveRouteStop(temp);
        }
        Database.deleteRouteStop(rs.getId());
        this.numStops--;
        return rs;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setAcceptabilityToDisabled(boolean acceptabilityToDisabled) {
        this.acceptabilityToDisabled = acceptabilityToDisabled;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public boolean isAcceptabilityToDisabled() {
        return acceptabilityToDisabled;
    }

    public int getNumStops() {
        return numStops;
    }
}
