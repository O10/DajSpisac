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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.olek.firsttest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.SpiceManager;
import com.swmansion.dajspisac.settings.ChooseClassActivity;

/**
 * Created by olek on 04.08.14.
 */
public class BooksActivity extends FragmentActivity implements TabHost.OnTabChangeListener{
    protected SpiceManager spiceManager;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    private TabHost mTabHost;

    //String baseQUERY=new String("ksiazki?class_nr=I+gimnazjum");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity_layout);
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
                if(positionOffset>0){
                }

            }

            @Override
            public void onPageSelected(int position) {
                //int pos = BooksActivity.this.mViewPager.getCurrentItem();
                BooksActivity.this.mTabHost.setCurrentTab(position);
                Toast.makeText(BooksActivity.this,Integer.toString(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        DisplayImageOptions displayimageOptions = new DisplayImageOptions.Builder().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);



    }

    private void initialiseTabHost(){
        mTabHost = (TabHost)findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        TabHost.TabContentFactory defaultTabCont=new TabHost.TabContentFactory(){
            @Override
            public View createTabContent(String s) {
                View v = new View(BooksActivity.this);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        };

        mTabHost.addTab(mTabHost.newTabSpec("chemia").setIndicator(getLayoutInflater().inflate(R.layout.tab_chemistry_layout,null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("matematyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_mathematics_layout,null)).setContent(defaultTabCont));
        mTabHost.addTab(mTabHost.newTabSpec("fizyka").setIndicator(getLayoutInflater().inflate(R.layout.tab_physics_layout,null)).setContent(defaultTabCont));

        mTabHost.setOnTabChangedListener(this);


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
        int pos = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(pos);


    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            BooksFragment fragment = new BooksFragment();
            //fragment.setSpiceManager(spiceManager);
            Bundle args = new Bundle();
            String queryFinish = new String();
            switch (i) {
                case 0:
                    queryFinish = new String("&subject=Chemia");
                    break;
                case 1:
                    queryFinish = new String("&subject=Matematyka");
                    break;
                case 2:
                    queryFinish = new String("&subject=Fizyka");
                    break;

            }
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

    // Instances of this class are fragments representing a single
// object in our collection.
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
