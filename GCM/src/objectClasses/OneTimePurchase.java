package objectClasses;

import java.io.Serializable;
import java.sql.Date;

import controller.Database;
import otherClasses.ClassMustProperties;

public class OneTimePurchase extends CityPurchase implements ClassMustProperties, Serializable
{
    private boolean wasDownload;

    private OneTimePurchase(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, boolean wasDownload) {
        super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
        this.wasDownload = wasDownload;
    }

    public static OneTimePurchase _createOneTimePurchase(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, boolean wasDownload){ //friend to Database
        return new OneTimePurchase(id,cityId,userId,purchaseDate, fullPrice,pricePayed,wasDownload);
    }

    public OneTimePurchase(Customer u,int cityId, Date purchaseDate, double fullPrice, double pricePayed) {
        super(u.getId(), cityId, purchaseDate, fullPrice, pricePayed);
        this.wasDownload = false;
    }

    public void saveToDatabase(){
        Database._saveOneTimePurchase(this);
    }

    public void deleteFromDatabase(){Database._deleteOneTimePurchase(this.getId());}

    public void reloadTempsFromDatabase(){
    	super.reloadTempsFromDatabase();
    }

    public boolean getWasDownload() {
        return wasDownload;
    }

    public void updateToWasDownload() {
        this.wasDownload = true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OneTimePurchase && ((OneTimePurchase) o).getId()==this.getId();
    }
}
