package club.nsuer.nsuer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class NsuNoticesTabAdapter extends FragmentPagerAdapter {

    //integer to count number of tabs
    int tabCount;

    private int mCurrentPosition = -1;


    //Constructor to the class
    public NsuNoticesTabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
//Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
//Returning the current tabs
        switch (position) {
            case 0:
                NsuNotices tab1 = new NsuNotices("notices");
                return tab1;
            case 1:
                NsuNotices tab2 = new NsuNotices("events");
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }





    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof DynamicHeightViewPager) {
            Fragment fragment = (Fragment) object;
            DynamicHeightViewPager pager = (DynamicHeightViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }





}
