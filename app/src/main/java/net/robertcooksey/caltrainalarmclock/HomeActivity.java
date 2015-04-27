package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements ICaltrainActivity {

    // Hard-coded strings used as keys when passing values between fragments
    public static final String STATION_NAME = "station_name";
    public static final String TOUCH_X = "touch_x";
    public static final String TOUCH_Y = "touch_y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.home_header));
        goToFragment(StationListFragment.class.getSimpleName(), null);
    }

    /**
     * Callback invoked by StationListFragment when a station in the list is selected.
     * @param bundle Arguments passed back to the activity,
     *               which should contain the name of the selected station.
     */
    @Override
    public void onStationSelected(Bundle bundle) {
        goToFragment(DirectionFragment.class.getSimpleName(), bundle);
    }

    /**
     * Private helper method to switch to a fragment
     * @param fragmentName
     * @param args
     */
    private void goToFragment(String fragmentName, Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if (fragmentName.equals(StationListFragment.class.getSimpleName())) {
            fragment = new StationListFragment();
        } else if (fragmentName.equals(DirectionFragment.class.getSimpleName())) {
            fragment = new DirectionFragment();
        }
        if (fragment != null) {
            fragment.setArguments(args);
            fragmentTransaction.add(android.R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
