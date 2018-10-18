package club.nsuer.nsuer;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class NsuNotices extends Fragment {

    MainActivity main;
    private NsuNoticesDatabase db;
    private Button dialogButton;
    private ArrayList<NsuNoticesItem> itemList;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private SimpleCursorAdapter mAdapter;
    private RecyclerView recyclerView2;
    NsuNoticesAdapter itemArrayAdapter2;

    private Context context;


    private ProgressBar p;


    private CardView pp;


    private View v;

    private String type = "notices";

    private int datePassed = 0;

    private String jsonCache = null;

    private SnapHelper snapHelper;
    private CacheHelper acCache;



    public NsuNotices() {
        // Required empty public constructor
    }


    public NsuNotices(String type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        main = (MainActivity) getActivity();

        context = getContext();


    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.reload_button, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navReloadButton:
                if(Utils.isNetworkAvailable(context)) {
                    loadJson(type);

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
        }else{
            // fragment is no longer visible
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        main.removeShadow();

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.notice_events_fragment, container, false);


        p = (ProgressBar) v.findViewById(R.id.progressBarNotice);




        // db.coursesDao().nukeTable();

        itemList = new ArrayList<NsuNoticesItem>();

        itemArrayAdapter2 = new NsuNoticesAdapter(R.layout.notice_events_recycler, itemList, context, type);



        recyclerView2 = (RecyclerView) v.findViewById(R.id.noticeEventRecycler);

        recyclerView2.setHasFixedSize(true);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());



        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_devider));


        recyclerView2.addItemDecoration(itemDecorator);




        final String key = type;


        acCache = new CacheHelper(context, key);


        int timeDiff = 9999;

        if (acCache.isExists()){

            jsonCache = acCache.retrieve();


            loadRecylcer(jsonCache);


            timeDiff = acCache.getTimeDiffMin();



        }

        if(!acCache.isExists() || timeDiff > 20 && Utils.isNetworkAvailable(getContext())) {


            loadJson(type);


        }





        recyclerView2.setAdapter(itemArrayAdapter2);


        return v;

    }



    private void loadJson(String type){

        String url;

        if(type.equals("events"))
            url = "https://nsuer.club/app/nsu-notice-events/events.json";
        else
            url = "https://nsuer.club/app/nsu-notice-events/notice.json";

        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("type", "nsuer");

        JSONParser parser = new JSONParser(url, "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                p.setVisibility(View.GONE);

                acCache.save(result.toString());

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

        p.setVisibility(View.GONE);

        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("dataArray");



            for (int j = 0; j < obj.length(); j++) {



                JSONObject data = obj.getJSONObject(j);


                String title = data.getString("title");
                String url = data.getString("url");
                String date = data.getString("date");

                if(j<15)
                    itemList.add(new NsuNoticesItem(title,date,url));


            }
        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }



        itemArrayAdapter2.notifyDataSetChanged();





    }




    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = main.getResources().getIdentifier("mdcolor_" + typeColor, "random_colors", main.getPackageName());
        if (arrayId != 0) {
            TypedArray colors = main.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }






}
