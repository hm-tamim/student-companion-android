package club.nsuer.nsuer;


import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UserProfile extends Fragment {

    private String name = "Username";
    private String email = "email@domain.com";
    private String gender = "gender";
    private String uid = "uid";
    private String memberID = "0";
    private String picture = "0";

    private double cgpa = 0.0;
    private int dept = 0;
    private int credit = 0;
    private int semester = 0;

    private ImageView dp;


    public UserProfile() {


    }


    private CoursesDatabase db;
    private View v;


    private TabLayout tabLayout;


    private ViewPager viewPager;

    private MainActivity main;

    private Context context;


    private NsuNoticesTabAdapter viewPagerAdapter;

    private MenuItem item;


    public String timeConverter(String time, int type) {

        try {
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.US);

            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm", Locale.US);

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


        main = (MainActivity) getActivity();

        context = getContext();


        this.name = main.getName();
        this.email = main.getEmail();
        this.gender = main.getGender();
        this.uid = main.getUid();
        this.memberID = main.getMemberID();
        this.picture = main.getPicture();

        try {
            this.cgpa = Double.parseDouble(main.getCgpa());
            this.credit = Integer.parseInt(main.getCredit());
        } catch (Exception e) {

            Log.e("cgpa", e.toString());
        }

        this.semester = main.getSemesterInt();
        this.dept = main.getDeptInt();


    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.facebook_button, menu);


        //item = menu.findItem(R.id.editButton);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editButton:

                Intent intent = new Intent(context, EditProfile.class);

                startActivity(intent);
                return true;
            case R.id.navFacbookButton:

                try {

                    Intent intent1;
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1972692516364422"));

                    startActivity(intent1);

                } catch (Exception e) {

                    Intent intent1;
                    intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/nsuerApp"));
                    startActivity(intent1);
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_profile, container, false);


        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("NSUer");


        db = Room.databaseBuilder(context,
                CoursesDatabase.class, "courses").allowMainThreadQueries().build();


        dp = (ImageView) v.findViewById(R.id.userProfilePicture);


        final String imgDir = context.getFilesDir().getPath() + File.separator + "images" + File.separator + "tamim.jpg";

        File dpp = new File(imgDir);


        if (!picture.equals("0")) {

            if (!dpp.exists()) {


                Picasso.get()
                        .load("https://nsuer.club/images/profile_picture/" + picture)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(dp);
            } else {
                Picasso.get()
                        .load(dpp)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(dp);
            }
        }


        dp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, EditProfile.class);

                startActivity(intent);

                main.finish();
            }
        });


        final ArcProgressz arc = (ArcProgressz) v.findViewById(R.id.arc_cgpa);

        TextView rdsBtn = v.findViewById(R.id.editProfileButton);


        rdsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Utils.CustomTab("https://rds3.northsouth.edu/index.php/welcome/enter_login",context);


                Intent intent = new Intent(context,
                        EditProfile.class);
                startActivity(intent);
                main.finish();


            }
        });


        final TextView showHideCgpa = v.findViewById(R.id.nsuRdsBtn);


        showHideCgpa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Utils.CustomTab("https://rds3.northsouth.edu/index.php/welcome/enter_login", context);


            }
        });


        TextView userDept = v.findViewById(R.id.userProfileDept);
        String[] deptArray = getResources().getStringArray(R.array.deptShort);

        userDept.setText(deptArray[dept]);


        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(main.findViewById(R.id.mainBar), "elevation", 0));
        main.findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);


//        String upperStringName = name.substring(0,1).toUpperCase() + name.substring(1);

        ((TextView) v.findViewById(R.id.userName)).setText(name);


