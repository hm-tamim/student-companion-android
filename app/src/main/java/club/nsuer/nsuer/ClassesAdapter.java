package club.nsuer.nsuer;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ClassesItem> itemList;
    private Context context;


    public ClassesAdapter(int layoutId, ArrayList<ClassesItem> itemList,Context context) {
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

        TextView time = holder.time;
        TextView event = holder.event;
        View circle = holder.circle;

        String isPassed = "no";


        time.setText(itemList.get(listPosition).getTime());
        event.setText(itemList.get(listPosition).getEvent());



        isPassed = itemList.get(listPosition).getColor();

        if(isPassed.equals("passed"))
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_filled_ddd));
        else
            circle.setBackground(ContextCompat.getDrawable(context, R.drawable.ac_circle_ddd));





    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView time;
        public TextView event;
        public View circle;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            time = (TextView) itemView.findViewById(R.id.crTime);
            event = (TextView) itemView.findViewById(R.id.crEvent);
            circle = (View) itemView.findViewById(R.id.crCircle);





        }


        @Override
        public void onClick(View view) {

        }



    }
}