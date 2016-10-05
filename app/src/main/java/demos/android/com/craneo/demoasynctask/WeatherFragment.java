package demos.android.com.craneo.demoasynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by crane on 10/4/2016.
 */
public class WeatherFragment extends Fragment{

    private static ArrayAdapter<String> weatherAdapter = null;

    public WeatherFragment(){}

    public void execute(String string){
        new FetchWeatherTask().execute(string);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //Nothing to do here
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        String[] weatherArray = {"Empty...."};

        List<String> weatherList = new ArrayList<>(Arrays.asList(weatherArray));

        weatherAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.item_weather, R.id.item_weather, weatherList);

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        ListView weatherView = (ListView) rootView.findViewById(R.id.weather_list);
        weatherView.setAdapter(weatherAdapter);


        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String weatherJsonStr = null;
            String postalCode = strings[0];
            String units = "metric";
            int days = 15;

            try{
                String apiId = "7cfd87d1a300a30a9c9645115dace16d";
                Uri.Builder builder  = new Uri.Builder();

                String uri = builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q",postalCode)
                        .appendQueryParameter("mode", "json")
                        .appendQueryParameter("unit", units)
                        .appendQueryParameter("cnt", String.valueOf(days))
                        .appendQueryParameter("APPID", apiId)
                        .build().toString();

                urlConnection = (HttpURLConnection)new URL(uri).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine()) != null){
                    buffer.append(line+"\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                weatherJsonStr = buffer.toString();
            }catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            String[] weatherArray = null;

            try{
                weatherArray = Util.getWeatherDataFromJson(weatherJsonStr, days);
            }catch(JSONException jsone){
                Log.e(LOG_TAG, jsone.getMessage(), jsone);
                jsone.printStackTrace();
            }

            return weatherArray;
        }

        @Override
        public void onPostExecute(String[] result){
            if(result != null){
                weatherAdapter.clear();
                weatherAdapter.addAll(result);
            }

        }

    }
}
