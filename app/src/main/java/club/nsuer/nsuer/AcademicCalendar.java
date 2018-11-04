package club.nsuer.nsuer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;



public class AcademicCalendar extends Fragment {



    private MainActivity main;

    private Context context;

    private ArrayList<AcademicCalendarItem> itemList;
    private View view;

    private int datePassed = 0;

    private String jsonCache = null;
    private AcademicCalendarAdapter itemAdapter;
    private RecyclerView recyclerView;

    private SnapHelper snapHelper;
    private CacheHelper acCache;



    public AcademicCalendar() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        main = (MainActivity) getActivity();
        context = getContext();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.academic_calendar, container, false);;

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.reload_button, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navReloadButton:
                if(Utils.isNetworkAvailable(context))
                    loadJson();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("Academic Calendar");

        snapHelper = new GravitySnapHelper(Gravity.TOP);


        itemList = new ArrayList<AcademicCalendarItem>();

        itemAdapter = new AcademicCalendarAdapter(R.layout.academic_calendar_recycler, itemList, getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.acRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        snapHelper.attachToRecyclerView(recyclerView);


        final String key = "academic_calendar";


        acCache = new CacheHelper(context, key);


        int timeDiff = 9999;

        if (acCache.isExists()){

            jsonCache = acCache.retrieve();


            loadRecylcer(jsonCache);


            timeDiff = acCache.getTimeDiffMin();

            Log.e("Cache", jsonCache);
            Log.e("Time", String.valueOf(timeDiff));

        }

        if(!acCache.isExists() || timeDiff > 40 && Utils.isNetworkAvailable(context)) {


            loadJson();


        }







//
//
//        itemList.add(new AcademicCalendarItem("02","SEP","Friday","Last day of grade submission (Summer 2018)",""));
//        itemList.add(new AcademicCalendarItem("03","SEP","Monday","No classes-Reading / Makeup",""));
//        itemList.add(new AcademicCalendarItem("04","SEP","Tuesday","Final exams start",""));
//        itemList.add(new AcademicCalendarItem("10","SEP","Monday","Final exams ends",""));
//        itemList.add(new AcademicCalendarItem("14","SEP","Friday","Last day of grade submission (Summer 2018)",""));
//        itemList.add(new AcademicCalendarItem("17","SEP","Monday","Online course registration for existing regular and probation students for Fall 2018 starts",""));
//
//        itemList.add(new AcademicCalendarItem("20","SEP","Thursday","Online course registration for existing regular and probation students for Fall 2018 ends",""));
//        itemList.add(new AcademicCalendarItem("21","SEP","Friday","Holiday-Ashura",""));
//        itemList.add(new AcademicCalendarItem("22","SEP","Saturday","Orientation for newly admitted undergraduate and graduatestudents for Fall 2018",""));



        recyclerView.setAdapter(itemAdapter);



    }

    private void loadJson(){


        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("type", "nsuer");

        JSONParser parser = new JSONParser("https://nsuer.club/app/academic-calendar/get-json.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                acCache.clear();

                acCache.save(result.toString());

                itemList.clear();

                loadRecylcer(result.toString());
                //Toast.makeText(getContext(),"Updated!",Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

                Toast.makeText(getContext(),"Error occurred!",Toast.LENGTH_SHORT).show();

            }
        });


        parser.execute();


    }



    private void loadRecylcer(String string){

        ProgressBar p = (ProgressBar) view.findViewById(R.id.acProgressBar);

        p.setVisibility(View.GONE);

        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("calendar");



            for (int j = 0; j < obj.length(); j++) {



                boolean  isPassed = false;

                JSONObject data = obj.getJSONObject(j);

                String sDate = data.getString("date");
                String sMonth = data.getString("month");
                String sDay = data.getString("day");
                String sYear = data.getString("year");
                String sEvent = data.getString("event");

                try {

                    if (new SimpleDateFormat("dd/MMM/yyyy", Locale.US).parse(sDate+"/"+sMonth + "/"+sYear).before(new Date())) {
                        datePassed = j;
                        isPassed = true;
                    }

                }catch(Exception e) {}


                //String sColor = data.getString("date");


                itemList.add(new AcademicCalendarItem(sDate,sMonth,sDay,sEvent,"",isPassed));

            }
        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }



        itemAdapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(datePassed);




    }

    }
