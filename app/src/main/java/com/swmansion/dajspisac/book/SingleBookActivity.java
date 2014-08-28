package com.swmansion.dajspisac.book;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

import java.util.ArrayList;


/**
 * Created by olek on 05.08.14.
 */
public class SingleBookActivity extends FragmentActivity implements TabHost.OnTabChangeListener{
    Book book;
    ViewPager viewPager;
    SpiceManager spiceManager;
    ArrayList<Integer> pagesNumbers;
    private TabHost mTabHost;
    TabHost.TabContentFactory defaultTabCont;
    private int previousTabIndex;
    private TextView textViewNrZadan,textViewNrStrony;
    private HorizontalScrollView horScrollView;
    private LinearLayout actionBar;
    int lastTabNum=-1;

    void setAdapter() {
        viewPager.setAdapter(new PagesForBookAdapter(getSupportFragmentManager()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT<12){
            setContentView(R.layout.single_book_activity_layout_noclip);
        }
        else{
            setContentView(R.layout.single_book_activity_layout);
        }
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);

        textViewNrZadan=(TextView)findViewById(R.id.textView);
        textViewNrStrony=(TextView)findViewById(R.id.pageNumTv);
        horScrollView = (HorizontalScrollView) findViewById(R.id.horizontalscrollview);
        horScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                textViewNrZadan.invalidate();
                horScrollView.invalidate();
            }
        });

        actionBar=(LinearLayout)findViewById(R.id.actionBar);
        actionBar.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.viewPagerSingleBook);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SingleBookActivity.this.mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        defaultTabCont = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View v = new View(SingleBookActivity.this);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);

        if(lastTabNum!=-1){
            Log.d("retro","Setting last tab "+Integer.toString(lastTabNum));
            viewPager.setCurrentItem(lastTabNum);
        }


        BookRequest request = new BookRequest(getIntent().getStringExtra("QUERY"));
        String lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_WEEK, new BookRequestListener());
    }


    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        lastTabNum=mTabHost.getCurrentTab();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onTabChanged(String s) {
        TabWidget mTabWidget = mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);

        previousView.setBackgroundResource(R.drawable.tab_background_default);
        TextView previousTextView = (TextView) previousView.findViewById(R.id.textViewPageNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
        DajSpisacUtilities.startTabUnChoosedAnimation(previousView,viewPager);
        int pos = mTabHost.getCurrentTab();
        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView = (TextView) previousView.findViewById(R.id.textViewPageNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));

        int width = DajSpisacUtilities.getScreenWidth(this);
        horScrollView.smoothScrollTo(previousView.getLeft() - (width / 2) + (previousView.getWidth() / 2), previousView.getTop());

        viewPager.setCurrentItem(pos);
        previousView.setBackgroundResource(R.drawable.tab_background_default_chosen);
        DajSpisacUtilities.startTabChoosedAnimation(previousView,viewPager);


    }

    public class PagesForBookAdapter extends FragmentStatePagerAdapter {
        public PagesForBookAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            SinglePageFragment fragment = new SinglePageFragment();
            Bundle args = new Bundle();
            String s = String.format("ksiazki/%d/zadania?page=%s", book.getId(), pagesNumbers.get(i));
            args.putInt("PAGENUM", pagesNumbers.get(i));
            args.putString("SUBJECT", book.getSubject());
            args.putInt("BOOKID", book.getId());

            args.putString("QUERY", s);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return pagesNumbers.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }



    private class BookRequestListener implements RequestListener<Book> {
        @Override
        public void onRequestFailure(SpiceException e) {
            DajSpisacUtilities.showInternetErrorToast(SingleBookActivity.this);
        }

        @Override
        public void onRequestSuccess(Book dBook) {
            if(book!=null){
                return;
            }
            book = dBook;

            TextView textViewSubject = (TextView) findViewById(R.id.textViewTitle);
            textViewSubject.setText(book.getSubject());
            ImageView bookMiniature = (ImageView) findViewById(R.id.imageViewRight);
            bookMiniature.setAdjustViewBounds(true);
            ImageView imageBack = (ImageView) findViewById(R.id.imageViewLeft);
            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            Bitmap miniature = BitmapLoadSave.loadBitmapFromInternal(SingleBookActivity.this, "lastminiature.png");
            bookMiniature.setImageBitmap(miniature);

            actionBar.setVisibility(View.VISIBLE);


            if (dBook != null) {
                pagesNumbers = new ArrayList<Integer>();
                LinearLayout ll = (LinearLayout) findViewById(R.id.top_buttons);
                for (int i = 0; i < dBook.getPages().size(); i++) {
                    View tabIndicatorView = getLayoutInflater().inflate(R.layout.tab_page_number_layout, null);
                    TextView textViewTab = (TextView) tabIndicatorView.findViewById(R.id.textViewPageNumber);
                    textViewTab.setText(Integer.toString(dBook.getPages().get(i)));
                    mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(i)).setIndicator(tabIndicatorView).setContent(defaultTabCont));
                    pagesNumbers.add(dBook.getPages().get(i));
                }
                setAdapter();
            }
        }
    }
}
