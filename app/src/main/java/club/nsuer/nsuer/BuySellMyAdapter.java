package club.nsuer.nsuer;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BuySellMyAdapter extends RecyclerView.Adapter<BuySellMyAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<BuySellItem> itemList;
    private Context context;
    private BuySell instance;
    private Dialog dialog;

    public BuySellMyAdapter(int layoutId, ArrayList<BuySellItem> itemList, Context context, BuySell instance, Dialog dialog) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
        this.instance = instance;
        this.dialog = dialog;
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
        final TextView time = holder.time;
        ImageView image = holder.image;
        Button edit = holder.editButton;
        Button delete = holder.deleteButton;

        Button markAsSold = holder.markAsSold;


        final int id = itemList.get(listPosition).getId();
        final String titleS = itemList.get(listPosition).getTitle();
        String priceS = itemList.get(listPosition).getPrice();

        if (Utils.isNumeric(priceS))
            priceS = "৳ " + priceS;

        final long timeL = itemList.get(listPosition).getTime();
        // String sellerS = itemList.get(listPosition).getSellerName();
        int sellerID = itemList.get(listPosition).getSellerID();

        final int category = itemList.get(listPosition).getCategory();

        final String img = itemList.get(listPosition).getImageUrl();

        String timeS = Utils.getTimeAgoShop(itemList.get(listPosition).getTime());


        time.setText(timeS);

        Picasso.get()
                .cancelRequest(image);

        Picasso.get()
                .load("https://nsuer.club/images/shop/" + img)
                .fit()
                .placeholder(R.drawable.default_image)
                .centerCrop(Gravity.TOP)
                .into(image);

        title.setText(titleS);
        price.setText(priceS);


        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                instance.sendDelete(listPosition);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });


        final int checkSold = itemList.get(listPosition).getSold();


        markAsSold.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String alertSold = "sold?";

                if (checkSold == 1)
                    alertSold = "unsold?";

                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setMessage("Do you really want to mark this item as " + alertSold)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                instance.sendSold(listPosition);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });


        final String description = itemList.get(listPosition).getDescription();
        final String prices = itemList.get(listPosition).getPrice();

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, BuySellCreate.class);
                intent.putExtra("title", titleS);
                intent.putExtra("category", category);
                intent.putExtra("price", prices);
                intent.putExtra("description", description);
                intent.putExtra("image", "https://nsuer.club/images/shop/" + img);
                intent.putExtra("timestamp", String.valueOf(timeL));
                intent.putExtra("msgID", String.valueOf(id));

                dialog.dismiss();

                instance.startActivityForResult(intent, 10001);


            }
        });


        if (checkSold == 1) {
            markAsSold.setText("SOLD");
            markAsSold.setBackground(context.getDrawable(R.drawable.sold_button_eee));
            markAsSold.setTextColor(Color.parseColor("#397550"));
        } else {


            markAsSold.setText("MARK AS SOLD");
            markAsSold.setBackground(context.getDrawable(R.drawable.sold_button));
            markAsSold.setTextColor(Color.parseColor("#333333"));
        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView price;
        public TextView time;
        public ImageView image;

        public Button editButton;
        public Button deleteButton;

        public View circle;

        public Button markAsSold;


        public ViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            price = (TextView) itemView.findViewById(R.id.price);

            image = (ImageView) itemView.findViewById(R.id.image);

            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.delete);

            markAsSold = (Button) itemView.findViewById(R.id.markAsSold);


        }


        @Override
        public void onClick(View view) {

        }


    }
}
