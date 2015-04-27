package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Fragment to show the list of stations from which the user can make a selection.
 */
public class StationListFragment extends Fragment {
    private View mView;
    private ICaltrainActivity mCallback;
    // The ListView of stations
    private ListView mStationList;
    // The adapter used to populate the list
    private StationAdapter mStationAdapter;
    // Store the touch points to be used for the transition animation.
    private int mTouchX, mTouchY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCallback = (ICaltrainActivity) getActivity();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_home, container, false);
        getActivity().setTitle(R.string.home_header);
        mStationList = (ListView) mView.findViewById(R.id.home_station_list);
        mStationAdapter = new StationAdapter(getActivity());
        mStationList.setAdapter(mStationAdapter);
        mStationList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Store the touch points when a station is selected.
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return false;
            }
        });
        mStationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create a bundle containing the selected station name and touch points
                Bundle bundle = new Bundle();
                String stationName = mStationAdapter.getItem(position);
                bundle.putString(HomeActivity.STATION_NAME, stationName);
                bundle.putInt(HomeActivity.TOUCH_X, mTouchX);
                bundle.putInt(HomeActivity.TOUCH_Y, mTouchY);
                // Trigger the activity callback to dispatch the next fragment, passing it the bundle.
                mCallback.onStationSelected(bundle);
            }
        });
        return mView;
    }
}
