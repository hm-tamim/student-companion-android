package club.nsuer.nsuer;

import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<StatusItem> itemList;
    private Context context;
    private String memID;
    private String uid;
    private StatusActivity instanse;
    private String dp;



    public StatusAdapter(int layoutId, ArrayList<StatusItem> itemList, Context context, String memID, String uid, StatusActivity instance) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.memID = memID;
        this.uid = uid;
        this.instanse = instance;
    }



    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);

        return myViewHolder;
    }

    public void addStutusTop(StatusItem statusItem){
        this.itemList.add(0, statusItem);
        notifyItemInserted(0);
    }


    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView name = holder.name;
        TextView course = holder.course;
        TextView time = holder.time;
        TextView post = holder.post;
        final Button likeButton = holder.like;
        Button commentButton = holder.comment;
        Button reportButton = holder.report;



        ImageView image = holder.image;
        final ImageView menu = holder.menu;

        name.setText(itemList.get(listPosition).getName());
        course.setText(itemList.get(listPosition).getCourse());
        post.setText(itemList.get(listPosition).getPost());

        final String picUrl = itemList.get(listPosition).getPicture();

        final String gender = itemList.get(listPosition).getGender();

        int placeH = R.drawable.ic_male_color;

        if(gender.equals("female"))
            placeH = R.drawable.ic_female_color;

        image.setImageDrawable(context.getResources().getDrawable(placeH));

        if(!picUrl.equals("0")) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(placeH);


            Glide.with(context)
                    .setDefaultRequestOptions(placeholderRequest)
                    .load("https://nsuer.club/images/profile_picture/" + picUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);

        }


        String timeString = itemList.get(listPosition).getTime();

        int timeInt = Integer.parseInt(timeString);

        String dates = Utils.getTimeAgo(timeInt);
        time.setText(dates);



        int comments = itemList.get(listPosition).getComments();



        String commentText = " Comment";
        if(comments < 1)
            commentText = "Comment";
        else if(comments == 1)
            commentText = "1 Comment";
        else
            commentText = comments + " Comments";


        commentButton.setText(commentText);


        boolean isLiked = itemList.get(listPosition).isLiked();
        int likes = itemList.get(listPosition).getLikes();


        String likeText = " Like";
        if(likes < 1)
            likeText = "Like";
        else if(likes == 1)
            likeText = "1 Like";
        else
            likeText = likes + " Likes";

        if(isLiked) {


            Drawable img = context.getResources().getDrawable( R.drawable.ic_heart_filled);
            img.setBounds( 0, 0, 50, 50);
            likeButton.setCompoundDrawables(img, null, null, null);
            likeButton.setText(likeText);


        } else {


            Drawable img = context.getResources().getDrawable( R.drawable.ic_heart_outline);
            img.setBounds( 0, 0, 52, 52);
            likeButton.setCompoundDrawables(img, null, null, null);
            likeButton.setText(likeText);

        }



        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String creatorID = itemList.get(listPosition).getMemID();
                sendLike(listPosition, likeButton, creatorID);


            }
        });



        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(context,"Post reported!",Toast.LENGTH_SHORT).show();

            }
        });


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String thisMsgID = String.valueOf(itemList.get(listPosition).getId());
                String creatorName = itemList.get(listPosition).getName();
                String creatorID = itemList.get(listPosition).getMemID();
                String courseGroup = itemList.get(listPosition).getCourse();
                String creatorGender = itemList.get(listPosition).getGender();
                String creatorPic = itemList.get(listPosition).getPicture();
                String creatorPost = itemList.get(listPosition).getPost();
                String postTime = itemList.get(listPosition).getTime();

                instanse.AlertComment(thisMsgID,creatorName,creatorGender,creatorPic,creatorID,courseGroup,creatorPost,postTime);



            }
        });





        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popup = new PopupMenu(context, menu);
                //inflating menu from xml resource


                String thisMemID = itemList.get(listPosition).getMemID();


                if(thisMemID.equals(memID))
                    popup.inflate(R.menu.newsfeed_menu_own);
                else
                    popup.inflate(R.menu.newsfeed_menu_others);


                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cr_action_remove:


                                new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                                        .setMessage("Do you really want to delete?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                sendDelete(listPosition);

                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();

                                break;

                            case R.id.cr_action_edit:


                                final Dialog dialog = new Dialog(context, R.style.WideDialog);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.setContentView(R.layout.alert_post_edit);
                                final CircularProgressButton btn = (CircularProgressButton) dialog.findViewById(R.id.editButton);
                                final EditText post = dialog.findViewById(R.id.editPost);
                                post.setText(itemList.get(listPosition).getPost());

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        btn.startAnimation();

                                        String newPost = post.getText().toString();

                                        sendEdit(listPosition, newPost);

                                        dialog.dismiss();

                                    }
                                });
                                break;

                            case R.id.cr_action_who_liked:
                                instanse.AlertWhoLiked(String.valueOf(itemList.get(listPosition).getId()));

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });


    }


    public ArrayList<StatusItem> getItemList(){

        return itemList;

    }






    private void sendEdit(int listPosition, String post){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getId();

        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("post",post);
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/edit-post.php", "POST", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                instanse.loadJson(true);

                Toast.makeText(context,"Post Edited", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }

    private void sendDelete(int listPosition){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getId();



        itemList.remove(listPosition);
        notifyItemRemoved(listPosition);
        notifyItemChanged(listPosition);
        notifyItemRangeChanged(listPosition, itemList.size());


        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/delete.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                instanse.loadJson(true);

                Toast.makeText(context,"Post Deleted", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }


    private void setLike(Button button, int listPosition){



        boolean isLiked = itemList.get(listPosition).isLiked();
        int likes = itemList.get(listPosition).getLikes();

        if(isLiked) {

            likes--;

            String likeText = " Like";
            if(likes < 1)
                likeText = "Like";
            else if(likes == 1)
                likeText = "1 Like";
            else
                likeText = likes + " Likes";


            itemList.get(listPosition).setLikes(likes);

            Drawable img = context.getResources().getDrawable( R.drawable.ic_heart_outline);
            img.setBounds( 0, 0, 52, 52);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(likeText);


            itemList.get(listPosition).setLiked(false);
        } else {

            likes++;

            String likeText = " Like";
            if(likes < 1)
                likeText = "Like";
            else if(likes == 1)
                likeText = "1 Like";
            else
                likeText = likes + " Likes";


            itemList.get(listPosition).setLikes(likes);

            Drawable img = context.getResources().getDrawable( R.drawable.ic_heart_filled);
            img.setBounds( 0, 0, 50, 50);
            button.setCompoundDrawables(img, null, null, null);


            button.setText(likeText);


            itemList.get(listPosition).setLiked(true);

        }


    }
    private void setDislike(Button button, int listPosition){

        Drawable img = context.getResources().getDrawable( R.drawable.ic_heart_outline);
        img.setBounds( 0, 0, 52, 52);

       // button.setTextColor(Color.parseColor("#55555"));
        button.setCompoundDrawables(img, null, null, null);
        //button.setTypeface(null, Typeface.NORMAL);
        button.setText("Like");

    }


    private void sendLike(int listPosition, final Button likeButton, final String creatorID){



        if(!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }


        instanse.likeSound();

        setLike(likeButton, listPosition);

        HashMap<String, String> parametters = new HashMap<String, String>();

        final int msgID = itemList.get(listPosition).getId();


        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);
        parametters.put("creatorID", creatorID);

        JSONParser parser = new JSONParser("https://nsuer.club/app/newsfeed/like-2.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                instanse.loadJson(false);


            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView course;
        public ImageView menu;
        public ImageView image;
        public TextView time;
        public TextView post;
        public Button like;
        public Button comment;
        public Button report;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView) itemView.findViewById(R.id.statusName);
            course = (TextView) itemView.findViewById(R.id.statusCourse);
            image = (ImageView) itemView.findViewById(R.id.statusImage);
            menu = (ImageView) itemView.findViewById(R.id.statusMenu);

            time = (TextView) itemView.findViewById(R.id.statusTime);
            post = (TextView) itemView.findViewById(R.id.statusPost);

            like = (Button) itemView.findViewById(R.id.likeButton);
            comment = (Button) itemView.findViewById(R.id.commentButtton);
            report = (Button) itemView.findViewById(R.id.reportButton);


        }


        @Override
        public void onClick(View view) {

        }



    }
}