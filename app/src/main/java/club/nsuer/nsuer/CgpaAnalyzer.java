package club.nsuer.nsuer;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CgpaAnalyzer extends Fragment {


    private View view;
    private MainActivity main;
    private Context context;
    private CgpaDatabase db;
    private ArrayList<CgpaRecyclerItem> itemList;
    private CgpaRecyclerAdapter itemArrayAdapter2;
    private RecyclerView recyclerView2;
    private HorizontalBarChart chartSubject;
    private int eeeColor;
    private int whiteColor;
    private BarChart chart;
    private BarData dataSub;
    private LinearLayout graphLayout;
    private ScrollView scroller;

    private List<BarEntry> entries;
    private TextView analyzerRequiredText;
    private ArrayList<String> courses;

    private XAxis xAxisx;

    private List<CgpaEntity> list;

    private List<BarEntry> targetGrade;
    private List<BarEntry> expectedGrade;

    private BarDataSet setSub;
    private BarDataSet setSub2;

    private ArrayList<String> labelsSub;

    private static final String CGPA_ANALYZER_DATA = "CgpaAnalyzerData";

    private SessionManager sessionManager;

    public CgpaAnalyzer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        main = (MainActivity) getActivity();
        context = getContext();
        db = Room.databaseBuilder(main.getApplicationContext(),
                CgpaDatabase.class, "cgpaAnalyzer").allowMainThreadQueries().build();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.cgpa_analyzer_activity, container, false);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.help_button, menu);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        main.setActionBarTitle("CGPA Analyzer");

        sessionManager = new SessionManager(context);


        graphLayout = (LinearLayout) view.findViewById(R.id.analyzeGraphLayout);
        analyzerRequiredText = view.findViewById(R.id.analyzerRequiredText);


        scroller = view.findViewById(R.id.analyzeScrollView);


        scroller.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroller.setFocusable(true);
        scroller.setFocusableInTouchMode(true);


        itemList = new ArrayList<CgpaRecyclerItem>();

        itemArrayAdapter2 = new CgpaRecyclerAdapter(R.layout.cgpa_recycler, itemList, getContext(), view, this);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.cgpaCalculatorRecycler);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        list = db.cgpaDao().getAll();


        for (int i = 0; i < list.size(); i++) {


            String course = list.get(i).getCourse();
            String credit = list.get(i).getCredit();
            String grade = list.get(i).getGrade();


            itemList.add(new CgpaRecyclerItem(course, credit, grade));


        }


        if (list.size() < 1) {

            for (int i = 0; i < 3; i++)
                itemList.add(new CgpaRecyclerItem("", "3", "A"));


        }


        recyclerView2.setAdapter(itemArrayAdapter2);


        Button addCourse = view.findViewById(R.id.analyzeAddCourseButton);
        Button analayzeButton = view.findViewById(R.id.analyzeButton);

        addCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (itemArrayAdapter2.getSize() >= 10) {
                    Toast.makeText(context, "Can't add more than 10 courses", Toast.LENGTH_SHORT).show();
                    return;
                }

                itemArrayAdapter2.addData(new CgpaRecyclerItem("", "3", "A"));


                scroller.post(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(View.FOCUS_DOWN);
                    }
                });

                //scroller.fullScroll(ScrollView.FOCUS_DOWN);


                //scroller.clearFocus();


                scroller.scrollTo(0, scroller.getBottom() + 500);


            }

        });

        analayzeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                analyzeCgpa();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(CgpaAnalyzer.this).attach(CgpaAnalyzer.this).commit();

                        scroller.fullScroll(ScrollView.FOCUS_UP);
                    }
                }, 100);


                scroller.fullScroll(ScrollView.FOCUS_UP);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        //scroller.fullScroll(ScrollView.FOCUS_UP);
                    }
                }, 300);

            }

        });


        chart = (BarChart) view.findViewById(R.id.cgpaBarChart);


        final ArrayList<String> labels = new ArrayList<>();

        labels.add("Total CGPA");
        labels.add("Your CGPA");
        labels.add("Target CGPA");
        labels.add("Expected CGPA");


        entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4.0f));
        entries.add(new BarEntry(1f, 3.43f));
        entries.add(new BarEntry(2f, 3.70f));
        entries.add(new BarEntry(3f, 3.51f));


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0.1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(0.2f);
        leftAxis.setGranularityEnabled(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });


        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(17f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        YAxis axisRight = chart.getAxisRight();
        axisRight.setEnabled(false);
        YAxis axisLeft = chart.getAxisLeft();

        LimitLine ll = new LimitLine(15, "Max Capacity");

        eeeColor = ContextCompat.getColor(context, R.color.eee);

        ll.setLineColor(eeeColor);
        ll.setLineWidth(1f);
        ll.setTextSize(12f);

        axisLeft.removeAllLimitLines();

        chart.getAxisLeft().addLimitLine(ll);

        BarDataSet set = new BarDataSet(entries, "");


        BarData data = new BarData(set);

        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat mFormat = new DecimalFormat("###,###,##0.00");
                return mFormat.format(value);
            }
        });


        data.setBarWidth(0.8f); // set custom bar width
        set.setValueTextSize(14f);

        set.setColors(new int[]{R.color.barColor1, R.color.barColor2, R.color.barColor3, R.color.barColor4}, context);

        whiteColor = ContextCompat.getColor(context, R.color.white);
        set.setValueTextColor(whiteColor);
        chart.setDrawValueAboveBar(false);
        set.setDrawValues(true);

        chart.getAxisLeft().setAxisMinimum(1f);
        chart.getAxisLeft().setAxisMaximum(4.0f);

        chart.setData(data);
        chart.setDescription(null);
        chart.getAxisRight().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.animateY(900);

        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.invalidate(); // refresh


        courses = new ArrayList<>();


        labelsSub = new ArrayList<>();


        // update bar data


        chartSubject = (HorizontalBarChart) view.findViewById(R.id.subjectBarChart);

        dataSub = new BarData();

        XAxis xAxisSub = chartSubject.getXAxis();
        xAxisSub.setGranularity(1f);
        xAxisSub.setGranularityEnabled(true);
        xAxisSub.setTextSize(12f);
        xAxisSub.setYOffset(0.1f);
        xAxisSub.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxisSub = chartSubject.getAxisLeft();
        leftAxisSub.setEnabled(false);


        chartSubject.getAxisLeft().setAxisMinimum(0.0f);
        chartSubject.getAxisLeft().setAxisMaximum(4.1f);

        chartSubject.setData(dataSub);
        chartSubject.setDescription(null);
        chartSubject.getLegend().setEnabled(true);

        chartSubject.getLegend().setTextSize(14f);

        chartSubject.animateY(900);


        chartSubject.getXAxis().setGranularity(1F);
        chartSubject.getXAxis().setCenterAxisLabels(true);
        chartSubject.getXAxis().setDrawGridLines(true);

        chartSubject.setTouchEnabled(true);
        chartSubject.setPinchZoom(false);
        chartSubject.setDoubleTapToZoomEnabled(false);

        xAxisx = chartSubject.getXAxis();


        targetGrade = new ArrayList<>();
        expectedGrade = new ArrayList<>();


        setSub = new BarDataSet(expectedGrade, "Required");
        setSub.setValueFormatter(new GradeValueChart());

        setSub2 = new BarDataSet(targetGrade, "Expected");
        setSub2.setValueFormatter(new GradeValueChart());


        int reqColor = ContextCompat.getColor(context, R.color.barColor6);
        int exColor = ContextCompat.getColor(context, R.color.barColor7);


        setSub2.setColor(reqColor);
        setSub.setColor(exColor);


        setSub.setValueTextColor(whiteColor);
        setSub2.setValueTextColor(whiteColor);

        chartSubject.setDrawValueAboveBar(false);
        setSub.setDrawValues(true);


        dataSub.addDataSet(setSub);
        dataSub.addDataSet(setSub2);


        YAxis axisRightSub = chartSubject.getAxisRight();
        axisRightSub.setEnabled(false);


        Paint pp = chartSubject.getPaint(chartSubject.PAINT_INFO);
        pp.setTextSize(17f);


        dataSub.setBarWidth(0.9f); // set custom bar width
        setSub.setValueTextSize(14f);
        setSub2.setValueTextSize(14f);


        dataSub.setBarWidth(0.50f);

        chartSubject.getXAxis().setAxisMinimum(0);


        updateManualCreditGpaInput();


    }


    private void updateGradeComp(final String course, float requiredPoints, float actualPoints) {


        if (requiredPoints >= 4) {

            requiredPoints = 4.0f;

        }

        labelsSub.add(course);


        float currentPos = (float) courses.size();

        targetGrade.add(new BarEntry(currentPos, requiredPoints));
        expectedGrade.add(new BarEntry(currentPos, actualPoints));


        courses.add(course);


        xAxisx.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {


                String val = null;
                try {
                    val = courses.get((int) value);
                } catch (IndexOutOfBoundsException e) {

                }
                return val;
            }
        });


        float barSpace = 0.01f;
        float groupSpace = 0.0f;
        int groupCount = courses.size();


        chartSubject.getXAxis().setAxisMaximum(0 + chartSubject.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chartSubject.groupBars(0, groupSpace, barSpace);
        chartSubject.invalidate(); // refresh


    }


    private void updateManualCreditCgpa(float credit, float cgpa, float target, float expected, float requiredPoints) {

        SharedPreferences.Editor editor = context.getSharedPreferences(CGPA_ANALYZER_DATA, MODE_PRIVATE).edit();
        editor.putFloat("yourCgpa", cgpa);
        editor.putFloat("yourCredit", credit);
        editor.putFloat("yourTarget", target);
        editor.putFloat("expectedCgpa", expected);
        editor.putFloat("requiredPoints", requiredPoints);


        updateBarGraph(cgpa, target, expected);

        editor.apply();

    }


    private void updateBarGraph(float cgpa, float target, float expected) {


        if (entries.size() > 0)
            entries.clear();

        entries.add(new BarEntry(0f, 4.0f));
        entries.add(new BarEntry(1f, cgpa));
        entries.add(new BarEntry(2f, target));
        entries.add(new BarEntry(3f, expected));


        chart.animateY(900);
        chart.invalidate();

    }


    public void updateManualCreditGpaInput() {


        SharedPreferences prefss = context.getSharedPreferences(CGPA_ANALYZER_DATA, MODE_PRIVATE);


        if (prefss.contains("yourTarget")) {


            float credits = prefss.getFloat("yourCredit", 0.0f);
            float gpa = prefss.getFloat("yourCgpa", 0.0f);
            float targetCgpa = prefss.getFloat("yourTarget", 0.0f);
            float expected = prefss.getFloat("expectedCgpa", 0.0f);
            float requiredPoints = prefss.getFloat("requiredPoints", 0.0f);


            String creditz;

            if ((credits - (int) credits) != 0)
                creditz = String.valueOf(new DecimalFormat("#.##").format(credits));
            else
                creditz = String.valueOf(new DecimalFormat("#").format(credits));


            ((EditText) view.findViewById(R.id.yourCredits)).setText(creditz, TextView.BufferType.EDITABLE);
            ((EditText) view.findViewById(R.id.yourCgpa)).setText(String.valueOf(gpa), TextView.BufferType.EDITABLE);
            ((EditText) view.findViewById(R.id.targetCgpa)).setText(String.valueOf(targetCgpa), TextView.BufferType.EDITABLE);

            String reqText;

            if (requiredPoints > 4)
                reqText = "You will need another semester to achieve your targeted CGPA. You will need <b>" + requiredPoints + "</b> points per course in this semester, which is not possible.";
            else
                reqText = "You will need to get <b>" + Utils.getGpaGrade(requiredPoints) + "</b> grade(<b>" + requiredPoints + "</b> points) per course to achieve your targeted CGPA.";

            analyzerRequiredText.setText(Html.fromHtml(reqText));


            updateBarGraph(gpa, targetCgpa, expected);
            addCourseFromDB();

        } else {


            ((EditText) view.findViewById(R.id.yourCgpa)).setText(String.valueOf(sessionManager.getCgpa()), TextView.BufferType.EDITABLE);
            ((EditText) view.findViewById(R.id.yourCredits)).setText(String.valueOf(sessionManager.getCredit()), TextView.BufferType.EDITABLE);

            graphLayout.setVisibility(View.GONE);

        }

    }


    public void analyzeCgpa() {


        int totalItems = recyclerView2.getAdapter().getItemCount();

        db.cgpaDao().nukeTable();

        double totalPoint = 0;
        double totalCredit = 0;

        double currentSemCredit = 0;


        double cgpaAfter;


        String targetCgpa = ((EditText) view.findViewById(R.id.targetCgpa)).getText().toString();
        String manualGpa = ((EditText) view.findViewById(R.id.yourCgpa)).getText().toString();
        String manualCredit = ((EditText) view.findViewById(R.id.yourCredits)).getText().toString();

        if (targetCgpa.equals("")) {
            Toast.makeText(context, "Enter target CGPA", Toast.LENGTH_SHORT).show();
            return;
        } else if (manualGpa.equals("")) {
            Toast.makeText(context, "Enter your CGPA", Toast.LENGTH_SHORT).show();
            return;
        } else if (manualCredit.equals("")) {
            Toast.makeText(context, "Enter your credits", Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i = 0; i < totalItems; i++) {


            View rv = recyclerView2.findViewHolderForAdapterPosition(i).itemView;

            String course = ((EditText) rv.findViewById(R.id.cgpaCourse)).getText().toString();
            String credit = ((Spinner) rv.findViewById(R.id.cgpaCredit)).getSelectedItem().toString();
            String grade = ((Spinner) rv.findViewById(R.id.cgpaGrade)).getSelectedItem().toString();


            if (course.equals("")) {

                Toast.makeText(context, "You must enter course initial.", Toast.LENGTH_SHORT).show();


            }

            if (course != null && !course.isEmpty()) {

                CgpaEntity arrData = new CgpaEntity();

                arrData.setCourse(course);
                arrData.setCredit(credit);
                arrData.setGrade(grade);
                db.cgpaDao().insertAll(arrData);

                totalCredit += Double.valueOf(credit);

                totalPoint += getGpaPoint(grade) * Double.valueOf(credit);

                currentSemCredit += Double.valueOf(credit);


            } else {

                itemArrayAdapter2.removeAt(i);
                i--;
                totalItems = recyclerView2.getAdapter().getItemCount();

            }

        }


        if (totalCredit < 1) {

            Toast.makeText(context, "Credits can't be 0", Toast.LENGTH_SHORT).show();
            return;

        }


        double mgc = Double.valueOf(manualGpa) * Double.valueOf(manualCredit);
        totalPoint += mgc;
        totalCredit += Double.valueOf(manualCredit);


        cgpaAfter = totalPoint / totalCredit;


        float expectedCgpa = Float.valueOf(new DecimalFormat("#.##").format(cgpaAfter));
        float creditf = Float.valueOf(new DecimalFormat("#.##").format(totalCredit));


        double achiveCgpa1 = Double.valueOf(targetCgpa) * totalCredit;

        double achiveCgpa2 = Double.valueOf(manualGpa) * Double.valueOf(manualCredit);

        double pointDifference = achiveCgpa1 - achiveCgpa2;

        double requiredPoints = pointDifference / currentSemCredit;


        DecimalFormat mFormat = new DecimalFormat("###,###,##0.00");
        String reqqPoints = mFormat.format(requiredPoints);


        updateManualCreditCgpa(Float.valueOf(manualCredit), Float.valueOf(manualGpa), Float.valueOf(targetCgpa), expectedCgpa, Float.valueOf(reqqPoints));
        updateManualCreditGpaInput();

        graphLayout.setVisibility(View.VISIBLE);


    }


    private void addCourseFromDB() {


        courses.clear();
        labelsSub.clear();
        chartSubject.invalidate();

        SharedPreferences prefss = context.getSharedPreferences(CGPA_ANALYZER_DATA, MODE_PRIVATE);


        float requiredPoints;

        if (prefss.contains("yourTarget")) {

            requiredPoints = prefss.getFloat("requiredPoints", 0.0f);
        } else {

            requiredPoints = 2.6f;


        }


        for (int i = list.size() - 1; i >= 0; i--) {


            String course = list.get(i).getCourse();

            String grade = list.get(i).getGrade();

            updateGradeComp(course, requiredPoints, (float) getGpaPoint(grade));

        }


    }


    public double getGpaPoint(String grade) {


        Log.d("Plus sign", grade);

        // grade = grade.replaceAll(Pattern.quote("+"), "p");

        double point = 0.0;
        if (grade.equals("A"))
            point = 4.0;
        else if (grade.equals("A-"))
            point = 3.7;
        else if (grade.equals("B+"))
            point = 3.3;
        else if (grade.equals("B"))
            point = 3.0;
        else if (grade.equals("B-"))
            point = 2.7;
        else if (grade.equals("Cp"))
            point = 2.3;
        else if (grade.equals("C"))
            point = 2.0;
        else if (grade.equals("C-"))
            point = 1.7;
        else if (grade.equals("Dp"))
            point = 1.3;
        else if (grade.equals("D"))
            point = 1.0;
        else if (grade.equals("F"))
            point = 0.0;
        else
            point = 0.0;

        return point;
    }


}

