package club.nsuer.nsuer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class AcademicCalendarAdapter extends RecyclerView.Adapter<AcademicCalendarAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<AcademicCalendarItem> itemList;
    private Context context;


    public AcademicCalendarAdapter(int layoutId, ArrayList<AcademicCalendarItem> itemList,Context context) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
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
        TextView date = holder.date;
        TextView month = holder.month;
        TextView day = holder.day;
        TextView event = holder.event;
        View circle = holder.circle;

        boolean isPassed = false;


        date.setText(itemList.get(listPosition).getDate());
        month.setText(itemList.get(listPosition).getMonth());
        day.setText(itemList.get(listPosition).getDay());
        event.setText(itemList.get(listPosition).getEvent());

        isPassed = itemList.get(listPosition).isPassed();

        if(isPassed)
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_filled_ddd));
        else
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_ddd));





//
//
//
//        if(listPosition % 7 == 0){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#227585"));
//            // #2196f3 #409de0 #39afd5  #7c94b8 #72c769 #ee945d  #f95b45
//
//        }else if(listPosition % 7 == 1){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#009688"));
//
//            // #39d2e3 #72c769
//        }else if(listPosition % 7 == 2){
//            //#7c94b6
//
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#17a2b8"));
//
//        }else if(listPosition % 7 == 3){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#1c7cb9"));
//            // #ff9800
//        }else if(listPosition % 7 == 4){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#2d566b"));
//        }else if(listPosition % 7 == 5){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#05879f"));
//        }else if(listPosition % 7 == 6){
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#f03a76"));
//        }


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView date;
        public TextView month;
        public TextView day;
        public TextView event;
        public View circle;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            date = (TextView) itemView.findViewById(R.id.acDate);
            month = (TextView) itemView.findViewById(R.id.acMonth);
            day = (TextView) itemView.findViewById(R.id.acDay);
            event = (TextView) itemView.findViewById(R.id.acEvent);
            circle = (View) itemView.findViewById(R.id.acCircle);





        }


        @Override
        public void onClick(View view) {

       }



    }
}