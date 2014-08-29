package com.swmansion.dajspisac.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.swmansion.dajspisac.R;

/**
 * Created by olek on 08.08.14.
 */
public class ChooseClassActivity extends FragmentActivity {
    private Button buttonSchoolPrimary, buttonSchoolSecondary, buttonClass1, buttonClass2, buttonClass3;
    private Button zmienButton, anulujButton;
    private ButtonListener mButtonListener;
    private String chosenClass;
    private boolean areClicked[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_class_layout);
        areClicked = new boolean[5];
        mButtonListener = new ButtonListener();
        buttonSchoolPrimary = (Button) findViewById(R.id.buttonSchoolPrimary);
        buttonSchoolPrimary.setTag(0);
        buttonSchoolPrimary.setOnClickListener(mButtonListener);

        buttonSchoolSecondary = (Button) findViewById(R.id.buttonSchoolSecondary);
        buttonSchoolSecondary.setTag(1);
        buttonSchoolSecondary.setOnClickListener(mButtonListener);

        buttonClass1 = (Button) findViewById(R.id.buttonKl1);
        buttonClass1.setTag(2);
        buttonClass1.setOnClickListener(mButtonListener);

        buttonClass2 = (Button) findViewById(R.id.buttonKl2);
        buttonClass2.setTag(3);
        buttonClass2.setOnClickListener(mButtonListener);

        buttonClass3 = (Button) findViewById(R.id.buttonKl3);
        buttonClass3.setTag(4);
        buttonClass3.setOnClickListener(mButtonListener);

        zmienButton = (Button) findViewById(R.id.buttonZmien);
        anulujButton = (Button) findViewById(R.id.buttonAnuluj);

        zmienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("MYCLASS", 0);
                SharedPreferences.Editor editor = preferences.edit();
                String value;
                if (areClicked[0])
                    value = String.format("%s szkoły podstawowej", chosenClass);
                else
                    value = String.format("%s gimnazjum", chosenClass);
                editor.putString("CLASS", value);
                Log.d("retro", "Puttin value to editor " + value);
                editor.commit();
                finish();
            }
        });

        anulujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            changeColor(view);
            areClicked[(Integer) view.getTag()] = !areClicked[(Integer) view.getTag()];
            switch ((Integer) view.getTag()) {
                case 0:
                    buttonClass1.setText("4");
                    buttonClass2.setText("5");
                    buttonClass3.setText("6");
                    if (areClicked[1]) {
                        changeColor(buttonSchoolSecondary);
                        areClicked[1] = false;
                    }
                    break;
                case 1:
                    buttonClass1.setText("1");
                    buttonClass2.setText("2");
                    buttonClass3.setText("3");
                    if (areClicked[0]) {
                        changeColor(buttonSchoolPrimary);
                        areClicked[0] = false;

                    }
                    break;
                case 2:
                    if (areClicked[3]) {
                        changeColor(buttonClass2);
                    }
                    if (areClicked[4]) {
                        changeColor(buttonClass3);
                    }
                    areClicked[3] = false;
                    areClicked[4] = false;
                    chosenClass = ((Button) view).getText().toString();
                    break;
                case 3:
                    if (areClicked[2]) {
                        changeColor(buttonClass1);
                    }
                    if (areClicked[4]) {
                        changeColor(buttonClass3);
                    }
                    areClicked[2] = false;
                    areClicked[4] = false;
                    chosenClass = ((Button) view).getText().toString();
                    break;
                case 4:
                    if (areClicked[2]) {
                        changeColor(buttonClass1);
                    }
                    if (areClicked[3]) {
                        changeColor(buttonClass2);
                    }
                    areClicked[2] = false;
                    areClicked[3] = false;
                    chosenClass = ((Button) view).getText().toString();
                    break;
            }

        }

        private void changeColor(View view) {
            if (areClicked[(Integer) view.getTag()]) {
                Log.d("retro", "areClicked true changed to False " +
                        "" + Integer.toString((Integer) view.getTag()));
                ((Button) view).setTextColor(getApplication().getResources().getColor(R.color.lightBlueDajSpisac));
                if ((Integer) view.getTag() == 2 || (Integer) view.getTag() == 0)
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclassdialog_left);
                else if ((Integer) view.getTag() == 1 || (Integer) view.getTag() == 4)
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclassdialog_right);
                else
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclass_dialog_middle);
            } else {
                Log.d("retro", "areClicked false changed to true " + Integer.toString((Integer) view.getTag()));
                ((Button) view).setTextColor(getApplication().getResources().getColor(R.color.buttonTextColorClicked));

                if ((Integer) view.getTag() == 2 || (Integer) view.getTag() == 0)
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclassdialog_left_clicked);
                else if ((Integer) view.getTag() == 1 || (Integer) view.getTag() == 4)
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclassdialog_right_clicked);
                else
                    ((Button) view).setBackgroundResource(R.drawable.button_background_chooseclassdialog_middle_clicked);
            }
        }
    }
}
