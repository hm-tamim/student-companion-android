package club.nsuer.nsuer;

import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<NotificationItem> itemList;
    private Context context;
    private MainActivity mainActivity;


    public NotificationAdapter(int layoutId, ArrayList<NotificationItem> itemList, Context context, MainActivity mainActivity) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.mainActivity = mainActivity;
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
        TextView titlew = holder.title;
        TextView time = holder.time;

        final ImageView image = holder.image;

        String type = itemList.get(listPosition).getType();

        if (type.equals("like"))
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_outline));
        else if (type.equals("comment"))
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_comments_outline));
        else if (type.equals("reminder"))
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_clock_white));
        else
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notification));

        ImageView menuBtn = holder.menu;

        holder.title.setText(itemList.get(listPosition).getBody());

        holder.time.setText(Utils.getTimeAgoShop(itemList.get(listPosition).getTime()));


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("senderMemID", itemList.get(listPosition).getSenderMemID());
                intent.putExtra("type", itemList.get(listPosition).getType());
                intent.putExtra("typeExtra", itemList.get(listPosition).getTypeExtra());
                intent.putExtra("typeExtra2", itemList.get(listPosition).getTypeExtra2());
                intent.putExtra("fromService", "true");

                mainActivity.finish();

                context.startActivity(intent);


            }
        });

        if (itemList.get(listPosition).isSeen()) {
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.title.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#f9f9f9"));
            holder.title.setTypeface(null, Typeface.BOLD);
        }


        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popup = new PopupMenu(context, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.courses_recycler_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cr_action_remove:


                                NotificationDatabase db = Room.databaseBuilder(context,
                                        NotificationDatabase.class, "notifications").allowMainThreadQueries().build();
                                db.notificationDao().deleteByTime(itemList.get(listPosition).getTime());

                                itemList.remove(listPosition);
                                notifyItemRemoved(listPosition);
                                notifyDataSetChanged();

                                break;

                            case R.id.cr_action_none:
                                //handle menu2 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView time;
        public ImageView menu;
        public ImageView image;

        public LinearLayout layout;

        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.image);
            menu = (ImageView) itemView.findViewById(R.id.menu);
            layout = (LinearLayout) itemView.findViewById(R.id.holder);


        }


        @Override
        public void onClick(View view) {

        }


    }
}