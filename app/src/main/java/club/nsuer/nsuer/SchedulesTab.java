package club.nsuer.nsuer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SchedulesTab extends Fragment {

    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SchedulesTabAdapter viewPagerAdapter;
    private FragmentTransaction ft;


    public SchedulesTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_schedules_tab, container, false);

        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        MainActivity main = (MainActivity) getActivity();

        main.setActionBarTitle("Schedules");


        ft = getFragmentManager().beginTransaction();

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);


        viewPager.setOffscreenPageLimit(1);

        if (!isAdded()) return;
        viewPagerAdapter = new SchedulesTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            ft.detach(SchedulesTab.this).attach(SchedulesTab.this).commit();
    }


}
