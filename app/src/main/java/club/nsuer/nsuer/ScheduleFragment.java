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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private View v;

    private MainActivity main;
    private Context context;
    private FloatingActionButton addButton;
    private FragmentTransaction ft;

    private ArrayList<ScheduleItem> itemList;
    private ScheduleAdapter itemAdapter;
    private RecyclerView recyclerView;
    private ScheduleDatabase db;

    private LinearLayout titleLinear;
    private LinearLayout crLine2;
    private LinearLayout noSchedule;
    private int scrollID = 999999;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    public ScheduleFragment(int scrollID) {

        this.scrollID = scrollID;

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
        v = inflater.inflate(R.layout.schedule_fragment, container, false);

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            ft.detach(ScheduleFragment.this).attach(ScheduleFragment.this).commit();
    }


    public void openActivityWithId(int id){

        Intent intent = new Intent(context,
                AddSchedule.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 10001);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Schedules");



        titleLinear = v.findViewById(R.id.titleLinear);
        crLine2  = v.findViewById(R.id.crLine2);
        noSchedule = v.findViewById(R.id.noSchedule);


        ft = getFragmentManager().beginTransaction();

        addButton = v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(context,
                        AddSchedule.class);
                startActivityForResult(intent, 10001);

            }
        });


        itemList = new ArrayList<ScheduleItem>();

        itemAdapter = new ScheduleAdapter(R.layout.schedule_recycler, itemList, context, this);

        recyclerView = (RecyclerView) v.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        List<ScheduleEntity> list = db.scheduleDao().getAll();

       if(list.size() < 1) {
           titleLinear.setVisibility(View.GONE);
           crLine2.setVisibility(View.GONE);
       } else {
           noSchedule.setVisibility(View.GONE);
       }

        for (int i=0; i < list.size(); i++) {

            int id = list.get(i).getId();
            String title = list.get(i).getTitle();
            String type = list.get(i).getType();
            String note = list.get(i).getExtraNote();
            long date = list.get(i).getDate();
            long reminderDate = list.get(i).getReminderDate();
            int color = list.get(i).getColor();
            boolean doRemind = list.get(i).isDoReminder();

            boolean isPassed = false;

            long unixTime = System.currentTimeMillis() / 1000L;

            if(unixTime > date)
                isPassed = true;


            itemList.add(new ScheduleItem(id, title, type, note, date, reminderDate, color, doRemind, isPassed));


        }


        recyclerView.setAdapter(itemAdapter);


    }

    }
