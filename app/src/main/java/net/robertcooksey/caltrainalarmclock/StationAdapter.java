package net.robertcooksey.caltrainalarmclock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ecookse on 4/25/15.
 */
public class StationAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mStations;

    public StationAdapter(Context context) {
        mContext = context;
        String[] stationNames = mContext.getResources().getStringArray(R.array.station_list);
        mStations = new ArrayList<>(Arrays.asList(stationNames));
        mLayoutInflater = ((Activity)mContext).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mStations.size();
    }

    @Override
    public String getItem(int position) {
        return mStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStations.get(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView stationTextView = (TextView) mLayoutInflater.inflate(R.layout.station_list_item, null);
        final String stationName = mStations.get(position);
        stationTextView.setText(stationName);
        return stationTextView;
    }
}
