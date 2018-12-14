package club.nsuer.nsuer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static android.content.Context.ALARM_SERVICE;

public class Utils {



    public static void syncSchedule(String memID, final Context context){


        final ScheduleDatabase scheduleDb = Room.databaseBuilder(context,
                ScheduleDatabase.class, "schedules").allowMainThreadQueries().build();


        String url = "https://nsuer.club/app/schedules/my-schedules.php";


        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("memID", memID);

        JSONParser parser = new JSONParser(url, "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {



                try {

                    JSONArray obj = result.getJSONArray("dataArray");


                    for (int j = 0; j < obj.length(); j++) {


                        JSONObject data = obj.getJSONObject(j);

                        int id = data.getInt("id");

                        String title = data.getString("s");
                        String type = data.getString("t");
                        String note = data.getString("en");
                        long date = data.getLong("d");
                        long reminderDate = data.getLong("rd");
                        int color = data.getInt("c");
                        int doRemindi = data.getInt("dr");


                        Calendar reminderCalendar = Calendar.getInstance();


                        boolean doRemind = false;

                        boolean isPassed = false;


                        if (doRemindi == 1)
                            doRemind = true;

                        long unixTime = System.currentTimeMillis() / 1000L;

                        if (unixTime > date)
                            isPassed = true;


                        long[] insertedIDlong = scheduleDb.scheduleDao().insertAll(new ScheduleEntity(id, title, type, note, date, reminderDate, color, doRemind));
                        int insertedID = (int) insertedIDlong[0];


                        if (doRemind){
                            reminderCalendar.setTimeInMillis(reminderDate*1000L);

                            String reminderText = title;

                            if(!type.equals(""))
                                reminderText += " - " + type;

                            Utils.setReminder(insertedID, reminderText, reminderCalendar, false, context);

                        }



                    }

                } catch (Exception e){


                }


            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }






    public static void syncReminders(final Context context){


        final ScheduleDatabase scheduleDb = Room.databaseBuilder(context,
                ScheduleDatabase.class, "schedules").allowMainThreadQueries().build();


        List<ScheduleEntity> list = scheduleDb.scheduleDao().getAll();





        for (int i=0; i < list.size(); i++) {

            int id = list.get(i).getId();
            String title = list.get(i).getTitle();
            String type = list.get(i).getType();
            String note = list.get(i).getExtraNote();
            long date = list.get(i).getDate();
            long reminderDate = list.get(i).getReminderDate();
            int color = list.get(i).getColor();
            boolean doRemind = list.get(i).isDoReminder();

            boolean isPassed = false;

            long unixTime = System.currentTimeMillis() / 1000L;

            if(unixTime > date)
                isPassed = true;

            if (doRemind && !isPassed) {
                Calendar reminderCalendar = Calendar.getInstance();
                reminderCalendar.setTimeInMillis(reminderDate * 1000L);

                String reminderText = title;
                if (!type.equals(""))
                    reminderText += " - " + type;
                Utils.setReminder(id, reminderText, reminderCalendar, false, context);
            }

        }





    }




    public static void setReminder(int idd, String reminderText, Calendar reminderCalendar, boolean showToast, Context context) {



        long unixTime = System.currentTimeMillis() / 1000L;

        long reminderTime = reminderCalendar.getTimeInMillis()/1000L;
        if(unixTime < reminderTime) {

            Intent intent = new Intent(context, ReminderBroadcast.class);

            intent.putExtra("text", reminderText);
            intent.putExtra("id", idd);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idd, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);

            String myFormat = "dd MMMM, yyyy 'at' hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            String reminderTextx = sdf.format(reminderCalendar.getTime());

            if (showToast)
                Toast.makeText(context, "Reminder is set to " + reminderTextx,Toast.LENGTH_LONG).show();

        }


    }



    public static String limitWords(int n, String str, boolean dots){

            String dot = "...";

            if (!dots)
                dot = "";

            return str.replaceAll("^((?:\\W*\\w+){" + n + "}).*$", "$1") + dot;
    }

