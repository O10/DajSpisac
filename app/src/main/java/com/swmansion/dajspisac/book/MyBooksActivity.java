package com.swmansion.dajspisac.book;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.olek.firsttest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by olek on 13.08.14.
 */
public class MyBooksActivity extends FragmentActivity implements TabHost.OnTabChangeListener{
    SpiceManager spiceManager;
    ViewPager mViewPager;
    RelativeLayout addBookLayout;
    private TabHost mTabHost;
    private final static int expectedFooterHeight=100;
    private int previousTabIndex = 0;
    Animation tabClickedAnimation;
    MyBooksCollectionPagerAdapter mMyBooksCollectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooksactivitylayout);

        tabClickedAnimation= AnimationUtils.loadAnimation(this, R.anim.tab_clicked_animation);


        spiceManager=new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);

        setTitle("Twoje książki");

        addBookLayout=(RelativeLayout)findViewById(R.id.addBookLayout);

        addBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyBooksActivity.this,BooksChooserActivity.class));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);

        mMyBooksCollectionPagerAdapter=new MyBooksCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMyBooksCollectionPagerAdapter);

        initialiseTabHost();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                MyBooksActivity.this.mTabHost.setCurrentTab(position);
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
                View v = new View(MyBooksActivity.this);
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

        previousView.startAnimation(tabClickedAnimation);
        previousTextView=(TextView)previousView.findViewById(R.id.textViewTab);
        previousImageView=(ImageView)previousView.findViewById(R.id.imageViewSubjectIcon);
        previousImageView.setImageResource(RDrawableCoverLosos[previousTabIndex]);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));

        mViewPager.setCurrentItem(pos);


    }


    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
        mMyBooksCollectionPagerAdapter.makeRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    private class MyBooksCollectionPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<String> myBooksIds;

        HashMap<Integer,MyBooksFragment> mPageReferenceMap;
        HashMap<String,Integer> subjectIndexMap;
        public MyBooksCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap=new HashMap<Integer, MyBooksFragment>();
            subjectIndexMap=new HashMap<String, Integer>();
            subjectIndexMap.put("Chemia",0);
            subjectIndexMap.put("Matematyka",1);
            subjectIndexMap.put("Fizyka",2);

        }
        void makeRequest(){
            myBooksIds= DajSpisacUtilities.getMyBookIds(MyBooksActivity.this);
            for(String s:myBooksIds){
                BookRequest request = new BookRequest(String.format("ksiazki/%s",s));
                String lastRequestCacheKey = request.createCacheKey();
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_WEEK,new BookListener());
            }
            Log.d("retro","Requests made");
        }

        @Override
        public Fragment getItem(int i) {
            MyBooksFragment myFragment = MyBooksFragment.newInstance(expectedFooterHeight);
            mPageReferenceMap.put(i, myFragment);
            Log.d("retro","Calling getITem "+Integer.toString(expectedFooterHeight));
            return myFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            MyBooksFragment fragment = (MyBooksFragment) super.instantiateItem(container,
                    position);
            mPageReferenceMap.put(position, fragment);
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

        private class BookListener implements RequestListener<Book> {
            @Override
            public void onRequestFailure(SpiceException e) {

            }

            @Override
            public void onRequestSuccess(Book book) {
                mPageReferenceMap.get(subjectIndexMap.get(book.getSubject())).addBook(book);
                Log.d("retro","Request succesfull for book: "+book.getName());
            }
        }
    }

    public static class MyBooksFragment extends Fragment {
        private ListView mListView;
        MyBooksAdapter myBooksAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            myBooksAdapter=new MyBooksAdapter(getActivity());
        }

        public static MyBooksFragment newInstance(int height) {
            MyBooksFragment myFragment = new MyBooksFragment();
            Bundle b=new Bundle();
            b.putInt("FOOTERHEIGHT",height);
            myFragment.setArguments(b);
            return myFragment;
        }
        public void addBook(Book book){
            myBooksAdapter.addBook(book);
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.books_activity_fragment_layout, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listViewBooks);
            Bundle arguments=getArguments();
            if(arguments!=null){
                View footerView=new View(getActivity());
                footerView.setMinimumHeight(arguments.getInt("FOOTERHEIGHT"));
                mListView.addFooterView(footerView,null,false);
            }
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mListView.setAdapter(myBooksAdapter);

        }

    }
}
