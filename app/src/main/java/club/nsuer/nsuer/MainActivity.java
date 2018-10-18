package club.nsuer.nsuer;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity instance;

    private TextView txtName;
    private TextView txtEmail;


    private SQLiteHandler db;
    private SessionManager session;


    private String name = "Username";
    private String email;
    private String gender;
    private String uid = "uid";
    private String memberID = "0";
    private String picture = "0";
    private String cgpa = "0";
    private String credit = "0";
    private String semester = "0";
    private String dept = "0";


    private double cgpaDouble= 0.0;
    private int creditInt = 0;
    private int semesterInt = 0;
    private int deptInt = 0;

    private Context context;

    public boolean isThereDp = false;

    private View headerView;

    public File userProfilePicture;

    private boolean isLatest = false;

    private boolean showCgpa = true;

    private boolean showWeather = true;



    int id;
    Fragment fragment;

    public Context customContext() {

        return this;

    }

    public void resetShadow() {


        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 16));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);


    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getPicture() {
        return picture;
    }

    public String getCgpa() {
        return cgpa;
    }

    public String getCredit() {
        return credit;
    }

    public String getSemester() {
        return semester;
    }

    public String getDept() {
        return dept;
    }

    public String getUid(){

        return uid;


    }

    public void removeShadow() {


        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 0));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);


    }


    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

         // before onCreate in MainActivity



        try {

            DisplayMetrics metrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(metrics);


            if(metrics.scaledDensity > 2.7f) {


                Configuration configuration = getResources().getConfiguration();
                configuration.fontScale = (float) 1.05; //0.85 small size, 1 normal size, 1,15 big etc

                // metrics.scaledDensity = configuration.fontScale * metrics.density;
                metrics.scaledDensity = ((metrics.xdpi / metrics.densityDpi) * metrics.density);
                configuration.densityDpi = ((int) getResources().getDisplayMetrics().xdpi);
                getBaseContext().getResources().updateConfiguration(configuration, metrics);

            }

            Log.d("Display", metrics.toString());




        }catch (Exception e){

            Log.e("Scaling", e.toString());

        }

        context = this;







        // SqLite database handler
        db = new SQLiteHandler(context);

        // session manager
        session = new SessionManager(context);









        if (session.isLoggedIn()) {

            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            name = user.get("name");
            uid = user.get("uid");
            email = user.get("email");
            gender = user.get("gender");
            memberID = user.get("memberID");
            picture = user.get("picture");

            cgpa = user.get("cgpa");
            credit = user.get("credit");
            semester = user.get("semester");
            dept = user.get("dept");

            showCgpa = session.willShowCgpa();
            showWeather = session.willShowWeather();

            try {

                cgpaDouble = Double.parseDouble(cgpa);
                creditInt = Integer.parseInt(credit);
                semesterInt = Integer.parseInt(semester);
                deptInt = Integer.parseInt(dept);

            } catch (Exception e){

                Log.e("e", e.toString());

            }

            createMainActivity();


        } else {

            logoutUser();

        }
