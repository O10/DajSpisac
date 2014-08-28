package com.swmansion.dajspisac.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swmansion.dajspisac.R;


/**
 * Created by olek on 13.08.14.
 */
public class FragmentChooseClass extends Fragment {
    private final int schoolCount = 2;
    private final int classCount = 3;
    private final int defaultClassIndex = 1;
    private final int defaultSchoolIndex = 1;
    private int previousClickedClassIndex = 0, previousSchoolClickedIndex = 0;
    boolean isInitial = false;
    private Button[] schoolButtons = new Button[schoolCount];
    private Button[] classButtons = new Button[classCount];
    private String[] schoolStrings = {"szkoły podstawowej", "gimnazjum"};
    private String[] primaryClassStrings = {"4", "5", "6"};
    private String[] secondaryClassStrings = {"I", "II", "III"};
    private String currentSchoolQuery;
    private int[] buttonDrawables = {R.drawable.button_background_chooseclassdialog_left, R.drawable.button_background_chooseclassdialog_right, R.drawable.button_background_chooseclass_dialog_middle};
    private int[] buttonDrawablesClicked = {R.drawable.button_background_chooseclassdialog_left_clicked, R.drawable.button_background_chooseclassdialog_right_clicked, R.drawable.button_background_chooseclassdialog_middle_clicked};

    private View.OnClickListener schoolButtonsListener;
    private View.OnClickListener classButtonsListener;

    ClassChangeListener mClassChangeListener;

    public interface ClassChangeListener {
        public void onClassChanged(String newClass);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mClassChangeListener = (ClassChangeListener) activity;
        } catch (ClassCastException e) {
            mClassChangeListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_class_fragment_layout, null);
        schoolButtons[0] = (Button) rootView.findViewById(R.id.buttonSchoolPrimary);
        schoolButtons[1] = (Button) rootView.findViewById(R.id.buttonSchoolSecondary);
        for (int i = 0; i < schoolButtons.length; i++) {
            schoolButtons[i].setTag(i);
        }

        classButtons[0] = (Button) rootView.findViewById(R.id.buttonKl1);
        classButtons[1] = (Button) rootView.findViewById(R.id.buttonKl2);
        classButtons[2] = (Button) rootView.findViewById(R.id.buttonKl3);
        for (int i = 0; i < classButtons.length; i++) {
            classButtons[i].setTag(i);
        }

        return rootView;
    }

    private void onSchoolButtonClicked(View view){
        int nowClickedIndex = (Integer) view.getTag();
        if (nowClickedIndex == previousSchoolClickedIndex && isInitial) {
            return;
        }
        isInitial = true;
        if (nowClickedIndex == 0) {
            changeTextsOnClassButtons(4);
        } else {
            changeTextsOnClassButtons(1);
        }
        changeState(schoolButtons, previousSchoolClickedIndex, nowClickedIndex);
        previousSchoolClickedIndex = nowClickedIndex;
        String classString;
        if (nowClickedIndex == 0) {
            classString = primaryClassStrings[previousClickedClassIndex];
        } else {
            classString = secondaryClassStrings[previousClickedClassIndex];
        }
        currentSchoolQuery = String.format("%s %s", classString, schoolStrings[nowClickedIndex]);
        if (mClassChangeListener != null) {
            mClassChangeListener.onClassChanged(currentSchoolQuery);
        }
    }

    private void onClassButtonClicked(View view){
        int nowClickedIndex = (Integer) view.getTag();
        if (nowClickedIndex == previousClickedClassIndex) {
            return;
        }
        changeState(classButtons, previousClickedClassIndex, nowClickedIndex);
        previousClickedClassIndex = nowClickedIndex;

        String classString;
        if (previousSchoolClickedIndex == 0) {
            classString = primaryClassStrings[nowClickedIndex];
        } else {
            classString = secondaryClassStrings[nowClickedIndex];
        }
        currentSchoolQuery = String.format("%s %s", classString, schoolStrings[previousSchoolClickedIndex]);
        if (mClassChangeListener != null) {
            mClassChangeListener.onClassChanged(currentSchoolQuery);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        schoolButtonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSchoolButtonClicked(view);
            }
        };

        classButtonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassButtonClicked(view);
            }
        };

        for (Button b : schoolButtons) {
            b.setOnClickListener(schoolButtonsListener);
        }
        for (Button b : classButtons) {
            b.setOnClickListener(classButtonsListener);
        }
        if (savedInstanceState == null) {
            onSchoolButtonClicked(schoolButtons[defaultSchoolIndex]);
            onClassButtonClicked(classButtons[defaultClassIndex]);
        } else {
            onSchoolButtonClicked(schoolButtons[savedInstanceState.getInt("SCHOOLINDEX", defaultSchoolIndex)]);
            onClassButtonClicked(classButtons[savedInstanceState.getInt("CLASSINDEX", defaultClassIndex)]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SCHOOLINDEX", previousSchoolClickedIndex);
        outState.putInt("CLASSINDEX", previousClickedClassIndex);
    }

    void changeTextsOnClassButtons(int startNumber) {
        for (int i = 0; i < classButtons.length; i++) {
            classButtons[i].setText(Integer.toString(startNumber++));
        }
    }

    private void changeState(Button[] buttonGroup, int previousClicked, int currentClicked) {
        int previousButtonResource;
        if (previousClicked == 0) {
            previousButtonResource = buttonDrawables[0];
        } else if (previousClicked == buttonGroup.length - 1) {
            previousButtonResource = buttonDrawables[1];
        } else {
            previousButtonResource = buttonDrawables[2];
        }
        buttonGroup[previousClicked].setBackgroundResource(previousButtonResource);
        buttonGroup[previousClicked].setTextColor(getResources().getColor(R.color.lightBlueDajSpisac));

        //current Button
        int currentButtonResource;
        if (currentClicked == 0) {
            currentButtonResource = buttonDrawablesClicked[0];
        } else if (currentClicked == buttonGroup.length - 1) {
            currentButtonResource = buttonDrawablesClicked[1];
        } else {
            currentButtonResource = buttonDrawablesClicked[2];
        }
        buttonGroup[currentClicked].setBackgroundResource(currentButtonResource);
        buttonGroup[currentClicked].setTextColor(getResources().getColor(R.color.buttonTextColorClicked));

    }
}
