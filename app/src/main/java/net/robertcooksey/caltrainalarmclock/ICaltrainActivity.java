package net.robertcooksey.caltrainalarmclock;

import android.os.Bundle;

/**
 * Interface implemented by the activity.
 * Holds callbacks to the activity that are triggered by various fragments.
 */
public interface ICaltrainActivity {
    public void onStationChange(Bundle bundle);
    public void onStationSelected(Bundle bundle);
    public void onDirectionSelected(Bundle bundle);
    public void onNumStationsSelected(Bundle bundle);
}
