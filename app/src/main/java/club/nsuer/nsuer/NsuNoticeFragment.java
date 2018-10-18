package club.nsuer.nsuer;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class NsuNoticeFragment extends Fragment {


    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NsuNoticesTabAdapter viewPagerAdapter;

    public NsuNoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_nsu_notice, container, false);

        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ProgressBar p = (ProgressBar) v.findViewById(R.id.progressBarOnProfile);

        p.setVisibility(View.GONE);

        tabLayout = (TabLayout) v.findViewById(R.id.nsuNoticesTabLayout);

        viewPager = (ViewPager) v.findViewById(R.id.nsuNoticesViewPager);


        viewPager.setOffscreenPageLimit(1);

        if (!isAdded()) return;
        viewPagerAdapter = new NsuNoticesTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }


}
