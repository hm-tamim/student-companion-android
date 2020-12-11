package club.nsuer.nsuer;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import androidx.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private double cgpaDouble = 0.0;
    private int creditInt = 0;
    private int semesterInt = 0;
    private int deptInt = 0;
    private int bloodGroup = -1;
    private Context context;
    public boolean isThereDp = false;
    private View headerView;
    public File userProfilePicture;
    private boolean isLatest = false;
    private boolean showCgpa = true;
    private boolean showWeather = true;
    private BottomNavigationView bottomNavigationView;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private View sheetBg;
    private boolean showingShadow = false;
    private boolean isBottomSheetOpen = false;
    int id;
    Fragment fragment;

    private boolean offlineSubs = false;

    private boolean advisingTools = false;

    MenuGridActivity includeMenu;

    public Context customContext() {

        return this;

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

    public int getBloodGroup() {
        return bloodGroup;
    }

    public String getUid() {
        return uid;
    }

    public void removeShadow() {
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 0));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);
        showingShadow = false;
    }

    public void resetShadow() {
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 16));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);
        showingShadow = true;
    }

    public void setMenuBackground(boolean red) {
        if (red) {
            // sheetBg.setBackgroundColor(Color.parseColor("#d3d30202"));
        } else {
            // sheetBg.setBackgroundColor(Color.parseColor("#e8317e96"));
        }
    }

    private void setStatusBarDim(boolean dim) {
        BloodBank bloodBank = (BloodBank) getSupportFragmentManager().findFragmentByTag("Blood Bank");
        if (bloodBank != null && bloodBank.isVisible()) {
        } else {
            getWindow().setStatusBarColor(isBottomSheetOpen ? Color.parseColor("#b1000000") : Color.parseColor("#ffffff"));
        }

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

            // fix scaling

            DisplayMetrics metrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(metrics);


            if (metrics.scaledDensity > 2.7f) {
                Configuration configuration = getResources().getConfiguration();
                configuration.fontScale = (float) 1.05; //0.85 small size, 1 normal size, 1,15 big etc
                // metrics.scaledDensity = configuration.fontScale * metrics.density;
                metrics.scaledDensity = ((metrics.xdpi / metrics.densityDpi) * metrics.density);
                configuration.densityDpi = ((int) getResources().getDisplayMetrics().xdpi);
                getBaseContext().getResources().updateConfiguration(configuration, metrics);

            }

            Log.d("Display", metrics.toString());

        } catch (Exception e) {

            Log.e("Scaling", e.toString());

        }

        context = this;

        // user session manager
        session = new SessionManager(context);

        if (session.isLoggedIn()) {
            // Fetching user details
            name = session.getName();
            uid = session.getUid();
            email = session.getEmail();
            gender = session.getGender();
            memberID = session.getMemberID();
            picture = session.getPicture();

            cgpa = session.getCgpa();
            credit = session.getCredit();
            semester = session.getSemester();
            dept = session.getDepartment();
            showCgpa = session.willShowCgpa();
            showWeather = session.willShowWeather();
            try {

                bloodGroup = Integer.parseInt(session.getBloodGroup());
                cgpaDouble = Double.parseDouble(cgpa);
                creditInt = Integer.parseInt(credit);
                semesterInt = Integer.parseInt(semester);
                deptInt = Integer.parseInt(dept);
            } catch (Exception e) {

                Log.e("e", e.toString());

            }
            createMainActivity();

        } else {
            logoutUser();
        }
        instance = this;

    }

    public boolean willShowCgpa() {
        return showCgpa;
    }

    public boolean willShowWeather() {
        return showWeather;
    }

    public void setShowCgpa(boolean bool) {
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

    public void hideBottomSheet() {
        isBottomSheetOpen = false;
        sheetBg.animate().alpha(0.0f).setDuration(150);
        if (showingShadow)
            removeShadow();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                sheetBg.setVisibility(View.GONE);

                setStatusBarDim(false);

            }
        }, 120);
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

        // bottomSheetBehavior.setState();
        sheetBg = findViewById(R.id.sheet_bg);


        sheetBg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideBottomSheet();
            }
        });

        sheetBg.setVisibility(View.GONE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBg.animate().alpha(0.0f).setDuration(150);
                    sheetBg.setVisibility(View.GONE);
                    isBottomSheetOpen = false;
                    setStatusBarDim(true);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    sheetBg.animate().alpha(1.0f).setDuration(150);
                    sheetBg.setVisibility(View.VISIBLE);
                    isBottomSheetOpen = true;
                    setStatusBarDim(true);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        switch (item.getItemId()) {
                            case R.id.nav_menu:

                                includeMenu = new MenuGridActivity(context, bottomSheet, getInstance(), advisingTools);
                                bottomSheet.bringToFront();

                                if (showingShadow)
                                    removeShadow();

                                sheetBg.animate().alpha(1.0f);

                                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                                    isBottomSheetOpen = true;
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    sheetBg.setVisibility(View.VISIBLE);
                                    setStatusBarDim(true);
                                } else
                                    hideBottomSheet();
                                break;

                            case R.id.nav_newsfeed:
                                fragment = new StatusActivity();
                                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                                //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
                                ft.replace(R.id.mainFrame, fragment, "Newsfeed");
                                ft.addToBackStack(null);
                                ft.commit();

                                hideBottomSheet();
                                break;

                            case R.id.nav_message:
                                fragment = new Messages();
                                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.fade_out, 0, 0);
                                //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
                                ft.replace(R.id.mainFrame, fragment, "Messages");
                                ft.addToBackStack(null);
                                ft.commit();


                                hideBottomSheet();
                                break;

                            case R.id.nav_buysell:
                                fragment = new BuySell();
                                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, 0, 0);
                                //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
                                ft.replace(R.id.mainFrame, fragment, "Buy-Sell Shop");
                                ft.addToBackStack(null);
                                ft.commit();
                                hideBottomSheet();
                                break;

                            case R.id.nav_home:
                                fragment = new UserProfile();
                                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.fade_out, 0, 0);
                                //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
                                ft.replace(R.id.mainFrame, fragment, "Profile");
                                ft.addToBackStack(null);
                                ft.commit();
                                hideBottomSheet();
                                break;
                        }
                        return true;
                    }
                });


        ImageView navDp = (ImageView) headerView.findViewById(R.id.profilePicNav);
        final String imgDir = context.getFilesDir().getPath() + File.separator + "images" + File.separator + "tamim.jpg";
        userProfilePicture = new File(imgDir);

        if (picture != null && !picture.equals("0")) {

            if (!userProfilePicture.exists()) {

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
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
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
        final Intent intent = getIntent();

        if (intent.hasExtra("type")) {

            String type = intent.getStringExtra("type");

            if (type.equals("like")) {
                String typeExtra = intent.getStringExtra("typeExtra");
                int openID = Integer.parseInt(typeExtra);
                Fragment fragments = new StatusActivity(1, openID);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Newsfeed");
                ft.commit();
            } else if (type.equals("shop")) {
                String typeExtra = intent.getStringExtra("typeExtra");
                int openID = Integer.parseInt(typeExtra);
                Fragment fragments = new BuySell();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "BuySell Shop");
                ft.commit();
            } else if (type.equals("url")) {
                Fragment fragment = new UserProfile();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment, "Profile");
                ft.commit();
                String typeExtra = intent.getStringExtra("typeExtra");
                Utils.CustomTab(typeExtra, context);
            } else if (type.equals("alert")) {
                Fragment fragment = new UserProfile();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment, "Profile");
                ft.commit();
                String typeExtra = intent.getStringExtra("typeExtra");

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme).create();
                alertDialog.setTitle("From notification");
                alertDialog.setMessage(typeExtra);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


            } else if (type.equals("comment")) {
                String typeExtra = intent.getStringExtra("typeExtra");
                int openID = Integer.parseInt(typeExtra);
                Fragment fragments = new StatusActivity(2, openID);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Newsfeed");
                ft.commit();

            } else if (type.equals("reminder")) {
                Fragment fragments = new ScheduleFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Schedule");
                ft.commit();

            } else if (type.equals("calendar")) {
                Fragment fragments = new AcademicCalendar();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Academic Calendar");
                ft.commit();
            } else if (type.equals("notice")) {
                Fragment fragments = new NsuNotices();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Notices & Events");
                ft.commit();

            } else if (type.equals("blood")) {
                Fragment fragments = new BloodBank();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Blood Bank");
                ft.commit();

            } else if (type.equals("playstore")) {


                Fragment fragment = new UserProfile();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment, "Profile");
                ft.commit();

                String appPackageName = getPackageName();

                if (!intent.getStringExtra("typeExtra").equals(""))
                    appPackageName = intent.getStringExtra("typeExtra");

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }


            } else if (type.equals("message")) {

                String senderMemID = intent.getStringExtra("senderMemID");
                String typeExtra = intent.getStringExtra("typeExtra");

                // save chat directly from notification


//
//                if(!intent.hasExtra("fromService")){
//                    ChatEntity chatItem = new ChatEntity();
//                    long time = Calendar.getInstance().getTimeInMillis()/1000L;
//                    String typeExtra2 =  intent.getStringExtra("typeExtra2");
//                    int idd = Integer.parseInt(typeExtra2);
//                    String message = intent.getStringExtra("body");
//                    chatItem.setMsg_id(idd);
//                    chatItem.setUser_from(senderMemID);
//                    chatItem.setUser_to(memberID);
//                    chatItem.setMessage(message);
//                    chatItem.setTime(time);
//                    String dbName = "chat_"+senderMemID;
//                    ChatDatabase chatDb= Room.databaseBuilder(context,
//                            ChatDatabase.class, dbName).allowMainThreadQueries().build();
//                    chatDb.chatDao().insertAll(chatItem);
//
//                }

                Fragment fragments = new Messages(senderMemID, typeExtra);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
                ft.replace(R.id.mainFrame, fragments, "Newsfeed");
                ft.commit();

            }

        } else {
            Fragment fragment = new UserProfile();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment, "Profile");
            ft.commit();

        }

        isLatestVersion();
        // Custom condition: 1 days and 5 launches
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        RateThisApp.init(config);


        if (bloodGroup < 0) {

            if (Utils.isNetworkAvailable(context)) {
                final Intent intentB = new Intent(context, BloodBeDonor.class);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intentB);

                    }
                }, 1500);


            }


        }


        if (!session.isPremium()) {


            offlineSubs = true;

//            Intent intentz = new Intent(MainActivity.this, Subscription.class);
//            startActivity(intentz);
//


        } else {
        }


    }


    public void showUpodateAlert(String msg) {

        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setMessage(msg)
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

        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setMessage("Do you really want to logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        session.setLogin(false);

                        CoursesDatabase cdb = Room.databaseBuilder(context,
                                CoursesDatabase.class, "courses").allowMainThreadQueries().build();
                        cdb.coursesDao().nukeTable();
                        BooksDatabase bdb = Room.databaseBuilder(context,
                                BooksDatabase.class, "books").allowMainThreadQueries().build();
                        bdb.booksDao().nukeTable();
                        FacultiesDatabase fdb = Room.databaseBuilder(context,
                                FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();
                        fdb.facultiesDao().nukeTable();


                        ScheduleDatabase scdb = Room.databaseBuilder(context,
                                ScheduleDatabase.class, "schedule").fallbackToDestructiveMigration().allowMainThreadQueries().build();
                        scdb.scheduleDao().nukeTable();

                        final String imgDirr = context.getFilesDir().getPath() + File.separator + "images" + File.separator + "tamim.jpg";
                        File userProfilePicturez = new File(imgDirr);

                        if (userProfilePicturez.exists()) {
                            Picasso.get().invalidate(userProfilePicturez);
                            userProfilePicturez.delete();

                        }

                        // Launching the login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }


    public void isLatestVersion() {

        Log.d("called", "called" + advisingTools);

        if (Utils.isNetworkAvailable(this)) {


            HashMap<String, String> parametters = new HashMap<String, String>();
            parametters.put("version", BuildConfig.VERSION_NAME);
            parametters.put("uid", session.getUid());
            JSONParser parser = new JSONParser("https://nsuer.club/apps/version.php", "GET", parametters);
            parser.setListener(new JSONParser.ParserListener() {
                @Override
                public void onSuccess(JSONObject result) {


                    Log.d("called", result.toString());

                    String latestVersion = "1.0";
                    String currentVersion = "1.0";
                    String msg = "Please update the app to latest version";
                    String closeMainActivity = "false";

                    try {

                        FirebaseMessaging.getInstance().subscribeToTopic("BLOOD." + session.getBloodGroup());


                        latestVersion = result.getString("version");

                        msg = result.getString("msg");


                        String isPremium = result.getString("isPremium");
                        String expire = result.getString("expire");

                        closeMainActivity = result.getString("closeMainActivity");

                        advisingTools = result.getBoolean("advisingTools");


                        if (isPremium.equals("true")) {
                            session.setPremium(true);
                            session.setExpireDate(expire);
                        } else {

                            session.setPremium(false);
                            session.setExpireDate("0");


                            if (result.getBoolean("showMembershipDialog")) {

                                if (!offlineSubs) {

                                    if (session.isPremium()) {
                                        Intent intentz = new Intent(MainActivity.this, Subscription.class);
                                        startActivity(intentz);

                                        if (closeMainActivity.equals("true"))
                                            finish();
                                    }

                                    if (closeMainActivity.equals("true")) {

                                        Intent intentz = new Intent(MainActivity.this, Subscription.class);
                                        startActivity(intentz);
                                        finish();
                                    }


                                } else {

                                    if (closeMainActivity.equals("true"))
                                        finish();
                                }


                            }
                        }


                        Log.d("called", "called" + advisingTools);


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

                    if (!latestVersion.equals(currentVersion))
                        showUpodateAlert(msg);
                }

                @Override
                public void onFailure() {
                }
            });

            parser.execute();

        }
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hideBottomSheet();
            return;
        }
        UserProfile test = (UserProfile) getSupportFragmentManager().findFragmentByTag("Profile");
        if (test != null && test.isVisible()) {
            finish();
        } else {

            Fragment fragment3 = new UserProfile();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.abc_popup_exit, 0, 0);
            ft.replace(R.id.mainFrame, fragment3, "Profile");
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY");
            startActivity(intent);
            finish();
        } else if (id == R.id.action_logout) {

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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_cgpa_calculator) {
            fragment = new CgpaCalculator();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Cgpa_Calculator");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_bloodbank) {

            fragment = new BloodBank();
            ft.setCustomAnimations(0, 0, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Blood Bank");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_message) {
            fragment = new Messages();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Messages");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_buysell) {
            fragment = new BuySell();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Buy-Sell Shop");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_schedules) {
            fragment = new ScheduleFragment();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Schedules");
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_user_profile) {


            fragment = new UserProfile();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Profile");
            ft.addToBackStack(null);
            ft.commit();


        } else if (id == R.id.nav_books) {

            fragment = new Books();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Books");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_courses) {

            fragment = new CoursesActivity();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Courses");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_faculty) {

            fragment = new FacultiesActivity();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Faculties");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_about) {

            fragment = new About();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "About");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_newsfeed) {

            fragment = new StatusActivity();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Newsfeed");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_classmates) {

            fragment = new ClassMates();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Classmates");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_classes) {

            fragment = new ClassesActivity();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Classes");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_academic_calendar) {

            fragment = new AcademicCalendar();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Academic Calendar");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_notice_events) {

            fragment = new NsuNoticeFragment();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "Notices");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_cgpa_analyzer) {

            fragment = new CgpaAnalyzer();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);

            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
            ft.replace(R.id.mainFrame, fragment, "CGPA Analyzer");
            ft.addToBackStack(null);
            ft.commit();

        }

//        else if (id == R.id.nav_faculty_predictor) {
//
//            fragment = new FacultyPredictor();
//            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom,R.animator.fade_out, 0, 0);
//
//            //overridePendingTransition(R.animator.slide_from_bottom, R.animator.slide_from_up);
//            ft.replace(R.id.mainFrame, fragment,"Faculty Predictor");
//            ft.addToBackStack(null);
//            ft.commit();
//
//        }
//        else if (id == R.id.nav_faculty_rankings) {
//
//
//            fragment = new FacultyRankings();
//            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
//            ft.replace(R.id.mainFrame, fragment,"Faculty_Rankings");
//            ft.addToBackStack(null);
//            ft.commit();
//
//
//        }
//        else if (id == R.id.nav_advising_archive) {
//
//            fragment = new AdvisingArchive(uid);
//            ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
//            ft.replace(R.id.mainFrame, fragment,"Advising_Archive");
//            ft.addToBackStack(null);
//            ft.commit();
//
//        }
//

        else if (id == R.id.nav_advising_assistant) {

            fragment = new AdvisingAssistant(uid);
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Advising_Assistant");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_contribute) {

            fragment = new Contribute();
            ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, R.animator.fade_out, 0, 0);
            ft.replace(R.id.mainFrame, fragment, "Contribute");
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_contact) {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "hm.tamim@northsouth.edu", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "[NSUer App] Message from " + getName());
            startActivity(intent);
            //startActivity(Intent.createChooser(intent, "Choose an Email client:"));
        } else if (id == R.id.nav_share) {

            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
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

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(this);
                hideBottomSheet();
            }
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
