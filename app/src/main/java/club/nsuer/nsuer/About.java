package club.nsuer.nsuer;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class About extends Fragment {


    private View view;
    private MainActivity main;

    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);

        main = MainActivity.getInstance();

        main.removeShadow();

        main.setActionBarTitle("About the app");

        TextView version = view.findViewById(R.id.appVersion);
        TextView lastupdate = view.findViewById(R.id.appUpdateDate);

        version.setText(BuildConfig.VERSION_NAME);

        lastupdate.setText("27 July, 2019");

        TextView email = view.findViewById(R.id.hmtamimEmail);


        return view;
    }

}
