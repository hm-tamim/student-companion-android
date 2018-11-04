package club.nsuer.nsuer;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import club.nsuer.nsuer.R;

public class ChatActivity extends AppCompatActivity {




    private RecyclerView recyclerView;
    private ArrayList<ChatItem> itemList;
    private ChatAdapter itemAdapter;


    private SQLiteHandler db;
    private SessionManager session;


    private String name = "Username";
    private String email;
    private String gender;
    private String uid;
    private String memberID;
    private String picture = "no";
    private String cgpa = "";
    private String credit = "";
    private String semester = "0";
    private String dept = "0";

    private String otherMemID;
    private String otherMemName;
    private String otherMemGender = "male";
    private String otherMemImage;

    private ActionBar actionBar;

    private EditText inputMessage;
    private ImageButton sendMessage;

    private int lastID = 988;

    private ChatDatabase chatDb;
    private ChatDao chatDao;

    private int chatStartID = 0;
    private int chatStartIDRefresh = 0;

    private int dbLength = 0;

    private boolean isLoaded = false;

    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;


    private MediaPlayer sound1;
    private MediaPlayer sound2;

    private MediaPlayer sound3;

    private ArrayList<Integer> sentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sentID = new ArrayList<>();



        Intent intent = getIntent();

        otherMemID = intent.getStringExtra("otherMemID");
        otherMemName = intent.getStringExtra("otherMemName");
        otherMemImage = otherMemID+".jpg";

        if(intent.hasExtra("otherMemGender"))
        otherMemGender = intent.getStringExtra("otherMemGender");



        actionBar.setTitle(otherMemName);


        sound1 =  MediaPlayer.create(ChatActivity.this, R.raw.chat_1);
        sound2 =  MediaPlayer.create(ChatActivity.this, R.raw.chat_2);
        sound3 =  MediaPlayer.create(ChatActivity.this, R.raw.bubble);




        db = new SQLiteHandler(getApplicationContext());


        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        uid = user.get("uid");
        email = user.get("email");
        gender = user.get("gender");
        memberID = user.get("memberID");
        picture = user.get("picture");

        cgpa = user.get("cgpa");
        credit = user.get("credit");
        semester = user.get("semester");
        dept = user.get("dept");


        String dbName = "chat_"+otherMemID;

        chatDb = Room.databaseBuilder(ChatActivity.this,
                ChatDatabase.class, dbName).allowMainThreadQueries().build();

        chatDao = chatDb.chatDao();

        dbLength = chatDao.count();

        if(dbLength > 0){

            chatStartID = chatDao.getLastIDJson();
            chatStartIDRefresh = chatDao.getLastID();

        }

        Log.d("LastID",chatDao.getLastIDJson()+"");



