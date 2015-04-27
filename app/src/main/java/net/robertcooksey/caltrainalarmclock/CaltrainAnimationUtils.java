package net.robertcooksey.caltrainalarmclock;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;

/**
 * Created by ecookse on 4/26/15.
 */
public class CaltrainAnimationUtils {

    /**
     * Animate a view to fade in.
     *
     * @param view         The view to be animated.
     * @param duration     Duration that the animation should run, in milliseconds; specify -1 to use the default.
     * @param interpolator The interpolator which defines the acceleration curve; can be null to use default.
     */
    public static void startFadeIn(View view, long duration, Interpolator interpolator) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        if (duration > 0) {
            alphaAnimation.setDuration(duration);
        }
        if (interpolator != null) {
            alphaAnimation.setInterpolator(interpolator);
        }
        view.startAnimation(alphaAnimation);
    }

    /**
     * Animate a view to fade out.
     *
     * @param view         The view to be animated.
     * @param duration     Duration that the animation should run, in milliseconds; specify -1 to use the default.
     * @param interpolator The interpolator which defines the acceleration curve; can be null to use default.
     */
    public static void startFadeOut(View view, long duration, Interpolator interpolator) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        if (duration > 0) {
            alphaAnimation.setDuration(duration);
        }
        if (interpolator != null) {
            alphaAnimation.setInterpolator(interpolator);
        }
        view.startAnimation(alphaAnimation);
    }

    /**
     * Convenience class to set onAnimationStart on an animator.
     */
    public static abstract class EmptyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            // Empty implementation
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // Empty implementation
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Empty implementation
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // Empty implementation
        }
    }

}
