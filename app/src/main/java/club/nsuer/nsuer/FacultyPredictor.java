package club.nsuer.nsuer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FacultyPredictor extends Fragment implements CoursesList {


    private MainActivity main;
    private Context context;
    private View view;
    private Spinner section;
    private ArrayList<FacultyPredictorItem> itemList;
    private int datePassed = 0;
    private String jsonCache = null;
    private FacultyPredictorAdapter itemAdapter;
    private RecyclerView recyclerView;
    private LinearLayout noPost;
    private LinearLayout predictLoading;
    private LinearLayout noPredict;
    private ImageView predictorSearchIcon;
    private TextView predictorText;


    public FacultyPredictor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        main = (MainActivity) getActivity();
        context = getContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.faculty_predictor, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.removeShadow();


        main.setActionBarTitle("Faculty Predictor");


        section = (Spinner) view.findViewById(R.id.predictorSection);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(context,
                R.array.numbers, R.layout.spinner_row_white);

        // adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        section.setPopupBackgroundResource(R.color.colorPrimary);
        section.setAdapter(adapter);


        noPredict = view.findViewById(R.id.noPredict);
        predictLoading = view.findViewById(R.id.predictLoading);


        predictorSearchIcon = view.findViewById(R.id.predictorSearchIcon);
        predictorText = view.findViewById(R.id.predictorText);


        itemList = new ArrayList<FacultyPredictorItem>();

        itemAdapter = new FacultyPredictorAdapter(R.layout.faculty_predictor_recycler, itemList, getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.predictionRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));

        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setAdapter(itemAdapter);


        final AutoCompleteTextView couseInitalSection = view.findViewById(R.id.courseInitialSection);
        final Spinner courseSection = view.findViewById(R.id.predictorSection);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (context, R.layout.suggestion_adapter_textview, SUGGESTIONS);

        couseInitalSection.setThreshold(2);
        couseInitalSection.setDropDownVerticalOffset(0);
        couseInitalSection.setAdapter(adapter2);

        final AutoCompleteTextView couseInitalFaculty = view.findViewById(R.id.courseInitialFaculty);
        final TextView facultyInitial = view.findViewById(R.id.facultyPredictorInitial);


        couseInitalFaculty.setThreshold(2);
        couseInitalFaculty.setDropDownVerticalOffset(0);
        couseInitalFaculty.setAdapter(adapter2);


        Button button = (Button) view.findViewById(R.id.predictorButtonSection);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String c = couseInitalSection.getText().toString();
                String s = courseSection.getSelectedItem().toString();

                couseInitalFaculty.setText("");
                facultyInitial.setText("");

                loadJson(c, s, "");


            }
        });


        Button button2 = (Button) view.findViewById(R.id.predictorButtonFaculty);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String c = couseInitalFaculty.getText().toString();
                String f = facultyInitial.getText().toString();

                couseInitalSection.setText("");
                courseSection.setSelection(0);

                loadJson(c, "", f);

            }
        });


    }


    private void loadJson(String course, String section, String faculty) {


        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection required!", Toast.LENGTH_SHORT).show();

            return;
        }

        itemList.clear();

        noPredict.setVisibility(View.GONE);

        itemAdapter.notifyDataSetChanged();


        predictLoading.setVisibility(View.VISIBLE);

        HashMap<String, String> parametters = new HashMap<String, String>();


        parametters.put("course", course);
        parametters.put("section", section);
        parametters.put("faculty", faculty);

        JSONParser parser = new JSONParser("https://nsuer.club/apps/faculty-predictor/get-json.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


                predictLoading.setVisibility(View.GONE);

                itemList.clear();

                loadRecylcer(result.toString());

                //Toast.makeText(getContext(),"Updated!",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure() {

                predictLoading.setVisibility(View.GONE);

                noPredict.setVisibility(View.VISIBLE);
                predictorText.setText("Unable to predict!");

                try {
                    predictorSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_nothing_found));
                } catch (Exception e) {

                    Log.e("Facutlty Predictor", e.toString());
                }


            }
        });


        parser.execute();


    }


    private void loadRecylcer(String string) {


        predictLoading.setVisibility(View.GONE);

        try {


            JSONObject result = new JSONObject(string);

            JSONArray obj = result.getJSONArray("dataArray");


            for (int j = 0; j < obj.length(); j++) {


                JSONObject data = obj.getJSONObject(j);


                String faculty = data.getString("faculty");
                String section = data.getString("section");
                String time = data.getString("time");
                String previousSection = data.getString("previousSections");

                itemList.add(new FacultyPredictorItem(faculty, section, time, previousSection));


            }

            if (itemList.size() > 0)
                noPredict.setVisibility(View.GONE);
            else {

                noPredict.setVisibility(View.VISIBLE);
                predictorText.setText("Unable to predict!");
                predictorSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_nothing_found));
            }

        } catch (JSONException e) {

            Log.e("JSON", e.toString());
        }


        itemAdapter.notifyDataSetChanged();


    }


}
