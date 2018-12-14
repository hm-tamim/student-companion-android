package club.nsuer.nsuer;


import android.animation.ObjectAnimator;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CgpaCalculator extends Fragment {

    private View view;
    private MainActivity main;
    private CgpaDatabase db;
    private ArrayList<CgpaRecyclerItem> itemList;
    private CgpaRecyclerAdapter itemArrayAdapter2;
    private RecyclerView recyclerView2;
    private ArcProgressz arc;
    private ArcProgressz arc_credit;

    private ScrollView scroller;


    private static final String CGPA_CALCULATOR_DATA = "CgpaData";


    public CgpaCalculator() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void updateGraph(float credit, float cgpa){

        arc = (ArcProgressz)view.findViewById(R.id.calculator_arc_cgpa);

        arc.setMax(4);



        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(arc, "progress", 0.0f, cgpa);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());



        objectAnimator.start();



        arc_credit = (ArcProgressz) view.findViewById(R.id.calculator_arc_credits);

        arc_credit.setMax(120);



        final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(arc_credit, "progress", 0.0f, credit);
        objectAnimators.setDuration(1000);
        objectAnimators.setInterpolator(new DecelerateInterpolator());


        objectAnimators.start();





    }

    private void updateCreditCgpa(float credit, float cgpa) {

        SharedPreferences.Editor editor = main.getApplicationContext().getSharedPreferences(CGPA_CALCULATOR_DATA, MODE_PRIVATE).edit();
        editor.putFloat("credit", credit);
        editor.putFloat("cgpa", cgpa);



        updateGraph(credit, cgpa);
        editor.apply();

    }



    private void updateManualCreditCgpa(float credit, float gpa) {

        SharedPreferences.Editor editor = main.getApplicationContext().getSharedPreferences(CGPA_CALCULATOR_DATA, MODE_PRIVATE).edit();
        editor.putFloat("manualCredit", credit);
        editor.putFloat("manualGpa", gpa);

        editor.apply();

    }

    public void updateManualCreditGpaInput(){


        SharedPreferences prefss = main.getApplicationContext().getSharedPreferences(CGPA_CALCULATOR_DATA, MODE_PRIVATE);



        if(prefss.contains("manualCredit")) {


            float credits = prefss.getFloat("manualCredit",0.0f);
            float gpa = prefss.getFloat("manualGpa",0.0f);


            String creditz;

            if((credits-(int)credits)!=0)
                creditz = String.valueOf(new DecimalFormat("#.##").format(credits));
            else
                creditz = String.valueOf(new DecimalFormat("#").format(credits));


            ((EditText) view.findViewById(R.id.cgpaManualCredit)).setText(creditz, TextView.BufferType.EDITABLE);
            ((EditText) view.findViewById(R.id.cgpaManualGpa)).setText(String.valueOf(gpa), TextView.BufferType.EDITABLE);

        }

    }






    public double getGpaPoint(String grade) {
        double point = 0.0;
        if (grade.equals("A"))
            point = 4.0;
        else if (grade.equals("A-"))
            point = 3.7;
        else if (grade.equals("A-"))
            point = 3.7;
        else if (grade.equals("B+"))
            point = 3.3;
        else if (grade.equals("B"))
            point = 3.0;
        else if (grade.equals("B-"))
            point = 2.7;
        else if (grade.equals("C+"))
            point = 2.3;
        else if (grade.equals("C"))
            point = 2.0;
        else if (grade.equals("C-"))
            point = 1.7;
        else if (grade.equals("D+"))
            point = 1.3;
        else if (grade.equals("D"))
            point = 1.0;
        else if (grade.equals("F"))
            point = 0.0;
        else
            point = 0.0;

        return point;
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.cgpa_calculator, container, false);




        main = (MainActivity) getActivity();

        main.removeShadow();

        main.setActionBarTitle("CGPA Calculator");

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        db = Room.databaseBuilder(main.getApplicationContext(),
                CgpaDatabase.class, "cgpaCalculator").allowMainThreadQueries().build();


        scroller = view.findViewById(R.id.cgpaScroller);


        scroller.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroller.setFocusable(true);
        scroller.setFocusableInTouchMode(true);





        final SharedPreferences prefs = main.getApplicationContext().getSharedPreferences(CGPA_CALCULATOR_DATA, MODE_PRIVATE);


        if(!prefs.contains("credit")) {

            updateCreditCgpa(0.f,0.00f);


        } else {

            updateManualCreditGpaInput();

            float spCredit = prefs.getFloat("credit",0.0f);
            float spCgpa = prefs.getFloat("cgpa",0.00f);

            updateCreditCgpa(spCredit,spCgpa);



        }




            // db.coursesDao().nukeTable();

        itemList = new ArrayList<CgpaRecyclerItem>();

        itemArrayAdapter2 = new CgpaRecyclerAdapter(R.layout.cgpa_recycler, itemList, getContext(),view, this);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.cgpaCalculatorRecycler);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        List<CgpaEntity> list = db.cgpaDao().getAll();
        String hmm = list.toString();

        //((TextView) v.findViewById(R.id.tttt)).setText(hmm);

        for (int i=0; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String credit = list.get(i).getCredit();
            String grade = list.get(i).getGrade();


            itemList.add(new CgpaRecyclerItem(course,credit,grade));


        }



        if(list.size() < 1){

            for (int i=0; i < 3; i++)
                itemList.add(new CgpaRecyclerItem("","3","A"));



        }


        recyclerView2.setAdapter(itemArrayAdapter2);



        FloatingActionButton addCourse = (FloatingActionButton) view.findViewById(R.id.cgpaAddCourseBtn);

        FloatingActionButton calculateCgpa = (FloatingActionButton) view.findViewById(R.id.cgpaCalculateBtn);


        addCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                itemArrayAdapter2.addData(new CgpaRecyclerItem("","3","A"));




                scroller.fullScroll(ScrollView.FOCUS_DOWN);


                scroller.clearFocus();



                //scroller.scrollTo(0, scroller.getBottom()+200);


            }

        });


        calculateCgpa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                calculateCgpa();

            }

        });








                return view;
    }


    public void calculateCgpa(){


        int totalItems = recyclerView2.getAdapter().getItemCount();

        db.cgpaDao().nukeTable();

        double totalPoint = 0;
        double totalCredit = 0;


        double cgpaAfter;


        String manualCredit = ((EditText) view.findViewById(R.id.cgpaManualCredit)).getText().toString();
        String manualGpa = ((EditText) view.findViewById(R.id.cgpaManualGpa)).getText().toString();


        for(int i=0; i < totalItems; i++) {



            View rv = recyclerView2.findViewHolderForAdapterPosition(i).itemView;

            String course = ((EditText) rv.findViewById(R.id.cgpaCourse)).getText().toString();
            String credit = ((Spinner) rv.findViewById(R.id.cgpaCredit)).getSelectedItem().toString();
            String grade = ((Spinner) rv.findViewById(R.id.cgpaGrade)).getSelectedItem().toString();


            if(course != null && !course.isEmpty()) {

                CgpaEntity arrData = new CgpaEntity();


                if (course.equals(""))
                    course = "Course";

                arrData.setCourse(course);
                arrData.setCredit(credit);
                arrData.setGrade(grade);
                db.cgpaDao().insertAll(arrData);

                totalCredit += Double.valueOf(credit);

                totalPoint += getGpaPoint(grade)*Double.valueOf(credit);


            } else if(course.equals("") && credit.equals("0")){

                itemArrayAdapter2.removeAt(i);
                i--;
                totalItems = recyclerView2.getAdapter().getItemCount();

            } else {

                Toast.makeText(getContext(),"Enter course initial.", Toast.LENGTH_LONG).show();


            }

        }


        try {



            if(manualCredit != null && !manualCredit.isEmpty() || manualGpa != null && !manualGpa.isEmpty()){


            double mgc =  Double.valueOf(manualGpa) * Double.valueOf(manualCredit);
            totalPoint += mgc;
            totalCredit += Double.valueOf(manualCredit);


            updateManualCreditCgpa(Float.valueOf(manualCredit), Float.valueOf(manualGpa));
            updateManualCreditGpaInput();




        }






            cgpaAfter = totalPoint / totalCredit;


            float cgpaAfterf = Float.valueOf(new DecimalFormat("#.##").format(cgpaAfter));

            float creditf = Float.valueOf(new DecimalFormat("#.##").format(totalCredit));


            updateCreditCgpa(creditf, cgpaAfterf);


            scroller.fullScroll(ScrollView.FOCUS_UP);
        } catch (Exception e){



        }



    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.help_button, menu);



    }






}
