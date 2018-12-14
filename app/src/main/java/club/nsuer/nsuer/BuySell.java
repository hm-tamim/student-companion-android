package club.nsuer.nsuer;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BuySell extends Fragment {


    private View view;

    private MainActivity main;
    private Context context;


    private ArrayList<BuySellItem> itemList;
    private BuySellAdapter itemAdapter;
    private RecyclerView recyclerView;


    private ArrayList<BuySellItem> itemListMy;
    private BuySellMyAdapter itemAdapterMy;
    private RecyclerView recyclerViewMy;



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

    private GridLayoutManager mLayoutManager;

    public BuySell() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        main = (MainActivity) getActivity();
        context =getContext();


    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
       // inflater.inflate(R.menu.main, menu);


        inflater.inflate(R.menu.sell_button, menu);
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
                itemAdapter.notifyDataSetChanged();

                loadJson(false, true, "0", 0, false, "");

                searchView.onActionViewCollapsed();
                return false;
            }
        });






    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navSellButton:
                loadMyAds(uid);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.buy_sell_fragment, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("BuySell Shop");

        main.removeShadow();

        uid = main.getUid();

        itemList = new ArrayList<BuySellItem>();
        itemAdapter = new BuySellAdapter(R.layout.buy_sell_recycler, itemList, context, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        loadingBar = view.findViewById(R.id.bottomLoadingBar);


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        noItem = view.findViewById(R.id.noItem);
        noCartIcon = view.findViewById(R.id.noCart);
        noItemText = view.findViewById(R.id.noItemText);
        progressBar = view.findViewById(R.id.progressBar);



        loadJson(false, true,"0", 0, false,"");


        recyclerView.setAdapter(itemAdapter);





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







        addButton = view.findViewById(R.id.addButton);



        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context,BuySellCreate.class);
                startActivityForResult(intent, 10001);

            }
        });



        ft = main.getSupportFragmentManager().beginTransaction();


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

        tabLayout = view.findViewById(R.id.tab);



        String[] categories = getResources().getStringArray(R.array.shopCat);


        for(int i = 0; i < categories.length; i++){


                int buttonStyle = R.style.ChipButtonShop;
                Button btnTag = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
                btnTag.setOnClickListener(btnclick);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80);
                lp.setMarginStart(20);
                btnTag.setLayoutParams(lp);

                btnTag.setTextAppearance(context, R.style.ChipButtonShop);


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
            itemAdapter.notifyDataSetChanged();

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

        itemAdapter.notifyDataSetChanged();




        teButton.setBackground(context.getDrawable(R.drawable.chip_background_shop_selected));
        teButton.setTextAppearance(context, R.style.ChipButtonShopSelected);





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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {


            currentBtn = -1;
            numOfButton = 0;
            searchQuery = "";
            ft.detach(BuySell.this).attach(BuySell.this).commit();


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

        JSONParser parser = new JSONParser("https://nsuer.club/app/buy-sell/get-all.php", "GET", parametters);


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

                String seller = data.getString("sn");
                int sellerID = data.getInt("si");
                long time = data.getLong("tm");
                String title = data.getString("t");
                String price = data.getString("p");
                String description = data.getString("d");
                int category = data.getInt("c");

                int id = data.getInt("id");


                int sold = data.getInt("s");

                int approved = data.getInt("a");

                String img = time + ".jpg";

                itemList.add(new BuySellItem(id, seller, sellerID, title, price, img, time, category, description, sold, approved));

                if (j == 0){

                    startWith = id;
                }

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
        itemAdapter.notifyDataSetChanged();


    }



    public void loadItemDetails(int listPosition){



        final Dialog dialog=new Dialog(context,android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));



        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.buy_sell_item_details);


        TextView title = dialog.findViewById(R.id.title);
        TextView time = dialog.findViewById(R.id.time);
        TextView price = dialog.findViewById(R.id.price);
        TextView seller = dialog.findViewById(R.id.editButton);
        TextView description = dialog.findViewById(R.id.description);
        FloatingActionButton button = dialog.findViewById(R.id.findDonors);

        ImageView image = dialog.findViewById(R.id.image);

        TextView cat = dialog.findViewById(R.id.category);




        ImageView back = dialog.findViewById(R.id.back);

        back.bringToFront();



        int id = itemList.get(listPosition).getId();
        final String titleS = itemList.get(listPosition).getTitle();
        String priceSs = itemList.get(listPosition).getPrice();
        long timeL = itemList.get(listPosition).getTime();
        final String sellerS = itemList.get(listPosition).getSellerName();
        final int sellerID = itemList.get(listPosition).getSellerID();

        int category = itemList.get(listPosition).getCategory();

        String img = itemList.get(listPosition).getImageUrl();

        String timeS = Utils.getTimeAgo((int)itemList.get(listPosition).getTime());

        String descriptionS = itemList.get(listPosition).getDescription();


        final String priceS;

        if(Utils.isNumeric(priceSs))
             priceS = "৳ "+priceSs;
        else
             priceS = priceSs;

        title.setText(titleS);
        price.setText(priceS);
        seller.setText(sellerS);
        description.setText(descriptionS);
        time.setText(timeS);

        Picasso.get()
                .load("https://nsuer.club/images/shop/"+img)
                .placeholder(R.drawable.default_image)
                .into(image);



        String catName = getResources().getStringArray(R.array.shopCat)[category];


        cat.setText(catName);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context,ChatActivity.class);

                intent.putExtra("otherMemID", String.valueOf(sellerID));
                intent.putExtra("otherMemName", sellerS);

                String pretext = titleS+"\nPrice: " + priceS  + "\n\nI'm interested in this item.";

                intent.putExtra("pretext", pretext);

                context.startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.dismiss();

            }
        });

        dialog.show();







    }




    public void loadMyAds(String uid){



        final Dialog dialog=new Dialog(context,android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));



        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.buy_sell_my_ads);



        itemListMy = new ArrayList<BuySellItem>();
        itemAdapterMy = new BuySellMyAdapter(R.layout.buy_sell_my_ads_recycler, itemListMy, context, this, dialog);
        recyclerViewMy = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerViewMy.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        recyclerViewMy.setItemAnimator(new DefaultItemAnimator());

        TextView titlebar = dialog.findViewById(R.id.title);



        recyclerViewMy.setAdapter(itemAdapterMy);







        final LinearLayout noItem = dialog.findViewById(R.id.noItem);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        final ImageView noCartIcon = dialog.findViewById(R.id.noCart);
        final TextView noItemText = dialog.findViewById(R.id.noItemText);






        noItemText.setVisibility(View.GONE);
        noCartIcon.setVisibility(View.GONE);


        noItem.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("uid",uid);

        JSONParser parser = new JSONParser("https://nsuer.club/app/buy-sell/get-by-uid.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray obj = result.getJSONArray("dataArray");



                    for (int j = 0; j < obj.length(); j++) {



                        boolean liked = false;


                        JSONObject data = obj.getJSONObject(j);

                        String seller = data.getString("sn");
                        int sellerID = data.getInt("si");
                        long time = data.getLong("tm");
                        String title = data.getString("t");
                        String price = data.getString("p");
                        String description = data.getString("d");
                        int category = data.getInt("c");

                        int id = data.getInt("id");


                        int sold = data.getInt("s");

                        int approved = data.getInt("a");

                        String img = time+".jpg";

                        itemListMy.add(new BuySellItem(id,seller,sellerID,title,price,img,time,category,description, sold, approved));


                    }


                    itemAdapterMy.notifyDataSetChanged();

                    if(itemListMy.size() > 0)
                        noItem.setVisibility(View.GONE);
                    else {


                        noItemText.setVisibility(View.VISIBLE);
                        noCartIcon.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }



                } catch (JSONException e) {

                    Log.e("JSON", e.toString());

                    noItemText.setVisibility(View.VISIBLE);
                    noCartIcon.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure() {

                noItemText.setVisibility(View.VISIBLE);
                noCartIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


            }
        });


        parser.execute();





        dialog.show();



        titlebar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });




    }





    public void sendDelete(int listPosition){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemListMy.get(listPosition).getId();



        itemListMy.remove(listPosition);
        itemAdapterMy.notifyItemRemoved(listPosition);
        itemAdapterMy.notifyItemChanged(listPosition);
        itemAdapterMy.notifyItemRangeChanged(listPosition, itemListMy.size());


        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/app/buy-sell/delete.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {



                Toast.makeText(context,"Your item is deleted", Toast.LENGTH_SHORT).show();

                loadJson(false,true,"0", 0, false,"");



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }



    public void sendSold(int listPosition){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemListMy.get(listPosition).getId();


        final int checkSold = itemListMy.get(listPosition).getSold();
        int setTo;

        if(checkSold == 0)
            setTo = 1;
        else
            setTo = 0;




        itemListMy.get(listPosition).setSold(setTo);
        itemAdapterMy.notifyItemChanged(listPosition);

        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);
        parametters.put("sold",String.valueOf(setTo));

        JSONParser parser = new JSONParser("https://nsuer.club/app/buy-sell/sold.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {



                if(checkSold == 0)
                    Toast.makeText(context,"Your item is marked as sold", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"Your item is marked as unsold", Toast.LENGTH_SHORT).show();


                loadJson(false,true,"0", 0, false,"");



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }








}
