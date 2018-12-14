package club.nsuer.nsuer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class BloodDonorsAdapter extends RecyclerView.Adapter<BloodDonorsAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<BloodDonorItem> itemList;
    private Context context;

    public BloodDonorsAdapter(int layoutId, ArrayList<BloodDonorItem> itemList, Context context) {
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
        TextView nameInput = holder.name;
        TextView bgroupInput = holder.bgroup;

        final ImageView image = holder.image;

        String gender = itemList.get(listPosition).getGender();


        if(gender.equals("female"))
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female_color));
        else
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male_color));

        ImageView chat = holder.chat;
        ImageView phoneInput = holder.phone;

        final String userName = itemList.get(listPosition).getName();

        nameInput.setText(userName);


        int bloodId = Integer.parseInt(itemList.get(listPosition).getBloodGroup());

        String bloodName = context.getResources().getStringArray(R.array.bloodGroups)[bloodId];

        bgroupInput.setText(bloodName);


        final String picUrl = itemList.get(listPosition).getImage();
        final String memID = itemList.get(listPosition).getMemID();
        final String address = itemList.get(listPosition).getAddress();

        holder.address.setText(address);

        int placeH = R.drawable.ic_male_color;

        if(gender.equals("female"))
            placeH = R.drawable.ic_female_color;


        if(picUrl.contains(".")) {

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(placeH);


            Glide.with(context)
                    .setDefaultRequestOptions(placeholderRequest)
                    .load("https://nsuer.club/images/profile_picture/" + picUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);

        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ChatActivity.class);

                intent.putExtra("otherMemID", memID);
                intent.putExtra("otherMemName", userName);

                context.startActivity(intent);

            }
        });



        final String phoneNo = itemList.get(listPosition).getPhone();

        if(!phoneNo.equals("null") && !phoneNo.equals("")){

            phoneInput.setVisibility(View.VISIBLE);

            phoneInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+phoneNo));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);

                }
            });

        } else {


            phoneInput.setVisibility(View.GONE);

        }


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView bgroup;
        public TextView address;
        public ImageView chat;
        public ImageView phone;
        public ImageView image;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView) itemView.findViewById(R.id.name);
            bgroup = (TextView) itemView.findViewById(R.id.bgroup);
            address = (TextView) itemView.findViewById(R.id.note);
            image = (ImageView) itemView.findViewById(R.id.image);
            chat = (ImageView) itemView.findViewById(R.id.chat);
            phone = (ImageView) itemView.findViewById(R.id.phone);


        }


        @Override
        public void onClick(View view) {

        }



    }
}