package com.swmansion.dajspisac.book;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.olek.firsttest.R;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.exercise.ExerciseList;
import com.swmansion.dajspisac.exercise.ExercisesRequest;
import com.swmansion.dajspisac.exercise.SingleExerciseActivity;
import com.swmansion.dajspisac.tools.BitmapLoadSave;

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
    private HorizontalScrollView horScrollView;

    void setAdapter() {
        viewPager.setAdapter(new PagesForBookAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.single_book_activity_layout);
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
        horScrollView=(HorizontalScrollView)findViewById(R.id.horizontalscrollview);

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


        Log.d("retro","Starting with string: "+getIntent().getStringExtra("QUERY"));
        BookRequest request = new BookRequest(getIntent().getStringExtra("QUERY"));
        String lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new BookRequestListener());

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
    public void onTabChanged(String s) {
        TabWidget mTabWidget=mTabHost.getTabWidget();
        View previousView = mTabWidget.getChildTabViewAt(previousTabIndex);

        TextView previousTextView=(TextView)previousView.findViewById(R.id.textViewPageNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));
        int pos = mTabHost.getCurrentTab();
        previousTabIndex = pos;

        previousView = mTabWidget.getChildTabViewAt(previousTabIndex);
        previousTextView=(TextView)previousView.findViewById(R.id.textViewPageNumber);
        previousTextView.setTextColor(getResources().getColor(R.color.orangeDajSpisac));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        horScrollView.smoothScrollTo(previousView.getLeft()-(width/2)+(previousView.getWidth()/2),previousView.getTop());

        viewPager.setCurrentItem(pos);
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
            args.putString("SUBJECT",book.getSubject());

            Log.d("retro", s);
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

    public static class SinglePageFragment extends Fragment {
        private SpiceManager spiceManager;
        TableLayout rootContainer;
        int bookId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.single_book_activity_fragment_layout, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            Log.d("retro","Entering onActivityCreated");
            super.onActivityCreated(savedInstanceState);
            this.spiceManager = ((SingleBookActivity) getActivity()).spiceManager;
            if(bookId==0){
                this.bookId = ((SingleBookActivity) getActivity()).book.getId();
            }
            ExercisesRequest request = new ExercisesRequest(getArguments().getString("QUERY"));
            String lastRequestCacheKey = request.createCacheKey();
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ExercisesRequestListener());


        }

        private class ExercisesRequestListener implements RequestListener<ExerciseList> {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("retro", "FAILURE Ex list");

            }

            @Override
            public void onRequestSuccess(final ExerciseList exercises) {
                Log.d("retro", "EList " + exercises.size());
                if(getView()==null){
                    return;
                }
                rootContainer = (TableLayout) getView().findViewById(R.id.allButtonsContainer);

                TableRow row = new TableRow(getActivity());
                TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                //rowParams.setMargins(10,10,10,10);
                rowParams.gravity = Gravity.CENTER_HORIZONTAL;

                tableParams.setMargins(0, 15, 0, 15);
                row.setLayoutParams(tableParams);
                for (int i = 0; i < exercises.size(); i++) {
                    Button b = (Button) getActivity().getLayoutInflater().inflate(R.layout.button_exercise_template, row, false);
                    final int exercise_Id = exercises.get(i).getId();
                    b.setText(exercises.get(i).getNumber());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), SingleExerciseActivity.class);
                            intent.putExtra("BOOK_ID", bookId);
                            intent.putExtra("EXERCISE_ID", exercise_Id);
                            int [] pageIds=new int[exercises.size()];
                            String [] pageNums=new String[exercises.size()];
                            for(int i=0;i<exercises.size();i++){
                                pageIds[i]=exercises.get(i).getId();
                                pageNums[i]=exercises.get(i).getNumber();
                            }
                            intent.putExtra("IDS",pageIds);
                            intent.putExtra("NUMS",pageNums);
                            intent.putExtra("SUBJECT",getArguments().getString("SUBJECT"));
                            intent.putExtra("PAGENUM",getArguments().getInt("PAGENUM"));
                            startActivity(intent);
                        }
                    });
                    row.addView(b);
                    if ((i + 1) % 3 == 0) {
                        rootContainer.addView(row);
                        row = new TableRow(getActivity());
                        tableParams.setMargins(0, 15, 0, 15);
                        row.setLayoutParams(tableParams);
                    }
                }
                if (row.getChildCount() != 0)
                    rootContainer.addView(row);
            }
        }


    }


    private class BookRequestListener implements RequestListener<Book> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d("retro", "Failure");

        }

        @Override
        public void onRequestSuccess(Book dBook) {
            book = dBook;

            ActionBar actionBar = getActionBar();
            actionBar.show();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            View rootView=getLayoutInflater().inflate(R.layout.actionbar_layout_singlebook,null);
            TextView textViewSubject=(TextView)rootView.findViewById(R.id.textViewSubjectName);
            textViewSubject.setText(book.getSubject());
            ImageView bookMiniature=(ImageView)rootView.findViewById(R.id.imageViewCoverMin);
            ImageView imageBack=(ImageView)rootView.findViewById(R.id.imageViewBack);
            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            Bitmap miniature= BitmapLoadSave.getThumbnail(SingleBookActivity.this,"lastmin.png");
            bookMiniature.setImageBitmap(miniature);
            actionBar.setCustomView(rootView);

            if (dBook != null) {
                pagesNumbers = new ArrayList<Integer>();
                LinearLayout ll=(LinearLayout)findViewById(R.id.top_buttons);
                for (int i = 0; i < dBook.getPages().size(); i++) {
                    View tabIndicatorView=getLayoutInflater().inflate(R.layout.tab_page_number_layout,null);
                    TextView textViewTab=(TextView)tabIndicatorView.findViewById(R.id.textViewPageNumber);
                    textViewTab.setText(Integer.toString(dBook.getPages().get(i)));
                    mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(i)).setIndicator(tabIndicatorView).setContent(defaultTabCont));
                    pagesNumbers.add(dBook.getPages().get(i));
                }
                setAdapter();
            }
        }
    }
}
