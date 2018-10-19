package club.nsuer.nsuer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.support.v4.app.DialogFragment;



public class AddSchedule extends AppCompatActivity {

    private Calendar myCalendar;
    private Calendar reminderCalendar;

    private EditText datePicker;
    private EditText timePicker;



    private EditText reminderDatePicker;
    private EditText reminderTimePicker;

    private EditText titlePicker;
    private EditText typePicker;
    private EditText extraNote;
    private EditText reminderPicker;
    private Context context;

    private LinearLayout colorPickLayout;
    private CardView colorCard;
    private FloatingActionButton addButton;
    private FloatingActionButton deleteButton;
    private Switch reminderSwitch;

    private int defaultColor = -11566660;
    private int quizColor =  -14246231;
    private int midColor =  -49023;
    private int examColor = -1695465;

    private int selectedColor = -11566660;

    private SpectrumDialog.Builder colorPicker;

    private ScheduleDatabase db;

    private int id;
    private boolean editPage = false;

    private LinearLayout reminderHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_add_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        db = Room.databaseBuilder(getApplicationContext(),
                ScheduleDatabase.class, "schedules").allowMainThreadQueries().build();




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

        final ArrayList<String> quickTime = new ArrayList<String>();

        quickTime.add("Remind me before 30 minutes");
        quickTime.add("Remind me before 1 hour");
        quickTime.add("Remind me before 2 hours");
        quickTime.add("Remind me at night before 1 day");
        quickTime.add("Remind me at night before 2 days");
        quickTime.add("Remind me at night before 3 days");





        myCalendar = Calendar.getInstance();


        reminderCalendar = Calendar.getInstance();

        titlePicker = (EditText) findViewById(R.id.titlePicker);
        typePicker = (EditText) findViewById(R.id.typePicker);



        reminderDatePicker = (EditText) findViewById(R.id.reminderDate);
        reminderTimePicker = (EditText) findViewById(R.id.reminderTime);


