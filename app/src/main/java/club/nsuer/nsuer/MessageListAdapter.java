package club.nsuer.nsuer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<MessageListItem> itemList;
    private Context context;
    private Messages instance;
    private String memID;


    public MessageListAdapter(int layoutId, ArrayList<MessageListItem> itemList, Context context, Messages instance, String memID) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
        this.memID = memID;
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
        TextView nameView = holder.name;
        TextView messageView = holder.message;
        ImageView imageView = holder.image;
        RelativeLayout relativeLayout = holder.relativeLayout;


        int id = itemList.get(listPosition).getId();
        final String name = itemList.get(listPosition).getName();
        String message = itemList.get(listPosition).getMessage();


        final String img = itemList.get(listPosition).getPicture();


        final String gender = itemList.get(listPosition).getGender();

        long time = itemList.get(listPosition).getTime();


        int placeH = R.drawable.ic_male_color;

        if (gender.equals("female"))
            placeH = R.drawable.ic_female_color;

        if (gender.equals("female"))
            imageView.setImageDrawable(context.getResources().getDrawable(placeH));
        else
            imageView.setImageDrawable(context.getResources().getDrawable(placeH));


        if (img.contains(".")) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(placeH);


            Glide.with(context)
                    .setDefaultRequestOptions(placeholderRequest)
                    .load("https://nsuer.club/images/profile_picture/" + img)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);

        }

        nameView.setText(name);


        holder.time.setText(Utils.getTimeAgoShop(time));

        messageView.setText(message);


        relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String openID;

                String from = itemList.get(listPosition).getUserFrom();

                String to = itemList.get(listPosition).getUserTo();

                if (memID.equals(to))
                    openID = from;
                else
                    openID = to;


                Intent intent = new Intent(context, ChatActivity.class);

                intent.putExtra("otherMemID", openID);
                intent.putExtra("otherMemName", name);
                intent.putExtra("otherMemGender", gender);

                instance.startActivityForResult(intent, 10001);


            }
        });


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public ImageView image;
        public TextView message;
        public RelativeLayout relativeLayout;
        public TextView time;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView) itemView.findViewById(R.id.name);
            message = (TextView) itemView.findViewById(R.id.message);

            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.image);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

        }


        @Override
        public void onClick(View view) {

        }


    }
}
