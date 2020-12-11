package club.nsuer.nsuer;


import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<CommentItem> itemList;
    private Context context;
    private String memID;
    private String uid;
    private StatusActivity instanse;


    public CommentAdapter(int layoutId, ArrayList<CommentItem> itemList, Context context, String memID, String uid, StatusActivity instance) {
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


    public void addComment(CommentItem commentItem) {
        this.itemList.add(getItemCount(), commentItem);
        notifyItemInserted(getItemCount());
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
        TextView name = holder.name;
        TextView time = holder.time;
        TextView text = holder.text;
        TextView delete = holder.delete;
        ImageView image = holder.image;

        name.setText(itemList.get(listPosition).getName());
        text.setText(itemList.get(listPosition).getText());

        final String picUrl = itemList.get(listPosition).getPicture();

        final String gender = itemList.get(listPosition).getGender();


        final String commentMemID = itemList.get(listPosition).getMemID();

        final int commentID = itemList.get(listPosition).getCommentID();


        if (commentMemID.equals(memID))
            delete.setVisibility(View.VISIBLE);
        else
            delete.setVisibility(View.GONE);

        int placeH = R.drawable.ic_male_color;

        if (gender.equals("female"))
            placeH = R.drawable.ic_female_color;

        image.setImageDrawable(context.getResources().getDrawable(placeH));

        if (!picUrl.equals("0")) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(placeH);


            Glide.with(context)
                    .setDefaultRequestOptions(placeholderRequest)
                    .load("https://nsuer.club/images/profile_picture/" + picUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);

        }


        String date = Utils.getTimeAgo(Integer.parseInt(itemList.get(listPosition).getTime()));
        time.setText(date);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                sendDelete(listPosition);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });


    }


    private void sendReport(int listPosition) {


        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getCommentID();


        itemList.remove(listPosition);
        notifyItemRemoved(listPosition);
        notifyItemChanged(listPosition);
        notifyItemRangeChanged(listPosition, itemList.size());


        parametters.put("msgID", String.valueOf(msgID));
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/newsfeed/comment-delete.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                instanse.loadJson(true);

                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }


    private void sendDelete(int listPosition) {


        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, String> parametters = new HashMap<String, String>();


        final int msgID = itemList.get(listPosition).getCommentID();


        String commentId = String.valueOf(itemList.get(listPosition).getCommentID());

        itemList.remove(listPosition);
        notifyItemRemoved(listPosition);
        notifyItemChanged(listPosition);
        notifyItemRangeChanged(listPosition, itemList.size());


        parametters.put("msgID", commentId);
        parametters.put("uid", uid);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/newsfeed/delete-comment.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                Toast.makeText(context, "Comment Deleted", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure() {

            }
        });


        parser.execute();

    }


    private String convertTime(String time) {


        try {
            String timeS = time;

            long unix_seconds = Long.decode(timeS);

            Date date = new Date(unix_seconds * 1000L);

            SimpleDateFormat jdf = new SimpleDateFormat("MMM dd 'at' h:mm a");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-6"));
            String java_date = jdf.format(date);

            return java_date;


        } catch (Exception e) {

            return null;

        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public ImageView image;
        public TextView time;
        public TextView text;
        public TextView delete;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView) itemView.findViewById(R.id.commenterName);
            image = (ImageView) itemView.findViewById(R.id.commenterPicture);

            time = (TextView) itemView.findViewById(R.id.commentTime);
            text = (TextView) itemView.findViewById(R.id.commentText);

            delete = (TextView) itemView.findViewById(R.id.commentDelete);


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
