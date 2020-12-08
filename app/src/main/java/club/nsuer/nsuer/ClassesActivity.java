package club.nsuer.nsuer;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ClassesActivity extends Fragment {

    private MainActivity main;
    private Context context;
    private View v;
    private ArrayList<ClassesItem> itemList;
    private ClassesAdapter itemAdapter;
    private RecyclerView recyclerView;
    private CoursesDatabase db;
    private LinearLayout line1;
    private LinearLayout line2;
    private LinearLayout noClass;
    private TextView crDay;

    public ClassesActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        main = (MainActivity) getActivity();
        context = getContext();

        db = Room.databaseBuilder(context,
                CoursesDatabase.class, "courses").allowMainThreadQueries().build();


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_classes, container, false);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("Classes");

        main.removeShadow();


        line1 = v.findViewById(R.id.crLine1);
        line2 = v.findViewById(R.id.crLine2);

        noClass = v.findViewById(R.id.noClasses);
        crDay = v.findViewById(R.id.crDay);


        itemList = new ArrayList<ClassesItem>();

        itemAdapter = new ClassesAdapter(R.layout.classes_recycler, itemList, getContext());

        recyclerView = (RecyclerView) v.findViewById(R.id.crRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        //DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");


        String todayDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(today.getTime());

        crDay.setText(todayDay);

        populateRecycler(todayDay);

        recyclerView.setAdapter(itemAdapter);


        CalendarView calendarView = (CalendarView) v.findViewById(R.id.crCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


                String dday = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());

                crDay.setText(dday);

                //Toast.makeText(getContext(), ""+dday, 0).show();
                populateRecycler(dday);

            }
        });


    }


    private void populateRecycler(String day) {


        itemList.clear();


        String dayFirstLetter = day.substring(0, 1);

        if (day.contains("Thursday"))
            dayFirstLetter = "R";
        if (day.contains("Saturday"))
            dayFirstLetter = "A";


        List<CoursesEntity> list = db.coursesDao().getAllByTime();


        int todayCount = 0;
        int tomorrowCount = 0;

        for (int i = 0; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String section = list.get(i).getSection();

            String startTime24 = list.get(i).getStartTime();
            String startTime = timeConverter(startTime24, 12);


            String endTime24 = list.get(i).getEndTime();
            String endTime = timeConverter(endTime24, 12);

            String dayz = list.get(i).getDay();
            String faculty = list.get(i).getFaculty();
            String room = list.get(i).getRoom();

            if (dayz.contains(dayFirstLetter)) {
                itemList.add(new ClassesItem(startTime + " - " + endTime, course, "notPassed"));


                todayCount++;
            }
        }


        itemAdapter.notifyDataSetChanged();

        if (todayCount > 0) {

            noClass.setVisibility(View.GONE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);


        } else {
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            noClass.setVisibility(View.VISIBLE);


        }


    }


}








