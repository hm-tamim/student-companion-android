package club.nsuer.nsuer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;


public class BloodBeDonor extends AppCompatActivity {


    private GridLayout bloodGroups;
    private SessionManager session;
    Context context;
    private int bloodGroup = -1;
    private EditText phoneInput;
    private EditText addressInput;
    private FloatingActionButton submitBtn;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_be_donor);

        context = this;

        session = new SessionManager(context);

        uid = session.getUid();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        try {
            bloodGroup = Integer.parseInt(session.getBloodGroup());
        } catch (Exception e) {


        }
        bloodGroups = findViewById(R.id.bloodGroups);
        phoneInput = findViewById(R.id.phone);
        addressInput = findViewById(R.id.address);
        submitBtn = findViewById(R.id.submitBtn);


        if (bloodGroup < 0)
            setTitle("Become a donor");
        else {
            setTitle("Edit Donor Profile");
            String buttonID = "b" + bloodGroup;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            TextView btnTag = bloodGroups.findViewById(resID);
            btnTag.setBackground(getResources().getDrawable(R.drawable.blood_group_selected));


        }


        String phoneNo = session.getPhone();

        if (phoneNo.equals("null"))
            phoneNo = "";

        phoneInput.setText(phoneNo);

        String addressString = session.getAddress();
        if (addressString.equals("null"))
            addressString = "";

        addressInput.setText(addressString);


        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (view instanceof TextView) {

                    int id = view.getId();

                    try {


                        selectBlood(id);


                    } catch (Exception e) {

                        Log.e("BloodRequest", e.toString());

                    }


                }
            }
        };


        for (int i = 0; i < 8; i++) {

            String buttonID = "b" + i;

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            TextView btnTag = bloodGroups.findViewById(resID);
            btnTag.setOnClickListener(btnclick);

        }


        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                makeBeDonor();

            }
        });


    }

    private void makeBeDonor() {


        final String phone = phoneInput.getText().toString();
        final String address = addressInput.getText().toString();

        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        } else if (bloodGroup < 0) {
            Toast.makeText(context, "Select blood group", Toast.LENGTH_SHORT).show();
            return;
        } else if (address.equals("")) {
            Toast.makeText(context, "Enter the area you live in.", Toast.LENGTH_SHORT).show();
            addressInput.setError("Required");
            return;
        }


        HashMap<String, String> parametters = new HashMap<String, String>();


        parametters.put("uid", uid);
        parametters.put("bgroup", String.valueOf(bloodGroup));
        parametters.put("phone", phone);
        parametters.put("address", address);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/blood-bank/bedonor.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                session.setBloodGroup(String.valueOf(bloodGroup));


                FirebaseMessaging.getInstance().subscribeToTopic("BLOOD." + bloodGroup);

                session.setPhone(phone);

                session.setAddress(address);

                Toast.makeText(context, "Information updated.", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }

    private void selectBlood(int id) {


        for (int i = 0; i < 8; i++) {

            String buttonID = "b" + i;

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            TextView btnTag = bloodGroups.findViewById(resID);

            if (resID != id) {

                btnTag.setBackground(getResources().getDrawable(R.drawable.blood_group));
            } else {


                btnTag.setBackground(getResources().getDrawable(R.drawable.blood_group_selected));

                bloodGroup = i;

            }

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        //Execute your code here


        setResult(Activity.RESULT_OK);
        finish();

    }

}
