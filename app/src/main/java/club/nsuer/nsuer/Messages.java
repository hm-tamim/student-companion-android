package club.nsuer.nsuer;


import android.app.Activity;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Messages extends Fragment {


    private View view;

    private MainActivity main;
    private Context context;
    private LinearLayout noItem;

    private ImageView noItemIcon;
    private TextView noItemText;
    private ProgressBar progressBar;

    private String uid;
    private String memID;


    private RecyclerView recyclerView;
    private ArrayList<MessageListItem> itemList;
    private MessageListAdapter itemAdapter;

    private FragmentTransaction ft;


    private boolean from_notification = false;
    private String from_notification_id;
    private String from_notification_name;

    private MessageDatabase db;

    private ArrayList<MessageEntity> DbItem;

    private int dbLength = 0;


    public Messages(String from_notification_id, String from_notification_name) {
        from_notification = true;
        this.from_notification_id = from_notification_id;
        this.from_notification_name = from_notification_name;
    }

    public Messages() {
        // Required empty public constructor
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);


        inflater.inflate(R.menu.classmates_button, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_classmates:

                ClassMates fragment = new ClassMates();
                ft.setCustomAnimations(R.animator.abc_grow_fade_in_from_bottom_slow, 0, 0, 0);
                ft.replace(R.id.mainFrame, fragment, "Classmates");
                ft.addToBackStack(null);
                ft.commit();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        main = (MainActivity) getActivity();
        context = getContext();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.messages_fragment, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("Messages");
        uid = main.getUid();
        memID = main.getMemberID();


        db = Room.databaseBuilder(context,
                MessageDatabase.class, "inbox").allowMainThreadQueries().build();


        dbLength = db.messageDao().count();


        ft = main.getSupportFragmentManager().beginTransaction();


        noItem = view.findViewById(R.id.noItem);
        noItemIcon = view.findViewById(R.id.noItemIcon);
        noItemText = view.findViewById(R.id.noItemText);
        progressBar = view.findViewById(R.id.progressBar);


        itemList = new ArrayList<MessageListItem>();
        itemAdapter = new MessageListAdapter(R.layout.message_list_recycler, itemList, context, this, memID);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        // itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_devider));


//        recyclerView.addItemDecoration(itemDecorator);


        recyclerView.setAdapter(itemAdapter);


        if (dbLength > 0)
            loadFromDb();


        if (Utils.isNetworkAvailable(context))
            loadJson(uid);


//
//        new CountDownTimer(300000, 5000) {
//
//            public void onTick(long millisUntilFinished) {
//                if(Utils.isNetworkAvailable(context))
//                    loadJson(uid);
//            }
//
//            public void onFinish() {
//
//            }
//        }.start();


        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (from_notification) {

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("otherMemID", from_notification_id);
                    intent.putExtra("otherMemName", from_notification_name);
                    startActivityForResult(intent, 10001);

                    from_notification = false;

                }

            }
        }, 100);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {

            ft.detach(Messages.this).attach(Messages.this).commit();


        }
    }


    public void loadFromDb() {


        List<MessageEntity> list = db.messageDao().getAllByTime();


        itemList.clear();
        for (int i = 0; i < list.size(); i++) {


            int id = list.get(i).getMsg_id();

            int chat_id = list.get(i).getChat_id();

            String from = list.get(i).getUser_from();
            String to = list.get(i).getUser_to();
            String message = list.get(i).getMessage();
            long time = list.get(i).getTime();


            String name = list.get(i).getUser_name();
            String picture = list.get(i).getUser_picture();
            String gender = list.get(i).getUser_gender();

            itemList.add(new MessageListItem(id, to, from, message, time, 0, name, gender, picture));


        }


        if (itemList.size() > 0)
            noItem.setVisibility(View.GONE);
        else {

            noItemText.setVisibility(View.VISIBLE);
            noItemIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }


        itemAdapter.notifyDataSetChanged();


    }


    public void loadJson(final String uid) {


        if (dbLength < 1) {

            noItem.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            noItemText.setVisibility(View.GONE);
            noItemIcon.setVisibility(View.GONE);
        }

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/chat/chat-list.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    db.messageDao().nukeTable();

                    JSONArray obj = result.getJSONArray("dataArray");


                    for (int j = 0; j < obj.length(); j++) {

                        JSONObject data = obj.getJSONObject(j);


                        int id = data.getInt("id");

                        String from = data.getString("f");
                        String to = data.getString("t");
                        String message = data.getString("m");
                        long time = data.getLong("tm");


                        int chat_id = data.getInt("ci");

                        String name = data.getString("n");
                        String picture = data.getString("p");
                        String gender = data.getString("g");


                        MessageEntity dbItem = new MessageEntity();

                        dbItem.setChat_id(chat_id);
                        dbItem.setMsg_id(id);
                        dbItem.setUser_from(from);
                        dbItem.setUser_to(to);
                        dbItem.setUser_name(name);
                        dbItem.setUser_gender(gender);
                        dbItem.setUser_picture(picture);
                        dbItem.setMessage(message);
                        dbItem.setTime(time);


                        db.messageDao().insertAll(dbItem);


                        //itemList.add(new MessageListItem(id, to, from, message, time, 0, name, gender, picture));


                    }


                    dbLength = db.messageDao().count();

                    loadFromDb();


                } catch (JSONException e) {

                    Log.e("JSON", e.toString());

                    noItemText.setVisibility(View.VISIBLE);
                    noItemIcon.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }


                //loadRecylcer(result.toString());


            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }


    private void loadRecylcer(String string) {


        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("dataArray");


            for (int j = 0; j < obj.length(); j++) {

                JSONObject data = obj.getJSONObject(j);


                int id = data.getInt("id");

                String from = data.getString("f");
                String to = data.getString("t");
                String message = data.getString("m");
                long time = data.getLong("tm");


                int chat_id = data.getInt("ci");

                String name = data.getString("n");
                String picture = data.getString("p");
                String gender = data.getString("g");


                itemList.add(new MessageListItem(id, to, from, message, time, 0, name, gender, picture));


            }


        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }


        if (itemList.size() > 0)
            noItem.setVisibility(View.GONE);
        else {

            noItemText.setVisibility(View.VISIBLE);
            noItemIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
        itemAdapter.notifyDataSetChanged();


    }


}