/*
        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                logoutUser();

            }

        });
        */

        instance = this;

    }

    public boolean willShowCgpa(){

        return showCgpa;

    }

    public boolean willShowWeather(){

        return showWeather;

    }

    public void setShowCgpa(boolean bool){

        session.setShowCgpa(bool);
        showCgpa = bool;

    }

    public static MainActivity getInstance() {
        return instance;
    }


    public int getSemesterInt() {
        return semesterInt;
    }

    public int getDeptInt() {
        return deptInt;
    }

    public void createMainActivity() {



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);






        ImageView navDp = (ImageView) headerView.findViewById(R.id.profilePicNav);


        final String imgDir = context.getFilesDir().getPath() + File.separator + "images" + File.separator + "tamim.jpg";

        userProfilePicture = new File(imgDir);



        if (picture != null && !picture.equals("0")){

            if(!userProfilePicture.exists()) {


                Picasso.get()
                        .load("https://nsuer.club/images/profile_picture/" + picture)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(navDp);


                Picasso.get()
                        .load("https://nsuer.club/images/profile_picture/" + picture)
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                      isThereDp = true;

                                      String destinationPath = getFilesDir().getPath() + File.separator + "images";

                                      FileOutputStream fileOutputStream = null;
                                      File file = new File(destinationPath);
                                      if (!file.exists()) {
                                          file.mkdirs();
                                      }

                                      File myFile = new File(destinationPath, "tamim.jpg");


                                      if (!myFile.exists()) {
                                          try {
                                              myFile.createNewFile();

                                          } catch (Exception e) {
                                          }
                                      }

                                      try {

                                          FileOutputStream out = new FileOutputStream(myFile, false);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                          out.flush();
                                          out.close();
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }



                                  }

                                  @Override
                                  public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable)
                                  {
                                  }

                              }
                        );


            } else {

                Picasso.get()
                        .load(userProfilePicture)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(navDp);


            }
        }






        TextView navName = (TextView) headerView.findViewById(R.id.userNameNavHeader);
        navName.setText(name);

        TextView navEmail = (TextView) headerView.findViewById(R.id.emailNavHeader);

        navEmail.setText(email);


        Intent intent = getIntent();


        // Toast.makeText(this, intent.toUri(0), Toast.LENGTH_SHORT).show();

        if(intent.hasExtra("type")) {

            String type = intent.getStringExtra("type");

            if(type.equals("like"))
            {
                String typeExtra = intent.getStringExtra("typeExtra");
                int openID = Integer.parseInt(typeExtra);
                Fragment fragments = new StatusActivity(1,openID);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments,"Newsfeed");
                ft.commit();

            } else if(type.equals("comment"))
            {
                String typeExtra = intent.getStringExtra("typeExtra");
                int openID = Integer.parseInt(typeExtra);
                Fragment fragments = new StatusActivity(2,openID);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments,"Newsfeed");
                ft.commit();

            }


        } else {



            Fragment fragment = new UserProfile();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.setCustomAnimations( R.animator.fade_in,0,0,R.animator.fade_out);

            // ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);

            ft.replace(R.id.mainFrame, fragment,"Profile");
            // ft.addToBackStack(null);
            ft.commit();




        }






        isLatestVersion();


        // Custom condition: 1 days and 5 launches

        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        RateThisApp.Config config = new RateThisApp.Config(3,5);
        RateThisApp.init(config);








    }

    public void showUpodateAlert(){

        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setMessage("\nYou are not using the latest version. Kindly update the app to run it smoothly. \n\nAnd don't forget to rate on Play Store.\n")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();


    }


    public void logoutUser() {

                        session.setLogin(false);

                        // Launching the login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


    }



    public void logoutUserDelete() {



//                .setTitle("Confi")

        new AlertDialog.Builder(this,R.style.AlertDialogTheme)
                .setMessage("Do you really want to logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        session.setLogin(false);

                        db.deleteUsers();

//        NsuNoticesDatabase ndb = Room.databaseBuilder(getApplicationContext(),
//                NsuNoticesDatabase.class, "notices").allowMainThreadQueries().build();
//
//        ndb.nsuNoticesDao().nukeTable();
//
//        ndb = Room.databaseBuilder(getApplicationContext(),
//                NsuNoticesDatabase.class, "events").allowMainThreadQueries().build();
//
//        ndb.nsuNoticesDao().nukeTable();


                        CoursesDatabase cdb = Room.databaseBuilder(getApplicationContext(),
                                CoursesDatabase.class, "courses").allowMainThreadQueries().build();
                        cdb.coursesDao().nukeTable();


                        BooksDatabase bdb = Room.databaseBuilder(getApplicationContext(),
                                BooksDatabase.class, "books").allowMainThreadQueries().build();
                        bdb.booksDao().nukeTable();


                        FacultiesDatabase fdb = Room.databaseBuilder(getApplicationContext(),
                                FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();
                        fdb.facultiesDao().nukeTable();


                        final String imgDirr = context.getFilesDir().getPath() + File.separator + "images" + File.separator + "tamim.jpg";

                        File userProfilePicturez = new File(imgDirr);


                        if(userProfilePicturez.exists()) {
                            Picasso.get().invalidate(userProfilePicturez);
                            userProfilePicturez.delete();

                        }



                        // Launching the login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }})
                .setNegativeButton(android.R.string.no, null).show();




    }





    public void isLatestVersion(){



        if(Utils.isNetworkAvailable(this)){


        HashMap<String, String> parametters = new HashMap<String, String>();


        parametters.put("version", "0.0");

        JSONParser parser = new JSONParser("https://nsuer.club/app/version.json", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

               String latestVersion = "1.0";

               String currentVersion = "1.0";

               try{

                  latestVersion = result.getString("version");



               } catch (JSONException e) {

                Log.e("JSON", e.toString());
            }




                try {


                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    currentVersion = (String) pInfo.versionName;

                   // Toast.makeText(context,latestVersion,Toast.LENGTH_LONG).show();

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if(!latestVersion.equals(currentVersion))
                    showUpodateAlert();



            }

            @Override
            public void onFailure() {


            }
        });


        parser.execute();

        }


    }


    @Override
    public void onBackPressed(){


//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }

//
//        if(getFragmentManager().getBackStackEntryCount() > 0){
//            super.onBackPressed();

        UserProfile test = (UserProfile) getSupportFragmentManager().findFragmentByTag("Profile");
        if (test != null && test.isVisible()) {
            finish();


        }
        else{

            Fragment fragment3 = new UserProfile();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment3,"Profile");
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY");
            startActivity(intent);
            finish();
        } else if (id == R.id.action_logout){

            logoutUserDelete();
        }


        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (id == R.id.nav_cgpa_calculator) {
            fragment = new CgpaCalculator();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Cgpa_Calculator");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_faculty_rankings) {


            fragment = new FacultyRankings(uid);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment,"Faculty_Rankings");
            ft.addToBackStack(null);
            ft.commit();


        } else if (id == R.id.nav_user_profile) {


            fragment = new UserProfile();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Profile");
            ft.addToBackStack(null);
            ft.commit();



        } else if (id == R.id.nav_advising_assistant) {

            fragment = new AdvisingAssistant(uid);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment,"Advising_Assistant");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_books) {

            fragment = new Books();


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment,"Books");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_courses) {

            fragment = new CoursesActivity();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
            ft.replace(R.id.mainFrame, fragment,"Courses");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_faculty) {

            fragment = new FacultiesActivity();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Faculties");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_about) {

            fragment = new About();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"About");
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_newsfeed) {

            fragment = new StatusActivity();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Newsfeed");
            ft.addToBackStack(null);
            ft.commit();

        }else if (id == R.id.nav_classmates) {

            fragment = new ClassMates();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Classmates");
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_classes) {

            fragment = new ClassesActivity();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Classes");
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_academic_calendar) {

            fragment = new AcademicCalendar();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Academic Calendar");
            ft.addToBackStack(null);
            ft.commit();

        }else if (id == R.id.nav_notice_events) {

            fragment = new NsuNoticeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Notices");
            ft.addToBackStack(null);
            ft.commit();

        }else if (id == R.id.nav_cgpa_analyzer) {

            fragment = new CgpaAnalyzer();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"CGPA Analyzer");
            ft.addToBackStack(null);
            ft.commit();

        }else if (id == R.id.nav_faculty_predictor) {

            fragment = new FacultyPredictor();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Faculty Predictor");
            ft.addToBackStack(null);
            ft.commit();

        }else if (id == R.id.nav_contribute) {

            fragment = new Contribute();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment,"Contribute");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_contact) {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","hm.tamim@northsouth.edu", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "[NSUer App] Message from "+getName());

            startActivity(intent);

            //startActivity(Intent.createChooser(intent, "Choose an Email client:"));


        }
        else if (id == R.id.nav_share) {

            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Share NSUer app with your friends.");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=club.nsuer.nsuer");

            startActivity(Intent.createChooser(share, "Share NSUer app with your friends: "));



        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        drawer.closeDrawer(GravityCompat.START);

            }
        }, 150);


        return true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
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





}