        itemList = new ArrayList<ChatItem>();
        itemAdapter = new ChatAdapter(R.layout.chat_recycler, itemList, ChatActivity.this, this, memberID, otherMemID, otherMemImage,otherMemGender);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());






        if(dbLength > 0)
            loadFromDb();


        if(Utils.isNetworkAvailable(ChatActivity.this))
            loadJson(uid,false);




        countDownTimer = new CountDownTimer(3000000, 5000) {

            public void onTick(long millisUntilFinished) {
                if(Utils.isNetworkAvailable(ChatActivity.this)) {

                    if(isLoaded) {

                      isLoaded = false;
                      loadJson(uid,true);

                    }

                }
            }

            public void onFinish() {

            }
        };

        countDownTimer.start();


        countDownTimer2 = new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                loadFromDbStart();

            }

            public void onFinish() {

            }
        };


        countDownTimer2.start();


        recyclerView.setAdapter(itemAdapter);


        inputMessage = findViewById(R.id.inputMessage);
        sendMessage = findViewById(R.id.sendMessage);




        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                sound1.start();
                String msg = inputMessage.getText().toString();

                long time = Calendar.getInstance().getTimeInMillis()/1000L;


                int randID = lastID+3524;

                itemList.add(new ChatItem(randID,memberID,otherMemID,msg,time));

                int postitionToRemove = itemList.size()-1;

                itemAdapter.notifyItemInserted(itemList.size()-1);

                inputMessage.setText("");


                recyclerView.smoothScrollToPosition(itemList.size()-1);

                sendMessage(otherMemID,msg,postitionToRemove);





            }
        });



        inputMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(itemList.size()>1) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(itemList.size() - 1);
                            }
                        });

                        recyclerView.scrollToPosition(itemList.size() - 1);

                    }
                }
            }
        });






    }



    public void loadFromDb(){


        List<ChatEntity> list = chatDao.getAllByTime();

        int oldSize = itemList.size();

        itemList.clear();


        for (int i = 0; i < list.size(); i++){


            int id = list.get(i).getMsg_id();

            String from = list.get(i).getUser_from();
            String to = list.get(i).getUser_to();
            String message = list.get(i).getMessage();
            long time = list.get(i).getTime();


            itemList.add(new ChatItem(id,from,to,message,time));




        }


        if(itemList.size()>1 && itemList.size() > oldSize) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(itemList.size() - 1);
                }
            });
        }
        itemAdapter.notifyDataSetChanged();






    }


    public void loadFromDbStart(){


        List<ChatEntity> list = chatDao.getAfterStart(chatStartIDRefresh);

        int oldSize = itemList.size();

        int counter = 0;
        boolean isSounded = false;

        for (int i = 0; i < list.size(); i++){

            int id = list.get(i).getMsg_id();
            boolean notExist = true;
            for(int k=0; k < itemList.size(); k++){
                if(itemList.get(k).getId() == id)
                {
                    notExist = false;
                    break;
                }
            }

            if(notExist) {
                String from = list.get(i).getUser_from();
                String to = list.get(i).getUser_to();
                String message = list.get(i).getMessage();
                long time = list.get(i).getTime();

                itemList.add(new ChatItem(id, from, to, message, time));

                itemAdapter.notifyItemChanged(itemList.size() - 1);

                counter++;

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(itemList.size() - 1);
                    }
                });

                if (!isSounded && !from.equals(memberID)) {
                    sound2.start();
                    isSounded = true;
                }
            }


        }

        chatStartIDRefresh = chatDao.getLastID();

        if(counter>0) {

        }
        }






    public void loadJson(final String uid, final boolean fromRefresh){



        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("uid", uid);
        parametters.put("otherMemID",otherMemID);
        parametters.put("chatStartID",String.valueOf(chatStartID));

        JSONParser parser = new JSONParser("https://nsuer.club/app/chat/message.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                try {


                    JSONArray obj = result.getJSONArray("messages");



                    JSONObject userInfo = result.getJSONObject("user");


                    String name = userInfo.getString("username");
                    String picture = userInfo.getString("picture");
                    String gender = userInfo.getString("gender");

                    itemAdapter.setOtherImage(picture);
                    itemAdapter.setOtherGender(gender);


                    actionBar.setTitle(name);



                    boolean isSounded = false;

                    for (int j = 0; j < obj.length(); j++) {

                        JSONObject data = obj.getJSONObject(j);


                        int id = data.getInt("id");



                        String from = data.getString("f");
                        String to = data.getString("t");
                        String message = data.getString("m");
                        long time = data.getLong("tm");

                        ChatEntity chatItem = new ChatEntity();


                        chatItem.setMsg_id(id);
                        chatItem.setUser_from(from);
                        chatItem.setUser_to(to);
                        chatItem.setMessage(message);
                        chatItem.setTime(time);
                        chatItem.setFrom_json(1);





                        //itemList.add(new ChatItem(id,from,to,message,time));



                        chatDao.insertAll(chatItem);

                        lastID = id;
                        chatStartID = id;

                        if(fromRefresh) {

                            if(!from.equals(memberID)) {
                                itemList.add(new ChatItem(id, from, to, message, time));

                                itemAdapter.notifyItemInserted(itemList.size()-1);

                                if(!isSounded) {
                                    sound2.start();
                                    isSounded = true;
                                }
                            }

                        }

                    }

                        loadFromDb();


                    isLoaded = true;
//
//                    if (fromRefresh && obj.length() > 0)
//                        itemAdapter.();



                } catch (JSONException e) {

                    Log.e("JSON", e.toString());
                }





            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }




    private void loadRecylcer(String string){


        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("messages");



            JSONObject userInfo = result.getJSONObject("user");


                String name = userInfo.getString("username");
                String picture = userInfo.getString("picture");
                String gender = userInfo.getString("gender");

                itemAdapter.setOtherImage(picture);
                itemAdapter.setOtherGender(gender);


                actionBar.setTitle(name);




            for (int j = 0; j < obj.length(); j++) {

                JSONObject data = obj.getJSONObject(j);


                int id = data.getInt("id");



                String from = data.getString("f");
                String to = data.getString("t");
                String message = data.getString("m");
                long time = data.getLong("tm");


                itemList.add(new ChatItem(id,from,to,message,time));

                lastID = id;


            }



        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }



//
//        if(itemList.size() > 0)
//            noItem.setVisibility(View.GONE);
//        else {
//
//            noItemText.setVisibility(View.VISIBLE);
//            noItemIcon.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//
//        }

        if(itemList.size()>1) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                        recyclerView.scrollToPosition(itemList.size() - 1);
                    }
                });
        }
        itemAdapter.notifyDataSetChanged();
    }



    private void sendMessage(final String to, final String message, final int postitionToRemove){


        isLoaded = true;


        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("uid", uid);
        parametters.put("to", to);
        parametters.put("message", message);


        final long unixTime = System.currentTimeMillis() / 1000L;

        final String timeStamp = String.valueOf(unixTime);



        JSONParser parser = new JSONParser("https://nsuer.club/app/chat/send-message.php", "GET", parametters);

        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                int id = 0;

                try {

                    if(!result.getBoolean("error")){
                        id = result.getInt("id");


                        ChatEntity chatItem = new ChatEntity();


                        chatItem.setMsg_id(id);
                        chatItem.setUser_from(memberID);
                        chatItem.setUser_to(to);
                        chatItem.setMessage(message);
                        chatItem.setTime(unixTime);

                        chatItem.setFrom_json(0);

                        chatDao.insertAll(chatItem);


                        itemList.remove(postitionToRemove);
                        itemAdapter.notifyItemRemoved(postitionToRemove);

                        loadFromDbStart();


                        sound3.start();

                        isLoaded = false;
                    }

                } catch (Exception e){

                    return;
                }





            }

            @Override
            public void onFailure() {
            }
        });
        parser.execute();


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        //Execute your code here


        setResult(Activity.RESULT_OK);

        countDownTimer.cancel();
        finish();

    }

}





