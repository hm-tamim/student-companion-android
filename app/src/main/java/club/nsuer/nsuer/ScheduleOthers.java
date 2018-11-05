package club.nsuer.nsuer;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleOthers extends Fragment {

    private View v;

    private MainActivity main;
    private Context context;
    private FloatingActionButton addButton;
    private FragmentTransaction ft;

    private ArrayList<ScheduleOthersItem> itemList;
    private ScheduleOthersAdapter itemAdapter;
    private RecyclerView recyclerView;
    private ScheduleDatabase db;

    private LinearLayout titleLinear;
    private LinearLayout crLine2;
    private LinearLayout noSchedule;
    private int scrollID = 999999;

    public ScheduleOthers() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        main = (MainActivity) getActivity();

        context = getContext();



        db = Room.databaseBuilder(context,
                ScheduleDatabase.class, "schedules").allowMainThreadQueries().build();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_schedule_others, container, false);

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            ft.detach(ScheduleOthers.this).attach(ScheduleOthers.this).commit();
    }


    public void openActivityWithId(int id){

        Intent intent = new Intent(context,
                AddSchedule.class);
        intent.putExtra("id", id);
        intent.putExtra("uid",main.getUid());
        startActivityForResult(intent, 10001);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        titleLinear = v.findViewById(R.id.titleLinear);
        crLine2  = v.findViewById(R.id.crLine2);
        noSchedule = v.findViewById(R.id.noSchedule);


        ft = getFragmentManager().beginTransaction();




        itemList = new ArrayList<ScheduleOthersItem>();

        itemAdapter = new ScheduleOthersAdapter(R.layout.schedule_others_recycler, itemList, context, this);

        recyclerView = (RecyclerView) v.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        List<ScheduleEntity> list = db.scheduleDao().getAll();


        recyclerView.setAdapter(itemAdapter);


        loadJson();


    }






    private void loadJson(){

        String url;


        url = "https://nsuer.club/app/schedules/find-others.php";

        HashMap<String, String> parametters = new HashMap<String, String>();


        JSONParser parser = new JSONParser(url, "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

              //  p.setVisibility(View.GONE);

                //acCache.save(result.toString());

                itemList.clear();

                loadRecylcer(result.toString());
                // Toast.makeText(context,"Updated!",Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }



    private void loadRecylcer(String string){

        //ProgressBar p = (ProgressBar) v.findViewById(R.id.acProgressBar);

        //p.setVisibility(View.GONE);

        //p.setVisibility(View.GONE);

        try {


            JSONObject result = new JSONObject(string);

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

                boolean doRemind = false;

                boolean isPassed = false;


                if(doRemindi == 1)
                    doRemind = true;

                long unixTime = System.currentTimeMillis() / 1000L;

                if(unixTime > date)
                    isPassed = true;


                itemList.add(new ScheduleOthersItem(id, title, type, note, date, reminderDate, color, doRemind, isPassed));


            }



            if(obj.length() < 1) {
                titleLinear.setVisibility(View.GONE);
                crLine2.setVisibility(View.GONE);
            } else {
                noSchedule.setVisibility(View.GONE);
            }



        } catch (JSONException e) {

            Log.e("JSON", e.toString());

            titleLinear.setVisibility(View.GONE);
            crLine2.setVisibility(View.GONE);
        }



        itemAdapter.notifyDataSetChanged();





    }

}