//
//
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//
//
//            }
//        }, 100);
//            }


        arc.setMax(4);


        float cgpaf;


        if (main.willShowCgpa()) {

            cgpaf = (float) cgpa;
            arc.setProgress(cgpaf);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(arc, "progress", 0.0f, cgpaf);
            objectAnimator.setDuration(1500);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.start();

        } else {

            arc.setProgress(0f);

        }


        ArcProgress arc_credit = (ArcProgress) v.findViewById(R.id.arc_credits);

        arc_credit.setMax(140);


        arc_credit.setProgress(credit);

        ObjectAnimator objectAnimators = ObjectAnimator.ofInt(arc_credit, "progress", 0, credit);
        objectAnimators.setDuration(1500);
        objectAnimators.setInterpolator(new DecelerateInterpolator());
        objectAnimators.start();


        ArcProgress arc_semester = (ArcProgress) v.findViewById(R.id.arc_semester);

        arc_semester.setMax(15);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(arc_semester, "progress", semester + 1);
        objectAnimator1.setDuration(1200);
        objectAnimator1.setInterpolator(new DecelerateInterpolator());
        objectAnimator1.start();

        arc_semester.setProgress(5);


        FirebaseMessaging.getInstance().subscribeToTopic("nsuer")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to notification";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to notification failed";
                        }
                        Log.d("FCMtopic", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        String userID = "USER." + memberID;

        FirebaseMessaging.getInstance().subscribeToTopic(userID);


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        //DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMMM", Locale.ENGLISH);

        String todayAsString = dateFormat.format(today);
        String tomorrowAsString = dateFormat.format(tomorrow);


        String todayDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(today.getTime());
        String tomorrowDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(tomorrow.getTime());


        String todayFirstLetter = todayDay.substring(0, 1);
        String tomorrowFirstLetter = tomorrowDay.substring(0, 1);

        if (todayDay.contains("Thursday"))
            todayFirstLetter = "R";
        if (todayDay.contains("Saturday"))
            todayFirstLetter = "A";

        if (tomorrowDay.contains("Thursday"))
            tomorrowFirstLetter = "R";
        if (tomorrowDay.contains("Saturday"))
            tomorrowFirstLetter = "A";


        ((TextView) v.findViewById(R.id.todayDate)).setText(todayAsString);
        ((TextView) v.findViewById(R.id.tomorrowDate)).setText(tomorrowAsString);


        ArrayList<Item> itemListToday = new ArrayList<Item>();

        CoursesAdapter itemArrayAdapterToday = new CoursesAdapter(R.layout.list_item, itemListToday);

        RecyclerView recyclerViewToday = (RecyclerView) v.findViewById(R.id.todayRecyclerView);
        recyclerViewToday.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewToday.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Item> itemListTomorrow = new ArrayList<Item>();

        CoursesAdapter itemArrayAdapterTomorrow = new CoursesAdapter(R.layout.list_item, itemListTomorrow);
        RecyclerView recyclerViewTomorrow = (RecyclerView) v.findViewById(R.id.tomorrowRecyclerView);
        recyclerViewTomorrow.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewTomorrow.setItemAnimator(new DefaultItemAnimator());


        //SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM coursesEntity ORDER BY strftime(\"%H:%M\",startTime) desc");
        // List<CoursesEntity> list = db.coursesDao().getUserViaQuery(query);


        List<CoursesEntity> list = db.coursesDao().getAllByTime();


        int todayCount = 0;
        int tomorrowCount = 0;

        for (int i = 0; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String section = list.get(i).getSection();

            String courseSection = course + "." + section;

            FirebaseMessaging.getInstance().subscribeToTopic(courseSection);


            String startTime24 = list.get(i).getStartTime();
            String startTime = timeConverter(startTime24, 12);


            String endTime24 = list.get(i).getEndTime();
            String endTime = timeConverter(endTime24, 12);

            String day = list.get(i).getDay();
            String faculty = list.get(i).getFaculty();
            String room = list.get(i).getRoom();

            if (day.contains(todayFirstLetter)) {
                itemListToday.add(new Item(course, startTime24, endTime24, room, "today"));
                todayCount++;
            }

            if (day.contains(tomorrowFirstLetter)) {
                itemListTomorrow.add(new Item(course, startTime24, endTime24, room, "tomorrow"));
                tomorrowCount++;
            }

        }

        recyclerViewToday.setAdapter(itemArrayAdapterToday);
        recyclerViewTomorrow.setAdapter(itemArrayAdapterTomorrow);

        if (todayCount > 0)
            ((CardView) v.findViewById(R.id.noClassToday)).setVisibility(View.GONE);
        if (tomorrowCount > 0)
            ((CardView) v.findViewById(R.id.noClassTomorrow)).setVisibility(View.GONE);


        WeatherUpdater updateWeather;
        View weatherCardHolder = v.findViewById(R.id.weatherCardIncluder);

        if (main.willShowWeather())
            updateWeather = new WeatherUpdater(context, v);
        else
            weatherCardHolder.setVisibility(View.GONE);


//
//        new Thread ( new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//


        // MenuGridActivity includeMenu = new MenuGridActivity(context,v, main);


    }


}
