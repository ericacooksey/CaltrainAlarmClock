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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        // Add the content view to the layout
        addForegroundLayout(R.layout.fragment_content_alarm);
        // Set the action bar title
        setTitle(mContext.getString(R.string.stops_header));
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