    // The public static function which can be called from other classes
    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(
                    darkenColor(
                            ContextCompat.getColor(activity, color)));
        }

    }


    public static boolean doesContain(String str1, String str2){

        return Pattern.compile(Pattern.quote(str2), Pattern.CASE_INSENSITIVE).matcher(str1).find();

    }


    // Code to darken the color supplied (mostly color of toolbar)
    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    public static String getGpaGrade(double point){


        NumberFormat formatter = new DecimalFormat("#0.0");


        String p = formatter.format(point);

        point = Double.valueOf(p);

        String grade = "F";

            if (point >= 4.0)
                grade = "A";
            else if (point >= 3.7)
                grade = "A-";
            else if (point >= 3.3)
                grade = "B+";
            else if (point >= 3.0)
                grade = "B";
            else if (point >= 2.7)
                grade = "B-";
            else if (point >= 2.3)
                grade = "C+";
            else if (point >= 2.0)
                grade = "C";
            else if (point >= 1.7)
                grade = "C-";
            else if (point >= 1.3)
                grade = "D+";
            else if (point >= 1.0)
                grade = "D";
            else
                grade = "F";

            return grade;
        }


    public static void CustomTab(String url, Context context)
    {
        Uri uri = Uri.parse(url);

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set desired toolbar colors

        intentBuilder.setShowTitle(true);
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        // add start and exit animations if you want(optional)
    /*intentBuilder.setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
            android.R.anim.slide_out_right);*/

        CustomTabsIntent customTabsIntent = intentBuilder.build();

        customTabsIntent.launchUrl(context, uri);
    }



    public static String getTimeAgo(int timestampi) {

        long timestamp = (long) timestampi;

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(timestamp * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDIM = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (timeDIM == 0) {
            timeAgo = "Just now";
        } else if (timeDIM == 1) {
            return "1 minute ago";
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = timeDIM + " minutes ago";
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "1 hour ago";
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = (Math.round(timeDIM / 60)) + " hours ago";
        } else if (timeDIM >= 1440) {

                Date tdate = new Date(timestamp * 1000);

                SimpleDateFormat jdf = new SimpleDateFormat("MMM dd 'at' h:mm a", Locale.ENGLISH);
                jdf.setTimeZone(TimeZone.getTimeZone("GMT-6"));

                timeAgo = jdf.format(tdate);

        }

        return timeAgo;
    }


    public static String getTimeAgoShop(long timestamp) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(timestamp * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDIM = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (timeDIM == 0) {
            timeAgo = "Just now";
        } else if (timeDIM == 1) {
            return "1 min ago";
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = timeDIM + " mins ago";
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "1 hour ago";
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = (Math.round(timeDIM / 60)) + " hours ago";
        } else if (timeDIM >= 1440) {

            Date tdate = new Date(timestamp * 1000);

            SimpleDateFormat jdf = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-6"));

            timeAgo = jdf.format(tdate);

        }

        return timeAgo;
    }



    public static String getTimeAgoChat(long timestamp) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(timestamp * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDIM = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (timeDIM == 0) {
            timeAgo = "Just now";
        } else if (timeDIM == 1) {
            return "1 min ago";
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = timeDIM + " mins ago";
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "1 hour ago";
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = (Math.round(timeDIM / 60)) + " hours ago";
        } else if (timeDIM >= 1440) {

            Date tdate = new Date(timestamp * 1000);

            SimpleDateFormat jdf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.ENGLISH);
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-6"));

            timeAgo = jdf.format(tdate);

        }

        return timeAgo;
    }




    public static String getHumanTime(long timestamp) {



            Date tdate = new Date(timestamp * 1000);

            SimpleDateFormat jdf = new SimpleDateFormat("MMM dd 'at' h:mm a", Locale.ENGLISH);


            String timeAgo = jdf.format(tdate);



        return timeAgo;
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



}

