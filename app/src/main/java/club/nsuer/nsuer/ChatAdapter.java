package club.nsuer.nsuer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ChatItem> itemList;
    private Context context;
    private ChatActivity instance;
    private String memID;

    private String otherMemID;
    private String otherImage;
    private String otherGender;

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;


    public ChatAdapter(int layoutId, ArrayList<ChatItem> itemList, Context context, ChatActivity instance, String memID, String otherMemID, String otherImage, String otherGender) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
        this.memID = memID;
        this.otherMemID = otherMemID;
        this.otherImage = otherImage;
        this.otherGender = otherGender;
    }

    public String getOtherMemID() {
        return otherMemID;
    }

    public void setOtherMemID(String otherMemID) {
        this.otherMemID = otherMemID;
    }

    public String getOtherImage() {
        return otherImage;
    }

    public void setOtherImage(String otherImage) {
        this.otherImage = otherImage;
    }

    public String getOtherGender() {
        return otherGender;
    }

    public void setOtherGender(String otherGender) {
        this.otherGender = otherGender;
    }

    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.chat_recycler_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.chat_recycler, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
            default:
                View viewChatOtherr = layoutInflater.inflate(R.layout.chat_recycler, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOtherr);
        }
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {

        String from = itemList.get(listPosition).getFrom();


        if(from.equals(memID)){


            configureMyChatViewHolder((MyChatViewHolder) holder, listPosition);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, listPosition);
        }
    }


    private void configureMyChatViewHolder(final MyChatViewHolder holder, final int listPosition) {
        final TextView messageView = holder.message;
        final TextView timeView = holder.time;

        int id = itemList.get(listPosition).getId();
        final String message = itemList.get(listPosition).getMessage();
        final long time = itemList.get(listPosition).getTime();

        holder.time.setText(Utils.getTimeAgoChat(time));
        messageView.setText(message);


        messageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String timeString = Utils.getTimeAgoShop(time);

                if(itemList.get(listPosition).isShowTime()) {
                    holder.time.setVisibility(View.GONE);
                    itemList.get(listPosition).setShowTime(false);
                } else {
                    holder.time.setVisibility(View.VISIBLE);itemList.get(listPosition).setShowTime(true);
                }
            }
        });
    }

    private void configureOtherChatViewHolder(final OtherChatViewHolder holder, final int listPosition) {
        final TextView messageView = holder.message;
        final ImageView imageView = holder.image;

        int id = itemList.get(listPosition).getId();
        final String message = itemList.get(listPosition).getMessage();
        final long time = itemList.get(listPosition).getTime();

        holder.time.setText(Utils.getTimeAgoChat(time));
        messageView.setText(message);



        int placeH = R.drawable.ic_male_color;

        if(otherGender.equals("female"))
            placeH = R.drawable.ic_female_color;

        imageView.setImageDrawable(context.getResources().getDrawable(placeH));

        messageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String timeString = Utils.getTimeAgoShop(time);

                if(itemList.get(listPosition).isShowTime()) {
                    holder.time.setVisibility(View.GONE);
                    itemList.get(listPosition).setShowTime(false);
                } else {
                    holder.time.setVisibility(View.VISIBLE);itemList.get(listPosition).setShowTime(true);
                }
            }
        });


        if(otherImage.contains(".")) {

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(placeH);


            Glide.with(context)
                    .setDefaultRequestOptions(placeholderRequest)
                    .load("https://nsuer.club/images/profile_picture/" + otherImage)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);

        }



    }

    @Override
    public int getItemViewType(int listPosition) {

        String from = itemList.get(listPosition).getFrom();


        if(from.equals(memID)){
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }


    static class MyChatViewHolder extends RecyclerView.ViewHolder {
        public TextView message, time;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        public TextView message, time;
        public ImageView image;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }


}
