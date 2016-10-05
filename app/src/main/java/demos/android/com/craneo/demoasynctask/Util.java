package demos.android.com.craneo.demoasynctask;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by crane on 10/4/2016.
 */
public class Util {

    public static String getReadableData(long time){
        SimpleDateFormat shortenderDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenderDateFormat.format(time);
    }

    private static String formatHighLow(double high, double low){
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh+" / "+roundedLow;
        return highLowStr;
    }

    public static String[] getWeatherDataFromJson(String weatherJsonStr, int numDays)
            throws JSONException{
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject weatherJson = new JSONObject(weatherJsonStr);
        JSONArray weatherArray = weatherJson.getJSONArray(OWM_LIST);

        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();

        String[] resultStr = new String[numDays];
        for(int i=0; i<weatherArray.length(); i++){
            String day;
            String description;
            String highAndLow;

            JSONObject dayWeather = weatherArray.getJSONObject(i);
            long dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableData(dateTime);

            JSONObject weatherObject = dayWeather.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayWeather.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);
            highAndLow = formatHighLow(high, low);

            resultStr[i] = day +" - "+description+" - "+highAndLow;
        }
        return resultStr;
    }

}
