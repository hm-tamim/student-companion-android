package club.nsuer.nsuer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Utils {



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

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }



}

