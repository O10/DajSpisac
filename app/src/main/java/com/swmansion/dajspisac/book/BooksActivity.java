package com.swmansion.dajspisac.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.olek.firsttest.R;
import com.octo.android.robospice.SpiceManager;
import com.swmansion.dajspisac.settings.ChooseClassActivity;

/**
 * Created by olek on 04.08.14.
 */
public class BooksActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
    protected SpiceManager spiceManager;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    private TabHost mTabHost;
    private int previousTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity_layout);

        setTitle("Wybierz książkę");
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        initialiseTabHost();


        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                BooksActivity.this.mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initialiseTabHost() {
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

        mTabHost.addTab(mTabHost.newTabSpec("chemia").setIndicator(getLayoutInflater().inflate(R.layout.tab_chemistry_layout, null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("matematyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_mathematics_layout, null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("fizyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_physics_layout, null)).setContent(defaultTabCont));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.chooseclass) {
            startActivity(new Intent(this, ChooseClassActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String s) {
        int[] RDrawableCoverBlada = {R.drawable.twojeksiazki_chemia_blady, R.drawable.twojeksiazki_matematyka_blada, R.drawable.twojeksiazki_fizyka_blada};
        int[] RDrawableCoverLosos = {R.drawable.twojeksiazki_chemia_losos, R.drawable.twojeksiazki_matematyka_lososiowa, R.drawable.twojeksiazki_fizyka_lososiowa};

        TabWidget mTabWidget = mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        TextView previousTextView = (TextView) previousView.findViewById(R.id.textViewTab);
        ImageView previousImageView = (ImageView) previousView.findViewById(R.id.imageViewSubjectIcon);


        previousImageView.setImageResource(RDrawableCoverBlada[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
        int pos = mTabHost.getCurrentTab();
        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView = (TextView) previousView.findViewById(R.id.textViewTab);
        previousImageView = (ImageView) previousView.findViewById(R.id.imageViewSubjectIcon);
        previousImageView.setImageResource(RDrawableCoverLosos[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));

        mViewPager.setCurrentItem(pos);


    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        String qFinish[] = {"&subject=Chemia", "&subject=Matematyka", "&subject=Fizyka"};

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            BooksFragment fragment = new BooksFragment();
            //fragment.setSpiceManager(spiceManager);
            Bundle args = new Bundle();
            String queryFinish = qFinish[i];

            args.putString("QUERY", "ksiazki?class_nr=I+gimnazjum" + queryFinish);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class BooksFragment extends Fragment {
        private ListView mListView;
        private SpiceManager spiceManager;
        Intent intent;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        void setSpiceManager(SpiceManager spiceManager) {
            this.spiceManager = spiceManager;
        }


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.books_activity_fragment_layout, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listViewBooks);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            this.spiceManager = ((BooksActivity) getActivity()).spiceManager;
            mListView.setAdapter(new BooksAdapter(getActivity(), spiceManager));
            query(getArguments().getString("QUERY"));
        }


        public void query(String query) {
            ((BooksAdapter) mListView.getAdapter()).query(query);
        }

    }

}
