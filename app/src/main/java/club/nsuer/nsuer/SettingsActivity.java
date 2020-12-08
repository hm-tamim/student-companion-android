package club.nsuer.nsuer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private SessionManager session;
    private boolean showCgpa;
    private boolean showWeather, showAdvisingTools;
    private String fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String fromActivityIntent = getIntent().getStringExtra("FROM_ACTIVITY");

        if(fromActivityIntent != null || !fromActivityIntent.equals("")){

            fromActivity = fromActivityIntent;

        }


        session = new SessionManager(this);




        Switch cgpaSwitch = findViewById(R.id.cgpaSwitch);

        showCgpa = session.willShowCgpa();

        cgpaSwitch.setChecked(showCgpa);


        cgpaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    session.setShowCgpa(true);
                else
                    session.setShowCgpa(false);
            }
        });




        Switch weatherSwitch = findViewById(R.id.weatherCardSwitch);

        showWeather = session.willShowWeather();

        weatherSwitch.setChecked(showWeather);


        weatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    session.setShowWeather(true);
                else
                    session.setShowWeather(false);
            }
        });

        Switch advisingToolsSwitch = findViewById(R.id.advisingTools);

        showAdvisingTools = session.showAdvisingTools();

        advisingToolsSwitch.setChecked(showAdvisingTools);

        advisingToolsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    session.setAdvisingTools(true);
                else
                    session.setAdvisingTools(false);
            }
        });


        FloatingActionButton btn = findViewById(R.id.editProfileButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finishNow();

            }
        });






    }

    @Override
    public void onBackPressed(){

        finishNow();

    }


    private void finishNow(){

        Intent intent;

        if(fromActivity.equals("EDIT_PROFILE")) {
            intent = new Intent(SettingsActivity.this,
                    EditProfile.class);
            finish();
        } else {

            intent = new Intent(SettingsActivity.this,
                    MainActivity.class);
            startActivity(intent);

            finish();

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
