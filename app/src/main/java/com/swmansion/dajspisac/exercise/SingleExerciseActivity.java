package com.swmansion.dajspisac.exercise;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.olek.firsttest.R;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.ExpandCollapseAnimation;

/**
 * Created by olek on 06.08.14.
 */
public class SingleExerciseActivity extends FragmentActivity implements TabHost.OnTabChangeListener{
    private ViewPager viewPager;
    private TabHost mTabHost;
    private int book_id,exercise_id,previousTabIndex;
    private TabHost.TabContentFactory defaultTabCont;
    private boolean mActive = false;
    private int[] exerciseIds;
    private String[]exerciseNumbers;
    private HorizontalScrollView horScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_activity_layout);

        Bundle b=getIntent().getExtras();
        if(b==null)
            finish();
        book_id=b.getInt("BOOK_ID");
        exercise_id=b.getInt("EXERCISE_ID");
        exerciseIds=b.getIntArray("IDS");
        exerciseNumbers=b.getStringArray("NUMS");

        setActionBar(b.getString("SUBJECT"),b.getInt("PAGENUM"));

        for(int i=0;i<exerciseIds.length;i++) {
            Log.d("retro", Integer.toString(exerciseIds[i]));
        }

        viewPager=(ViewPager)findViewById(R.id.viewPagerSingleExercise);
        viewPager.setAdapter(new ExerciseFragmentsAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SingleExerciseActivity.this.mTabHost.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        horScrollView=(HorizontalScrollView)findViewById(R.id.horizontalscrollview);

        initializeTabHost();

    }

    private void initializeTabHost(){
        mTabHost=(TabHost)findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        defaultTabCont = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View v = new View(SingleExerciseActivity.this);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        };
        for(int i=0;i<exerciseIds.length;i++){
            View tabIndicatorView=getLayoutInflater().inflate(R.layout.tab_exercise_number_layout,null);
            TextView textViewTab=(TextView)tabIndicatorView.findViewById(R.id.textViewExerciseNumber);
            textViewTab.setText(exerciseNumbers[i]);
            mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(i)).setIndicator(tabIndicatorView).setContent(defaultTabCont));
            if(exerciseIds[i]==exercise_id){
                mTabHost.setCurrentTab(i);
            }
        }
    }

    private void setActionBar(String subjectName, int pageNum){
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View rootView=getLayoutInflater().inflate(R.layout.actionbar_layout_singlebook,null);
        TextView textViewSubject=(TextView)rootView.findViewById(R.id.textViewSubjectName);
        textViewSubject.setText(subjectName+", str. "+Integer.toString(pageNum));
        ImageView bookMiniature=(ImageView)rootView.findViewById(R.id.imageViewCoverMin);
        Bitmap miniature= BitmapLoadSave.getThumbnail(SingleExerciseActivity.this, "lastmin.png");
        bookMiniature.setImageBitmap(miniature);
        ImageView imageBack=(ImageView)rootView.findViewById(R.id.imageViewBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBar.setCustomView(rootView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTabChanged(String s) {
        TabWidget mTabWidget=mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);

        TextView previousTextView=(TextView)previousView.findViewById(R.id.textViewExerciseNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
        int pos = mTabHost.getCurrentTab();

        previousTabIndex=pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView=(TextView)previousView.findViewById(R.id.textViewExerciseNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        horScrollView.smoothScrollTo(previousView.getLeft()-(width/2)+(previousView.getWidth()/2),previousView.getTop());

        viewPager.setCurrentItem(pos);
    }

    public class ExerciseFragmentsAdapter extends FragmentStatePagerAdapter {
        public ExerciseFragmentsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            SingleExerciseFragment fragment = new SingleExerciseFragment();
            Bundle args = new Bundle();
            String s=String.format("ksiazki/%d/zadania/%d",book_id,exerciseIds[i]);
            args.putString("QUERY",s);

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return exerciseIds.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    public static class SingleExerciseFragment extends Fragment {
        private SpiceManager spiceManager;
        private boolean isSolActive,isTrescActive;
        private Button buttonTresc,buttonSolution;
        private TextView textViewTresc,textViewSolution;
        private View seperatorAfterTresc,seperatorAfterSolution;
        private WebView mWebWievSolution;
        Exercise mExercise;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        }

        @Override
        public void onStart() {
            super.onStart();
            spiceManager.start(getActivity());
        }

        @Override
        public void onStop() {
            super.onStop();
            if(spiceManager.isStarted()){
                spiceManager.shouldStop();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.exercise_activity_fragment_layout, container, false);
            textViewTresc=(TextView)rootView.findViewById(R.id.textViewTresc);
            //textViewSolution=(TextView)rootView.findViewById(R.id.textViewSolution);
            buttonTresc=(Button)rootView.findViewById(R.id.buttonTresc);
            buttonSolution=(Button)rootView.findViewById(R.id.buttonSolution);
            seperatorAfterTresc=rootView.findViewById(R.id.sepAfterTresc);
            seperatorAfterSolution=rootView.findViewById(R.id.sepAfteSolution);
            mWebWievSolution=(WebView)rootView.findViewById(R.id.webViewSolution);

            return rootView;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if(mExercise==null){
                ExerciseRequest request=new ExerciseRequest(getArguments().getString("QUERY"));
                String lastRequestCacheKey = request.createCacheKey();
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ExerciseRequestListener());
            }
            else{
                updateViews();
            }
        }

        public void showSolution(){
            if(!isSolActive){
                buttonSolution.callOnClick();
            }
        }

        public void hideSolution() {
            if (isSolActive) {
                buttonSolution.callOnClick();
            }
        }
        private void updateViews(){
            textViewTresc.setText(Html.fromHtml(mExercise.getContent()));
            WebSettings settings = mWebWievSolution.getSettings();
            settings.setJavaScriptEnabled(true);

            mWebWievSolution.loadData(mExercise.getSolution(), "text/html; charset=UTF-8", "UTF-8");
            mWebWievSolution.setVisibility(View.VISIBLE);

            buttonTresc.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(isTrescActive){
                        buttonTresc.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.wybrana_ksiazka_dol),null);
                    }
                    else{
                        buttonTresc.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.wybrana_ksiazka_gora),null);

                    }
                    ExpandCollapseAnimation.setHeightForWrapContent(getActivity(), textViewTresc);
                    ExpandCollapseAnimation animation = null;
                    if(isTrescActive) {
                        animation = new ExpandCollapseAnimation(textViewTresc, 500, 1);
                        animation.setAnimationListener(new ViewVisibilityListener(seperatorAfterTresc));
                        isTrescActive = false;
                    } else {
                        animation = new ExpandCollapseAnimation(textViewTresc, 500, 0);
                        isTrescActive = true;
                        seperatorAfterTresc.setVisibility(View.VISIBLE);
                    }
                    textViewTresc.startAnimation(animation);
                }
            });

            buttonSolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSolActive) {
                        buttonSolution.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wybrana_ksiazka_dol), null);
                    } else {
                        buttonSolution.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wybrana_ksiazka_gora), null);

                    }

                    ExpandCollapseAnimation.setHeightForWrapContent(getActivity(), mWebWievSolution);
                    ExpandCollapseAnimation animation = null;
                    if (isSolActive) {
                        animation = new ExpandCollapseAnimation(mWebWievSolution, 500, 1);
                        isSolActive = false;
                        animation.setAnimationListener(new ViewVisibilityListener(mWebWievSolution));
                    } else {
                        animation = new ExpandCollapseAnimation(mWebWievSolution, 500, 0);
                        isSolActive = true;
                        seperatorAfterSolution.setVisibility(android.view.View.VISIBLE);
                    }
                    mWebWievSolution.startAnimation(animation);
                }
            });


        }

        private class ExerciseRequestListener implements RequestListener<Exercise> {
            @Override
            public void onRequestFailure(SpiceException e) {

            }

            @Override
            public void onRequestSuccess(Exercise exercise) {
                mExercise=exercise;
                if(isAdded()){
                    updateViews();
                }
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
