import java.io.Serializable;
import java.util.ArrayList;

public class City implements ClassMustProperties, Serializable
{
    private int id;
    private String cityName;
    private String cityDescription;
    private Integer publishedVersionId;

    private ArrayList<CityDataVersion> temp_unpublishedVersions;
    private ArrayList<CityDataVersion> temp_removeVersions;
    private CityDataVersion temp_publishedVersion;

    private City(int id, String cityName, String cityDescription, Integer publishedVersionId) {
        this.id = id;
        this.cityName = cityName;
        this.cityDescription = cityDescription;
        this.publishedVersionId = publishedVersionId;
        reloadTempsFromDatabase();
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
        this.temp_unpublishedVersions=new ArrayList<>();
        this.temp_removeVersions=new ArrayList<>();
        this.temp_publishedVersion=null;
    }

    public void saveToDatabase()
    {
        Database.saveCity(this);
        //delete removes
        for(CityDataVersion cdv:temp_removeVersions)
        {
            if(!temp_unpublishedVersions.contains(cdv) && !temp_publishedVersion.equals(cdv))
                cdv.deleteFromDatabase();
        }
        temp_removeVersions=new ArrayList<>();
        //save
        for(CityDataVersion cdv:temp_unpublishedVersions)
            cdv.saveToDatabase();
        if(temp_publishedVersion!=null)
            temp_publishedVersion.saveToDatabase();
    }

    public void deleteFromDatabase() {
        Database.deleteCity(this.getId());
        //delete removes
        for(CityDataVersion cdv:temp_removeVersions)
            cdv.deleteFromDatabase();
        temp_removeVersions=new ArrayList<>();

        for(CityDataVersion cdv:temp_unpublishedVersions)
            cdv.deleteFromDatabase();
        if(temp_publishedVersion!=null)
            temp_publishedVersion.deleteFromDatabase();
        //remove statistics
        ArrayList<Integer> ids=Database.searchStatistic(this.id,null);
        for(int id:ids)
        {
            Statistic s=Database._getStatisticById(id);
            if(s!=null)
                s.deleteFromDatabase();
        }
    }

    public void reloadTempsFromDatabase() {
        this.temp_unpublishedVersions = generateUnpublishedCityDataVersions();
        this.temp_removeVersions = new ArrayList<>();
        if (this.publishedVersionId == null)
            this.temp_publishedVersion = null;
        else
        {
            this.temp_publishedVersion=Database._getCityDataVersionById(publishedVersionId);
            if(temp_publishedVersion==null)
                this.publishedVersionId=null;
        }
    }

    private ArrayList<CityDataVersion> generateUnpublishedCityDataVersions() {
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

    public int getNumUnpublishedCityDataVersions(){
        return temp_unpublishedVersions.size();
    }

    public CityDataVersion getUnpublishedCityDataVersionById(int cdvId)
    {
        for(CityDataVersion cdv:temp_unpublishedVersions){
            if(cdv.getId()==cdvId)
                return cdv;
        }
        return null;
    }

    public CityDataVersion addUnpublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(this,versionName,priceOneTime,pricePeriod);
        temp_unpublishedVersions.add(cdv);
        return cdv;
    }

    public CityDataVersion addPublishedCityDataVersion(String versionName, double priceOneTime, double pricePeriod)
    {
        CityDataVersion cdv=new CityDataVersion(this,versionName,priceOneTime,pricePeriod);
        if(this.publishedVersionId!=null && this.temp_publishedVersion!=null)
            temp_unpublishedVersions.add(temp_publishedVersion);
        publishedVersionId=cdv.getId();
        temp_publishedVersion=cdv;
        return cdv;
    }

    public boolean setUnpublishedToPublishedByVersionId(int cdvId)
    {
        CityDataVersion cdv=null;
        for(int i=0;i<temp_unpublishedVersions.size();i++)
        {
            CityDataVersion temp=temp_unpublishedVersions.get(i);
            if(temp.getId()==cdvId)
            {
                temp_unpublishedVersions.remove(i);
                cdv=temp;
                break;
            }
        }
        if(cdv==null)
            return false;
        setPublishedToUnpublished();
        this.publishedVersionId=cdv.getId();
        this.temp_publishedVersion=cdv;
        return true;
    }

    public boolean setPublishedToUnpublished()
    {
        if(this.publishedVersionId == null || this.temp_publishedVersion==null)
            return false;
        temp_unpublishedVersions.add(temp_publishedVersion);
        publishedVersionId = null;
        temp_publishedVersion=null;
        return true;
    }

    public CityDataVersion getPublishedVersion()
    {
        return temp_publishedVersion;
    }

    public Integer getPublishedVersionId()
    {
        return publishedVersionId;
    }

    public boolean isTherePublishedVersion()
    {
        return getPublishedVersion()!=null;
    }

    public CityDataVersion removeCityDataVersionById(int cdvId)
    {
        if(cdvId==publishedVersionId)
        {
            CityDataVersion cdv=temp_publishedVersion;
            publishedVersionId=null;
            temp_removeVersions.add(cdv);
            temp_publishedVersion=null;
            return cdv;
        }
        for(int i=0;i<temp_unpublishedVersions.size();i++)
        {
            CityDataVersion cdv=temp_unpublishedVersions.get(i);
            if(cdv.getId()==cdvId)
            {
                temp_unpublishedVersions.remove(i);
                temp_removeVersions.add(cdv);
                return cdv;
            }
        }
        return null;
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

    public CityDataVersion getCopyPublishedVersion() {
        return temp_publishedVersion;
    }

    public ArrayList<CityDataVersion> getCopyUnpublishedVersions() {
        return new ArrayList<>(temp_unpublishedVersions);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof City && ((City) o).getId()==this.getId();
    }
}
