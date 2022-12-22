package database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

    public static java.sql.Date TZStrToSQLDate(String s) {
        if(s == null){
            return null;
        }

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
            java.util.Date utilDate = dateFormat.parse(s.replace("Z", "+0000"));

            return new java.sql.Date(utilDate.getTime());
        }catch (Exception e){
            return null;
        }


    }
}
