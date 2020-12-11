package club.nsuer.nsuer;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class ClassMatesAdapter extends RecyclerView.Adapter<ClassMatesAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ClassMatesItem> itemList;
    private Context context;
    private String myName;


    public ClassMatesAdapter(int layoutId, ArrayList<ClassMatesItem> itemList, Context context, String myName) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.myName = myName;
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
        TextView name = holder.name;
        TextView course = holder.course;

        final ImageView image = holder.image;

        String gender = itemList.get(listPosition).getGender();

//        Picasso.get()
//                .cancelRequest(holder.image);


        if (gender.equals("female"))
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female_color));
        else
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male_color));

        ImageView chat = holder.chat;

        final String userName = itemList.get(listPosition).getName();

        name.setText(userName);
        course.setText(itemList.get(listPosition).getCourse());


        final String picUrl = itemList.get(listPosition).getImage();


        final String email = itemList.get(listPosition).getEmail();

        final String memID = itemList.get(listPosition).getMemID();


        int placeH = R.drawable.ic_male_color;

        if (gender.equals("female"))
            placeH = R.drawable.ic_female_color;


        if (picUrl.contains(".")) {

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

//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                        "mailto",email, null));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "[NSUer App] Message from "+ myName);
//
//                context.startActivity(intent);


                Intent intent = new Intent(context, ChatActivity.class);

                intent.putExtra("otherMemID", memID);
                intent.putExtra("otherMemName", userName);

                context.startActivity(intent);

            }
        });


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView course;
        public ImageView chat;
        public ImageView image;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            name = (TextView) itemView.findViewById(R.id.classmateName);
            course = (TextView) itemView.findViewById(R.id.classmateCourse);
            image = (ImageView) itemView.findViewById(R.id.classmateImage);
            chat = (ImageView) itemView.findViewById(R.id.classmateChat);


        }


        @Override
        public void onClick(View view) {

        }


    }
}