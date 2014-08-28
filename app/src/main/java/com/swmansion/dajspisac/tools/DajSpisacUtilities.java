package com.swmansion.dajspisac.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.swmansion.dajspisac.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olek on 2014-08-16.
 */
public class DajSpisacUtilities {
    public static ArrayList<String> getMyBookIds(Context context) {
        ArrayList<String> bookIds;
        SharedPreferences preferences = context.getSharedPreferences("BOOKIDS", 0);
        String initialString = preferences.getString("BOOKIDS", "");
        bookIds = new ArrayList<String>(Arrays.asList(initialString.split(",")));
        if (bookIds.size() > 0 && bookIds.get(0).equals("")) {
            bookIds.remove(0);
        }
        return bookIds;
    }

    public static void removeBookById(Context context, int id) {
        ArrayList<String> myBooksIds = getMyBookIds(context);
        myBooksIds.remove(Integer.toString(id));
        addBookList(context, myBooksIds);
    }

    public static void addBookById(Context context, int id) {
        ArrayList<String> currentIds = getMyBookIds(context);
        currentIds.add(Integer.toString(id));
        addBookList(context, currentIds);
    }

    public static void addBookList(Context context, ArrayList<String> idList) {
        SharedPreferences preferences = context.getSharedPreferences("BOOKIDS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder result = new StringBuilder();
        for (String string : idList) {
            result.append(string);
            result.append(",");
        }
        String res = result.length() > 0 ? result.substring(0, result.length() - 1) : "";
        editor.putString("BOOKIDS", res);
        editor.commit();
    }

    public static int getScreenWidth(Activity activity) {
        int screenWidth = 0;
        if (android.os.Build.VERSION.SDK_INT < 13) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            screenWidth = display.getWidth();
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    public static void startTabChoosedAnimation(final View view,final View viewInvalidate){
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return;
        }
        float scalingFactor = 1.2f; // scale down to half the size
        AnimatorSet aSet = new AnimatorSet();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "ScaleX", 1f, scalingFactor);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "ScaleY", 1f, scalingFactor);
        anim.setDuration(250);
        /*anim2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                //viewPage.invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //viewPage.invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //viewPage.invalidate();
            }
        });*/
        anim2.setDuration(250);
        aSet.play(anim).with(anim2);
        aSet.start();
    }

    public static void startTabUnChoosedAnimation(final View view,final View viewInvalidate){
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return;
        }
        float scalingFactor = 1f; // scale down to half the size
        AnimatorSet aSet = new AnimatorSet();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "ScaleX", 1.2f, scalingFactor);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "ScaleY", 1.2f, scalingFactor);
        anim.setDuration(250);
        anim2.setDuration(250);
        /*anim2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                viewPage.invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //viewPage.invalidate();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //viewPage.invalidate();
            }
        });*/
        aSet.play(anim).with(anim2);
        aSet.start();
    }

    public static void showInternetErrorToast(Activity activity){
        Toast toast = new Toast(activity.getApplicationContext());
        View errorToastView=activity.getLayoutInflater().inflate(R.layout.internet_error_toast_layout,(ViewGroup)activity.findViewById(R.id.toastlayout));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(errorToastView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,200);
        toast.show();
        Log.d("retro","Showing Toast");
    }
}
