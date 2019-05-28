import java.util.ArrayList;

public class City
{
    int id;
    String cityName;
    String cityDescription;
    //list of cityDataVersion
    Integer publishedVersionId;

    private City(int id, String cityName, String cityDescription, Integer publishedVersionId) {
        this.id = id;
        this.cityName = cityName;
        this.cityDescription = cityDescription;
        this.publishedVersionId = publishedVersionId;
    }

    public static City _createCity(int id, String cityName, String cityDescription, Integer publishedVersionId){ //friend to Database
        return new City(id,cityName,cityDescription,publishedVersionId);
    }

    public City(String cityName, String cityDescription)
    {
        this.id=Database.generateIdCity();
        this.cityName = cityName;
        this.cityDescription=cityDescription;
        this.publishedVersionId=null;
    }

    public ArrayList<CityDataVersion> getAllCityDataVersions() {
        ArrayList<Integer> ids= Database.searchCityDataVersion(this.id);
        ArrayList<CityDataVersion> arrList=new ArrayList<CityDataVersion>();
        for(int id : ids)
        {
            CityDataVersion o=Database._getCityDataVersionById(id);
            if(o!=null)
                arrList.add(o);
        }
        return arrList;
    }

    public ArrayList<CityDataVersion> getAllUnpublishedCityDataVersions() {
        ArrayList<Integer> ids= Database.searchCityDataVersion(this.id);
        ArrayList<CityDataVersion> arrList=new ArrayList<CityDataVersion>();
        for(int id : ids)
        {
            CityDataVersion o=Database._getCityDataVersionById(id);
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
        CityDataVersion cdv=Database._getCityDataVersionById(cdvId);
        if(cdv==null || cdv.getCityId()!=this.id)
            return null;
        return cdv;
    }

    public CityDataVersion addUnpublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(versionName,priceOneTime,pricePeriod,this.id);
        Database._saveCityDataVersion(cdv);
        return cdv;
    }

    public CityDataVersion addPublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(versionName,priceOneTime,pricePeriod,this.id);
        this.publishedVersionId=cdv.getId();
        Database._saveCityDataVersion(cdv);
        return cdv;
    }

    public boolean setPublishedVersionId(int cdvId)
    {
        CityDataVersion cdv=Database._getCityDataVersionById(cdvId);
        if(cdv==null || cdv.getCityId()!=this.id)
            return false;
        this.publishedVersionId=cdvId;
        return true;
    }

    public CityDataVersion getPublishedVersion()
    {
        CityDataVersion cdv=null;
        if(this.publishedVersionId!=null)
            cdv=Database._getCityDataVersionById(this.publishedVersionId);
        if(cdv==null || cdv.getCityId()!=this.id)
        {
            this.publishedVersionId=null;
            return null;
        }
        return cdv;
    }

    public Integer getPublishedVersionId()
    {
        CityDataVersion cdv=getPublishedVersion();
        if(cdv==null)
            this.publishedVersionId=null;
        return this.publishedVersionId;
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
        Database._deleteCityDataVersion(cdv.getId());
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