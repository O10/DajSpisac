package com.example.olek.firsttest;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

/**
 * Created by olek on 05.08.14.
 */
public class SingleBookActivity extends FragmentActivity{
    Book book;
    ViewPager viewPager;
    SpiceManager spiceManager;
    ActionBar.TabListener tabListener;
    ArrayList <Integer> pagesNumbers;

    void setAdapter(){
        viewPager.setAdapter(new PagesForBookAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlebookactivitylayout);
        viewPager = (ViewPager) findViewById(R.id.viewPagerSingleBook);

        //Log.d("retro",Integer.toString(getIntent().getIntArrayExtra("PAGES")[1]));
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);


        tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }
        };

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


    public class PagesForBookAdapter extends FragmentStatePagerAdapter {
        public PagesForBookAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            SinglePageFragment fragment = new SinglePageFragment();
            Bundle args = new Bundle();
            String s=String.format("ksiazki/%d/zadania?page=%s",book.getId(),pagesNumbers.get(i));
            Log.d("retro",s);
            args.putString("QUERY",s);
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
        ArrayList<Exercise> exercises;
        TableLayout rootContainer;
        int bookId;
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
                    R.layout.singlepagefragment, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.d("retro","onActivityCreated called");
            this.spiceManager=((SingleBookActivity)getActivity()).spiceManager;
            this.bookId=((SingleBookActivity)getActivity()).book.getId();
                ExercisesRequest request=new ExercisesRequest(getArguments().getString("QUERY"));
                String lastRequestCacheKey = request.createCacheKey();
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ExercisesRequestListener());


        }

        private class ExercisesRequestListener implements  RequestListener<ExerciseList>{
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("retro","FAILURE Ex list");

            }

            @Override
            public void onRequestSuccess(ExerciseList exercises) {
                Log.d("retro","EList "+exercises.size());
                rootContainer= (TableLayout) getView().findViewById(R.id.allButtonsContainer);

                TableRow row=new TableRow(getActivity());
                TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                //rowParams.setMargins(10,10,10,10);
                rowParams.gravity= Gravity.CENTER_HORIZONTAL;

                tableParams.setMargins(0,15,0,15);
                row.setLayoutParams(tableParams);
                for(int i=0;i<exercises.size();i++){
                    Button b=(Button)getActivity().getLayoutInflater().inflate(R.layout.buttontemplate, row,false);
                    final int exercise_Id=exercises.get(i).getId();
                    b.setText(exercises.get(i).getNumber());
                    b.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getActivity(),SingleExerciseActivity.class);
                            intent.putExtra("BOOK_ID",bookId);
                            intent.putExtra("EXERCISE_ID",exercise_Id);
                            startActivity(intent);
                        }
                    });
                    row.addView(b);
                    if((i+1)%3==0)
                    {
                        rootContainer.addView(row);
                        row=new TableRow(getActivity());
                        tableParams.setMargins(0,15,0,15);
                        row.setLayoutParams(tableParams);
                    }
                }
                if(row.getChildCount()!=0)
                    rootContainer.addView(row);
            }
        }


    }




    private  class BookRequestListener implements RequestListener<Book> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d("retro", "Failure");

        }

        @Override
        public void onRequestSuccess(Book dBook) {
            book=dBook;
            if(dBook!=null){
                ActionBar actionBar=getActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                pagesNumbers=new ArrayList<Integer>();
                for(int i=0;i<dBook.getPages().size();i++){
                    actionBar.addTab(
                            actionBar.newTab()
                                    .setText(Integer.toString(dBook.getPages().get(i))).setTabListener(tabListener));
                    pagesNumbers.add(dBook.getPages().get(i));
                }
                setAdapter();
            }
        }
    }
}
