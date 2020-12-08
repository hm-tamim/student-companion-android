package club.nsuer.nsuer;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NsuNoticesAdapter extends RecyclerView.Adapter<NsuNoticesAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<NsuNoticesItem> itemList;
    private Context context;
    private String type;

    public NsuNoticesAdapter(int layoutId, ArrayList<NsuNoticesItem> itemList, Context context, String type) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

        CardView cardView = holder.cardView;


        TextView url = holder.url;

        TextView vType = holder.vType;


        vType.setText(type);

        url.setText(itemList.get(listPosition).getUrl());


        String sTitle = itemList.get(listPosition).getTitle();

        title.setText(sTitle);

        title.setMaxLines(2);

        // firstLetter.setText(sTitle.substring(0, 1));

        String fullDate = itemList.get(listPosition).getDate();


        Date dateFormated = null;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateFormated = format.parse(fullDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DateFormat formateDate = new SimpleDateFormat("dd");


        DateFormat formateMonth = new SimpleDateFormat("MMM");


        date.setText(formateDate.format(dateFormated));

        month.setText(formateMonth.format(dateFormated));


        //holder.viewPager.setId(listPosition);


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

        public TextView title;
        public TextView date;

        public TextView month;

        public TextView firstLetter;

        public CardView cardView;

        public ViewPager viewPager;

        public TextView url;

        public TextView vType;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            url = (TextView) itemView.findViewById(R.id.nsuNoticeUrl);
            vType = (TextView) itemView.findViewById(R.id.nsuNoticeType);

            title = (TextView) itemView.findViewById(R.id.nsuNoticesTitle);
            date = (TextView) itemView.findViewById(R.id.nsuNoticesDate);
            month = (TextView) itemView.findViewById(R.id.nsuNoticesMonth);

            viewPager = (ViewPager) itemView.findViewById(R.id.nsuNoticesViewPager);


        }


        @Override
        public void onClick(View view) {


            MainActivity main = MainActivity.getInstance();

            if (!Utils.isNetworkAvailable(main.getApplicationContext())) {
                Toast.makeText(main.getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }


            //Intent intent = new Intent(view.getContext(), NoticeBrowser.class);
            String nsuurl = "http://www.northsouth.edu/" + url.getText();

            Utils.CustomTab(nsuurl, view.getContext());

            //intent.putExtra("URL", nsuurl);

            //intent.putExtra("TYPE", vType.getText());


            //view.getContext().startActivity(intent);


            Log.d("onclick", "onClick " + getLayoutPosition() + " " + title.getText());

            Log.d("onclick", "onClick " + getLayoutPosition() + " " + url.getText());
        }


    }
}