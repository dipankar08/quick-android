package in.co.dipankar.quickandorid.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.opengl.Visibility;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import in.co.dipankar.quickandorid.R;

public class AnimationUtils {

    public enum Type{
        MOVE_UP_DOWN,
        FADE_IN_OUT
    }
    public static void doPulseAnimation(View view) {
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofPropertyValuesHolder(
                        view,
                        PropertyValuesHolder.ofFloat("scaleX", 1.5f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.5f));
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.start();
    }

    public static void doShackAnimation(Context context, View view) {
        Animation shake = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.pin_shake);
        view.startAnimation(shake);
    }

    public static void setVisibilityWithAnimation(final View view, int visibility, Type type) {
        switch (type) {
            case MOVE_UP_DOWN:
                if (visibility == View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            view.getHeight(),  // fromYDelta
                            0);                // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    view.startAnimation(animate);
                } else {
                    // Make SURE there is NO MARGIN..
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            0,                 // fromYDelta
                            view.getHeight()); // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    view.startAnimation(animate);
                }
            case FADE_IN_OUT:
                if(visibility == View.VISIBLE){
                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
                    animation.addAnimation(new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));

                    animation.setDuration(500);
                    animation.setInterpolator(new AnticipateInterpolator());
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }

                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    view.startAnimation(animation);
                } else{

                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(new AlphaAnimation(1F, 0F));
                    animation.addAnimation(new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                    animation.setDuration(500);
                    animation.setInterpolator(new AnticipateInterpolator());
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }
                    });
                    view.startAnimation(animation);
                }
        }
    }
}
