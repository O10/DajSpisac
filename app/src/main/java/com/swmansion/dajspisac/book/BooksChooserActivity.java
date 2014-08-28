package com.swmansion.dajspisac.book;

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
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.settings.FragmentChooseClass;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by olek on 04.08.14.
 */
public class BooksChooserActivity extends BooksActivity implements FragmentChooseClass.ClassChangeListener {
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private String currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        TextView pageTitle = (TextView) findViewById(R.id.textViewTitle);
        pageTitle.setText("Wybierz książkę");
        ImageView imageViewTemp = (ImageView) findViewById(R.id.imageViewRight);
        imageViewTemp.setVisibility(View.GONE);
        imageViewTemp = (ImageView) findViewById(R.id.imageViewLeft);
        imageViewTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClassChanged(String newClass) {
        if (mDemoCollectionPagerAdapter == null) {
            mDemoCollectionPagerAdapter =
                    new DemoCollectionPagerAdapter(
                            getSupportFragmentManager());
            mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        }
        try {
            currentClass = URLEncoder.encode(newClass, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mViewPager.getAdapter().notifyDataSetChanged();

    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        String qFinish[] = {"&subject=Matematyka","&subject=Chemia",  "&subject=Fizyka"};

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            BooksFragment fragment = new BooksFragment();
            Bundle args = new Bundle();
            String queryFinish = qFinish[i];

            args.putString("QUERY", "ksiazki?class_nr=" + currentClass + queryFinish);
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
        private SpiceManager spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        BooksChooseAdapter mBooksChooserAdapter;
        private static boolean isToastShown = false;
        private TextView textViewNoBooks;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            mBooksChooserAdapter = new BooksChooseAdapter(getActivity());

        }

        @Override
        public void onStart() {
            super.onStart();
            BooksRequest request = new BooksRequest(getArguments().getString("QUERY"));
            String lastRequestCacheKey = request.createCacheKey();
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_WEEK, new ListBooksRequestListener());

            spiceManager.start(getActivity());
        }

        @Override
        public void onStop() {
            super.onStop();
            if (spiceManager.isStarted()) {
                spiceManager.shouldStop();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.books_activity_fragment_layout, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listViewBooks);
            textViewNoBooks = (TextView) rootView.findViewById(R.id.textViewNoBooks);
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
                if (!isToastShown) {
                    DajSpisacUtilities.showInternetErrorToast(getActivity());
                    isToastShown = true;
                }
            }

            @Override
            public void onRequestSuccess(BookList books) {
                if (books != null) {
                    mBooksChooserAdapter.setBooksArray(books);
                    if (books.size() == 0) {
                        textViewNoBooks.setVisibility(View.VISIBLE);
                    }
                }
                isToastShown = false;
            }
        }


    }

}
