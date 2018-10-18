package club.nsuer.nsuer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;


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

        Picasso.get()
                .cancelRequest(holder.image);

        ImageView chat = holder.chat;

        String userName = itemList.get(listPosition).getName();

        name.setText(userName);
        course.setText(itemList.get(listPosition).getCourse());


        final String picUrl = itemList.get(listPosition).getImage();

        String gender = itemList.get(listPosition).getGender();

        final String email = itemList.get(listPosition).getEmail();


        int placeH = R.drawable.ic_male_color;

        if(gender.equals("female"))
             placeH = R.drawable.ic_female_color;


        if(picUrl.contains(".")) {
            Picasso.get()
                    .load("https://nsuer.club/images/profile_picture/" + picUrl)
                    .fit()
                    .placeholder(placeH)
                    .error(placeH)
                    .transform(new CircleTransform())
                    .centerCrop(Gravity.TOP)
                    .into(image);

        }else {

            if(gender.equals("female"))
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female_color));
            else
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male_color));

        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "[NSUer App] Message from "+ myName);

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