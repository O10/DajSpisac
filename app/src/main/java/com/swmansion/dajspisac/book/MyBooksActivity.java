package com.swmansion.dajspisac.book;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by olek on 13.08.14.
 */
public class MyBooksActivity extends BooksActivity {
    private RelativeLayout addBookLayout;
    private final static int expectedFooterHeight = 171;
    private MyBooksCollectionPagerAdapter mMyBooksCollectionPagerAdapter;
    private SpiceManager spiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 12) {
            setContentView(R.layout.mybooksactivitylayout_noclip);
        } else {
            setContentView(R.layout.mybooksactivitylayout);
        }

        final AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);

        TextView pageTitle = (TextView) findViewById(R.id.textViewTitle);
        ImageView imageViewTemp = (ImageView) findViewById(R.id.imageViewLeft);
        imageViewTemp.setVisibility(View.GONE);

        imageViewTemp = (ImageView) findViewById(R.id.imageViewRight);
        imageViewTemp.setPadding(6, 5, 0, 5);


        pageTitle.setText("Twoje książki");


        addBookLayout = (RelativeLayout) findViewById(R.id.addBookLayout);

        addBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyBooksActivity.this, BooksChooserActivity.class));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);

        mMyBooksCollectionPagerAdapter = new MyBooksCollectionPagerAdapter(getSupportFragmentManager());
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
        private boolean isAnyToastShowing = false;
        private HashMap<Integer, MyBooksFragment> mPageReferenceMap;
        private HashMap<String, Integer> subjectIndexMap;
        private boolean areAnyBooks[] = new boolean[3];

        public MyBooksCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new HashMap<Integer, MyBooksFragment>();
            subjectIndexMap = new HashMap<String, Integer>();
            subjectIndexMap.put("Matematyka", 0);
            subjectIndexMap.put("Chemia", 1);
            subjectIndexMap.put("Fizyka", 2);

        }

        void makeRequest() {
            myBooksIds = DajSpisacUtilities.getMyBookIds(MyBooksActivity.this);


            for (String s : myBooksIds) {
                BookRequest request = new BookRequest(String.format("ksiazki/%s", s));
                String lastRequestCacheKey = request.createCacheKey();
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_WEEK, new BookListener());
            }
        }

        @Override
        public Fragment getItem(int i) {
            MyBooksFragment myFragment = MyBooksFragment.newInstance(expectedFooterHeight);
            mPageReferenceMap.put(i, myFragment);
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
                if (!isAnyToastShowing) {
                    DajSpisacUtilities.showInternetErrorToast(MyBooksActivity.this);
                    isAnyToastShowing = true;
                }
            }

            @Override
            public void onRequestSuccess(Book book) {
                int index = subjectIndexMap.get(book.getSubject());
                mPageReferenceMap.get(index).addBook(book);

                areAnyBooks[index] = true;
                for (int i = 0; i < 3; i++) {
                    if (!areAnyBooks[i]) {
                        mPageReferenceMap.get(i).setTextNoBooksVisible();
                    }
                }
            }
        }
    }

    public static class MyBooksFragment extends Fragment {
        private ListView mListView;
        private MyBooksAdapter myBooksAdapter;
        private TextView textViewNoBooks;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            myBooksAdapter = new MyBooksAdapter(getActivity());

        }

        void setTextNoBooksVisible() {
            textViewNoBooks.setVisibility(View.VISIBLE);
        }

        public static MyBooksFragment newInstance(int height) {
            MyBooksFragment myFragment = new MyBooksFragment();
            Bundle b = new Bundle();
            b.putInt("FOOTERHEIGHT", height);
            myFragment.setArguments(b);
            return myFragment;
        }

        public void addBook(Book book) {
            myBooksAdapter.addBook(book);
            textViewNoBooks.setVisibility(View.GONE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.books_activity_fragment_layout, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listViewBooks);
            textViewNoBooks = (TextView) rootView.findViewById(R.id.textViewNoBooks);
            Bundle arguments = getArguments();
            if (arguments != null) {
                View footerView = new View(getActivity());
                footerView.setMinimumHeight(arguments.getInt("FOOTERHEIGHT"));
                mListView.addFooterView(footerView, null, false);
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
