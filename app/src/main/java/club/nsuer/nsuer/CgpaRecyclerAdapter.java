package club.nsuer.nsuer;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CgpaRecyclerAdapter extends RecyclerView.Adapter<CgpaRecyclerAdapter.ViewHolder> implements CoursesList {

    private int listCgpaRecyclerItemLayout;
    private ArrayList<CgpaRecyclerItem> CgpaRecyclerItemList;
    private MainActivity main;
    private Context context;

    private int lastPosition = -1;
    private int animateDelay = 30;

    private ScrollView scroller;

    private Object instance;

    private View view;

    private View mainView;

    private FloatingActionButton addButton;

    private FloatingActionButton calculateButton;


    public CgpaRecyclerAdapter(int layoutId, ArrayList<CgpaRecyclerItem> CgpaRecyclerItemList, Context context, View mainView, Object instance) {
        listCgpaRecyclerItemLayout = layoutId;
        this.CgpaRecyclerItemList = CgpaRecyclerItemList;
        this.context = context;
        this.mainView = mainView;

        this.scroller = mainView.findViewById(R.id.cgpaScroller);

        this.addButton = mainView.findViewById(R.id.cgpaAddCourseBtn);

        this.calculateButton = mainView.findViewById(R.id.cgpaCalculateBtn);


        this.instance = instance;
    }

    @Override
    public int getItemCount() {

        return CgpaRecyclerItemList == null ? 0 : CgpaRecyclerItemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(listCgpaRecyclerItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);


        return myViewHolder;
    }


    private void setAnimation(View viewToAnimate, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.animator.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }


    public void addData(CgpaRecyclerItem cgpaItem) {
        Log.d("notifyData ", CgpaRecyclerItemList.size() + "");
        this.CgpaRecyclerItemList.add(cgpaItem);

        int size = CgpaRecyclerItemList.size() - 1;


        notifyItemInserted(size);

        notifyItemChanged(size);

        notifyItemRangeChanged(size, CgpaRecyclerItemList.size());

    }

    public int getSize() {

        return CgpaRecyclerItemList.size();


    }


    private void moveButtonToRight() {


        new Handler().postDelayed(new Runnable() {
            public void run() {

                TransitionManager.beginDelayedTransition((ViewGroup) mainView.getParent());
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) addButton.getLayoutParams();
                lp.gravity = Gravity.BOTTOM | GravityCompat.END;
                lp.setMargins(0, 0, 0, 55);
                lp.setMarginEnd(250);

                addButton.setLayoutParams(lp);
            }

        }, 150);


    }

    private void moveButtonToLeft() {


        new Handler().postDelayed(new Runnable() {
            public void run() {
                TransitionManager.beginDelayedTransition((ViewGroup) mainView.getParent());
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) addButton.getLayoutParams();
                lp.gravity = Gravity.BOTTOM | GravityCompat.START;
                lp.setMargins(0, 0, 0, 55);
                lp.setMarginEnd(0);


                addButton.setLayoutParams(lp);

            }

        }, 100);


    }


    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        final AutoCompleteTextView coursez = holder.course;
        Spinner credit = holder.credit;
        Spinner grade = holder.grade;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, R.layout.suggestion_adapter_textview, SUGGESTIONS);

        coursez.setThreshold(2);

        coursez.setDropDownVerticalOffset(0);

        coursez.setAdapter(adapter);


        if (instance instanceof CgpaCalculator) {
            coursez.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                    moveButtonToLeft();


                }

            });


            coursez.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus)
                        moveButtonToLeft();
                    else
                        moveButtonToRight();

                    if (hasFocus)
                        moveButtonToRight();
                    else
                        moveButtonToLeft();


                }
            });


            coursez.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveButtonToRight();
                }
            });

            coursez.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        moveButtonToLeft();
                    }
                    return false;
                }
            });


            coursez.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        moveButtonToLeft();
                        // ((LinearLayoutManager) mainView.getLayoutManager()).scrollToPositionWithOffset(listPosition, 0);
                    }
                    return false;
                }
            });

        } else {


            coursez.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);


                }

            });


        }


        ImageView buttonViewOption = holder.closeBtn;

        main = MainActivity.getInstance();


        coursez.setText(CgpaRecyclerItemList.get(listPosition).getCourse());


        String dCredit = CgpaRecyclerItemList.get(listPosition).getCredit();
        String dGrade = CgpaRecyclerItemList.get(listPosition).getGrade();


        credit.setSelection(getIndex(credit, dCredit));
        grade.setSelection(getIndex(grade, dGrade));


        grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String cc = coursez.getText().toString();

                // if(cc != null && !cc.isEmpty())
                //   instance.calculateCgpa();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }

        });


        buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CgpaRecyclerItemList.size() > 1) {
//                    CgpaDatabase db = Room.databaseBuilder(context,
//                            CgpaDatabase.class, "cgpaCalculator").allowMainThreadQueries().build();
//                    db.cgpaDao().deleteByCourseName(CgpaRecyclerItemList.get(listPosition).getCourse());


                    CgpaRecyclerItemList.remove(listPosition);
                    notifyItemRemoved(listPosition);
                    notifyItemChanged(listPosition);
                    notifyItemRangeChanged(listPosition, CgpaRecyclerItemList.size());


                    String cc = coursez.getText().toString();

                    if (cc != null && !cc.isEmpty()) {
                        if (instance instanceof CgpaCalculator)
                            ((CgpaCalculator) instance).calculateCgpa();
                    }

                } else {

                    Toast.makeText(context, "Minimum one course is required", Toast.LENGTH_SHORT).show();

                }

            }
        });


        setAnimation(holder.itemView, listPosition);


    }


    public void removeAt(int position) {
        CgpaRecyclerItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, CgpaRecyclerItemList.size());
    }


    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AutoCompleteTextView course;
        public Spinner credit;
        public Spinner grade;
        public ImageView closeBtn;


        public ViewHolder(View CgpaRecyclerItemView) {

            super(CgpaRecyclerItemView);


            CgpaRecyclerItemView.setOnClickListener(this);

            course = (AutoCompleteTextView) CgpaRecyclerItemView.findViewById(R.id.cgpaCourse);

            course.clearFocus();


            credit = (Spinner) CgpaRecyclerItemView.findViewById(R.id.cgpaCredit);


            grade = (Spinner) CgpaRecyclerItemView.findViewById(R.id.cgpaGrade);

            closeBtn = (ImageView) CgpaRecyclerItemView.findViewById(R.id.cgpaCloseButton);

        }


        @Override
        public void onClick(View view) {
            // Log.d("onclick", "onClick " + getLayoutPosition() + " " + CgpaRecyclerItem.getText());
        }


    }
}