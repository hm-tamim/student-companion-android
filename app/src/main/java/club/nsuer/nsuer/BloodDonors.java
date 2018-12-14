package club.nsuer.nsuer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BloodDonors extends AppCompatActivity {

    private ArrayList<BloodDonorItem> itemList;
    private BloodDonorsAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private LinearLayout tabLayout;
    private int currentBtn = -1;
    private int numOfButton = 0;
    private FragmentTransaction ft;
    private LinearLayout noItem;
    private ImageView noCartIcon;
    private TextView noItemText;
    private ProgressBar progressBar;
    private String uid;
    private SearchView searchView;
    private MenuItem menuItem;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int startWith = 0;
    private String searchQuery = "";
    private ProgressBar loadingBar;
    private LinearLayoutManager mLayoutManager;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donors);


        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Find Blood Donors");
        actionBar.setElevation(0);

        itemList = new ArrayList<BloodDonorItem>();
        adapter = new BloodDonorsAdapter(R.layout.recycler_donor, itemList, BloodDonors.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        mLayoutManager = new LinearLayoutManager(context);


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_devider));


        recyclerView.addItemDecoration(itemDecorator);

        loadingBar = findViewById(R.id.bottomLoadingBar);


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        noItem = findViewById(R.id.noItem);
        noCartIcon = findViewById(R.id.noImage);
        noItemText = findViewById(R.id.noItemText);
        progressBar = findViewById(R.id.progressBar);



        loadJson(false, true,"0", 0, false,"");


        recyclerView.setAdapter(adapter);





        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;

                            loadingBar.setVisibility(View.VISIBLE);


                            if(currentBtn >= 0) {


                                loadJson(true, false, String.valueOf(currentBtn), startWith, false,"");

                            } else if(!searchQuery.equals("")){

                                loadJson(false, false, "00", startWith, true, searchQuery);



                            }else{

                                loadJson(false, false, "0", startWith, false, "");

                            }



                        }
                    }
                }
            }
        });





        ft = getSupportFragmentManager().beginTransaction();


        View.OnClickListener btnclick = new View.OnClickListener(){
            @Override
            public void onClick(View view){


                if (view instanceof Button){

                    int id = view.getId();

                    try {
                        loadByBtn(id);
                    } catch (Exception e){

                        Log.e("ClassMates", e.toString());

                        Toast.makeText(context,"No items found.", Toast.LENGTH_SHORT).show();
                    }



                }
            }
        };

        tabLayout = findViewById(R.id.tab);



        String[] categories = getResources().getStringArray(R.array.bloodGroups);


        for(int i = 0; i < categories.length; i++){


            int buttonStyle = R.style.ChipButtonShop;
            Button btnTag = new Button(new ContextThemeWrapper(this, buttonStyle), null, buttonStyle);
            btnTag.setOnClickListener(btnclick);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80);
            lp.setMarginStart(20);
            btnTag.setLayoutParams(lp);

            btnTag.setTextAppearance(this, R.style.ChipButtonShop);


            String courseName = categories[i];
            btnTag.setText(courseName);
            btnTag.setTag(courseName);
            btnTag.setId(numOfButton++);

            tabLayout.addView(btnTag);

        }



    }



    private void loadByBtn(int id){




        searchView.onActionViewCollapsed();

        startWith = 0;
        searchQuery = "";


        Button teButton = tabLayout.findViewById(id);

        String btnText = teButton.getText().toString();

        if(currentBtn == id){

            itemList.clear();
            adapter.notifyDataSetChanged();

            loadJson(false, true, String.valueOf(id), 0, false,"");

            Button tempBtn =  tabLayout.findViewById(id);
            String tempText = tempBtn.getText().toString();
            tempBtn.setBackground(context.getDrawable(R.drawable.chip_background_shop));
            tempBtn.setTextColor(Color.WHITE);

            currentBtn= -1;

            return;
        }

        currentBtn = id;

        itemList.clear();

        adapter.notifyDataSetChanged();




        teButton.setBackground(this.getDrawable(R.drawable.chip_background_shop_selected));
        teButton.setTextAppearance(this, R.style.ChipButtonShopSelected);





        loadJson(true,true, String.valueOf(id), 0, false,"");

        for(int i = 0; i < numOfButton; i++){

            Button tempBtn =  tabLayout.findViewById(i);
            String tempText = tempBtn.getText().toString();

            if(!tempText.equals(btnText)) {
                tempBtn.setBackground(context.getDrawable(R.drawable.chip_background_shop));
                tempBtn.setTextAppearance(context, R.style.ChipButtonShop);
                // tempBtn.setTextColor(Color.WHITE);
            }

        }

    }



    public void loadJson(final boolean loadByCat, final boolean clearItem, final String cat, final int start, final boolean loadBySearch, final String query){



        noItem.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noItemText.setVisibility(View.GONE);
        noCartIcon.setVisibility(View.GONE);

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("loadCat",String.valueOf(loadByCat));
        parametters.put("cat", cat);
        parametters.put("start", String.valueOf(start));
        parametters.put("loadSearch",String.valueOf(loadBySearch));
        parametters.put("query", query);

        JSONParser parser = new JSONParser("https://nsuer.club/app/blood-bank/donors.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                if(clearItem) {
                    itemList.clear();

                    startWith = 0;

                }

                loadRecylcer(result.toString());
                loading = true;
                loadingBar.setVisibility(View.GONE);



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

            JSONArray obj = result.getJSONArray("dataArray");



            for (int j = 0; j < obj.length(); j++) {

                JSONObject data = obj.getJSONObject(j);


                int id = Integer.parseInt(data.getString("memberID"));
                String name = data.getString("username");
                String bgroup = data.getString("bgroup");
                String gender = data.getString("gender");
                String image = data.getString("picture");
                String memID = data.getString("memberID");
                String address = data.getString("address");
                String phone = data.getString("phone");

                if (address.equals("") || address.equals("null"))
                    address = "Dhaka";


                itemList.add(new BloodDonorItem(name,memID,image,gender,bgroup,address,phone));

                    startWith = id;


            }



        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }




        if(itemList.size() > 0)
            noItem.setVisibility(View.GONE);
        else {

            noItemText.setVisibility(View.VISIBLE);
            noCartIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.search_button, menu);



        menuItem = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) menuItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                startWith = 0;


                if(currentBtn >= 0) {
                    Button tempBtn = tabLayout.findViewById(currentBtn);
                    String tempText = tempBtn.getText().toString();
                    tempBtn.setBackground(context.getDrawable(R.drawable.chip_background_shop));
                    tempBtn.setTextColor(Color.WHITE);

                }
                currentBtn= -1;


                currentBtn = -1;

                searchQuery = query;

                loadJson(false, true, "0", startWith, true, searchQuery);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;

            }



        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {

                itemList.clear();
                adapter.notifyDataSetChanged();

                loadJson(false, true, "0", 0, false, "");

                searchView.onActionViewCollapsed();
                return false;
            }
        });



        return true;



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
        finish();

    }




}
