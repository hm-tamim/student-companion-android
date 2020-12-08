package club.nsuer.nsuer;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {


    private MainActivity main;
    private Context context;
    private View v;
    private NotificationDatabase db;

    private LinearLayout noItem;
    private NotificationDao notificationDao;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        main = (MainActivity) getActivity();
        context = getContext();


        db = Room.databaseBuilder(context,
                NotificationDatabase.class, "notifications").allowMainThreadQueries().build();
        notificationDao = db.notificationDao();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_notification, container, false);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Notifications");

        noItem = v.findViewById(R.id.noItem);


        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        ArrayList<NotificationItem> itemList = new ArrayList<NotificationItem>();

        NotificationAdapter itemAdapter = new NotificationAdapter(R.layout.notification_recycler, itemList, context, main);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        List<NotificationEntity> list = db.notificationDao().getAllByLimit();


        Log.d("noti", list.size() + "");

        if (list.size() < 1) {
            noItem.setVisibility(View.VISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
        }

        for (int i = 0; i < list.size(); i++) {


            itemList.add(new NotificationItem(list.get(i).getTitle(), list.get(i).getBody(), list.get(i).getType(), list.get(i).getTypeExtra(), list.get(i).getTypeExtra2(), list.get(i).getSenderMemID(), list.get(i).getTime(), list.get(i).isSeen()));


            int idd = notificationDao.setSeen(list.get(i).getMsg_id());


        }


        recyclerView.setAdapter(itemAdapter);


    }


}
