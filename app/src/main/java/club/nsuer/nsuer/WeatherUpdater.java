package club.nsuer.nsuer;

import androidx.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class WeatherUpdater {

    private Context context;
    private View view;
    private static final String WEATHER_DATA = "WeatherData";
    private String todayTitle;
    private String todayDate;
    private String todayTemp;
    private String todayRealFeel;
    private String forecast = "No name defined";
    private String lastUpdate;
    private int todayIcons;


    WeatherUpdater(Context context, View view) {

        this.context = context;

        final Context finalContext = context;

        this.view = view;

        Date now = new Date();

        long MAX_DURATION = MILLISECONDS.convert(15, MINUTES);


        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA, MODE_PRIVATE);


        if (!prefs.contains("lastUpdate")) {

            if (Utils.isNetworkAvailable(context)) {

                try {
                    updateWeaterNow();
                } catch (Exception e) {
                    Log.e("Weather", e.toString());
                }
            }

        } else if (prefs.contains("lastUpdate")) {

            updateWeatherOnCard();


            long lupdate = prefs.getLong("lastUpdate", 0L);

            Date lastdate = new Date(lupdate);

            long lastupdates = lastdate.getTime();

            long duration = now.getTime() - lastupdates;

            if (duration >= MAX_DURATION) {

                if (Utils.isNetworkAvailable(context)) {

                    try {
                        updateWeaterNow();
                        // syncCourse(MainActivity.getInstance().getMemberID(), finalContext);
                    } catch (Exception e) {
                        Log.e("Weather", e.toString());
                    }


                }

            } else {

                updateWeatherOnCard();

            }

        }

        final ImageView button = view.findViewById(R.id.weatherReloadButton);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Utils.isNetworkAvailable(finalContext)) {
                    updateWeaterNow();
                    //  syncCourse(MainActivity.getInstance().getMemberID(), finalContext);
                } else {
                    Toast.makeText(finalContext, "Internet connection required.", Toast.LENGTH_SHORT).show();
                    button.setAnimation(null);
                }

            }

        });


    }


    private void syncCourse(String memID, final Context context) {


        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date1 = sdf.parse("2018-09-26");
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(c);
            if (cal1.before(cal2)) {
                return;
            }
        } catch (Exception e) {
        }


        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("memID", memID);


        JSONParser parser = new JSONParser("https://nsuer.club/apps/courses/get-all-courses.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                CoursesDatabase db = Room.databaseBuilder(context,
                        CoursesDatabase.class, "courses").allowMainThreadQueries().build();

                db.coursesDao().nukeTable();


                FacultiesDatabase dbFaculties = Room.databaseBuilder(context,
                        FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();

                dbFaculties.facultiesDao().nukeTable();


                try {
                    JSONArray obj = result.getJSONArray("dataArray");

                    String firstCourse = null;
                    String firstSection = null;


                    for (int i = 0; i < obj.length(); i++) {


                        JSONObject data = obj.getJSONObject(i);

                        // Add books

                        if (i > 0) {

                            if (data.has("books")) {

//                                int id = data.getInt("id");
//                                String course = data.getString("course");
//                                String books = data.getString("books");
//
//                                BooksEntity arrData = new BooksEntity();
//
//                                arrData.setCourse(course);
//                                arrData.setBooks(books);
//                                dbBooks.booksDao().insertAll(arrData);

                                continue;
                            }
                        }


                        // Add faculties

                        if (i > 0) {

                            if (data.has("initial")) {


                                int id = data.getInt("id");


                                String name = data.getString("name");
                                String rank = data.getString("rank");

                                String image = data.getString("image");

                                String initial = trim(data.getString("initial"));

                                String course = db.coursesDao().getCourseByFaculty(initial);

                                String section = db.coursesDao().getSectionByFaculty(trim(initial));

                                String email = data.getString("email");
                                String phone = data.getString("phone");
                                String ext = data.getString("ext");

                                String department = data.getString("dept");
                                String office = data.getString("office");
                                String url = data.getString("url");


                                FacultiesEntity arrData = new FacultiesEntity();

                                arrData.setName(name);
                                arrData.setRank(rank);
                                arrData.setImage(image);
                                arrData.setInitial(initial);
                                arrData.setCourse(course);
                                arrData.setSection(section);
                                arrData.setEmail(email);
                                arrData.setPhone(phone);
                                arrData.setExt(ext);
                                arrData.setDepartment(department);
                                arrData.setOffice(office);
                                arrData.setUrl(url);

                                dbFaculties.facultiesDao().insertAll(arrData);
                                continue;
                            }
                        }


                        int id = data.getInt("id");
                        String course = data.getString("course");
                        String section = data.getString("section");
                        String faculty = data.getString("faculty");


                        if (i == 0)
                            firstCourse = course;

                        if (i == 0)
                            firstSection = section;


                        String startTime = data.getString("startTime");
                        startTime = timeConverter(startTime, 24);

                        String endTime = data.getString("endTime");
                        endTime = timeConverter(endTime, 24);

                        String day = data.getString("day");
                        String room = data.getString("room");


                        CoursesEntity arrData = new CoursesEntity();

                        arrData.setCourse(course);
                        arrData.setFaculty(faculty);
                        arrData.setSection(section);
                        arrData.setStartTime(startTime);
                        arrData.setEndTime(endTime);
                        arrData.setRoom(room);
                        arrData.setDay(day);
                        db.coursesDao().insertAll(arrData);

                    }
                } catch (JSONException e) {

                    Log.e("JSON", e.toString());
                }

            }

            @Override
            public void onFailure() {


            }
        });
        parser.execute();


    }


    public String trim(String str) {
        int len = str.length();
        int st = 0;

        char[] val = str.toCharArray();

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return str.substring(st, len);
    }


    public String timeConverter(String time, int type) {

        try {
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            if (type == 24) {
                String t24 = date24Format.format(date12Format.parse(time));
                return t24;
            } else {
                String t12 = date12Format.format(date24Format.parse(time));
                return t12;
            }
        } catch (final ParseException e) {
            e.printStackTrace();

            return "nothing";
        }

    }


    private void updateWeaterNow() {

        final ImageView loader = view.findViewById(R.id.weatherReloadButton);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);

        loader.startAnimation(anim);

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("user", "nsuer_app");

        JSONParser parser = new JSONParser("https://nsuer.club/apps/weather/weather.json", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                try {
                    JSONArray obj = result.getJSONArray("current");


                    JSONObject data = obj.getJSONObject(0);

                    String title = data.getString("title");

                    String date = data.getString("time");

                    int todayIconz = data.getInt("icon");

                    String temp = data.getString("temp");

                    String feels = data.getString("realfeel");


                    String forecasts = data.getString("forecast");


                    SharedPreferences.Editor editor = context.getSharedPreferences(WEATHER_DATA, MODE_PRIVATE).edit();
                    editor.putString("todayTitle", title);
                    editor.putString("todayDate", date);


                    SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                    SimpleDateFormat myFormat = new SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH);

                    try {

                        String reformattedStr = myFormat.format(fromUser.parse(date));

                        editor.putString("todayDate", reformattedStr);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    editor.putString("todayRealFeel", feels);
                    editor.putString("todayTemp", temp);
                    editor.putInt("todayIcon", todayIconz);
                    editor.putString("forecast", forecasts);

                    Date noww = new Date();

                    long cTime = noww.getTime();

                    editor.putLong("lastUpdate", cTime);

                    editor.apply();

                    loader.setAnimation(null);
                    updateWeatherOnCard();


                } catch (JSONException e) {

                    Log.e("JSON", e.toString());

                }

            }

            @Override
            public void onFailure() {

            }
        });
        parser.execute();


    }


    private void updateWeatherOnCard() {


        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA, MODE_PRIVATE);


        String restoredText = prefs.getString("todayTitle", null);
        if (restoredText != null) {
            todayTitle = prefs.getString("todayTitle", "No name defined");
            todayDate = prefs.getString("todayDate", "No name defined");
            todayRealFeel = prefs.getString("todayRealFeel", "No name defined");
            todayTemp = prefs.getString("todayTemp", "No name defined");

            forecast = prefs.getString("forecast", "No name defined");

            todayIcons = prefs.getInt("todayIcon", 7);
        }


        TextView todayTitleView = view.findViewById(R.id.weatherTitle);
        TextView todayDateView = view.findViewById(R.id.weatherDate);
        TextView todayFeelView = view.findViewById(R.id.weatherFeels);
        TextView todayTempView = view.findViewById(R.id.weatherTemp);
        ImageView todayIcon = view.findViewById(R.id.weatherTodayIcon);

        todayIcon.setImageDrawable(context.getResources().getDrawable(getIconUrl(todayIcons)));
        todayTitleView.setText(todayTitle);
        todayDateView.setText(todayDate);
        todayFeelView.setText("Feels like " + todayRealFeel + "°");
        todayTempView.setText(todayTemp);


        // forecast

        try {

            JSONArray obj = new JSONArray(forecast);


            JSONObject data;
            String date;
            String high;
            String low;
            int icon;


            data = obj.getJSONObject(0);

            date = data.getString("date");
            high = data.getString("max");
            low = data.getString("min");
            icon = data.getInt("icon");

            TextView day1Day = view.findViewById(R.id.weatherDay1);
            ImageView day1Icon = view.findViewById(R.id.weatherDay1Icon);
            TextView day1HighLow = view.findViewById(R.id.weatherDay1HighLow);

            day1Day.setText(getDay(date));
            day1Icon.setImageDrawable(context.getResources().getDrawable(getIconUrl(icon)));
            day1HighLow.setText(low + "/" + high + "°");


            data = obj.getJSONObject(1);

            date = data.getString("date");
            high = data.getString("max");
            low = data.getString("min");
            icon = data.getInt("icon");

            TextView day2Day = view.findViewById(R.id.weatherDay2);
            ImageView day2Icon = view.findViewById(R.id.weatherDay2Icon);
            TextView day2HighLow = view.findViewById(R.id.weatherDay2HighLow);

            day2Day.setText(getDay(date));
            day2Icon.setImageDrawable(context.getResources().getDrawable(getIconUrl(icon)));
            day2HighLow.setText(low + "/" + high + "°");


            data = obj.getJSONObject(2);

            date = data.getString("date");
            high = data.getString("max");
            low = data.getString("min");
            icon = data.getInt("icon");

            TextView day3Day = view.findViewById(R.id.weatherDay3);
            ImageView day3Icon = view.findViewById(R.id.weatherDay3Icon);
            TextView day3HighLow = view.findViewById(R.id.weatherDay3HighLow);

            day3Day.setText(getDay(date));
            day3Icon.setImageDrawable(context.getResources().getDrawable(getIconUrl(icon)));
            day3HighLow.setText(low + "/" + high + "°");


            data = obj.getJSONObject(3);

            date = data.getString("date");
            high = data.getString("max");
            low = data.getString("min");
            icon = data.getInt("icon");

            TextView day4Day = view.findViewById(R.id.weatherDay4);
            ImageView day4Icon = view.findViewById(R.id.weatherDay4Icon);
            TextView day4HighLow = view.findViewById(R.id.weatherDay4HighLow);

            day4Day.setText(getDay(date));
            day4Icon.setImageDrawable(context.getResources().getDrawable(getIconUrl(icon)));
            day4HighLow.setText(low + "/" + high + "°");

        } catch (JSONException e) {

            Log.e("JSON Parser", "Error parsing data " + e.toString());

        }


    }


    private int getIconUrl(int n) {

        switch (n) {

            case 1:
                return R.drawable.weather_ic_sunny;
            case 2:
                return R.drawable.weather_ic_sunny;
            case 3:
                return R.drawable.weather_ic_sunny_intervals;
            case 4:
                return R.drawable.weather_ic_cloudy;
            case 5:
                return R.drawable.weather_ic_sunny;
            case 6:
                return R.drawable.ic_weather_ic_partly_cloudy;
            case 7:
                return R.drawable.weather_ic_cloudy;
            case 8:
                return R.drawable.weather_ic_overcast;
            case 11:
                return R.drawable.weather_ic_fog;
            case 12:
                return R.drawable.weather_ic_shower;
            case 13:
                return R.drawable.weather_ic_shower;
            case 14:
                return R.drawable.weather_ic_sunny_intervals;
            case 15:
                return R.drawable.weather_ic_thundershower;
            case 16:
                return R.drawable.weather_ic_thundershower;
            case 17:
                return R.drawable.weather_ic_sunny_intervals;
            case 18:
                return R.drawable.weather_ic_rain;
            case 19:
                return R.drawable.weather_ic_flurry;
            case 20:
                return R.drawable.weather_ic_cloudy;
            case 21:
                return R.drawable.weather_ic_sunny;
            case 22:
                return R.drawable.weather_ic_snow;
            case 23:
                return R.drawable.weather_ic_cloudy;
            case 24:
                return R.drawable.weather_ic_snowstorm;
            case 25:
                return R.drawable.weather_ic_sleet;
            case 26:
                return R.drawable.weather_ic_rain;
            case 29:
                return R.drawable.weather_ic_snow;
            case 30:
                return R.drawable.ic_weather_ic_hot;
            case 31:
                return R.drawable.weather_ic_cold;
            case 32:
                return R.drawable.weather_ic_windy;
            case 33:
                return R.drawable.weather_ic_sunny;
            case 34:
                return R.drawable.weather_ic_sunny;
            case 38:
                return R.drawable.weather_ic_cloudy;
            case 39:
                return R.drawable.weather_ic_rain;
            case 40:
                return R.drawable.weather_ic_rain;
            case 41:
                return R.drawable.weather_ic_thundershower;
            case 42:
                return R.drawable.weather_ic_thundershower;
            case 43:
                return R.drawable.weather_ic_cloudy;
            case 44:
                return R.drawable.weather_ic_drizzle;

            default:
                return R.drawable.weather_ic_sunny;

        }


    }


    private String getDay(String date) {

        String reformattedStr = null;

        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat myFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);


            reformattedStr = myFormat.format(fromUser.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reformattedStr;


    }


}
