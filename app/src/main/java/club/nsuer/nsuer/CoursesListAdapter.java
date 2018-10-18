package club.nsuer.nsuer;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.ViewHolder> {

    private int listCoursesListItemLayout;
    private ArrayList<CoursesListItem> CoursesListItemList;
    private MainActivity main;
    private Context MyContext;

    private int lastPosition = -1;
    private int animateDelay = 30;
    private String uid;



    public CoursesListAdapter(int layoutId, ArrayList<CoursesListItem> CoursesListItemList, Context context, String uid) {
        listCoursesListItemLayout = layoutId;
        this.CoursesListItemList = CoursesListItemList;
        this.MyContext = context;
        this.uid = uid;
    }

    @Override
    public int getItemCount() {

        return CoursesListItemList == null ? 0 : CoursesListItemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(listCoursesListItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);


        return myViewHolder;
    }


    private void setAnimation(View viewToAnimate, int position)
    {


//        Animation animation = AnimationUtils.loadAnimation(MyContext,
//                (position > lastPosition) ? R.animator.up_from_bottom
//                        : R.animator.down_from_top);
//        viewToAnimate.startAnimation(animation);
//        lastPosition = position;


        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(MyContext, R.animator.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        TextView coursesName = holder.CoursesListItem;
        TextView classStart = holder.classStart;
        TextView classEnd = holder.classEnd;
        TextView classRoomNo = holder.classRoomNo;
        TextView faculty = holder.faculty;
        TextView day = holder.day;
        TextView section = holder.section;


        String sec = CoursesListItemList.get(listPosition).getSection() + "sec";

        SpannableStringBuilder mSSBuilder = new SpannableStringBuilder(sec);

        SuperscriptSpan superscriptSpan = new SuperscriptSpan();



        // Apply the SuperscriptSpan
        mSSBuilder.setSpan(
                superscriptSpan,
                sec.indexOf("sec"),
                sec.indexOf("sec") + String.valueOf("sec").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Minimize the Subscript and Superscript text size
        //showSmallSizeText("2");
        //showSmallSizeText("5");


        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(.25f);

        // Apply the RelativeSizeSpan to display small text
        mSSBuilder.setSpan(
                relativeSizeSpan,
                sec.indexOf("sec"),
                sec.indexOf("sec") + String.valueOf("sec").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        section.setText(mSSBuilder);


        coursesName.setText(CoursesListItemList.get(listPosition).getName());
        classStart.setText(CoursesListItemList.get(listPosition).getStart());
        classEnd.setText(CoursesListItemList.get(listPosition).getEnd());
        classRoomNo.setText(CoursesListItemList.get(listPosition).getRoom());
        faculty.setText(CoursesListItemList.get(listPosition).getFaculty());
        day.setText(CoursesListItemList.get(listPosition).getDay());
        //section.setText();



        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                PopupMenu popup = new PopupMenu(MyContext, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.courses_recycler_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cr_action_remove:


                                if(!Utils.isNetworkAvailable(MyContext)) {
                                    Toast.makeText(MyContext, "Internet connection required.", Toast.LENGTH_SHORT).show();
                                    break;
                                }



                                HashMap<String, String> parametters = new HashMap<String, String>();


                                final String course = CoursesListItemList.get(listPosition).getName();
                                final String section = CoursesListItemList.get(listPosition).getSection();






                                CoursesListItemList.remove(listPosition);
                                notifyItemRemoved(listPosition);
                                notifyItemChanged(listPosition);
                                notifyItemRangeChanged(listPosition, CoursesListItemList.size());


                                String allCourse = course + "." + section;


                                FirebaseMessaging.getInstance().unsubscribeFromTopic(allCourse);


                                parametters.put("course", allCourse);
                                parametters.put("uid", uid);

                                JSONParser parser = new JSONParser("https://nsuer.club/app/courses/delete.php", "GET", parametters);


                                parser.setListener(new JSONParser.ParserListener() {
                                    @Override
                                    public void onSuccess(JSONObject result) {



                                        CoursesDatabase db = Room.databaseBuilder(MyContext,
                                                CoursesDatabase.class, "courses").allowMainThreadQueries().build();
                                        db.coursesDao().deleteByCourseName(course);


                                        BooksDatabase dbBooks = Room.databaseBuilder(MyContext,
                                                BooksDatabase.class, "books").allowMainThreadQueries().build();
                                        dbBooks.booksDao().deleteByCourseName(course);

                                        FacultiesDatabase dbFaculties = Room.databaseBuilder(MyContext,
                                                FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();

                                        dbFaculties.facultiesDao().deleteByCourse(course);






                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });


                                parser.execute();

                                break;

                            case R.id.cr_action_none:
                                //handle menu2 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });



        if(listPosition % 7 == 0){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#0fa1e4"));
            // #2196f3 #409de0 #39afd5  #7c94b8 #72c769 #ee945d #2d566b

        }else if(listPosition % 7 == 1){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#227585"));

            // #39d2e3 #72c769
        }else if(listPosition % 7 == 2){
            //#7c94b6

            holder.cardView.setCardBackgroundColor(Color.parseColor("#009688"));

        }else if(listPosition % 7 == 3){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#00bbd4"));
            // #ff9800
        }else if(listPosition % 7 == 4){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#1c7cb9"));
        }else if(listPosition % 7 == 5){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#2d566b"));
        }else if(listPosition % 7 == 6){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#2196f3"));
        }



        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setAnimation(holder.itemView, listPosition);
            }
        }, animateDelay);

        animateDelay += 10;




    }




    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView CoursesListItem;
        public TextView classStart;
        public TextView classEnd;
        public TextView classRoomNo;
        public TextView faculty;
        public TextView day;
        public TextView section;

        public CardView cardView;
        public ImageView buttonViewOption;



        public ViewHolder(View CoursesListItemView) {

            super(CoursesListItemView);
            CoursesListItemView.setOnClickListener(this);

            cardView = (CardView) CoursesListItemView.findViewById(R.id.rcCardView);

            CoursesListItem = (TextView) CoursesListItemView.findViewById(R.id.rcCourseName);
            classStart = (TextView) CoursesListItemView.findViewById(R.id.classStartCourses);
            classEnd = (TextView) CoursesListItemView.findViewById(R.id.classEndCourses);
            classRoomNo = (TextView) CoursesListItemView.findViewById(R.id.classRoomCourses);
            faculty = (TextView) CoursesListItemView.findViewById(R.id.facultyCourses);
            day = (TextView) CoursesListItemView.findViewById(R.id.rcCourseDay);
            section = (TextView) CoursesListItemView.findViewById(R.id.rcCourseSection);
            buttonViewOption = (ImageView) CoursesListItemView.findViewById(R.id.rcOptionButton);

        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + CoursesListItem.getText());
        }



    }
}