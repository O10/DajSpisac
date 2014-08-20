package com.swmansion.dajspisac.tools;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by olek on 18.08.14.
 */
public class TabAnimationListener implements Animation.AnimationListener {
    View view;
    int initialHeight;
    int initialwWidth;

    public TabAnimationListener(View view){
        this.view=view;
        initialwWidth=view.getWidth();
        initialHeight=view.getHeight();
    }
    public void onAnimationEnd(Animation animation) {
        view.setMinimumHeight((int)(initialHeight*1.2));
        view.setMinimumWidth((int)(initialwWidth*1.2));
    }
    public void onAnimationRepeat(Animation animation) {
    }
    public void onAnimationStart(Animation animation) {
    }
}