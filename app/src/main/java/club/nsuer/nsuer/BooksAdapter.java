package club.nsuer.nsuer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import club.nsuer.nsuer.BooksListItem;
import club.nsuer.nsuer.R;
import club.nsuer.nsuer.Utils;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.SingleItemRowHolder> {

    private int listItemLayout;
    private ArrayList<BooksListItem> itemList;
    private Context context;
    private View v;

    public BooksAdapter(ArrayList<BooksListItem> itemList, Context context) {
       // listItemLayout = layoutId;
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    //@Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_book_list, parent, false);


        SingleItemRowHolder myViewHolder = new SingleItemRowHolder(view);


        return myViewHolder;
    }




    // load data in each row element
    //@Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int listPosition) {

        TextView title = holder.title;
        TextView edition = holder.edition;
        TextView author = holder.author;


        String bookTitle = itemList.get(listPosition).getBookName();




        int textLength = bookTitle.length();

        if (textLength > 40) {
            title.setTextSize(17);
        } else if (textLength < 15) {
            title.setTextSize(20);
        } else {
            title.setTextSize(18);
        }


        title.setText(bookTitle);
        edition.setText(itemList.get(listPosition).getEdition() + " Edition");
        author.setText(itemList.get(listPosition).getAuthor());


        List<String> colors;

        colors=new ArrayList<String>();

        colors.add("#0fa1e4");
        colors.add("#377697");
        colors.add("#009688");
        colors.add("#1c7cb9");
        colors.add("#3d5eab");
        colors.add("#17a2b8");
        colors.add("#25bec6");
        colors.add("#2d566b");
        colors.add("#227585");
        colors.add("#861e6a");
        colors.add("#16a9ff");
        colors.add("#29996a");
        colors.add("#4f81bc");
        colors.add("#db843d");
       // colors.add("#007fff");
       // colors.add("#009688");

        Random r = new Random();
        int i1 = r.nextInt(colors.size()- 0) + 0;

        holder.cardView.setCardBackgroundColor(Color.parseColor(colors.get(i1)));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Uri uri = Uri.parse("https://nsuer.club/books/download/" + itemList.get(listPosition).getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                context.startActivity(intent);

                String url = "https://nsuer.club/books/download/" + itemList.get(listPosition).getUrl();


                Utils.CustomTab(url,context);


            }

        });




    }

    // Static inner class to initialize the views of rows
    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView title;
        public TextView edition;
        public TextView author;
        public CardView cardView;
        public View view;

        public SingleItemRowHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.brBookTitle);
            edition = (TextView) itemView.findViewById(R.id.brBookEdition);
            author = (TextView) itemView.findViewById(R.id.brBookAuthor);

            cardView = (CardView) itemView.findViewById(R.id.rblCardView);

        }



        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + title.getText());
        }



    }
}