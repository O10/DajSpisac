package com.swmansion.dajspisac.tools;

/**
 * Created by olek on 07.08.14.
 */

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandCollapseAnimation extends Animation {
    private View mAnimatedView;
    private int mEndHeight;
    private int mType;

    public ExpandCollapseAnimation() {

    }

    public ExpandCollapseAnimation(View view, int duration, int type) {
        setDuration(duration);
        mAnimatedView = view;
        mEndHeight = mAnimatedView.getLayoutParams().height;
        mType = type;
        if (mType == 0) {
            mAnimatedView.getLayoutParams().height = 0;
            mAnimatedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (mType == 0) {
                mAnimatedView.getLayoutParams().height = (int) (mEndHeight * interpolatedTime);
            } else {
                mAnimatedView.getLayoutParams().height = mEndHeight - (int) (mEndHeight * interpolatedTime);
            }
            mAnimatedView.requestLayout();
        } else {
            if (mType == 0) {
                mAnimatedView.getLayoutParams().height = mEndHeight;
                mAnimatedView.requestLayout();
            } else {
                mAnimatedView.getLayoutParams().height = 0;
                mAnimatedView.setVisibility(View.GONE);
                mAnimatedView.requestLayout();
                mAnimatedView.getLayoutParams().height = mEndHeight;
            }
        }
    }

    public static void setHeightForWrapContent(Activity activity, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (activity == null) {
            return;
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY);

        view.measure(widthMeasureSpec, heightMeasureSpec);
        int height = view.getMeasuredHeight();
        view.getLayoutParams().height = height;
    }
}