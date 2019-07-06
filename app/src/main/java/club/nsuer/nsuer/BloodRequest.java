package club.nsuer.nsuer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BloodRequest extends AppCompatActivity {

    private GridLayout bloodGroups;
    private Context context;
    private int bloodGroup = -1;
    private int bags = 1;
    private long whenNeeded;
    private String phone;
    private String address;
    private Calendar myCalendar;
    private Spinner bagsInput;
    private EditText datePicker;
    private EditText timePicker;
    private EditText phoneInput;
    private EditText addressInput;
    private EditText extraNote;
    private FloatingActionButton submitBtn;
    private SessionManager session;
    private String isEdit = "false";
    private String msgID = "544545";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);


        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        myCalendar = Calendar.getInstance();

        session = new SessionManager(BloodRequest.this);
        bloodGroups = findViewById(R.id.bloodGroups);


        submitBtn = findViewById(R.id.submitBtn);

        datePicker = (EditText) findViewById(R.id.datePicker);
        timePicker = (EditText) findViewById(R.id.timePicker);

        phoneInput = findViewById(R.id.phone);
        bagsInput = findViewById(R.id.bags);
        addressInput = findViewById(R.id.address);
        extraNote = findViewById(R.id.note);

        datePicker = (EditText) findViewById(R.id.datePicker);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDatePicker();

            }

        };

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        timePicker = (EditText) findViewById(R.id.timePicker);

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);


            }
        };

        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new TimePickerDialog(context, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),false).show();
                updateTimePicker();
            }
        });


        Intent intent = getIntent();


        if(!intent.hasExtra("bgroup"))
            setTitle("Request Blood");
        else {
            setTitle("Edit Information");


            isEdit = "true";

            bloodGroup = intent.getIntExtra("bgroup",-1);


            long datez = intent.getLongExtra("date",0);

            msgID = String.valueOf(intent.getIntExtra("msgID",45456));

            myCalendar.setTimeInMillis(datez*1000L);


            bagsInput.setSelection(intent.getIntExtra("bags",1)-1);

            String buttonID = "b" + bloodGroup;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            TextView btnTag = bloodGroups.findViewById(resID);

            btnTag.setBackground(getResources().getDrawable(R.drawable.blood_group_selected));

            String phoneNo = session.getPhone();

            if (phoneNo.equals("null"))
                phoneNo = "";

            phoneInput.setText(intent.getStringExtra("phone"));
            addressInput.setText(intent.getStringExtra("address"));
            extraNote.setText(intent.getStringExtra("note"));

            updateDatePicker();
            updateTimePicker();

        }



        View.OnClickListener btnclick = new View.OnClickListener(){
            @Override
            public void onClick(View view){


                if (view instanceof TextView){

                    int id = view.getId();

                    try {


                        selectBlood(id);


                    } catch (Exception e){

                        Log.e("BloodRequest", e.toString());

                    }



                }
            }
        };


        for(int i = 0; i < 8; i++){

            String buttonID = "b" + i;

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            TextView btnTag = bloodGroups.findViewById(resID);
            btnTag.setOnClickListener(btnclick);

        }



        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                        .setMessage("Are you sure everything is correct?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                requestBlood();

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });


    }

    public void requestBlood(){


        String bags = String.valueOf(bagsInput.getSelectedItemPosition() + 1);

        String phone = phoneInput.getText().toString();
        String address = addressInput.getText().toString();
        String note = extraNote.getText().toString();
        String time = timePicker.getText().toString();
        String date = datePicker.getText().toString();

        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        } else if (bloodGroup < 0){
            Toast.makeText(context,"Select blood group",Toast.LENGTH_SHORT).show();
            return;
        }else if (date.equals("")){
            Toast.makeText(context,"Enter a date",Toast.LENGTH_SHORT).show();
            datePicker.setError("Required");
            return;
        }else if (time.equals("")){
            Toast.makeText(context,"Set a time",Toast.LENGTH_SHORT).show();
            timePicker.setError("Required");
            return;
        }else if (phone.equals("")){
            Toast.makeText(context,"Enter phone number",Toast.LENGTH_SHORT).show();
            phoneInput.setError("Required");
            return;
        }else if (address.equals("")){
            Toast.makeText(context,"Enter address(including hospital)",Toast.LENGTH_SHORT).show();
            addressInput.setError("Required");
            return;
        }

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("uid", session.getUid());
        parametters.put("bgroup",String.valueOf(bloodGroup));
        long timez = myCalendar.getTimeInMillis()/1000L;
        parametters.put("bags",String.valueOf(bags));
        parametters.put("whenNeeded",String.valueOf(timez));
        parametters.put("phone",phone);
        parametters.put("address",address);
        parametters.put("note",note);
        parametters.put("isEdit",isEdit);
        parametters.put("msgID",msgID);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/blood-bank/request-blood.php", "GET", parametters);

        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                Toast.makeText(context,"Blood request submitted", Toast.LENGTH_SHORT).show();


                setResult(Activity.RESULT_OK);
                finish();

            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }


    private void updateDatePicker() {
        String myFormat = "dd MMMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH); //like "HH:mm" or just "mm", whatever you want

        timePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void selectBlood(int id){


        for(int i = 0; i < 8; i++){

            String buttonID = "b" + i;

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            TextView btnTag = bloodGroups.findViewById(resID);

            if(resID != id){

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
