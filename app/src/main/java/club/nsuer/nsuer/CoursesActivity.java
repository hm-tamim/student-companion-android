package club.nsuer.nsuer;


import android.app.Dialog;
import androidx.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class CoursesActivity extends Fragment implements CoursesList {

    private MainActivity main;
    private NumberPicker picker;
    private AutoCompleteTextView courseInput;
    private MyNumberPicker sectionInput;
    private View v;
    private CoursesDatabase db;
    private BooksDatabase dbBooks;
    private FacultiesDatabase dbFaculties;
    private Button dialogButton;
    private ArrayList<CoursesListItem> itemList2;
    private Context context;
    private LinearLayout noCourse;

    public CoursesActivity() {
        // Required empty public constructor

    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState)
//    {
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8)
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            //your codes here
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.add_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_courses, container, false);

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main = (MainActivity) getActivity();
        main.resetShadow();

        main.setActionBarTitle("Courses");

        context = getContext();


        db = Room.databaseBuilder(context,
                CoursesDatabase.class, "courses").allowMainThreadQueries().build();

        dbBooks = Room.databaseBuilder(context,
                BooksDatabase.class, "books").allowMainThreadQueries().build();
        dbFaculties = Room.databaseBuilder(context,
                FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();


        noCourse = v.findViewById(R.id.noCourse);

        // db.coursesDao().nukeTable();

        itemList2 = new ArrayList<CoursesListItem>();

        CoursesListAdapter itemArrayAdapter2 = new CoursesListAdapter(R.layout.recycler_courses, itemList2, getContext(), main.getUid());
        RecyclerView recyclerView2 = (RecyclerView) v.findViewById(R.id.rcRecyclerView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        List<CoursesEntity> list = db.coursesDao().getAll();
        String hmm = list.toString();

        //((TextView) v.findViewById(R.id.tttt)).setText(hmm);

        if (list.size() > 0)
            noCourse.setVisibility(View.GONE);

        for (int i = 0; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String section = list.get(i).getSection();
            String startTime = list.get(i).getStartTime();
            startTime = timeConverter(startTime, 12);

            String endTime = list.get(i).getEndTime();
            endTime = timeConverter(endTime, 12);

            String day = list.get(i).getDay();
            String faculty = list.get(i).getFaculty();
            String room = list.get(i).getRoom();

            itemList2.add(new CoursesListItem(course, section, startTime, endTime, room, faculty, day));


        }


        recyclerView2.setAdapter(itemArrayAdapter2);


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addButton) {

            if (!Utils.isNetworkAvailable(getContext())) {
                Toast.makeText(getContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
                return false;
            }

            final Dialog dialog = new Dialog(getContext(), R.style.WideDialog);


            //dialog.setTitle("Add Course & Section");

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 300);

            dialog.setContentView(R.layout.alert_add_course);

            courseInput = (AutoCompleteTextView) dialog.findViewById(R.id.aaCourseInitial);
            sectionInput = (MyNumberPicker) dialog.findViewById(R.id.numberPickerAlert);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, R.layout.suggestion_adapter_textview, SUGGESTIONS);

            courseInput.setThreshold(2);

            courseInput.setDropDownVerticalOffset(0);

            courseInput.setAdapter(adapter);


            ImageView closeButton = (ImageView) dialog.findViewById(R.id.aaCloseButton);


            dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    // dialogButton.setBackgroundColor(Color.RED);

                    CircularProgressButton btn = (CircularProgressButton) dialog.findViewById(R.id.dialogButtonOK);

                    btn.startAnimation();


                    HashMap<String, String> parametters = new HashMap<String, String>();

                    parametters.put("course", courseInput.getText().toString());
                    parametters.put("section", Integer.toString(sectionInput.getValue()));
                    parametters.put("uid", main.getUid());


                    JSONParser parser = new JSONParser("https://nsuer.club/apps/get-course-info.php", "GET", parametters);


                    parser.setListener(new JSONParser.ParserListener() {
                        @Override
                        public void onSuccess(JSONObject result) {


                            int courseCount = 0;

                            try {
                                JSONArray obj = result.getJSONArray("dataArray");

                                String firstCourse = null;
                                String firstSection = null;


                                for (int i = 0; i < obj.length(); i++) {


                                    JSONObject data = obj.getJSONObject(i);

                                    // Add books

                                    if (i > 0) {

                                        if (data.has("books")) {

                                            int id = data.getInt("id");
                                            String course = data.getString("course");
                                            String books = data.getString("books");

                                            BooksEntity arrData = new BooksEntity();

                                            arrData.setCourse(firstCourse);
                                            arrData.setBooks(books);
                                            dbBooks.booksDao().insertAll(arrData);

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

                                            String initial = data.getString("initial");

                                            String course = firstCourse;

                                            String section = firstSection;

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


                                    FirebaseMessaging.getInstance().subscribeToTopic(course + "." + section);


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

                                    courseCount++;

                                    // itemList2.add(new CoursesListItem(course, section, startTime, endTime, room, faculty, day));
                                }
                            } catch (JSONException e) {

                                Log.e("JSON", e.toString());
                            }


                            dialog.dismiss();


                            if (courseCount < 1)
                                Toast.makeText(context, "Can't find this course in database.", Toast.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(CoursesActivity.this).attach(CoursesActivity.this).commit();
                                }
                            }, 100);

                        }

                        @Override
                        public void onFailure() {
                            dialog.dismiss();
                        }
                    });
                    parser.execute();

                }


            });


            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


}
