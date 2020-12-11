package club.nsuer.nsuer;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class FacultyPredictorAdapter extends RecyclerView.Adapter<FacultyPredictorAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<FacultyPredictorItem> itemList;
    private Context context;

    public FacultyPredictorAdapter(int layoutId, ArrayList<FacultyPredictorItem> itemList, Context context) {
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
        TextView faculty = holder.faculty;
        TextView section = holder.section;


        TextView time = holder.time;
        TextView previousSections = holder.previousSections;

        faculty.setText(itemList.get(listPosition).getFaculty());
        section.setText(itemList.get(listPosition).getSection());

        time.setText(itemList.get(listPosition).getTime());
        previousSections.setText(Html.fromHtml(itemList.get(listPosition).getPreviousSections()));


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

        public TextView faculty;
        public TextView section;
        public TextView time;
        public TextView previousSections;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            faculty = (TextView) itemView.findViewById(R.id.fpFaculty);
            section = (TextView) itemView.findViewById(R.id.fpSection);
            time = (TextView) itemView.findViewById(R.id.fpTime);
            previousSections = (TextView) itemView.findViewById(R.id.fpPrevious);


        }


        @Override
        public void onClick(View view) {

//
//            MainActivity main = MainActivity.getInstance();
//
//            if(!Utils.isNetworkAvailable(main.getApplicationContext())) {
//                Toast.makeText(main.getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//            Intent intent = new Intent(view.getContext(), NoticeBrowser.class);
//            String nsuurl = "http://www.northsouth.edu/" + url.getText();
//
//            intent.putExtra("URL", nsuurl);
//
//            intent.putExtra("TYPE", vType.getText());
//
//
//            view.getContext().startActivity(intent);
//
//
//            Log.d("onclick", "onClick " + getLayoutPosition() + " " + title.getText());
//
//            Log.d("onclick", "onClick " + getLayoutPosition() + " " + url.getText());
        }


    }
}