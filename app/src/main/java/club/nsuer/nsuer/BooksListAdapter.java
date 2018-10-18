package club.nsuer.nsuer;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;


public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ItemRowHolder> {

    private int listItemLayout;
    private ArrayList<BooksItem> itemList;
    private Context context;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private SnapHelper snapHelper;
    private MainActivity main;

    public BooksListAdapter(ArrayList<BooksItem> itemList, Context context) {

        this.itemList = itemList;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemCount() {

        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_books, parent, false);
        ItemRowHolder myViewHolder = new ItemRowHolder(view);
        snapHelper = new GravitySnapHelper(Gravity.START);
        return myViewHolder;
    }




    // load data in each row element
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int listPosition) {


        final String courseName = itemList.get(listPosition).getCourse();
        ArrayList<BooksListItem> singleCourseItems = itemList.get(listPosition).getBooks();

        holder.course.setText(courseName);

        BooksAdapter adapter = new BooksAdapter(singleCourseItems, context);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);

        EdgeDecorator leftRightPadding = new EdgeDecorator(10,30);
        holder.recyclerView.addItemDecoration(leftRightPadding);

        holder.recyclerView.setRecycledViewPool(recycledViewPool);
        snapHelper.attachToRecyclerView(holder.recyclerView);
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.menuButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.courses_recycler_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cr_action_remove:

                                BooksDatabase db = Room.databaseBuilder(context,
                                        BooksDatabase.class, "books").allowMainThreadQueries().build();

                                db.booksDao().deleteByCourseName(itemList.get(listPosition).getCourse());
                                itemList.remove(listPosition);
                                notifyItemRemoved(listPosition);
                                notifyItemChanged(listPosition);
                                notifyItemRangeChanged(listPosition, itemList.size());
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
    static class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView course;
        public RecyclerView recyclerView;
        public ImageView menuButton;


        public ItemRowHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            this.course = (TextView) itemView.findViewById(R.id.brCourseInitial);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.bookListRecycler);
            this.menuButton = (ImageView) itemView.findViewById(R.id.rbMenuButton);

        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + course.getText());
        }



    }
}