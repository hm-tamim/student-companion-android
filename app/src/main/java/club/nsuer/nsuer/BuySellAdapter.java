package club.nsuer.nsuer;



import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class BuySellAdapter extends RecyclerView.Adapter<BuySellAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<BuySellItem> itemList;
    private Context context;
    private BuySell instance;


    public BuySellAdapter(int layoutId, ArrayList<BuySellItem> itemList,Context context, BuySell instance) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
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
        TextView title = holder.title;
        TextView price = holder.price;
        TextView seller = holder.seller;
        TextView time = holder.time;
        ImageView image = holder.image;
        CardView cardView = holder.cardView;




        int id = itemList.get(listPosition).getId();
        String titleS = itemList.get(listPosition).getTitle();
        String priceS = itemList.get(listPosition).getPrice();
        long timeL = itemList.get(listPosition).getTime();
        String sellerS = itemList.get(listPosition).getSellerName();
        int sellerID = itemList.get(listPosition).getSellerID();

        int category = itemList.get(listPosition).getCategory();



//        Picasso.get()
//                .cancelRequest(image);
//
        Picasso.get()
                    .load("https://nsuer.club/images/profile_picture/6.jpg")
                    .fit()
                    .centerCrop(Gravity.TOP)
                    .into(image);

        title.setText(titleS);
        price.setText(priceS);
        seller.setText(sellerS);



        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //   instance.openActivityWithId(itemList.get(listPosition).getId());

            }
        });



    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView price;
        public TextView seller;
        public TextView time;
        public ImageView image;
        public CardView cardView;

        public View circle;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            price = (TextView) itemView.findViewById(R.id.price);
            seller = (TextView) itemView.findViewById(R.id.seller);

            image = (ImageView) itemView.findViewById(R.id.image);

            cardView = (CardView) itemView.findViewById(R.id.cardView);





        }


        @Override
        public void onClick(View view) {

        }



    }
}
