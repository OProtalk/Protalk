package sharkfeel.homeautomation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Jimyeol on 2017-05-25.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                BedroomFrag fragBedroom = new BedroomFrag();
                return fragBedroom;
            case 1:
                LivingFrag fragLiving = new LivingFrag();
                return fragLiving;
            case 2:
                BathroomFrag fragBathroom = new BathroomFrag();
                return fragBathroom;
            case 3:
                KitchenFrag fragKitchen = new KitchenFrag();
                return fragKitchen;
            case 4:
                YardFrag fragYard = new YardFrag();
                return fragYard;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}