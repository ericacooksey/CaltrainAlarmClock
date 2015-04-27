package net.robertcooksey.caltrainalarmclock;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Base class with built-in transition animations.
 */
public class CaltrainAnimatedFragment extends Fragment {

    /* Values which affect the animation */
    public static final int DURATION_CIRCULAR_REVEAL = 1000;
    public static final int DURATION_FADE = 1000;
    public static final int DURATION_ACTION_BAR_FADE = 1200;
    /* For a full list of interpolators, please see
    http://developer.android.com/reference/android/view/animation/Interpolator.html */
    protected Interpolator mCircularRevealInterpolator = new AccelerateDecelerateInterpolator();
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
        int finalRadius = size.x;
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
                // Fade out the old view by fading in a solid black view
                CaltrainAnimationUtils.startFadeIn(mForegroundLayout, DURATION_FADE, mFadeInterpolator);
                // Fade in the ActionBar
                CaltrainAnimationUtils.startFadeIn(getActionBarView(), DURATION_ACTION_BAR_FADE, mActionBarFadeInterpolator);
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

    protected class CaptureTouchPointListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mTouchX = (int) event.getX();
            mTouchY = (int) event.getY();
            return false;
        }
    }

}
