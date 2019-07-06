package club.nsuer.nsuer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BloodRequestsByMe extends AppCompatActivity {

    private SessionManager session;
    private Context context;
    private int bloodGroup;
    private String uid;
    private String memberID;
    private MainActivity main;
    private View v;
    private LinearLayout findDonors;
    private LinearLayout requstBlood;
    private LinearLayout beDonor;
    private LinearLayout bloodFact;
    private RecyclerView recyclerView;
    private BloodRequestsByMeAdapter adapter;
    private ArrayList<BloodRequestItem> itemList;
    private LinearLayout noItem;
    private ImageView noItemIcon;
    private TextView noItemText;
    private ProgressBar progressBar;
    private TextView beDonorText;
    private TextView requestsByMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_requests_by_me);

        context = this;

        session = new SessionManager(context);

        uid = session.getUid();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Blood Requests By Me");


        noItem = findViewById(R.id.noItem);
        noItemIcon = findViewById(R.id.noItemImage);
        noItemText = findViewById(R.id.noItemText);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        itemList = new ArrayList<BloodRequestItem>();
        adapter = new BloodRequestsByMeAdapter(R.layout.blood_requests_by_me_recycler, itemList, context, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadJson();
        recyclerView.setAdapter(adapter);

    }


    public void loadJson(){

        noItem.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noItemText.setVisibility(View.GONE);
        noItemIcon.setVisibility(View.GONE);

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("memID",session.getMemberID());

        JSONParser parser = new JSONParser("https://nsuer.club/apps/blood-bank/blood-requests.php", "GET", parametters);

        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                try {


                    JSONArray obj = result.getJSONArray("dataArray");



                    for (int j = 0; j < obj.length(); j++) {

                        JSONObject data = obj.getJSONObject(j);


                        int id = data.getInt("id");

                        String memID = data.getString("memID");
                        String name = data.getString("name");
                        int bgroup = Integer.parseInt(data.getString("bgroup"));

                        String whenDateS = data.getString("whenDate");
                        long whenDate = Long.parseLong(whenDateS);

                        String postedOnS = data.getString("date");
                        long postedOn = Long.parseLong(postedOnS);

                        String bags = data.getString("bags");
                        String phone = data.getString("phone");
                        String address = data.getString("address");
                        String note = data.getString("note");

                        String isMan = data.getString("isManaged");
                        boolean isManaged = false;

                        if(isMan.equals("1"))
                            isManaged = true;


                        itemList.add(new BloodRequestItem(id,memID,name,postedOn,bgroup,bags,whenDate,phone,address,note,isManaged));

                    }


                } catch (JSONException e) {

                    Log.e("JSON", e.toString());
                }




                if(itemList.size() > 0)
                    noItem.setVisibility(View.GONE);
                else {

                    noItemText.setVisibility(View.VISIBLE);
                    noItemIcon.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }






    public void sendDelete(int listPosition){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getId();



        itemList.remove(listPosition);
        adapter.notifyItemRemoved(listPosition);
        adapter.notifyItemChanged(listPosition);
        adapter.notifyItemRangeChanged(listPosition, itemList.size());


        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/blood-bank/delete.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {



                Toast.makeText(context,"You blood request is deleted.", Toast.LENGTH_SHORT).show();

              //  loadJson();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }



    public void sendManaged(int listPosition){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getId();


        final boolean isManaged = itemList.get(listPosition).isManaged();
        int setTo;

        if(isManaged)
            setTo = 0;
        else
            setTo = 1;




        itemList.get(listPosition).setManaged((!isManaged));
        adapter.notifyItemChanged(listPosition);

        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);
        parametters.put("managed",String.valueOf(setTo));

        JSONParser parser = new JSONParser("https://nsuer.club/apps/blood-bank/mark-managed.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {



                if(!isManaged)
                    Toast.makeText(context,"Marked as managed", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"Marked as not managed", Toast.LENGTH_SHORT).show();


               // loadJson();



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
        setResult(Activity.RESULT_OK);
        finish();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, BloodRequestsByMe.class);
            startActivity(refresh);
            this.finish();
        }

    }


}
