package club.nsuer.nsuer;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class BloodRequestsByMeAdapter extends RecyclerView.Adapter<BloodRequestsByMeAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<BloodRequestItem> itemList;
    private Context context;
    private BloodRequestsByMe instance;

    public BloodRequestsByMeAdapter(int layoutId, ArrayList<BloodRequestItem> itemList, Context context, BloodRequestsByMe instance) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
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
        RelativeLayout mainHolder = holder.mainHolder;
        TextView titleInput = holder.title;
        TextView bgroupInput = holder.bloodGroup;
        TextView postedBy = holder.byUser;
        TextView whenNeeded = holder.whenDate;

        int bloodId = itemList.get(listPosition).getBloodGroup();
        String bloodName = context.getResources().getStringArray(R.array.bloodGroups)[bloodId];
        String bags = itemList.get(listPosition).getBags();
        final String userName = itemList.get(listPosition).getName();
        final String memID = itemList.get(listPosition).getMemID();
        final String address = itemList.get(listPosition).getAddress();
        final String phoneNo = itemList.get(listPosition).getPhone();



        final long postedOn = itemList.get(listPosition).getPostedDate();
        final long neededOn = itemList.get(listPosition).getWhenDate();

        String bagsC = "Bags";

        if (bags.equals("1"))
            bagsC = "Bag";

        final String title = bags+ " "+bagsC+" (" + bloodName + ") Blood Needed";
        titleInput.setText(title);
        bgroupInput.setText(bloodName);
        bgroupInput.setText(bloodName);



        postedBy.setText("By "+userName+ ", "+ Utils.getTimeAgo((int)postedOn));

        whenNeeded.setText("Needed on " + Utils.getHumanTime(neededOn));

        holder.address.setText(address);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                instance.sendDelete(listPosition);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();



            }
        });

        final boolean isManaged = itemList.get(listPosition).isManaged();


        holder.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String alertSold = "managed?";

                if(isManaged)
                    alertSold = "not managed?";

                new AlertDialog.Builder(context,R.style.AlertDialogTheme)
                        .setMessage("Do you really want to mark it as "+alertSold)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                instance.sendManaged(listPosition);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });


        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context,BloodRequest.class);

                intent.putExtra("bgroup", itemList.get(listPosition).getBloodGroup());
                intent.putExtra("bags", Integer.parseInt(itemList.get(listPosition).getBags()));
                intent.putExtra("date",itemList.get(listPosition).getWhenDate());
                intent.putExtra("phone",itemList.get(listPosition).getPhone());
                intent.putExtra("address",itemList.get(listPosition).getAddress());
                intent.putExtra("note",itemList.get(listPosition).getNote());

                intent.putExtra("msgID",itemList.get(listPosition).getId());

                instance.startActivityForResult(intent, 10001);

            }
        });




        Button markAsManaged = holder.manage;

        if(isManaged) {
            markAsManaged.setText("MANAGED");
            markAsManaged.setBackground(context.getDrawable(R.drawable.sold_button_eee));
            markAsManaged.setTextColor(Color.parseColor("#397550"));
        } else{


            markAsManaged.setText("MARK AS MANAGED");
            markAsManaged.setBackground(context.getDrawable(R.drawable.sold_button));
            markAsManaged.setTextColor(Color.parseColor("#333333"));
        }




    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout mainHolder;
        public LinearLayout overlay;
        public TextView title;
        public TextView bloodGroup;
        private TextView byUser;
        private TextView whenDate;
        public TextView address;
        public Button manage;

        public Button editBtn;
        public Button deleteBtn;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            mainHolder = (RelativeLayout) itemView.findViewById(R.id.mainHolder);

            title = (TextView) itemView.findViewById(R.id.title);
            bloodGroup = (TextView) itemView.findViewById(R.id.bloodGroup);

            byUser = (TextView) itemView.findViewById(R.id.byUser);
            whenDate = (TextView) itemView.findViewById(R.id.whenDate);
            address = (TextView) itemView.findViewById(R.id.address);

            manage = (Button) itemView.findViewById(R.id.managed);

            editBtn = (Button) itemView.findViewById(R.id.edit);
            deleteBtn = (Button) itemView.findViewById(R.id.delete);

            editBtn.setTypeface(null, Typeface.NORMAL);
            deleteBtn.setTypeface(null, Typeface.NORMAL);
            manage.setTypeface(null, Typeface.NORMAL);


        }


        @Override
        public void onClick(View view) {

        }



    }
}