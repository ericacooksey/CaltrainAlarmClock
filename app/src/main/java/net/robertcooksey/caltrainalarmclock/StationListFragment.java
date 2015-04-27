package net.robertcooksey.caltrainalarmclock;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ecookse on 4/26/15.
 */
public class StationListFragment extends Fragment {
    private View mView;
    private ListView mStationList;
    private StationAdapter mStationAdapter;
    private ICaltrainActivity mCallback;
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
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return false;
            }
        });
        mStationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                String stationName = mStationAdapter.getItem(position);
                bundle.putString(HomeActivity.STATION_NAME, stationName);
                bundle.putInt(HomeActivity.TOUCH_X, mTouchX);
                bundle.putInt(HomeActivity.TOUCH_Y, mTouchY);
                mCallback.onStationSelected(bundle);
            }
        });
        return mView;
    }
}
