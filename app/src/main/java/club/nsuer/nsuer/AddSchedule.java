package club.nsuer.nsuer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.support.v4.app.DialogFragment;



public class AddSchedule extends AppCompatActivity {

    private Calendar myCalendar;
    private EditText datePicker;
    private EditText timePicker;

    private EditText titlePicker;
    private EditText typePicker;
    private Context context;

    private LinearLayout colorPickLayout;
    private CardView colorCard;

    private int defaultColor = -11566660;
    private int quizColor =  -14246231;
    private int midColor =  -49023;
    private int examColor = -1695465;

    private SpectrumDialog.Builder colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_add_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final ArrayList<String> courses = new ArrayList<String>();
        courses.add("CSE231");
        courses.add("MAT120");
        courses.add("EEE141");

        final ArrayList<String> types = new ArrayList<String>();
        types.add("Quiz");
        types.add("Mid");
        types.add("Final exam");
        types.add("Assignment");
        types.add("Project");


        myCalendar = Calendar.getInstance();

        titlePicker = (EditText) findViewById(R.id.titlePicker);
        typePicker = (EditText) findViewById(R.id.typePicker);
        colorPickLayout = (LinearLayout) findViewById(R.id.colorPickLayout);
        colorCard = (CardView) findViewById(R.id.colorCard);

        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        colorPicker = new SpectrumDialog.Builder(context, R.style.AlertDialogTheme);

        colorCard.setCardBackgroundColor(defaultColor);

        colorPicker.setColors(R.array.color_palette);
        colorPicker.setDismissOnColorSelected(true);
        colorPicker.setSelectedColor(defaultColor);
        colorPicker.setOutlineWidth(0);
        colorPicker.setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                if (positiveResult) {

                                    //selectedColor = color;
                                    colorPicker.setSelectedColor(color);

                                    colorCard.setCardBackgroundColor(color);

                                    Log.d("color", color+" ");

                                    Toast.makeText(AddSchedule.this, "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Dialog cancelled", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

        colorPickLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                colorPicker.build().show(fm,"yo");
            }
        });



        titlePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddSchedule.this, R.style.AlertDialogCustom);
                builder.setTitle("Choose a subject");

                builder.setPositiveButton("Add Manually",  new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddSchedule.this, R.style.AlertDialogTheme);
                        alertDialog.setTitle("Add Subject Manually");

                        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_edit_text, null);
                        alertDialog.setView(dialogLayout);
                        final EditText input = dialogLayout.findViewById(R.id.alertEditText);
                        input.setText(titlePicker.getText().toString());
                        alertDialog.setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        titlePicker.setText(input.getText().toString());
                                    }
                                });

                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();
                    }
                });

                builder.setItems(courses.toArray(new String[courses.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titlePicker.setText(courses.get(which));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        typePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddSchedule.this, R.style.AlertDialogCustom);
                builder.setTitle("Choose a type");
                builder.setPositiveButton("Add Manually", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddSchedule.this, R.style.AlertDialogTheme);
                        alertDialog.setTitle("Add Type Manually");
                        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_edit_text, null);
                        alertDialog.setView(dialogLayout);
                        final EditText input = dialogLayout.findViewById(R.id.alertEditText);
                        input.setText(typePicker.getText().toString());

                        alertDialog.setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        typePicker.setText(input.getText().toString());
                                        String type = input.getText().toString();
                                        updateColorCard(type);
                                    }
                                });

                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                    }
                });
                builder.setNegativeButton("Leave Blank", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        typePicker.setText("");
                    }
                 });

                builder.setItems(types.toArray(new String[types.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type = types.get(which);
                        updateColorCard(type);
                        typePicker.setText(types.get(which));

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        datePicker = (EditText) findViewById(R.id.datePicker);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDatePicker();
            }

        };

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSchedule.this, date, myCalendar
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

               updateTimePicker();

            }
        };

        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddSchedule.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),false).show();
            }
        });

    }

    private void updateColorCard(String type){

        if(Utils.doesContain(type, "quiz")){

            colorPicker.setSelectedColor(quizColor);
            colorCard.setCardBackgroundColor(quizColor);
        } else if(Utils.doesContain(type, "mid")){

            colorPicker.setSelectedColor(midColor);
            colorCard.setCardBackgroundColor(midColor);
        } else if(Utils.doesContain(type, "exam")){

            colorPicker.setSelectedColor(examColor);
            colorCard.setCardBackgroundColor(examColor);
        } else{

            colorPicker.setSelectedColor(defaultColor);
            colorCard.setCardBackgroundColor(defaultColor);
        }



    }


    private void updateDatePicker() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH); //like "HH:mm" or just "mm", whatever you want

        timePicker.setText(sdf.format(myCalendar.getTime()));
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
