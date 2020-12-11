package club.nsuer.nsuer;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Contribute extends Fragment {


    private MainActivity main;
    private View view;
    private Context context;

    public Contribute() {
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
        view = inflater.inflate(R.layout.fragment_contribute, container, false);


        main.setActionBarTitle("Contribute");


        return view;
    }

}
