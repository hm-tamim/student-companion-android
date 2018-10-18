package club.nsuer.nsuer;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ClassMates extends Fragment {



    private MainActivity main;

    private Context context;

    private ArrayList<ClassMatesItem> itemList;
    private View view;

    private int datePassed = 0;

    private String jsonCache = null;
    private ClassMatesAdapter itemAdapter;
    private RecyclerView recyclerView;

    private LinearLayout noPost;

    private CacheHelper acCache;
    private CoursesDatabase db;
    private List<CoursesEntity> list;
    private String postz;
    private String myID;

    private LinearLayout noClassmates;
    private LinearLayout tabLayout;
    private HorizontalScrollView scrollView;
    private CardView cardView;
    private LinearLayout parent;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    int currentBtn = 0;
    int numOfButton = -1;


    public ClassMates() {
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

        view = inflater.inflate(R.layout.class_mates, container, false);




        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.reload_button, menu);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("Classmates");



        db = Room.databaseBuilder(context,
                CoursesDatabase.class, "courses").allowMainThreadQueries().build();


        list = db.coursesDao().getAll();



        myID = main.getMemberID();

        noClassmates = view.findViewById(R.id.noClassmates);


        itemList = new ArrayList<ClassMatesItem>();

        itemAdapter = new ClassMatesAdapter(R.layout.classmates_recycler, itemList, getContext(), main.getName());

        recyclerView = (RecyclerView) view.findViewById(R.id.classmatesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));


        recyclerView.addItemDecoration(itemDecorator);


        final String key = "classmates";


        acCache = new CacheHelper(context, key);


        int timeDiff = 9999;

        if (acCache.isExists() && list.size() > 0) {

            jsonCache = acCache.retrieve();


            try {


            loadRecylcer(jsonCache, "All");

            } catch (Exception e){
                Log.e("Classmate", e.toString());


            }

            timeDiff = acCache.getTimeDiffMin();


        }

        if(!acCache.isExists() || timeDiff > 10 && Utils.isNetworkAvailable(context) && list.size() > 0) {


            try {

                loadJson();

            } catch (Exception e){
                Log.e("Classmate", e.toString());
            }


        }


        recyclerView.setAdapter(itemAdapter);



        tabLayout = (LinearLayout) view.findViewById(R.id.tabLinearClassmates);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.tabScrollClassmates);
        parent = view.findViewById(R.id.classmateParentLinear);

        cardView = view.findViewById(R.id.classmatesCardView);

        cardView.setZ(6656.0f);


//
//
//
//
//        recyclerView.setOnTouchListener(new OnSwipeTouchListener(context) {
//            @Override
//            public void onSwipeLeft() {
//                if(currentBtn < numOfButton)
//                    loadByBtn(currentBtn+1);
//
//               // Toast.makeText(context,"Left" + currentBtn, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSwipeRight() {
//                if(currentBtn > 0)
//                    loadByBtn(currentBtn-1);
//
//
//            }
//        });



        View.OnClickListener btnclick = new View.OnClickListener(){
            @Override
            public void onClick(View view){


                if (view instanceof Button){

                    int id = view.getId();

                    try {
                        loadByBtn(id);
                    } catch (Exception e){

                        Log.e("ClassMates", e.toString());

                        Toast.makeText(context,"No Class mates found.", Toast.LENGTH_SHORT).show();
                    }



                }
            }
        };


        for(int i = -1; i < list.size(); i++){

            if(i<0) {

                int buttonStyle = R.style.ChipButtonSelected;
                Button btnTag = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
                btnTag.setOnClickListener(btnclick);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(230, 100);
                lp.setMarginStart(30);
                btnTag.setLayoutParams(lp);
                btnTag.setText("All");
                btnTag.setTag("All");
                btnTag.setId(numOfButton + 1);
                tabLayout.addView(btnTag);
                numOfButton++;
                currentBtn = 0;

            } else {

                int buttonStyle = R.style.ChipButton;
                Button btnTag = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
                btnTag.setOnClickListener(btnclick);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100);
                lp.setMarginStart(30);
                btnTag.setLayoutParams(lp);

                String courseName = list.get(i).getCourse();
                btnTag.setText(courseName);
                btnTag.setTag(courseName);
                btnTag.setId(numOfButton + 1);
                String lastLetter = courseName.substring(courseName.length() - 1);


                if(!lastLetter.equals("L")) {
                    tabLayout.addView(btnTag);
                    numOfButton++;
                }
            }
        }








    }

    private void loadByBtn(int id){

        Button teButton = tabLayout.findViewById(id);

        String btnText = teButton.getText().toString();

        currentBtn = id;

        teButton.setBackground(context.getDrawable(R.drawable.chips_background_selected));
        teButton.setTextAppearance(context, R.style.ChipButtonSelected);

        itemList.clear();
        loadRecylcer(acCache.retrieve(), btnText);

        // Toast.makeText(context,btnText,Toast.LENGTH_SHORT).show();


        for(int i = -1; i < numOfButton; i++){

            Button tempBtn =  tabLayout.findViewById(i+1);
            String tempText = tempBtn.getText().toString();

            if(!tempText.equals(btnText)) {
                tempBtn.setBackground(context.getDrawable(R.drawable.chips_background));
                tempBtn.setTextAppearance(context, R.style.ChipButton);
            }

        }

    }


    private void loadJson(){


        HashMap<String, String> parametters = new HashMap<String, String>();

        if(list.size()<1)
            return;


        String course0 = list.get(0).getCourse();
        String section0 = list.get(0).getSection();

        String allCourse = course0 + "." + section0;


        for (int i=1; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String section = list.get(i).getSection();

            String lastLetter = course.substring(course.length() - 1);

            if(!lastLetter.equals("L"))
                allCourse += ","+course + "." + section;


        }

        parametters.put("course", allCourse);

        JSONParser parser = new JSONParser("https://nsuer.club/app/classmates/get-json-2.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                acCache.clear();

                acCache.save(result.toString());

                itemList.clear();

                loadRecylcer(result.toString(), "All");
                //Toast.makeText(getContext(),"Updated!",Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();



    }



    private void loadRecylcer(String string, String tabChips){

      //  ProgressBar p = (ProgressBar) view.findViewById(R.id.acProgressBar);

//        p.setVisibility(View.GONE);

        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("dataArray");



            for (int j = 0; j < obj.length(); j++) {

                JSONObject data = obj.getJSONObject(j);

                String name = data.getString("username");
                String course = data.getString("course");
                String gender = data.getString("gender");
                String image = data.getString("picture");
                String memID = data.getString("memberID");
                String email = data.getString("email");


                if(!memID.equals(myID)) {
                    if(tabChips.equals("All")) {
                        itemList.add(new ClassMatesItem(name, course, memID, image, gender, email));
                    } else if(course.contains(tabChips))
                    {

                        String dept = data.getString("dept");

                        String[] deptArray = getResources().getStringArray(R.array.deptShort);

                        String deptString = deptArray[Integer.parseInt(dept)];

                        itemList.add(new ClassMatesItem(name, deptString, memID, image, gender, email));
                    }

                }
            }

            if(itemList.size() > 1)
                noClassmates.setVisibility(View.GONE);

        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }



        itemAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(0);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navReloadButton:
                if(Utils.isNetworkAvailable(context)) {
                    loadJson();
                    loadByBtn(0);

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
