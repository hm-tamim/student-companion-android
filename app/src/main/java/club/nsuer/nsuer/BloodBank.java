package club.nsuer.nsuer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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


public class BloodBank extends Fragment {

    private MainActivity main;
    private Context context;
    private View v;
    private LinearLayout findDonors;
    private LinearLayout requstBlood;
    private LinearLayout beDonor;
    private LinearLayout bloodFact;
    private RecyclerView recyclerView;
    private BloodRequestAdapter adapter;
    private ArrayList<BloodRequestItem> itemList;
    private LinearLayout noItem;
    private ImageView noItemIcon;
    private TextView noItemText;
    private ProgressBar progressBar;

    private TextView beDonorText;

    private TextView requestsByMe;

    private ActionBar bar;
    private Window window;

    public BloodBank() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        main = (MainActivity) getActivity();
        context = getContext();
        bar = main.getSupportActionBar();

        main.removeShadow();

        window = main.getWindow();

        try {
            makeRed();
        } catch (Exception e){



        }
    }

    private void makeRed(){

        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blood_color)));
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.blood_color));
        main.setMenuBackground(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_blood_bank, container, false);


        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Blood Bank");


        beDonor = v.findViewById(R.id.beDonor);
        beDonorText = v.findViewById(R.id.beDonorText);
        if (main.getBloodGroup() >= 0){
            beDonorText.setText("My Profile");
        }

        findDonors = v.findViewById(R.id.findDonors);

        requstBlood = v.findViewById(R.id.requestBlood);

        requestsByMe = v.findViewById(R.id.myBtn);

        bloodFact = v.findViewById(R.id.bloodFact);

        requestsByMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,BloodRequestsByMe.class);


                startActivityForResult(intent, 10001);

                makeRed();

            }
        });


        beDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,BloodBeDonor.class);

                startActivity(intent);

                makeRed();

            }
        });

        findDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,BloodDonors.class);

                startActivity(intent);

                makeRed();

            }
        });

        requstBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,BloodRequest.class);

                startActivityForResult(intent, 10001);
                makeRed();

            }
        });

        bloodFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.CustomTab("https://nsuer.club/app/blood-bank/blood-facts.html",context);

            }
        });


        noItem = v.findViewById(R.id.noItem);
        noItemIcon = v.findViewById(R.id.noItemImage);
        noItemText = v.findViewById(R.id.noItemText);
        progressBar = v.findViewById(R.id.progressBar);

        recyclerView = v.findViewById(R.id.recyclerView);


        itemList = new ArrayList<BloodRequestItem>();
        adapter = new BloodRequestAdapter(R.layout.blood_request_recycler, itemList, context);


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

        parametters.put("memID","");

        JSONParser parser = new JSONParser("https://nsuer.club/app/blood-bank/blood-requests.php", "GET", parametters);


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


                        itemList.add(new BloodRequestItem(id, memID,name,postedOn,bgroup,bags,whenDate,phone,address,note,isManaged));

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






    @Override
    public void onResume() {
        super.onResume();

        makeRed();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            ft.detach(BloodBank.this).attach(BloodBank.this).commit();
    }



    public void onDestroyView(){
        super.onDestroyView();
        ActionBar bar = main.getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4196af")));

        Window window = main.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#388096"));
        main.setMenuBackground(false);


    }

}
