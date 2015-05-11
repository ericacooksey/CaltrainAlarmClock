package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by ecookse on 4/27/15.
 */
public class DirectionFragment extends CaltrainAnimatedFragment {

    // The toggle buttons
    private ToggleButton mToggleNorth;
    private ToggleButton mToggleSouth;
    private String mStationName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        // Add the content view to the layout
        addForegroundLayout(R.layout.fragment_content_direction);
        // Set the action bar title
        setTitle(mContext.getResources().getString(R.string.direction_header));
        // Set up the toggle buttons
        setupToggleButtons();
        // Obtain the direction text
        mStationName = getArguments().getString(HomeActivity.STATION_NAME);

        return mView;
    }

    /**
     * Private helper method to set up the toggle button behavior.
     */
    private void setupToggleButtons() {
        mToggleNorth = (ToggleButton) mView.findViewById(R.id.direction_toggle_north);
        mToggleSouth = (ToggleButton) mView.findViewById(R.id.direction_toggle_south);
        // Set listeners to capture the touch point
        mToggleNorth.setOnTouchListener(new CaptureTouchPointListener());
        mToggleSouth.setOnTouchListener(new CaptureTouchPointListener());
        // Set listeners to ensure that the toggle buttons are mutually exclusive
        mToggleNorth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mToggleNorth.isChecked();
                mToggleNorth.setChecked(checked);
                mToggleSouth.setChecked(!checked);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.STATION_NAME, mStationName);
                bundle.putInt(HomeActivity.TOUCH_X, mTouchX);
                bundle.putInt(HomeActivity.TOUCH_Y, mTouchY);
                bundle.putString(HomeActivity.DIRECTION, HomeActivity.NORTH);
                mCallback.onDirectionSelected(bundle);
            }
        });
        mToggleSouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mToggleSouth.isChecked();
                mToggleSouth.setChecked(checked);
                mToggleNorth.setChecked(!checked);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.STATION_NAME, mStationName);
                bundle.putInt(HomeActivity.TOUCH_X, mTouchX);
                bundle.putInt(HomeActivity.TOUCH_Y, mTouchY);
                bundle.putString(HomeActivity.DIRECTION, HomeActivity.SOUTH);
                mCallback.onDirectionSelected(bundle);
            }
        });
    }
}
