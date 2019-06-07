import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class DownloadVersion
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
}
