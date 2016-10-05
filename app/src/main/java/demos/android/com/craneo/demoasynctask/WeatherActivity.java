package demos.android.com.craneo.demoasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        buttonRefresh = (Button) findViewById(R.id.refres);
        buttonRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refres:
                String postalCode= "30309";
                WeatherFragment wf = new WeatherFragment();
                wf.execute(postalCode);
                break;
            default:
                break;

        }
    }
}
