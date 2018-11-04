package club.nsuer.nsuer;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ScheduleItem> itemList;
    private Context context;
    private ScheduleFragment instance;


    public ScheduleAdapter(int layoutId, ArrayList<ScheduleItem> itemList,Context context, ScheduleFragment instance) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
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


    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView title = holder.title;
        TextView date = holder.date;
        TextView month = holder.month;
        TextView time = holder.time;
        TextView note = holder.note;
        ImageView alarmIcon = holder.alarmIcon;
        RelativeLayout noteHolder = holder.noteHolder;
        CardView cardView = holder.cardView;
        View circle = holder.circle;



        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                instance.openActivityWithId(itemList.get(listPosition).getId());

            }
        });

        boolean isPassed = false;


        String titleS = itemList.get(listPosition).getTitle();
        String typeS = itemList.get(listPosition).getType();

        String totalTitle = titleS;

        if(!typeS.equals(""))
         totalTitle += " - " + typeS;

        title.setText(totalTitle);



        long timeI =  itemList.get(listPosition).getDate();

        long timestamp = (long) timeI * 1000L;

        //TimeZone tz = TimeZone.getTimeZone("GMT-6");

        SimpleDateFormat dateSdf = new SimpleDateFormat("dd", Locale.ENGLISH);
        //dateSdf.setTimeZone(tz);

        SimpleDateFormat monthSdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
        //monthSdf.setTimeZone(tz);

        SimpleDateFormat timeSdf = new SimpleDateFormat("EEE, hh:mm a", Locale.ENGLISH);
        //timeSdf.setTimeZone(tz);



        String dateS = dateSdf.format(new Date(timestamp));
        String monthS = monthSdf.format(new Date(timestamp));
        String timeS = timeSdf.format(new Date(timestamp));

        date.setText(dateS);
        month.setText(monthS);
        time.setText(timeS);

        cardView.setCardBackgroundColor(itemList.get(listPosition).getColor());




        String noteS = itemList.get(listPosition).getExtraNote();

        if(noteS.equals(""))
            noteHolder.setVisibility(View.GONE);
        else
            note.setText(noteS);


        boolean doReminder = itemList.get(listPosition).isDoReminder();

        if(!doReminder)
            alarmIcon.setVisibility(View.GONE);







        isPassed = itemList.get(listPosition).isPassed();

        if(isPassed)
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_filled_ddd));
        else
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_ddd));




    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView date;
        public TextView month;
        public TextView time;
        public TextView note;
        public ImageView alarmIcon;
        public RelativeLayout noteHolder;
        public CardView cardView;

        public View circle;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);
            month = (TextView) itemView.findViewById(R.id.month);
            note = (TextView) itemView.findViewById(R.id.note);
            noteHolder = (RelativeLayout) itemView.findViewById(R.id.noteHolder);
            alarmIcon = (ImageView) itemView.findViewById(R.id.alarmIcon);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            circle = (View) itemView.findViewById(R.id.acCircle);





        }


        @Override
        public void onClick(View view) {

        }



    }
}