package com.example.olek.firsttest;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by olek on 06.08.14.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    Exercise exercise;
    public ExpandableListAdapter(Context context,Exercise exercise){
        this.context=context;
        this.exercise=exercise;
    }
    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.elistgrouplayout, null);
        }
        TextView tView=(TextView)convertView.findViewById(R.id.elistgroupText);;
        if(groupPosition==0){
            tView.setText("TREŚĆ ZADANIA");
        }
        else{

            tView.setText("ROZWIĄZANIE");
        }
    return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.elistchildlayout, null);
        }
        TextView tView=(TextView)convertView.findViewById(R.id.textView);

        //WebView webView=(WebView)convertView.findViewById(R.id.webViewSolution);
        String s;
        if(groupPosition==0){
            tView.setText(Html.fromHtml(exercise.getContent()));
            //s=String.format("<html><body>%s</body></html>",exercise.getContent());
        }
        else {
            //s=String.format("<html><body>%s</body></html>",exercise.getSolution());
            tView.setText(Html.fromHtml(exercise.getSolution()));
        }
        //webView.loadData(s,"text/html",null);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}