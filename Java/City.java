import java.util.ArrayList;

public class City
{
    int id;
    String cityName;
    String cityDescription;
    ArrayList<CityDataVersion> unpublishedVersions;
    CityDataVersion publishedVersion;

    public City(String cityName,String cityDescription)
    {
        this.id=Database.generateIdCity();
        this.cityName = cityName;
        this.unpublishedVersions=new ArrayList<CityDataVersion>();
        this.publishedVersion=null;
        this.cityDescription=cityDescription;
    }

    public City(String cityName, String cityDescription,CityDataVersion cityDataVersion, boolean isVersionPublished) {
        this.id=Database.generateIdCity();
        this.cityName = cityName;
        this.unpublishedVersions=new ArrayList<CityDataVersion>();
        this.publishedVersion=null;
        if(isVersionPublished)
            publishedVersion=cityDataVersion;
        else
            unpublishedVersions.add(cityDataVersion);
        this.cityDescription=cityDescription;
    }

    public City(String cityName, ArrayList<CityDataVersion> unpublishedVersions, CityDataVersion publishedVersion) {
        this.id=Database.generateIdCity();
        this.cityName = cityName;
        this.unpublishedVersions = unpublishedVersions;
        this.publishedVersion = publishedVersion;
    }

    public int numUnpulished(){
        return unpublishedVersions.size();
    }

    public boolean isPublished(){
        return publishedVersion==null;
    }

    public String getCityName() {
        return cityName;
    }

    public ArrayList<CityDataVersion> getUnpublishedVersions() {
        return unpublishedVersions;
    }

    public CityDataVersion getPublishedVersion() {
        return publishedVersion;
    }

    public int getId() {
        return id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setUnpublishedVersions(ArrayList<CityDataVersion> unpublishedVersions) {
        this.unpublishedVersions = unpublishedVersions;
    }

    public void setPublishedVersion(CityDataVersion publishedVersion) {
        this.publishedVersion = publishedVersion;
    }

    public boolean publishVersion(int cityDataVersionId)
    {
        for (int i=0;i<unpublishedVersions.size();i++)
        {
            if(unpublishedVersions.get(i).getId()==cityDataVersionId)
            {
                CityDataVersion temp=publishedVersion;
                publishedVersion=unpublishedVersions.remove(i);
                unpublishedVersions.add(temp);
                return true;
            }
        }
        return false;
    }

    public boolean unpublish()
    {
        if(publishedVersion==null)
            return false;
        else
        {
            unpublishedVersions.add(publishedVersion);
            publishedVersion=null;
            return true;
        }
    }

    public boolean addUnpublishedVersion(CityDataVersion newVersion)
    {
        int newVersionId=newVersion.getId();
        for (CityDataVersion cVersion:unpublishedVersions)
        {
            if(cVersion.getId()==newVersionId)
                return false;
        }
        if(isPublished() && newVersionId== publishedVersion.getId())
            return false;
        unpublishedVersions.add(newVersion);
        return true;
    }

    public boolean addPublishedVersion(CityDataVersion newVersion)
    {
        int newVersionId=newVersion.getId();
        for (CityDataVersion cVersion:unpublishedVersions)
        {
            if(cVersion.getId()==newVersionId)
                return false;
        }
        boolean isP=isPublished();
        if(isP && newVersionId== publishedVersion.getId())
            return false;
        if(isP)
            unpublishedVersions.add(publishedVersion);
        publishedVersion=newVersion;
        return true;
    }

    public CityDataVersion getVersionById(int versionId)
    {
        for (CityDataVersion cVersion:unpublishedVersions)
        {
            if(cVersion.getId()==versionId)
                return cVersion;
        }
        if(isPublished() && versionId == publishedVersion.getId())
            return publishedVersion;
        return null;
    }

    public boolean removeUnpublishedVersion(int versionId)
    {
        for (int i=0;i<unpublishedVersions.size();i++)
        {
            if(unpublishedVersions.get(i).getId()==versionId)
            {
                unpublishedVersions.remove(i);
                return true;
            }
        }
        return false;
    }

    public CityDataVersion removePublishedVersion()
    {
        CityDataVersion temp=publishedVersion;
        publishedVersion=null;
        return temp;
    }
}
