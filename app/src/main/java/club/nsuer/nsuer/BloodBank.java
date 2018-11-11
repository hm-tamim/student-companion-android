package club.nsuer.nsuer;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class BloodBank extends Fragment {

    private MainActivity main;
    private Context context;
    private View v;

    public BloodBank() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        main = (MainActivity) getActivity();
        context = getContext();


        makeRed();
    }

    private void makeRed(){

        ActionBar bar = main.getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d60823")));


        Window window = main.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#d60823"));
        main.setMenuBackground(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_blood_bank, container, false);


        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Blood Bank");
    }



    @Override
    public void onResume() {
        super.onResume();

        makeRed();


    }

    @Override
    public void onPause() {
        super.onPause();
        ActionBar bar = main.getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4196af")));

        Window window = main.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#388096"));
        main.setMenuBackground(false);
    }
}
