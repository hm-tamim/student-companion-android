package club.nsuer.nsuer;


import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import club.nsuer.nsuer.BooksDatabase;
import club.nsuer.nsuer.BooksEntity;
import club.nsuer.nsuer.BooksItem;
import club.nsuer.nsuer.BooksListAdapter;
import club.nsuer.nsuer.BooksListItem;
import club.nsuer.nsuer.CoursesList;
import club.nsuer.nsuer.JSONParser;
import club.nsuer.nsuer.MainActivity;
import club.nsuer.nsuer.MyNumberPicker;
import club.nsuer.nsuer.R;
import club.nsuer.nsuer.Utils;


public class Books extends Fragment implements CoursesList {



    private MainActivity main;
    private AutoCompleteTextView courseInput;
    private MyNumberPicker sectionInput;
    private View v;
    private BooksDatabase dbBooks;
    private Button dialogButton;
    private ArrayList<BooksItem> itemList;
    private ArrayList<BooksListItem> itemListBooks;
    private LinearLayout noBook;

    public Books() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.activity_books, container, false);



        noBook = v.findViewById(R.id.noBook);


        main = (MainActivity) getActivity();
        main.resetShadow();

        main.setActionBarTitle("Books");



        dbBooks = Room.databaseBuilder(main.getApplicationContext(),
                BooksDatabase.class, "books").allowMainThreadQueries().build();

        // db.coursesDao().nukeTable();


        itemList = new ArrayList<BooksItem>();




        List<BooksEntity> list = dbBooks.booksDao().getAll();
        String hmm = list.toString();

        //((TextView) v.findViewById(R.id.tttt)).setText(hmm);

        if(list.size() > 0)
            noBook.setVisibility(View.GONE);


        for (int i=0; i < list.size(); i++) {

            String course = list.get(i).getCourse();
            String books = list.get(i).getBooks();

            itemListBooks = new ArrayList<BooksListItem>();

            try {


                JSONObject result = new JSONObject(books);
                JSONArray obj = result.getJSONArray("books");
                String bookName;
                String edition;
                String author;
                String url;

            for (int j = 0; j < obj.length(); j++) {


                JSONObject data = obj.getJSONObject(j);

                String bookString = data.getString("name");

                String[] bookArray = bookString.split(" Ed ");

                url = data.getString("link");


                if(bookArray.length > 1) {

                    int index = bookArray[0].lastIndexOf(" ");

                    bookName = bookArray[0].substring(0, index);
                    edition = bookArray[0].substring(bookArray[0].lastIndexOf(" ") + 1);


                    String[] bookArray2 = bookArray[1].split("by ");

                    author = "By " + bookArray2[1];

                } else {

                    bookName = bookString;
                    edition = "1st";
                    author = "For " + course;

                    if(bookString.contains("by ")){

                        String[] bookArray3 = bookString.split("by");

                        bookName = bookArray3[0];
                        author = "By " + bookArray3[1];

                    }

                }

                itemListBooks.add((new BooksListItem(bookName,edition,author,url)));


                }




            } catch (JSONException e) {

            Log.e("JSON", e.toString());



        }



        itemList.add(new BooksItem(course,itemListBooks));


        }

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rcBooksRecyclerView);
        recyclerView.setHasFixedSize(true);

        BooksListAdapter adapter = new BooksListAdapter(itemList,this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.add_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addButton) {

            if(!Utils.isNetworkAvailable(getContext())) {
                Toast.makeText(getContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
                return false;
            }


            final Dialog dialog = new Dialog(getContext(), R.style.WideDialog);


            //dialog.setTitle("Add Course & Section");

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 200);

            dialog.setContentView(R.layout.alert_add_book);

            courseInput = (AutoCompleteTextView) dialog.findViewById(R.id.aabBookInitial);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(),R.layout.suggestion_adapter_textview, SUGGESTIONS);

            courseInput.setThreshold(2);

            courseInput.setDropDownVerticalOffset(0);

            courseInput.setAdapter(adapter);



            //courseInput.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));


            ImageView closeButton = (ImageView) dialog.findViewById(R.id.aabCloseButton);






            dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {



                    // dialogButton.setBackgroundColor(Color.RED);

                    CircularProgressButton btn = (CircularProgressButton) dialog.findViewById(R.id.dialogButtonOK);

                    btn.startAnimation();


                    HashMap<String, String> parametters = new HashMap<String, String>();

                    parametters.put("course", courseInput.getText().toString());
                    parametters.put("section", "1");

                    JSONParser parser = new JSONParser("https://nsuer.club/app/get-books.php", "GET", parametters);



                    parser.setListener(new JSONParser.ParserListener() {
                        @Override
                        public void onSuccess(JSONObject result) {

                            int bookCount = 0;


                            try {
                                JSONArray obj = result.getJSONArray("dataArray");

                                String firstCourse = null;


                                for (int i = 0; i < obj.length(); i++) {


                                    JSONObject data = obj.getJSONObject(i);


                                            int id = data.getInt("id");
                                            String course = data.getString("course");
                                            String books = data.getString("books");

                                            BooksEntity arrData = new BooksEntity();

                                            arrData.setCourse(course);
                                            arrData.setBooks(books);
                                            dbBooks.booksDao().insertAll(arrData);

                                            bookCount++;




                                }
                            } catch (JSONException e) {

                                Log.e("JSON", e.toString());
                            }


                            dialog.dismiss();


                            if(bookCount < 1)
                                Toast.makeText(getContext(),"Can't find this course's books in database.",Toast.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(Books.this).attach(Books.this).commit();
                                }
                            }, 100);

                        }

                        @Override
                        public void onFailure() {
                            dialog.dismiss();
                        }
                    });
                    parser.execute();

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

        return super.onOptionsItemSelected(item);
    }



}
