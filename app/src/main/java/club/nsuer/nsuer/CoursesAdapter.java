package club.nsuer.nsuer;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<Item> itemList;
    private Context context;
    private Drawable doneIcon;
    private Drawable progressIcon;

    public CoursesAdapter(int layoutId, ArrayList<Item> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
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


        context = parent.getContext();

        doneIcon = context.getResources().getDrawable(R.drawable.ic_ok_round);
        progressIcon = context.getResources().getDrawable(R.drawable.ic_progress_round);

        return myViewHolder;
    }


    public String timeConverter(String time, int type) {

        try {
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            if (type == 24) {
                String t24 = date24Format.format(date12Format.parse(time));
                return t24;
            } else {
                String t12 = date12Format.format(date24Format.parse(time));
                return t12;
            }
        } catch (final ParseException e) {
            e.printStackTrace();

            return "nothing";
        }

    }


    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        TextView classStart = holder.classStart;
        TextView classEnd = holder.classEnd;
        ImageView isClassComplete = holder.isClassComplete;
        TextView classRoom = holder.classRoom;


        String startTime24 = itemList.get(listPosition).getStart();
        String startTime = timeConverter(startTime24, 12);


        String endTime24 = itemList.get(listPosition).getEnd();
        String endTime = timeConverter(endTime24, 12);


        item.setText(itemList.get(listPosition).getName());
        classStart.setText(startTime);

        classEnd.setText(endTime);


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String currentTime = sdf.format(new Date());
        currentTime = timeConverter(currentTime, 24);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        dateFormat.format(date);

        if (itemList.get(listPosition).getDate() == "today") {


            try {


                if (dateFormat.parse(currentTime).after(dateFormat.parse(startTime24)))
                    isClassComplete.setImageDrawable(progressIcon);

                if (dateFormat.parse(currentTime).after(dateFormat.parse(endTime24)))
                    isClassComplete.setImageDrawable(doneIcon);


            } catch (Exception e) {
            }


        }

        classRoom.setText(itemList.get(listPosition).getRoom());


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView item;
        public TextView classStart;
        public TextView classEnd;
        ImageView isClassComplete;
        public TextView classRoom;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.courseNameRecycler);
            classStart = (TextView) itemView.findViewById(R.id.classStartRecycler);
            classEnd = (TextView) itemView.findViewById(R.id.classEndRecycler);
            classRoom = (TextView) itemView.findViewById(R.id.classRoomNo);
            isClassComplete = (ImageView) itemView.findViewById(R.id.isClassDone);

        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }


    }
}