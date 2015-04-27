package net.robertcooksey.caltrainalarmclock;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * The class is responsible for displaying the screen where the user selects their direction,
 * either northbound or southbound.
 */
public class DirectionFragment extends Fragment {
    // The root view for the fragment's layout
    private View mView;
    // Blank background view that is positioned behind the content layout in the z-axis
    private View mBackgroundView;
    // The content layout
    private RelativeLayout mDirectionLayout;
    // Keep track of whether we played the animation so that it does not play again
    // (i.e., so that we do not animate upon device orientation change)
    private volatile boolean mHasAnimated;
    // The application context
    private Context mContext;

    // The toggle buttons
    private ToggleButton mToggleNorth;
    private ToggleButton mToggleSouth;

    // The duration for the circular reveal animation, set from XML
    private int mDurationCircularReveal;
    private int mDurationFade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.content_direction, container, false);
        // Set the action bar title
        getActivity().setTitle(mContext.getResources().getString(R.string.direction_header));
        // Set up the fields
        mDirectionLayout = (RelativeLayout) mView.findViewById(R.id.content_direction);
        mBackgroundView = mView.findViewById(R.id.background_direction);
        mHasAnimated = false;
        mDurationCircularReveal = mContext.getResources().getInteger(R.integer.time_msec_circular_reveal);
        mDurationFade = mContext.getResources().getInteger(R.integer.time_msec_fade);
        mToggleNorth = (ToggleButton) mView.findViewById(R.id.direction_toggle_north);
        mToggleSouth = (ToggleButton) mView.findViewById(R.id.direction_toggle_south);
        setupToggleButtons();
        // Set the destination text based on which station was clicked
        setDirectionText();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // We need to trigger the animation in onStart, after the view has been inflated.
        if (!mHasAnimated) {
            startCircularReveal();
        }
    }

    /**
     * Set up the toggle buttons to ensure they are mutually exclusive.
     */
    private void setupToggleButtons() {
        mToggleNorth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mToggleNorth.isChecked();
                mToggleNorth.setChecked(checked);
                mToggleSouth.setChecked(!checked);
            }
        });
        mToggleSouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mToggleSouth.isChecked();
                mToggleSouth.setChecked(checked);
                mToggleNorth.setChecked(!checked);
            }
        });
    }

    /**
     * Private helper method to set the destination station.
     */
    private void setDirectionText() {
        // Obtain a reference to the TextView
        TextView textView = (TextView) mView.findViewById(R.id.direction_destination);
        // Get the station name that was passed into this fragment from the last one
        textView.setText(getArguments().getString(HomeActivity.STATION_NAME));
        // Center-align the text
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    /**
     * Start the circular reveal from the touch point in the previous fragment.
     */
    private void startCircularReveal() {
        // Get the touch points that were passed in from the previous fragment
        int cx = getArguments().getInt(HomeActivity.TOUCH_X);
        int cy = getArguments().getInt(HomeActivity.TOUCH_Y);
        // Obtain the dimensions of the display
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // Set the final radius for the circular reveal to the screen width
        int finalRadius = size.x;
        // Create the circular reveal animator with the params obtained above.
        Animator anim = ViewAnimationUtils.createCircularReveal(mDirectionLayout, cx, cy, 0, finalRadius);
        anim.setDuration(mDurationCircularReveal);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        mDirectionLayout.setVisibility(View.VISIBLE);
        anim.addListener(new CaltrainAnimationUtils.EmptyAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Fade in the new view while it's being revealed
                CaltrainAnimationUtils.startFadeIn(mDirectionLayout, mDurationFade, new AccelerateDecelerateInterpolator());
                // Fade out the old view by fading in a solid black view
                CaltrainAnimationUtils.startFadeIn(mBackgroundView, mDurationFade, new AccelerateDecelerateInterpolator());
                // Fade in the ActionBar
                CaltrainAnimationUtils.startFadeIn(getActionBarView(), mDurationFade, new AccelerateDecelerateInterpolator());
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mHasAnimated = true;
            }
        });
        anim.start();
    }

    /**
     * Private helper method (ugly hack) to return the ActionBar's View
     * @return
     */
    private View getActionBarView() {
        Window window = getActivity().getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", getActivity().getPackageName());
        return v.findViewById(resId);
    }
}