        extraNote = findViewById(R.id.note);
        reminderPicker = findViewById(R.id.reminderPicker);
        reminderSwitch = findViewById(R.id.setReminder);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);

        reminderHolder = findViewById(R.id.reminderHolder);

        reminderHolder.setVisibility(View.GONE);

        colorPickLayout = (LinearLayout) findViewById(R.id.colorPickLayout);
        colorCard = (CardView) findViewById(R.id.colorCard);




        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    reminderHolder.setVisibility(View.VISIBLE);
                else
                    reminderHolder.setVisibility(View.GONE);
            }
        });







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

                                    selectedColor = color;
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





        // reminder pickers

        final DatePickerDialog.OnDateSetListener dateReminder = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                reminderCalendar.set(Calendar.YEAR, year);
                reminderCalendar.set(Calendar.MONTH, monthOfYear);
                reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReminderDate();
            }

        };

        reminderDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSchedule.this, dateReminder, reminderCalendar
                        .get(Calendar.YEAR), reminderCalendar.get(Calendar.MONTH),
                        reminderCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final TimePickerDialog.OnTimeSetListener timeReminder = new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderCalendar.set(Calendar.MINUTE, minute);

                updateReminderTime();

            }
        };

        reminderTimePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddSchedule.this, timeReminder, reminderCalendar
                        .get(Calendar.HOUR_OF_DAY), reminderCalendar.get(Calendar.MINUTE),false).show();
            }
        });





        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setMessage("Are you sure you want to delete this schedule?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            db.scheduleDao().deleteById(id);

                            cancelReminder(id);

                            onBackPressed();



                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
                }
        });




        reminderPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddSchedule.this, R.style.AlertDialogCustom);
                builder.setTitle("Select Quick Reminder Time");

                builder.setPositiveButton("Close", null);

                builder.setItems(quickTime.toArray(new String[quickTime.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        long ScheduleTime = myCalendar.getTimeInMillis();
                        long reminderTime = ScheduleTime;


                        Calendar temp;
                        switch (which){

                            case 0:
                                reminderTime = ScheduleTime-(1800*1000L);
                                break;
                            case 1:
                                reminderTime = ScheduleTime-(3600*1000L);
                                break;
                            case 2:
                                reminderTime = ScheduleTime-(7200*1000L);
                                break;
                            case 3:
                                temp = Calendar.getInstance();
                                temp.setTimeInMillis(ScheduleTime);
                                temp.add(Calendar.DATE, -1);
                                temp.set(Calendar.HOUR_OF_DAY, 22);
                                temp.set(Calendar.MINUTE, 0);

                                reminderTime = temp.getTimeInMillis();
                                break;
                            case 4:
                                temp = Calendar.getInstance();
                                temp.setTimeInMillis(ScheduleTime);
                                temp.add(Calendar.DATE, -2);
                                temp.set(Calendar.HOUR_OF_DAY, 22);
                                temp.set(Calendar.MINUTE, 0);

                                reminderTime = temp.getTimeInMillis();
                                break;
                            case 6:
                                temp = Calendar.getInstance();
                                temp.setTimeInMillis(ScheduleTime);
                                temp.add(Calendar.DATE, -3);
                                temp.set(Calendar.HOUR_OF_DAY, 22);
                                temp.set(Calendar.MINUTE, 0);

                                reminderTime = temp.getTimeInMillis();
                                break;


                        }


                        reminderCalendar.setTimeInMillis(reminderTime);

                        updateReminderDate();
                        updateReminderTime();



                        reminderPicker.setText(quickTime.get(which));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });








        Intent intent = getIntent();

        if(intent.hasExtra("id")) {

            editPage = true;

            deleteButton.setVisibility(View.VISIBLE);

            id = intent.getIntExtra("id", 0);

            actionBar.setTitle("Edit Schedule");

            ScheduleEntity item = db.scheduleDao().findById(id);

            String title = item.getTitle();
            String type = item.getType();
            String note = item.getExtraNote();
            long dateExtra = item.getDate();
            long reminderDate = item.getReminderDate();

            myCalendar.setTimeInMillis(dateExtra*1000L);
            reminderCalendar.setTimeInMillis(reminderDate*1000L);

            updateDatePicker();
            updateTimePicker();

            updateReminderDate();
            updateReminderTime();


            int color = item.getColor();
            boolean doRemind =  item.isDoReminder();

            titlePicker.setText(title);
            typePicker.setText(type);
            extraNote.setText(note);
            reminderSwitch.setChecked(doRemind);


            colorPicker.setSelectedColor(color);
            colorCard.setCardBackgroundColor(color);


            if(doRemind)
                reminderHolder.setVisibility(View.VISIBLE);
            else
                reminderHolder.setVisibility(View.GONE);


        }




        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = titlePicker.getText().toString();
                String type = typePicker.getText().toString();
                long date = myCalendar.getTimeInMillis()/1000L;

                long reminderDate =  reminderCalendar.getTimeInMillis()/1000L;

                String note = extraNote.getText().toString();
                int color = selectedColor;
                boolean doRemind = false;

                if(reminderSwitch.isChecked())
                    doRemind = true;


                if(title.equals("")) {
                    Toast.makeText(context,"Enter subject", Toast.LENGTH_SHORT).show();
                    return;
                }

                long[] insertedIDlong;
                int insertedID = id;


                if(!editPage) {
                    insertedIDlong = db.scheduleDao().insertAll(new ScheduleEntity(title, type, note, date, reminderDate, color, doRemind));
                    insertedID = (int) insertedIDlong[0];
                }else {

                    db.scheduleDao().deleteById(id);
                    cancelReminder(id);

                    insertedIDlong = db.scheduleDao().insertAll(new ScheduleEntity(title, type, note, date, reminderDate, color, doRemind));
                    insertedID = (int) insertedIDlong[0];
                    id = insertedID;
                }


                if(reminderSwitch.isChecked()){

                    String reminderText = title;


                    if(!type.equals(""))
                        reminderText += " - " + type;

                    setReminder(insertedID,reminderText);
                } else {
                    cancelReminder(insertedID);
                }

                onBackPressed();


            }
        });







    }




    public void setReminder(int idd, String reminderText) {
        int timeInSec = 10;

        Intent intent = new Intent(this, ReminderBroadcast.class);

        intent.putExtra("text",reminderText);
        intent.putExtra("id",idd);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), idd, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);

        String myFormat = "dd MMMM, yyyy 'at' hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String reminderTextx = sdf.format(reminderCalendar.getTime());

        // Toast.makeText(this, "Reminder is set to " + reminderTextx,Toast.LENGTH_LONG).show();
    }

    public void cancelReminder(int idd){

        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent.getBroadcast(this.getApplicationContext(), idd, intent,
                PendingIntent.FLAG_UPDATE_CURRENT).cancel();

    }

    private void updateColorCard(String type){

        if(Utils.doesContain(type, "quiz")){

            colorPicker.setSelectedColor(quizColor);
            colorCard.setCardBackgroundColor(quizColor);
            selectedColor = quizColor;
        } else if(Utils.doesContain(type, "mid")){

            colorPicker.setSelectedColor(midColor);
            colorCard.setCardBackgroundColor(midColor);
            selectedColor = midColor;

        } else if(Utils.doesContain(type, "exam")){

            colorPicker.setSelectedColor(examColor);
            colorCard.setCardBackgroundColor(examColor);
            selectedColor = examColor;

        } else{

            colorPicker.setSelectedColor(defaultColor);
            colorCard.setCardBackgroundColor(defaultColor);
            selectedColor = defaultColor;
        }



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


    private void updateReminderDate() {
        String myFormat = "dd MMMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        reminderDatePicker.setText(sdf.format(reminderCalendar.getTime()));
    }

    private void updateReminderTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH); //like "HH:mm" or just "mm", whatever you want

        reminderTimePicker.setText(sdf.format(reminderCalendar.getTime()));
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
