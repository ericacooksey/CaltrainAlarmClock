package net.robertcooksey.caltrainalarmclock;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ActionBarContainer;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Base class with built-in transition animations.
 */
public class CaltrainAnimatedFragment extends Fragment {

    /* Values which affect the animation */
    public static final int DURATION_CIRCULAR_REVEAL = 350;
    public static final int DURATION_FADE = 500;
    public static final int DURATION_ACTION_BAR_FADE = 400;
    /* For a full list of interpolators, please see
    http://developer.android.com/reference/android/view/animation/Interpolator.html */
    protected Interpolator mCircularRevealInterpolator = new AccelerateInterpolator();
    protected Interpolator mFadeInterpolator = new AccelerateDecelerateInterpolator();
    protected Interpolator mActionBarFadeInterpolator = new AccelerateDecelerateInterpolator();

    protected Context mContext;
    protected View mView;
    protected ICaltrainActivity mCallback;
    protected FrameLayout mRootLayout;
    protected View mBackgroundView;
    protected RelativeLayout mForegroundLayout;
    protected int mTouchX, mTouchY;
    private LayoutInflater mInflater;
    private volatile boolean mHasAnimated;
    private CharSequence mTitle;
    private TextView mStationView;
    private TextView mDirectionView;
    private TextView mNumStationsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mCallback = (ICaltrainActivity) getActivity();
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_layout, container, false);
        mHasAnimated = false;
        mRootLayout = (FrameLayout) mView.findViewById(R.id.fragment_layout_root);
        mBackgroundView = mView.findViewById(R.id.fragment_layout_background);
        mForegroundLayout = new RelativeLayout(mContext);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mForegroundLayout != null) {
            setupDestinationTextView();
            setupDirectionTextView();
            setupNumStationsTextView();
        }
    }

    /**
     * Add the content layout to the fragment
     * @param layoutId resource id of the layout to add
     */
    protected void addForegroundLayout(int layoutId) {
        mForegroundLayout = (RelativeLayout) mInflater.inflate(layoutId, null, false);
        mRootLayout.addView(mForegroundLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        // If the child class set mTitle in onCreateView,
        // remove the title for now so that we can animate it in.
        if (mTitle != null && !mTitle.equals("")) {
            getActivity().setTitle(" ");
        }
        if (!mHasAnimated) {
            startCircularReveal();
        }
    }

    /**
     * Play the circular reveal animation
     */
    protected void startCircularReveal() {
        // Get the touch points that were passed in from the previous fragment, if applicable
        int cx = getArguments().getInt(HomeActivity.TOUCH_X);
        int cy = getArguments().getInt(HomeActivity.TOUCH_Y);
        // Obtain the dimensions of the display
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // If the touch point was not passed in, set the center of the reveal to the middle of the screen.
        if (cx == 0) {
            cx = size.x/2;
        }
        if (cy == 0) {
            cy = size.y/2;
        }
        // Set the final radius for the circular reveal to the screen width
        int finalRadius = size.y;
        // Calculate the delay for the action bar reveal
        float msecPerPixel = (((float) DURATION_CIRCULAR_REVEAL) / (float) (finalRadius - getActionBarView().getHeight())) / 2.0f;
        float pixelsBeforeActionBar = (float)(cy - getActionBarView().getHeight());
        final float msecBeforeActionBar = Math.max(0.0f, msecPerPixel * pixelsBeforeActionBar);
        final TextView actionBarTextView = getActionBarTextView();
        // Create the circular reveal animator with the params obtained above.
        Animator anim = ViewAnimationUtils.createCircularReveal(mForegroundLayout, cx, cy, 0, finalRadius);
        anim.setDuration(DURATION_CIRCULAR_REVEAL);
        anim.setInterpolator(mCircularRevealInterpolator);
        mForegroundLayout.setVisibility(View.VISIBLE);
        anim.addListener(new CaltrainAnimationUtils.EmptyAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Fade in the new view while it's being revealed
                CaltrainAnimationUtils.startFadeIn(mForegroundLayout, DURATION_FADE, mFadeInterpolator);
                // Fade in the ActionBar once the circular reveal touches the view
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (mTitle != null) {
                            getActivity().setTitle(mTitle);
                        }
                        CaltrainAnimationUtils.startFadeIn(actionBarTextView, DURATION_ACTION_BAR_FADE, mActionBarFadeInterpolator);
                    }
                };
                new Handler().postDelayed(r, (long)msecBeforeActionBar);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHasAnimated = true;
            }
        });
        anim.start();
    }

    /**
     * Set the action bar title.
     * This MUST be called in onCreateView.
     * @param title
     */
    protected void setTitle(CharSequence title) {
        mTitle = title;
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

    /**
     * Exceptionally ugly hack to get the TextView from the ActionBar.
     * Unfortunately the TextView does not have an internal resource ID, so I have to do it this way.
     * @return The TextView that holds the ActionBar title.
     */
    private TextView getActionBarTextView() {
        ActionBarContainer actionBarContainer = (ActionBarContainer) getActionBarView();
        final TextView textView = (TextView)(((ViewGroup) actionBarContainer.getChildAt(0)).getChildAt(0));
        return textView;
    }

    /**
     * Initialize the destination TextView, if present.
     */
    protected void setupDestinationTextView() {
        // Only initialize this once
        if (mStationView == null) {
            // Check if the foreground layout contains the destination TextView
            mStationView = (TextView) mForegroundLayout.findViewById(R.id.text_destination);
            if (mStationView != null) {
                // Set the destination name
                mStationView.setText(getArguments().getString(HomeActivity.STATION_NAME));
                // Center-align the text
                mStationView.setGravity(Gravity.CENTER_HORIZONTAL);
                // Set a click listener to return to the station selector
                mStationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onStationChange(getArguments());
                    }
                });
            }
        }
    }

    /**
     * Initialize the direction TextView, if present.
     */
    protected void setupDirectionTextView() {
        // Only initialize this once
        if (mDirectionView == null) {
            // Check if the foreground layout contains the direction TextView
            mDirectionView = (TextView) mForegroundLayout.findViewById(R.id.text_direction);
            if (mDirectionView != null) {
                // Set the destination string
                String direction = getArguments().getString(HomeActivity.DIRECTION);
                if (direction.equals(HomeActivity.NORTH)) {
                    mDirectionView.setText(mContext.getResources().getString(R.string.northbound));
                } else {
                    mDirectionView.setText(mContext.getResources().getString(R.string.southbound));
                }
                mDirectionView.setGravity(Gravity.CENTER_HORIZONTAL);
                // Set a click listener to return to the direction selector
                mDirectionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onStationSelected(getArguments());
                    }
                });
            }
        }
    }

    /**
     * Initialize the number of stations TextView, if present.
     */
    protected void setupNumStationsTextView() {
        // Only initialize this once
        if (mNumStationsView == null) {
            // Check if the foreground layout contains the number of stations TextView
            mNumStationsView = (TextView) mForegroundLayout.findViewById(R.id.text_num_stations);
            if (mNumStationsView != null) {
                // Set the text
                mNumStationsView.setText(getArguments().getString(HomeActivity.NUM_STATIONS));
                mNumStationsView.setGravity(Gravity.CENTER_HORIZONTAL);
                // Set a click listener to return to the number of stations selector
                mNumStationsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onDirectionSelected(getArguments());
                    }
                });
            }
        }
    }

    protected class CaptureTouchPointListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mTouchX = (int) event.getX();
            mTouchY = (int) event.getY();
            return false;
        }
    }

}
