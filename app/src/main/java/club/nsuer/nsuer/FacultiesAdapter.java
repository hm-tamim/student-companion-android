package club.nsuer.nsuer;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FacultiesAdapter extends RecyclerView.Adapter<FacultiesAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<FacultiesItem> itemList;
    private Context context;

    private int lastPosition = -1;
    private int animateDelay = 30;

    public FacultiesAdapter(int layoutId, ArrayList<FacultiesItem> itemList,Context context) {
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

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.animator.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView name = holder.name;
        TextView rank = holder.rank;
        ImageView image = holder.image;

        TextView initial = holder.initial;
        TextView course = holder.course;
        TextView section = holder.section;
        TextView email = holder.email;
        TextView phone = holder.phone;
        TextView ext = holder.ext;
        TextView department = holder.department;
        TextView office = holder.office;

        CardView cardView = holder.cardView;


        final String dept1 = itemList.get(listPosition).getDepartment();

        String dept2 = null;

        switch (dept1){

            case "1": dept2 = "Department Of Accounting & Finance";
                break;
            case "2": dept2 = "Department Of Economics";
                break;
            case "3": dept2 = "Department Of Management";
                break;
            case "4": dept2 = "Dept Of Marketing & International Business";
                break;
            case "5": dept2 = "Department Of Architecture";
                break;
            case "6": dept2 = "Dept Of Civil and Environmental Engineering";
                break;
            case "7": dept2 = "Department Of Mathematics & Physics";
                break;
            case "8": dept2 = "Department Of English & Modern Languages";
                break;
            case "9": dept2 = "Department Of Political Science & Sociology";
                break;
            case "10": dept2 = "Department Of Law";
                break;
            case "11": dept2 = "Department Of History & Philosophy";
                break;
            case "12": dept2 = "Department Of Biochemistry & Microbiology";
                break;
            case "13": dept2 = "Dept Of Environmental Science & Management";
                break;
            case "14": dept2 = "Department Of Pharmaceutical Sciences";
                break;
            case "15": dept2 = "Department Of Public Health";
                break;
            case "16": dept2 = "Dept Of Electrical & Computer Engineering";
                break;

        }



        String imageUrl = null;




        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                Utils.CustomTab(getUrlFac(dept1)+itemList.get(listPosition).getUrl(),context);


            }
        });


        imageUrl = getUrl(dept1) + itemList.get(listPosition).getImage();



        name.setText(itemList.get(listPosition).getName());
        rank.setText(itemList.get(listPosition).getRank());

       // image.setText(itemList.get(listPosition).getImage());

        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop(Gravity.TOP)
                .into(image);




        initial.setText(itemList.get(listPosition).getInitial());
        course.setText(itemList.get(listPosition).getCourse());
        section.setText("SECTION " + itemList.get(listPosition).getSection());
        email.setText(itemList.get(listPosition).getEmail());

        String sPhone = itemList.get(listPosition).getPhone();
        String sExt = itemList.get(listPosition).getExt();



        phone.setAutoLinkMask(Linkify.PHONE_NUMBERS);


        if(sPhone != null && !sPhone.isEmpty()) {

            phone.setText(sPhone);
        }

        if(sExt != null && !sExt.isEmpty())
            ext.setText("Ext - "+sExt);



        department.setText(dept2);


        String sOffice = itemList.get(listPosition).getOffice();

        if(sOffice.equals("")) {

            office.setText("Office Room");
        } else {

            sOffice = sOffice.replaceAll("(?<=[A-Za-z])(?=[0-9])|(?<=[0-9])(?=[A-Za-z])", " ");

            office.setText(sOffice);

        }










        if(listPosition % 7 == 0){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#227585"));
            // #2196f3 #409de0 #39afd5  #7c94b8 #72c769 #ee945d  #f95b45

        }else if(listPosition % 7 == 1){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#009688"));

            // #39d2e3 #72c769
        }else if(listPosition % 7 == 2){
            //#7c94b6

            holder.cardView.setCardBackgroundColor(Color.parseColor("#17a2b8"));

        }else if(listPosition % 7 == 3){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#1c7cb9"));
            // #ff9800
        }else if(listPosition % 7 == 4){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#2d566b"));
        }else if(listPosition % 7 == 5){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#05879f"));
        }else if(listPosition % 7 == 6){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#03a9f4"));
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setAnimation(holder.itemView, listPosition);
            }
        }, animateDelay);

        animateDelay += 10;

    }




    private String getUrl(String dept1){

        String imageUrlz = null;

        switch (dept1) {

            case "6":
                imageUrlz = "http://institutions.northsouth.edu/cee/";
                break;
            case "16":
                imageUrlz = "http://ece.northsouth.edu/wp-content/";
                break;
            default:
                imageUrlz = "http://www.northsouth.edu/";
        }

        return imageUrlz;

    }



    private String getUrlFac(String dept1){

        String imageUrlz = null;

        switch (dept1) {

            case "6":
                imageUrlz = "http://institutions.northsouth.edu/cee/";
                break;
            case "16":
                imageUrlz = "http://ece.northsouth.edu/people/";
                break;
            default:
                imageUrlz = "http://www.northsouth.edu/faculty-members/";
        }

        return imageUrlz;

    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView rank;
        public ImageView image;

        public TextView initial;
        public TextView course;
        public TextView section;
        public TextView email;
        public TextView phone;
        public TextView ext;
        public TextView department;
        public TextView office;
        public CardView cardView;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.facultyFullName);
            rank = (TextView) itemView.findViewById(R.id.facultyRank);
            image = (ImageView) itemView.findViewById(R.id.facultyProfileImage);
            initial = (TextView) itemView.findViewById(R.id.facultyInitial);

            course = (TextView) itemView.findViewById(R.id.facultyCourse);
            section = (TextView) itemView.findViewById(R.id.facultySection);
            email = (TextView) itemView.findViewById(R.id.facultyEmail);
            phone = (TextView) itemView.findViewById(R.id.facultyPhone);
            ext = (TextView) itemView.findViewById(R.id.facultyExt);
            office = (TextView) itemView.findViewById(R.id.facultyOffice);
            department = (TextView) itemView.findViewById(R.id.facultyDepartment);

            cardView = (CardView) itemView.findViewById(R.id.facultyHolderCard);


        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + initial.getText());



        }



    }
}