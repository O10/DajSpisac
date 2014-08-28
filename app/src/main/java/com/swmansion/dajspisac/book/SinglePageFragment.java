package com.swmansion.dajspisac.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.exercise.ExerciseList;
import com.swmansion.dajspisac.exercise.ExercisesRequest;
import com.swmansion.dajspisac.exercise.SingleExerciseActivity;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;

public class SinglePageFragment extends Fragment {
    private SpiceManager spiceManager;
    private int bookId;
    private boolean hasButtons = false;
    private ExerciseList mExercises;
    private static boolean isToastShown = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bookId = getArguments().getInt("BOOKID");
        spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.single_book_activity_fragment_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
        if (mExercises == null) {
            ExercisesRequestListener listener = new ExercisesRequestListener();
            ExercisesRequest request = new ExercisesRequest(getArguments().getString("QUERY"));
            String lastRequestCacheKey = request.createCacheKey();
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_WEEK, listener);
        } else if (!hasButtons) {
            updateButtonViews();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasButtons = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    void updateButtonViews() {
        hasButtons = true;
        TableLayout rootContainer;
        rootContainer = (TableLayout) getView().findViewById(R.id.allButtonsContainer);

        TableRow row = new TableRow(getActivity());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;

        tableParams.setMargins(0, 15, 0, 15);
        row.setLayoutParams(tableParams);
        for (int i = 0; i < mExercises.size(); i++) {
            Button b = (Button) getActivity().getLayoutInflater().inflate(R.layout.button_exercise_template, row, false);
            final int exercise_Id = mExercises.get(i).getId();
            b.setText(mExercises.get(i).getNumber());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SingleExerciseActivity.class);
                    intent.putExtra("BOOK_ID", bookId);
                    Log.d("retro", "Putting extra " + Integer.toString(bookId));
                    intent.putExtra("EXERCISE_ID", exercise_Id);
                    int[] pageIds = new int[mExercises.size()];
                    String[] pageNums = new String[mExercises.size()];
                    for (int i = 0; i < mExercises.size(); i++) {
                        pageIds[i] = mExercises.get(i).getId();
                        pageNums[i] = mExercises.get(i).getNumber();
                    }
                    intent.putExtra("IDS", pageIds);
                    intent.putExtra("NUMS", pageNums);
                    intent.putExtra("SUBJECT", getArguments().getString("SUBJECT"));
                    intent.putExtra("PAGENUM", getArguments().getInt("PAGENUM"));
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

    private class ExercisesRequestListener implements RequestListener<ExerciseList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            if (!isToastShown) {
                DajSpisacUtilities.showInternetErrorToast(getActivity());
                isToastShown = true;
            }
        }

        @Override
        public void onRequestSuccess(final ExerciseList exercises) {
            mExercises = exercises;
            if (isAdded()) {
                updateButtonViews();
            }
            isToastShown = false;
        }
    }


}