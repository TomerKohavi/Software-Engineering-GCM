import java.util.ArrayList;

public class City
{
    int id;
    String cityName;
    String cityDescription;
    //list of cityDataVersion
    int publishedVersionId;

    public City(String cityName,String cityDescription)
    {
        this.id=Database.generateIdCity();
        this.cityName = cityName;
        this.cityDescription=cityDescription;
        this.publishedVersionId=-1;
    }

    public ArrayList<CityDataVersion> getAllCityDataVersions() {
        int[] ids= Database.searchCityDataVersion(this.id);
        ArrayList<CityDataVersion> arrList=new ArrayList<CityDataVersion>();
        for(int id : ids)
        {
            CityDataVersion o=Database.getCityDataVersionById(id);
            if(o!=null)
                arrList.add(o);
        }
        return arrList;
    }

    public ArrayList<CityDataVersion> getAllUnpublishedCityDataVersions() {
        int[] ids= Database.searchCityDataVersion(this.id);
        ArrayList<CityDataVersion> arrList=new ArrayList<CityDataVersion>();
        for(int id : ids)
        {
            CityDataVersion o=Database.getCityDataVersionById(id);
            if(o!=null && id!=this.publishedVersionId)
            arrList.add(o);
        }
        return arrList;
    }

    public int getNumCityDataVersions(){
        return getAllCityDataVersions().size();
    }

    public CityDataVersion getCityDataVersionById(int cdvId)
    {
        CityDataVersion cdv=Database.getCityDataVersionById(cdvId);
        if(cdv==null || cdv.getCityId()!=this.id)
            return null;
        return cdv;
    }

    public CityDataVersion addUnpublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(versionName,priceOneTime,pricePeriod,this.id);
        Database.saveCityDataVersion(cdv);
        return cdv;
    }

    public CityDataVersion addPublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(versionName,priceOneTime,pricePeriod,this.id);
        this.publishedVersionId=cdv.getId();
        Database.saveCityDataVersion(cdv);
        return cdv;
    }

    public boolean setPublishedVersionId(int cdvId)
    {
        CityDataVersion cdv=Database.getCityDataVersionById(cdvId);
        if(cdv==null || cdv.getCityId()!=this.id)
            return false;
        this.publishedVersionId=cdvId;
        return true;
    }

    public CityDataVersion getPublishedVersion()
    {
        CityDataVersion cdv=null;
        if(this.publishedVersionId>0)
            cdv=Database.getCityDataVersionById(this.publishedVersionId);
        if(cdv==null || cdv.getCityId()!=this.id)
        {
            this.publishedVersionId=-1;
            return null;
        }
        return cdv;
    }

    public int getPublishedVersionId()
    {
        CityDataVersion cdv=getPublishedVersion();
        if(cdv==null)
            return -1;
        return cdv.getId();
    }

    public boolean isTherePublishedVersion()
    {
        return getPublishedVersion()!=null;
    }

    public CityDataVersion removeCityDataVersionById(int cdvId)
    {
        CityDataVersion cdv=getCityDataVersionById(cdvId);
        if(cdv==null || cdv.getCityId()!=this.id)
            return null;
        Database.deleteCityDataVersion(cdv.getId());
        return cdv;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }
}
