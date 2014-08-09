package com.example.olek.firsttest;

import android.app.ActionBar;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by olek on 04.08.14.
 */
public class BooksActivity  extends FragmentActivity {
    protected SpiceManager spiceManager;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    //String baseQUERY=new String("ksiazki?class_nr=I+gimnazjum");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booksactivitylayout);
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

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
                                                   getActionBar().setSelectedNavigationItem(position);
                                               }

                                               @Override
                                               public void onPageScrollStateChanged(int state) {

                                               }
                                           });


        DisplayImageOptions displayimageOptions = new DisplayImageOptions.Builder().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }
        };
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Chemia").setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Matematyka").setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Fizyka").setTabListener(tabListener));

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
            /*ChooseClassDialogFragment dialog = new ChooseClassDialogFragment();
            dialog.show(getFragmentManager(),"TAG");*/
            startActivity(new Intent(this,ChooseClassActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            String queryFinish=new String();
            switch(i)
            {
                case 0:
                    queryFinish=new String("&subject=Chemia");
                    break;
                case 1:
                    queryFinish=new String("&subject=Matematyka");
                    break;
                case 2:
                    queryFinish=new String("&subject=Fizyka");
                    break;

            }
            args.putString("QUERY", "ksiazki?class_nr=I+gimnazjum"+queryFinish);
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

        void setSpiceManager(SpiceManager spiceManager)
        {
            this.spiceManager=spiceManager;
        }


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.booksfragment, container, false);
            mListView= (ListView) rootView.findViewById(R.id.listViewBooks);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            this.spiceManager=((BooksActivity)getActivity()).spiceManager;
            mListView.setAdapter(new BooksAdapter(getActivity(),spiceManager));
            query(getArguments().getString("QUERY"));
        }


        public void query (String query){
            ((BooksAdapter)mListView.getAdapter()).query(query);
        }

    }



}
