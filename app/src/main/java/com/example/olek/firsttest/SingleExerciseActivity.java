package com.example.olek.firsttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by olek on 06.08.14.
 */
public class SingleExerciseActivity extends FragmentActivity{
    ViewPager viewPager;
    SpiceManager spiceManager;
    int book_id,exercise_id;
    private boolean mActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleexerciselayout);


        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        viewPager=(ViewPager)findViewById(R.id.viewPagerSingleExercise);
        viewPager.setAdapter(new ExerciceFragmentsAdapter(getSupportFragmentManager()));

        Bundle b=getIntent().getExtras();
        if(b==null)
            finish();
        book_id=b.getInt("BOOK_ID");
        exercise_id=b.getInt("EXERCISE_ID");

    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }


    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public class ExerciceFragmentsAdapter extends FragmentStatePagerAdapter {
        public ExerciceFragmentsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            SingleExerciseFragment fragment = new SingleExerciseFragment();
            Bundle args = new Bundle();
            String s=String.format("ksiazki/%d/zadania/%d",book_id,exercise_id);
            Log.d("retro", s);
            args.putString("QUERY",s);

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    public static class SingleExerciseFragment extends Fragment {
        private SpiceManager spiceManager;
        private boolean isSolActive,isTrescActive,mActive=false;
        private Button buttonTresc,buttonSolution;
        private TextView textViewTresc,textViewSolution;
        private View seperatorAfterTresc,seperatorAfterSolution;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.singleexercisefragment, container, false);
            textViewTresc=(TextView)rootView.findViewById(R.id.textViewTresc);
            textViewSolution=(TextView)rootView.findViewById(R.id.textViewSolution);
            buttonTresc=(Button)rootView.findViewById(R.id.buttonTresc);
            buttonSolution=(Button)rootView.findViewById(R.id.buttonSolution);
            seperatorAfterTresc=rootView.findViewById(R.id.sepAfterTresc);
            seperatorAfterSolution=rootView.findViewById(R.id.sepAfteSolution);
            return rootView;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            this.spiceManager=((SingleExerciseActivity)getActivity()).spiceManager;
            ExerciseRequest request=new ExerciseRequest(getArguments().getString("QUERY"));
            String lastRequestCacheKey = request.createCacheKey();
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ExerciseRequestListener());
        }

        private class ExerciseRequestListener implements RequestListener<Exercise> {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("retro","FAILURE Ex list");

            }

            @Override
            public void onRequestSuccess(Exercise exercise) {
                Log.d("retro","Reqqqq");
                textViewTresc.setText(Html.fromHtml(exercise.getContent()));
                textViewSolution.setText(Html.fromHtml(exercise.getSolution()));

                buttonTresc.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ExpandCollapseAnimation.setHeightForWrapContent(getActivity(), textViewTresc);
                        ExpandCollapseAnimation animation = null;
                        if(isTrescActive) {
                            animation = new ExpandCollapseAnimation(textViewTresc, 1000, 1);
                            animation.setAnimationListener(new ViewVisibilityListener(seperatorAfterTresc));
                            isTrescActive = false;
                        } else {
                            animation = new ExpandCollapseAnimation(textViewTresc, 1000, 0);
                            isTrescActive = true;
                            seperatorAfterTresc.setVisibility(View.VISIBLE);
                        }
                        textViewTresc.startAnimation(animation);
                    }
                });

                buttonSolution.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ExpandCollapseAnimation.setHeightForWrapContent(getActivity(), textViewSolution);
                        ExpandCollapseAnimation animation = null;
                        if(isSolActive) {
                            animation = new ExpandCollapseAnimation(textViewSolution, 1000, 1);
                            isSolActive = false;
                            animation.setAnimationListener(new ViewVisibilityListener(seperatorAfterSolution));
                        } else {
                            animation = new ExpandCollapseAnimation(textViewSolution, 1000, 0);
                            isSolActive = true;
                            seperatorAfterSolution.setVisibility(android.view.View.VISIBLE);
                        }
                        textViewSolution.startAnimation(animation);
                    }
                });

            }


        }
        private class ViewVisibilityListener implements Animation.AnimationListener{
            View view;
            public ViewVisibilityListener(View view)
            {
                this.view=view;
            }
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }

    }




}
