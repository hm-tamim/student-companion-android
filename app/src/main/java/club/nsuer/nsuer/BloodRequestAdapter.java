package club.nsuer.nsuer;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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


public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<BloodRequestItem> itemList;
    private Context context;

    public BloodRequestAdapter(int layoutId, ArrayList<BloodRequestItem> itemList, Context context) {
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
        RelativeLayout mainHolder = holder.mainHolder;
        TextView titleInput = holder.title;
        TextView bgroupInput = holder.bloodGroup;
        TextView postedBy = holder.byUser;
        TextView whenNeeded = holder.whenDate;
        Button chat = holder.share;
        Button phoneInput = holder.phone;
        ImageView shareBtn = holder.shareBtn;

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


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ChatActivity.class);

                intent.putExtra("otherMemID", memID);
                intent.putExtra("otherMemName", userName);

                context.startActivity(intent);

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context, R.style.WideDialog);


                dialog.setTitle("Share on Social Media");

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 300);

                dialog.setContentView(R.layout.dialog_copy_text);

                String post = title + "\n\nLocation: "+ address + "\nNeeded on: " + Utils.getHumanTime(neededOn) + "\nContact: " + phoneNo;


                final EditText text = (EditText) dialog.findViewById(R.id.text);



                text.setText(post);

                Button closeButton = (Button) dialog.findViewById(R.id.cancel);


                Button copyButton = (Button) dialog.findViewById(R.id.copy);


                copyButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Blood Donors", text.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(context,"Copied, now paste and share.",Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }



                });



                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        postedBy.setText("By "+userName+ ", "+ Utils.getTimeAgo((int)postedOn));

        whenNeeded.setText("Needed on " + Utils.getHumanTime(neededOn));

        holder.address.setText(address);

        if(itemList.get(listPosition).isManaged()) {

            phoneInput.setText("Blood Managed");
            phoneInput.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_tick_small, 0, 0, 0);

        }
        else {

            phoneInput.setText(phoneNo);


        }



        if(!phoneNo.equals("")){

            phoneInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(itemList.get(listPosition).isManaged()) {
                        Toast.makeText(context, "Blood is managed", Toast.LENGTH_SHORT).show();
                    } else
                    {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + phoneNo));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(callIntent);
                    }
                }
            });

        }


        if(itemList.get(listPosition).getNote().equals(""))
        {
            holder.noteHolder.setVisibility(View.GONE);

        } else {

            holder.noteHolder.setVisibility(View.VISIBLE);
            holder.note.setText(itemList.get(listPosition).getNote());

        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout mainHolder;
        public LinearLayout overlay;
        public TextView title;
        public TextView bloodGroup;
        private TextView byUser;
        private TextView whenDate;
        public TextView address;

        public Button share;
        public Button phone;
        public ImageView shareBtn;

        public TextView note;
        public LinearLayout noteHolder;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            mainHolder = (RelativeLayout) itemView.findViewById(R.id.mainHolder);

            title = (TextView) itemView.findViewById(R.id.title);
            bloodGroup = (TextView) itemView.findViewById(R.id.bloodGroup);

            byUser = (TextView) itemView.findViewById(R.id.byUser);
            whenDate = (TextView) itemView.findViewById(R.id.whenDate);
            address = (TextView) itemView.findViewById(R.id.address);

            share = (Button) itemView.findViewById(R.id.chat);
            phone = (Button) itemView.findViewById(R.id.phone);

            shareBtn = (ImageView) itemView.findViewById(R.id.share);

            note = itemView.findViewById(R.id.note);
            noteHolder = itemView.findViewById(R.id.noteHolder);


        }


        @Override
        public void onClick(View view) {

        }



    }
}