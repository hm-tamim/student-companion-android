package club.nsuer.nsuer;


import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;


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

    public ScheduleFragment() {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Schedules");

        Toast.makeText(context,"Yoo",Toast.LENGTH_SHORT).show();


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

        itemAdapter = new ScheduleAdapter(R.layout.schedule_recycler, itemList, context);

        recyclerView = (RecyclerView) v.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        itemList.add(new ScheduleItem(1,"CSE231", "PROJECT 2", "Bring calculator and ruler" , 1540120800, 000, -11566660, true, false));

        itemList.add(new ScheduleItem(1,"CSE231", "QUIZ 2", "Chapter 3, 4, 6 and 7" , 1540552801,000,-14246231, true, false));

        itemList.add(new ScheduleItem(1,"MAT130", "QUIZ 3", "Chapter 2.5, 2.6, and 2.7" , 1540892400,000,-14246231, true, false));

        itemList.add(new ScheduleItem(1,"EEE141", "MID 2", "Chapter 4, 6, 7 and 8" , 1541515200,000,-49023, false, false));



        recyclerView.setAdapter(itemAdapter);


    }

    }
