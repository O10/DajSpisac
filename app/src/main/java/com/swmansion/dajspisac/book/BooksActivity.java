package com.swmansion.dajspisac.book;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

public class BooksActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
    private int previousTabIndex = -1;
    protected TabHost mTabHost;
    protected ViewPager mViewPager;

    protected void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        TabHost.TabContentFactory defaultTabCont = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View v = new View(BooksActivity.this);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        };
        mTabHost.addTab(mTabHost.newTabSpec("matematyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_mathematics_layout, null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("chemia").setIndicator(getLayoutInflater().inflate(R.layout.tab_chemistry_layout, null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("fizyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_physics_layout, null)).setContent(defaultTabCont));

    }

    @Override
    public void onTabChanged(String s) {
        int[] RDrawableCoverBlada = {R.drawable.twojeksiazki_matematyka_blada,R.drawable.twojeksiazki_chemia_blady, R.drawable.twojeksiazki_fizyka_blada};
        int[] RDrawableCoverLosos = { R.drawable.twojeksiazki_matematyka_lososiowa,R.drawable.twojeksiazki_chemia_losos, R.drawable.twojeksiazki_fizyka_lososiowa};

        TabWidget mTabWidget = mTabHost.getTabWidget();
        View previousView;
        TextView previousTextView;
        ImageView previousImageView;
        if (previousTabIndex != -1) {
            previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
            previousTextView = (TextView) previousView.findViewById(R.id.textViewTab);
            previousImageView = (ImageView) previousView.findViewById(R.id.imageViewSubjectIcon);


            previousImageView.setImageResource(RDrawableCoverBlada[previousTabIndex]);
            previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
            DajSpisacUtilities.startTabUnChoosedAnimation(previousView);
        }
        int pos = mTabHost.getCurrentTab();
        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);

        previousTextView = (TextView) previousView.findViewById(R.id.textViewTab);
        previousImageView = (ImageView) previousView.findViewById(R.id.imageViewSubjectIcon);
        previousImageView.setImageResource(RDrawableCoverLosos[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));

        mViewPager.setCurrentItem(pos);
        DajSpisacUtilities.startTabChoosedAnimation(previousView);
    }

}