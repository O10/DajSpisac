package com.swmansion.dajspisac.exercise;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

/**
 * Created by olek on 06.08.14.
 */
public class SingleExerciseActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
    private ViewPager viewPager;
    private TabHost mTabHost;
    private int book_id, exercise_id, previousTabIndex;
    private int[] exerciseIds;
    private String[] exerciseNumbers;
    private HorizontalScrollView horScrollView;
    private RelativeLayout relativeContainer;
    private int SCREENWIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 12) {
            setContentView(R.layout.exercise_activity_layout_noclip);
        } else {
            setContentView(R.layout.exercise_activity_layout);
        }
        SCREENWIDTH = DajSpisacUtilities.getScreenWidth(this);
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Bundle b = getIntent().getExtras();
        relativeContainer = (RelativeLayout) findViewById(R.id.relativeContainer);
        if (b == null)
            finish();
        book_id = b.getInt("BOOK_ID");
        exercise_id = b.getInt("EXERCISE_ID");
        exerciseIds = b.getIntArray("IDS");
        exerciseNumbers = b.getStringArray("NUMS");

        setActionBar(b.getString("SUBJECT"), b.getInt("PAGENUM"));

        viewPager = (ViewPager) findViewById(R.id.viewPagerSingleExercise);
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

        horScrollView = (HorizontalScrollView) findViewById(R.id.horizontalscrollview);
        horScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                relativeContainer.invalidate();
            }
        });
        initializeTabHost();
    }

    private void initializeTabHost() {
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        TabHost.TabContentFactory defaultTabCont;

        defaultTabCont = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View v = new View(SingleExerciseActivity.this);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        };
        for (int i = 0; i < exerciseIds.length; i++) {
            View tabIndicatorView = getLayoutInflater().inflate(R.layout.tab_exercise_number_layout, null);
            TextView textViewTab = (TextView) tabIndicatorView.findViewById(R.id.textViewExerciseNumber);
            textViewTab.setText(exerciseNumbers[i]);
            mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(i)).setIndicator(tabIndicatorView).setContent(defaultTabCont));
            if (exerciseIds[i] == exercise_id) {
                mTabHost.setCurrentTab(i);
                horScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        View currentView = mTabHost.getCurrentTabView();
                        horScrollView.scrollTo(currentView.getLeft() - (SCREENWIDTH / 2) + (currentView.getWidth() / 2), 0);
                    }
                });
            }
        }
    }

    private void setActionBar(String subjectName, int pageNum) {

        TextView textViewSubject = (TextView) findViewById(R.id.textViewTitle);
        textViewSubject.setText(subjectName + ", str. " + Integer.toString(pageNum));
        ImageView bookMiniature = (ImageView) findViewById(R.id.imageViewRight);
        bookMiniature.setAdjustViewBounds(true);
        Bitmap miniature = BitmapLoadSave.loadBitmapFromInternal(SingleExerciseActivity.this, "lastminiature.png");
        bookMiniature.setImageBitmap(miniature);
        ImageView imageBack = (ImageView) findViewById(R.id.imageViewLeft);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTabChanged(String s) {
        Log.d("retro", "On tab changedinvoked");
        TabWidget mTabWidget = mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousView.setBackgroundResource(R.drawable.tab_background_default);

        TextView previousTextView = (TextView) previousView.findViewById(R.id.textViewExerciseNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));

        DajSpisacUtilities.startTabUnChoosedAnimation(previousView);
        int pos = mTabHost.getCurrentTab();

        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView = (TextView) previousView.findViewById(R.id.textViewExerciseNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));
        horScrollView.smoothScrollTo(previousView.getLeft() - (SCREENWIDTH / 2) + (previousView.getWidth() / 2), previousView.getTop());

        viewPager.setCurrentItem(pos);
        previousView.setBackgroundResource(R.drawable.tab_background_default_chosen);
        DajSpisacUtilities.startTabChoosedAnimation(previousView);
    }

    public class ExerciseFragmentsAdapter extends FragmentStatePagerAdapter {
        public ExerciseFragmentsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            SingleExerciseFragment fragment = new SingleExerciseFragment();
            Bundle args = new Bundle();
            String s = String.format("ksiazki/%d/zadania/%d", book_id, exerciseIds[i]);
            args.putString("QUERY", s);

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
        private WebView mWebView;
        private ProgressBar mProgressBar;
        Exercise mExercise;
        private static boolean isToastShown = false;


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
            if (mExercise == null) {
                ExerciseRequest request = new ExerciseRequest(getArguments().getString("QUERY"));
                String lastRequestCacheKey = request.createCacheKey();
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new ExerciseRequestListener());
            } else {
                updateViews();
            }

        }

        @Override
        public void onStop() {
            super.onStop();
            if (spiceManager.isStarted()) {
                spiceManager.shouldStop();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.exercise_activity_fragment_layout, container, false);
            mWebView = (WebView) rootView.findViewById(R.id.webView);
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            return rootView;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        private void prepareWebView(WebView webView) {
            WebSettings settings = webView.getSettings();
            settings.setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= 11) {
                settings.setDisplayZoomControls(false);
            }
            settings.setSupportZoom(true);
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(false);

        }

        private void updateViews() {
            mProgressBar.setMax(100);
            prepareWebView(mWebView);


            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(0);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(100);
                    super.onPageFinished(view, url);
                }
            });


            String htmlString;
            if (mExercise.getAttachments() != null && mExercise.getAttachments().size() != 0) {
                String imgSrcString = String.format(getResources().getString(R.string.imgsrc), mExercise.getAttachments().get(0).getImage().getImage().getUrl());
                htmlString = getResources().getString(R.string.htmlstart) + "\n" + mExercise.getContent() + "<hr>" + mExercise.getSolution() + "\n" + imgSrcString + getResources().getString(R.string.htmlend);
            } else {
                htmlString = getResources().getString(R.string.htmlstart) + "\n" + mExercise.getContent() + "<hr>" + mExercise.getSolution() + "\n" + getResources().getString(R.string.htmlend);
            }

            mWebView.loadDataWithBaseURL("http://dajspisac.pl/", htmlString, "text/html", "UTF-8", "");
        }

        private class ExerciseRequestListener implements RequestListener<Exercise> {
            @Override
            public void onRequestFailure(SpiceException e) {
                if (!isToastShown) {
                    DajSpisacUtilities.showInternetErrorToast(getActivity());
                    isToastShown = true;
                }
            }

            @Override
            public void onRequestSuccess(Exercise exercise) {
                mExercise = exercise;
                if (isAdded()) {
                    updateViews();
                }
                isToastShown = false;
            }


        }
    }
}
