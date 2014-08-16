package com.swmansion.dajspisac.book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olek.firsttest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.settings.FragmentChooseClass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by olek on 04.08.14.
 */
public class BooksChooserActivity extends FragmentActivity implements TabHost.OnTabChangeListener,FragmentChooseClass.ClassChangeListener {
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    private TabHost mTabHost;
    private int previousTabIndex = 0;
    private String currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity_layout);

        setTitle("Wybierz książkę");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        initialiseTabHost();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                BooksChooserActivity.this.mTabHost.setCurrentTab(position);
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
                View v = new View(BooksChooserActivity.this);
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
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTabChanged(String s) {
        int []RDrawableCoverBlada={R.drawable.twojeksiazki_chemia_blady,R.drawable.twojeksiazki_matematyka_blada,R.drawable.twojeksiazki_fizyka_blada};
        int []RDrawableCoverLosos={R.drawable.twojeksiazki_chemia_losos,R.drawable.twojeksiazki_matematyka_lososiowa,R.drawable.twojeksiazki_fizyka_lososiowa};

        TabWidget mTabWidget=mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        TextView previousTextView=(TextView)previousView.findViewById(R.id.textViewTab);
        ImageView previousImageView=(ImageView)previousView.findViewById(R.id.imageViewSubjectIcon);


        previousImageView.setImageResource(RDrawableCoverBlada[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
        int pos = mTabHost.getCurrentTab();
        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView=(TextView)previousView.findViewById(R.id.textViewTab);
        previousImageView=(ImageView)previousView.findViewById(R.id.imageViewSubjectIcon);
        previousImageView.setImageResource(RDrawableCoverLosos[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));

        mViewPager.setCurrentItem(pos);


    }

    @Override
    public void onClassChanged(String newClass) {
        if(mDemoCollectionPagerAdapter==null){
            mDemoCollectionPagerAdapter =
                    new DemoCollectionPagerAdapter(
                            getSupportFragmentManager());
            mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        }
        try {
            currentClass= URLEncoder.encode(newClass,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mViewPager.getAdapter().notifyDataSetChanged();

    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        String qFinish[]={"&subject=Chemia","&subject=Matematyka","&subject=Fizyka"};

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            BooksFragment fragment = new BooksFragment();
            Bundle args = new Bundle();
            String queryFinish = qFinish[i];

            args.putString("QUERY", "ksiazki?class_nr="+currentClass+ queryFinish);
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }



    ////////////////////////FRAGMENT


    public static class BooksFragment extends Fragment {
        private ListView mListView;
        private SpiceManager spiceManager=new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        BooksChooseAdapter mBooksChooserAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            mBooksChooserAdapter=new BooksChooseAdapter(getActivity(), spiceManager);

            BooksRequest request = new BooksRequest(getArguments().getString("QUERY"));
            String lastRequestCacheKey = request.createCacheKey();
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListBooksRequestListener());
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
            mListView.setAdapter(mBooksChooserAdapter);
        }

        private class ListBooksRequestListener implements RequestListener<BookList> {
            @Override
            public void onRequestFailure(SpiceException e) {
                Toast.makeText(getActivity(), "Błąd połączenia", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(BookList books) {
                if (books != null) {
                    mBooksChooserAdapter.setBooksArray(books);
                }
            }
        }





    }

}
