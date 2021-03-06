
package club.nsuer.nsuer;


import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ScheduleOthersAdapter extends RecyclerView.Adapter<ScheduleOthersAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ScheduleOthersItem> itemList;
    private Context context;
    private ScheduleOthers instance;

    public ScheduleOthersAdapter(int layoutId, ArrayList<ScheduleOthersItem> itemList, Context context, ScheduleOthers instance) {
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


        boolean isPassed = false;


        String titleS = itemList.get(listPosition).getTitle();
        String typeS = itemList.get(listPosition).getType();

        String totalTitle = titleS;

        if (!typeS.equals(""))
            totalTitle += " - " + typeS;

        title.setText(totalTitle);


        holder.user.setText("By " + itemList.get(listPosition).getUser());


        long timeI = itemList.get(listPosition).getDate();

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

        if (noteS.equals(""))
            noteHolder.setVisibility(View.GONE);
        else
            note.setText(noteS);


        boolean doReminder = itemList.get(listPosition).isDoReminder();

        if (!doReminder)
            alarmIcon.setVisibility(View.GONE);


        isPassed = itemList.get(listPosition).isPassed();

        if (isPassed)
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_filled_ddd));
        else
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_ddd));


        holder.addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, AddSchedule.class);

                intent.putExtra("classmate", true);
                intent.putExtra("title", itemList.get(listPosition).getTitle());
                intent.putExtra("type", itemList.get(listPosition).getType());
                intent.putExtra("note", itemList.get(listPosition).getExtraNote());
                intent.putExtra("date", itemList.get(listPosition).getDate());
                intent.putExtra("reminderDate", itemList.get(listPosition).getReminderDate());
                intent.putExtra("color", itemList.get(listPosition).getColor());
                intent.putExtra("doRemind", itemList.get(listPosition).isDoReminder());

                context.startActivity(intent);
            }
        });


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
        public Button addButton;
        public TextView user;

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

            addButton = (Button) itemView.findViewById(R.id.findDonors);
            user = (TextView) itemView.findViewById(R.id.user);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            circle = (View) itemView.findViewById(R.id.acCircle);


        }


        @Override
        public void onClick(View view) {

        }


    }
}