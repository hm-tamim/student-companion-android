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


    public AcademicCalendarAdapter(int layoutId, ArrayList<AcademicCalendarItem> itemList, Context context) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
    }


    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();
    }


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

        if (isPassed)
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_filled_ddd));
        else
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_ddd));


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