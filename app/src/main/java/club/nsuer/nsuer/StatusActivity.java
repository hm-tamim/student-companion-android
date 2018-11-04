package club.nsuer.nsuer;


import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Loader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class StatusActivity extends Fragment {



    private MainActivity main;

    private Context context;

    private ArrayList<StatusItem> itemList;
    private View view;

    private int datePassed = 0;

    private String jsonCache = null;
    private StatusAdapter itemAdapter;
    private RecyclerView recyclerView;
    private LinearLayout noPost;

    private CacheHelper acCache;
    private CoursesDatabase db;
    private List<CoursesEntity> list;
    private String postz;
    private String ownMemID;
    private MediaPlayer mp;

    private int notificationType = 0;
    private int openID = 0;



    public void likeSound(){

        mp.start();
    }

    public StatusActivity() {
        // Required empty public constructor
    }

    public StatusActivity(int notificationType, int openID) {

        this.notificationType = notificationType;
        this.openID = openID;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


        main = (MainActivity) getActivity();


        ownMemID = main.getMemberID();

        context = getContext();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.status_activity, container, false);;

        return view;
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
                if(Utils.isNetworkAvailable(context))
                    loadJson(true);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("Newsfeed");

        main.resetShadow();

        //main.removeShadow();



        db = Room.databaseBuilder(context,
                CoursesDatabase.class, "courses").allowMainThreadQueries().build();


        mp = MediaPlayer.create(context, R.raw.bubble);


        list = db.coursesDao().getAll();

        itemList = new ArrayList<StatusItem>();

        itemAdapter = new StatusAdapter(R.layout.status_recycler, itemList, getContext(), main.getMemberID(), main.getUid(), this);

        recyclerView = (RecyclerView) view.findViewById(R.id.statusRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setAdapter(itemAdapter);




        noPost = view.findViewById(R.id.noStatus);



        final String key = "newsfeed";


        acCache = new CacheHelper(context, key);


        int timeDiff = 9999;

        if (acCache.isExists() && list.size() > 0){

            jsonCache = acCache.retrieve();


            loadRecylcer(jsonCache);


            timeDiff = acCache.getTimeDiffMin();

          //  Log.e("Cache", jsonCache);
          //  Log.e("Time", String.valueOf(timeDiff));

        }

        if(!acCache.isExists() || timeDiff > 2 && Utils.isNetworkAvailable(context) && list.size() > 0 || notificationType != 0) {


                        loadJson(true);


        }

        if(notificationType != 0){

            if(notificationType == 1){


                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getId() == openID) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }

            }


            if(notificationType == 2){


                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getId() == openID) {




                        String thisMsgID = String.valueOf(itemList.get(i).getId());
                        String creatorName = itemList.get(i).getName();
                        String creatorID = itemList.get(i).getMemID();
                        String courseGroup = itemList.get(i).getCourse();
                        String creatorGender = itemList.get(i).getGender();
                        String creatorPic = itemList.get(i).getPicture();
                        String creatorPost = itemList.get(i).getPost();
                        String postTime = itemList.get(i).getTime();

                        AlertComment(thisMsgID,creatorName,creatorGender,creatorPic,creatorID,courseGroup,creatorPost,postTime);



                        break;
                    }
                }

            }


        }











        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.AddPostFloatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!Utils.isNetworkAvailable(getContext())) {
                    Toast.makeText(getContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(list.size() < 1){

                    Toast.makeText(getContext(), "Add some courses first.", Toast.LENGTH_SHORT).show();
                    return;

                }


                final Dialog dialog = new Dialog(getContext(), R.style.WideDialog);


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 200);

                dialog.setContentView(R.layout.alert_status);

                final EditText postInput = (EditText) dialog.findViewById(R.id.createPostInput);


                //courseInput.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));




                List<String> spinnerArray =  new ArrayList<String>();


                for (int i=0; i < list.size(); i++) {


                    String course = list.get(i).getCourse();
                    String section = list.get(i).getSection();

                    String lastLetter = course.substring(course.length() - 1);

                    if(!lastLetter.equals("L"))
                        spinnerArray.add(course + "." + section);


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner sItems = (Spinner) dialog.findViewById(R.id.alertStatusSpinner);
                sItems.setAdapter(adapter);

                sItems.invalidate();







                ImageView closeButton = (ImageView) dialog.findViewById(R.id.aabCloseButton);


                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {




                        // dialogButton.setBackgroundColor(Color.RED);

                        CircularProgressButton btn = (CircularProgressButton) dialog.findViewById(R.id.dialogButtonOK);

                        btn.startAnimation();


                        HashMap<String, String> parametters = new HashMap<String, String>();

                        String uid = main.getUid();

                        final String coursez = sItems.getItemAtPosition(sItems.getSelectedItemPosition()).toString();

                        String posts = postInput.getText().toString();

                        try {
                            postz = new String(posts.getBytes("UTF-8"), "UTF-8");
                        } catch (Exception e){


                        }

                        parametters.put("uid", uid);
                        parametters.put("course", coursez);
                        parametters.put("post",postz);

                        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/create-post-2.php", "GET", parametters);



                        parser.setListener(new JSONParser.ParserListener() {
                            @Override
                            public void onSuccess(JSONObject result) {


                                int id = 0;

                                try {

                                    if(!result.getBoolean("error")){
                                        id = result.getInt("id");

                                        String topic = "COMMENT."+id;

                                        Log.d("topic",topic);

                                        FirebaseMessaging.getInstance().subscribeToTopic(topic);

                                    }

                                } catch (Exception e){

                                    return;
                                }


                                long unixTime = System.currentTimeMillis() / 1000L;

                                String timeStamp = String.valueOf(unixTime);


                                // itemList.add();



                                itemAdapter.addStutusTop(new StatusItem(id,main.getName(),coursez,main.getPicture(),timeStamp,main.getGender(),main.getMemberID(),postz,0,0,false));

                                itemAdapter.notifyDataSetChanged();

                                recyclerView.smoothScrollToPosition(0);

                                dialog.dismiss();

                                loadJson(false);


                            }

                            @Override
                            public void onFailure() {
                                dialog.dismiss();
                            }
                        });
                        parser.execute();

                    }


                });


                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });

    }




    public void loadJson(final boolean loadReycler){


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

        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/get-json-2.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                acCache.save(result.toString());

                if(loadReycler) {
                    itemList.clear();
                    loadRecylcer(result.toString());
                }

                //Toast.makeText(getContext(),"Updated!",Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }



    private void loadRecylcer(String string){

        //  ProgressBar p = (ProgressBar) view.findViewById(R.id.acProgressBar);

//        p.setVisibility(View.GONE);


       // Log.d("newsfeed",string);

        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("dataArray");



            for (int j = 0; j < obj.length(); j++) {



                boolean liked = false;


                JSONObject data = obj.getJSONObject(j);

                String name = data.getString("username");
                String course = data.getString("course");
                String picture = data.getString("picture");
                String memID = data.getString("memberID");
                String gender = data.getString("gender");
                String post = data.getString("post");
                String time = data.getString("time");

                String likedBy = data.getString("likedBy");
                int likes = data.getInt("likes");
                int comments = data.getInt("comments");

                int id = data.getInt("id");



                int ownIntMemID = Integer.parseInt(ownMemID);

                JSONArray likedJSON = new JSONArray(likedBy);

                for (int l = 0; l < likedJSON.length(); l++) {

                    if(likedJSON.get(l).toString().equals(ownMemID))
                        liked = true;
                }


                itemList.add(new StatusItem(id,name,course,picture,time,gender,memID,post,likes,comments,liked));

            }

            if(itemList.size() > 0)
                noPost.setVisibility(View.GONE);

        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }






        itemAdapter.notifyDataSetChanged();








    }



    public void  AlertComment(final String msgID, String creatorName, String creatorGender, String picUrl, final String creatorID, String courseGroup, String creatorPost, String creatorTime) {


        final Dialog dialog = new Dialog(context, R.style.WideDialogComments);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.alert_comments);

        TextView name = (TextView) dialog.findViewById(R.id.statusName);
        name.setText(creatorName);

        TextView course = (TextView) dialog.findViewById(R.id.statusCourse);
        course.setText(courseGroup);

        ImageView image = (ImageView) dialog.findViewById(R.id.statusImage);

        if(!picUrl.equals("0")) {
            Picasso.get()
                    .load("https://nsuer.club/images/profile_picture/" + picUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .centerCrop(Gravity.TOP)
                    .into(image);

        }else {
            if(creatorGender.equals("female"))
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female_user));
            else
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male_user));
        }

        ImageView menu = (ImageView) dialog.findViewById(R.id.statusMenu);
        TextView time = (TextView) dialog.findViewById(R.id.statusTime);
        String date = Utils.getTimeAgo(Integer.parseInt(creatorTime));
        time.setText(date);
        TextView post = (TextView) dialog.findViewById(R.id.statusPost);
        post.setText(creatorPost);

        final String thisMsgID = String.valueOf(msgID);



        final ArrayList commentList = new ArrayList<CommentItem>();

        final CommentAdapter commentAdapter = new CommentAdapter(R.layout.alert_comment_recycler, commentList, context, main.getMemberID(), main.getUid(), this);

        final RecyclerView commentRecyclerView = (RecyclerView) dialog.findViewById(R.id.commentRecyclerView);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentRecyclerView.setItemAnimator(new DefaultItemAnimator());


        final EditText commentInput = dialog.findViewById(R.id.commentInput);
        final ImageButton commentButton = dialog.findViewById(R.id.commentButton);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                HashMap<String, String> parametters = new HashMap<String, String>();


                commentInput.setEnabled(false);
                commentButton.setEnabled(false);

                commentButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_loading_win10));

                final String comment = commentInput.getText().toString();

                parametters.put("msgID", msgID);
                parametters.put("uid", main.getUid());
                parametters.put("comment", comment);
                parametters.put("creatorID", creatorID);


                long unixTime = System.currentTimeMillis() / 1000L;

                final String timeStamp = String.valueOf(unixTime);



                JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/create-comment-2.php", "GET", parametters);

                parser.setListener(new JSONParser.ParserListener() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        commentInput.setText("");

                        commentInput.setEnabled(true);
                        commentButton.setEnabled(true);

                        commentButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_send));



                        int id = 0;

                        try {

                            if(!result.getBoolean("error")){
                                id = result.getInt("id");

                            }

                        } catch (Exception e){

                            return;
                        }


                        commentList.add(new CommentItem(main.getName(),main.getGender(),main.getPicture(),id,main.getMemberID(),comment,timeStamp));

                        String commentTopic = "COMMENT."+msgID;
                        FirebaseMessaging.getInstance().subscribeToTopic(commentTopic);

                        commentAdapter.notifyDataSetChanged();

                        commentRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                commentRecyclerView.smoothScrollToPosition(commentList.size() - 1);
                            }
                        });


                    }

                    @Override
                    public void onFailure() {
                    }
                });
                parser.execute();





                InputMethodManager inputManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                // check if no view has focus:
                View currentFocusedView = dialog.getCurrentFocus();
                if (currentFocusedView != null) {
                    inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }


            }

        });







        HashMap<String, String> parametters = new HashMap<String, String>();


        parametters.put("msgID", msgID);


        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/comment.php", "GET", parametters);


        final TextView noComment = dialog.findViewById(R.id.noComments);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linearLayout);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        noComment.setVisibility(View.GONE);

        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                progressBar.setVisibility(View.GONE);


                try {

                    commentList.clear();



                    JSONArray obj = result.getJSONArray("dataArray");



                    for (int j = 0; j < obj.length(); j++) {



                        JSONObject data = obj.getJSONObject(j);

                        String name = data.getString("username");
                        String picture = data.getString("picture");
                        int memIDs = data.getInt("memberID");

                        String commentMemID = String.valueOf(memIDs);

                        String gender = data.getString("gender");
                        String text = data.getString("comment");
                        String time = data.getString("commentTime");
                        int commentID = data.getInt("id");




                        commentList.add(new CommentItem(name,gender,picture,commentID,commentMemID,text,time));

                    }



                    commentAdapter.notifyDataSetChanged();


                    if(commentList.size() < 1)
                        noComment.setVisibility(View.VISIBLE);
                    else{
                        linearLayout.setVisibility(View.GONE);

                        commentRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                commentRecyclerView.smoothScrollToPosition(commentList.size() - 1);
                            }
                        });

                    }

                } catch (JSONException e) {

                    // Log.e("JSON", e.toString());
                }






            }

            @Override
            public void onFailure() {
            }
        });
        parser.execute();




        commentRecyclerView.setAdapter(commentAdapter);


    }

    public void  AlertWhoLiked(String msgID){


        final Dialog dialog = new Dialog(context, R.style.WideDialogComments);


        //dialog.setTitle("Add Course & Section");

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        dialog.setContentView(R.layout.alert_who_liked);


        final ArrayList likerList = new ArrayList<ClassMatesItem>();

        final ClassMatesAdapter likerAdapter = new ClassMatesAdapter(R.layout.classmates_recycler, likerList, context, main.getName());

        final RecyclerView likerReyclerView = (RecyclerView) dialog.findViewById(R.id.likeRecyclerView);
        likerReyclerView.setLayoutManager(new LinearLayoutManager(context));
        likerReyclerView.setItemAnimator(new DefaultItemAnimator());

        likerReyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        likerReyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_devider));


        likerReyclerView.addItemDecoration(itemDecorator);





        //likerList.add(new ClassMatesItem("Tamim", "CSE", "20", "/20.jpg", "make", "hmtamim"));


        HashMap<String, String> parametters = new HashMap<String, String>();


        parametters.put("msgID", msgID);


        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/wholiked.php", "GET", parametters);


        final TextView noLike = dialog.findViewById(R.id.noLike);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linearLayout);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        noLike.setVisibility(View.GONE);



        likerReyclerView.setAdapter(likerAdapter);



        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                progressBar.setVisibility(View.GONE);





                try {

                    likerList.clear();




                    JSONArray obj = result.getJSONArray("dataArray");


                    Log.d("Length", " " + obj.length());

                    for (int j = 0; j < obj.length(); j++) {


                        JSONObject data = obj.getJSONObject(j);

                        String name = data.getString("username");
                        String picture = data.getString("picture");

                        String email = data.getString("email");



                        String dept = data.getString("dept");

                        String[] deptArray = getResources().getStringArray(R.array.deptShort);

                        String deptString = deptArray[Integer.parseInt(dept)];


                        int memIDs = data.getInt("memberID");

                        String memID = String.valueOf(memIDs);

                        String gender = data.getString("gender");



                         likerList.add(new ClassMatesItem(name, deptString, memID, picture, gender, email));


                    }



                    likerAdapter.notifyDataSetChanged();







                    if(likerList.size() < 1)
                        noLike.setVisibility(View.VISIBLE);
                    else{
                        linearLayout.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {

                    // Log.e("JSON", e.toString());
                }

            }

            @Override
            public void onFailure() {
            }
        });
        parser.execute();


    }







}
