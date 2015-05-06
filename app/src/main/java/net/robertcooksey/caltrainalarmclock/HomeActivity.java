package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;

/**
 * Main activity class. Responsible for dispatching fragments.
 */
public class HomeActivity extends AppCompatActivity implements ICaltrainActivity {

    // Hard-coded strings used as keys when passing values between fragments
    public static final String STATION_NAME = "station_name";
    public static final String TOUCH_X = "touch_x";
    public static final String TOUCH_Y = "touch_y";
    public static final String DIRECTION = "direction";
    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String NUM_STATIONS = "num_stations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.home_header));
        goToFragment(StationListFragment.class.getSimpleName(), null, false);
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

    @Override
    public void onDirectionSelected(Bundle bundle) {
        goToFragment(NumberStopsFragment.class.getSimpleName(), bundle);
    }

    @Override
    public void onNumStationsSelected(Bundle bundle) {
        goToFragment(AlarmFragment.class.getSimpleName(), bundle);
    }

    /**
     * Private helper method to switch to a fragment
     * @param fragmentName
     * @param args
     */
    private void goToFragment(String fragmentName, Bundle args, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if (fragmentName.equals(StationListFragment.class.getSimpleName())) {
            fragment = new StationListFragment();
        } else if (fragmentName.equals(DirectionFragment.class.getSimpleName())) {
            fragment = new DirectionFragment();
        } else if (fragmentName.equals(NumberStopsFragment.class.getSimpleName())) {
            fragment = new NumberStopsFragment();
        } else if (fragmentName.equals(AlarmFragment.class.getSimpleName())) {
            fragment = new AlarmFragment();
        }
        if (fragment != null) {
            fragment.setArguments(args);
            fragment.setExitTransition(new Fade(Fade.OUT));
            fragmentTransaction.replace(android.R.id.content, fragment);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    /**
     * Wrapper method for goToFragment, which adds to the backstack by default.
     * @param fragmentName
     * @param args
     */
    private void goToFragment(String fragmentName, Bundle args) {
        goToFragment(fragmentName, args, true);
    }
}
