package club.nsuer.nsuer;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class MenuGridActivity{

    private StaticGridView androidGridView;

    private final Context context;

    private View view;


    private Fragment fragment;

    private FragmentTransaction ft;

    public MainActivity main;


    private String[] gridViewString = {
            "Courses",
            "Classmates",
            "Newsfeed",
            "Faculties",
            "Notices & Events",
            "Academic Calender",
            "Books",
            "Classes",
            "Schedules",
            "CGPA Calculator",
            "CGPA Analyzer",
            "Faculty Predictor",
            "Faculty Rankings",
            "Advising Assistant",
            "Weather",
            "Storage",
            "Buy Sell",
            "Contribute"


    } ;

    private int[] gridViewImageId = {
            R.drawable.ic_courses,
            R.drawable.ic_students_white,
            R.drawable.ic_newsfeed,

            R.drawable.ic_people_dark,
            R.drawable.ic_board_white,
            R.drawable.ic_planer_white,
            R.drawable.ic_books,
            R.drawable.ic_classroom_white,
            R.drawable.ic_schedule_dark,
            R.drawable.ic_calculator,
            R.drawable.ic_analyzer,
            R.drawable.ic_faculty_predictor,
            R.drawable.ic_faculty_ranking,
            R.drawable.ic_advising_assistant,
            R.drawable.ic_weather_white_umbrella,
            R.drawable.ic_downloads_folder,
            R.drawable.ic_shopping_cart_white,
            R.drawable.ic_handshake_white

    };


    MenuGridActivity(Context contextx, View view, MainActivity mainx){

        this.context = contextx;
        this.view = view;
        this.main = mainx;

//        main = (MainActivity) MainActivity.getInstance();
//
//        actionBar = main.getSupportActionBar();
//


        ft = main.getSupportFragmentManager().beginTransaction();



        androidGridView= (StaticGridView) view.findViewById(R.id.menuGridView);

        MenuGridAdapter adapterViewAndroid = new MenuGridAdapter(context, view, gridViewString, gridViewImageId);

        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {


                if (gridViewString[+i].equals("Books")){

                    fragment = new Books();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                } else if(gridViewString[+i].equals("Courses")){

                    fragment = new CoursesActivity();
                    //ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, 0, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("Faculties")){

                    fragment = new FacultiesActivity();
                    //ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom, 0, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("Classmates")){

                    fragment = new ClassMates();
                    ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom_slow, 0, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("CGPA Calculator")){

                    fragment = new CgpaCalculator();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("CGPA Analyzer")){

                    fragment = new CgpaAnalyzer();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("Contribute")){

                    fragment = new Contribute();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                }
                else if(gridViewString[+i].equals("Faculty Rankings")){

                    fragment = new FacultyRankings();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }else if(gridViewString[+i].equals("Faculty Predictor")){

                    fragment = new FacultyPredictor();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }

                else if(gridViewString[+i].equals("Advising Assistant")){

                    fragment = new AdvisingAssistant(main.getUid());
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("Newsfeed")){

                    fragment = new StatusActivity();
                    ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom_slow, R.animator.abc_popup_exit, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(gridViewString[+i].equals("Academic Calender")){

                    fragment = new AcademicCalendar();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                } else if(gridViewString[+i].equals("Notices & Events")){

                    fragment = new NsuNoticeFragment();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                } else if(gridViewString[+i].equals("Classes")){

                    fragment = new ClassesActivity();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                }else if(gridViewString[+i].equals("Weather")){

                    comingSoon();

                }else if(gridViewString[+i].equals("Schedules")){

                    fragment = new ScheduleFragment();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
                    ft.replace(R.id.mainFrame, fragment, gridViewString[+i]);
                    ft.addToBackStack(null);
                    ft.commit();

                }else if(gridViewString[+i].equals("Buy Sell")){

                    comingSoon();

                }else if(gridViewString[+i].equals("Storage")){

                    comingSoon();

                }





               // Toast.makeText(context, "GridView Item: " + i + "   " + gridViewString[+i], Toast.LENGTH_LONG).show();
            }
        });

    }

    private void comingSoon(){


        Toast.makeText(context,"Coming soon, under development...", Toast.LENGTH_SHORT).show();
    }


}