package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ecookse on 4/27/15.
 */
public class AlarmFragment extends CaltrainAnimatedFragment {

    private TextView mNumStopsTextView;
    private TextView mDestinationTextView;
    private TextView mDirectionTextView;
    private String mSelectedStation;
    private String mDirection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        // Add the content view to the layout
        addForegroundLayout(R.layout.fragment_content_alarm);
        // Set the action bar title
        getActivity().setTitle(mContext.getString(R.string.stops_header));
        setupText();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setupText() {
        mNumStopsTextView = (TextView) mView.findViewById(R.id.text_num_stations);
        mDestinationTextView = (TextView) mView.findViewById(R.id.text_destination);
        mDirectionTextView = (TextView) mView.findViewById(R.id.text_direction);

        mNumStopsTextView.setText(getArguments().getString(HomeActivity.NUM_STATIONS));
        mSelectedStation = getArguments().getString(HomeActivity.STATION_NAME);
        mDestinationTextView.setText(mSelectedStation);
        mDirection = getArguments().getString(HomeActivity.DIRECTION);
        if (mDirection.equals(HomeActivity.NORTH)) {
            mDirectionTextView.setText(mContext.getResources().getString(R.string.northbound));
        } else {
            mDirectionTextView.setText(mContext.getResources().getString(R.string.southbound));
        }
    }
}
