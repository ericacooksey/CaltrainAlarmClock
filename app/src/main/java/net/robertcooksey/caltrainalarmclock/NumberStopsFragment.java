package net.robertcooksey.caltrainalarmclock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by ecookse on 4/27/15.
 */
public class NumberStopsFragment extends CaltrainAnimatedFragment {

    private Button mBtnStops;
    private String mSelectedStation;
    private String mDirection;
    private String[] mStationNames;
    private int mNumStations;

    private static final int MAX_STATIONS = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        mSelectedStation = getArguments().getString(HomeActivity.STATION_NAME);
        mDirection = getArguments().getString(HomeActivity.DIRECTION);
        // Add the content view to the layout
        addForegroundLayout(R.layout.fragment_content_number_stops);
        // Set the action bar title
        getActivity().setTitle(mContext.getString(R.string.stops_header));
        // Set up the button
        setupButton();
        return mView;
    }

    private void setupButton() {
        mBtnStops = (Button) mView.findViewById(R.id.btn_num_stations);
        mBtnStops.setOnTouchListener(new CaptureTouchPointListener());
        mBtnStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] selections = getStationOptions();
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CaltrainAlarmClockTheme_AlertDialog);
                builder.setTitle(getResources().getString(R.string.stops_header))
                        .setItems(selections, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNumStations = which;
                                mBtnStops.setText(getStationOptions()[mNumStations]);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Bundle bundle = new Bundle();
                        bundle.putString(HomeActivity.NUM_STATIONS, (String) mBtnStops.getText());
                        bundle.putString(HomeActivity.DIRECTION, mDirection);
                        bundle.putString(HomeActivity.STATION_NAME, mSelectedStation);
                        mCallback.onNumStationsSelected(bundle);
                    }
                });
                dialog.show();
            }
        });
    }

    private CharSequence[] getStationOptions() {
        mStationNames = getResources().getStringArray(R.array.station_list);
        int selectedStationIndex = Arrays.asList(mStationNames).indexOf(mSelectedStation);
        int maxNumStations;
        if (mDirection == HomeActivity.NORTH) {
            maxNumStations = Math.min(MAX_STATIONS, mStationNames.length - selectedStationIndex);
        } else {
            maxNumStations = Math.min(MAX_STATIONS, selectedStationIndex + 1);
        }
        CharSequence[] selections = new CharSequence[maxNumStations];
        if (mDirection == HomeActivity.NORTH) {
            for (int i = maxNumStations; i > 0; i--) {
                if (i == 1) {
                    selections[maxNumStations - i] = String.format(getResources().getString(
                            R.string.stop_formatter_single), (maxNumStations - i),
                            mStationNames[selectedStationIndex + (maxNumStations - i)]);
                } else {
                    selections[maxNumStations - i] = String.format(getResources().getString(
                            R.string.stop_formatter_plural), (maxNumStations - i),
                            mStationNames[selectedStationIndex + (maxNumStations - i)]);
                }
            }
        } else {
            for (int i = 0; i < maxNumStations; i++) {
                if (i == 1) {
                    selections[i] = String.format(getResources().getString(
                            R.string.stop_formatter_single), i, mStationNames[selectedStationIndex - i]);
                } else {
                    selections[i] = String.format(getResources().getString(
                            R.string.stop_formatter_plural), i, mStationNames[selectedStationIndex - i]);
                }
            }
        }
        return selections;
    }
}
