package com.swmansion.dajspisac.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.swmansion.dajspisac.book.SingleBookActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olek on 2014-08-16.
 */
public class DajSpisacUtilities {
    public static ArrayList<String> getMyBookIds(Context context){
        ArrayList<String> bookIds;
        SharedPreferences preferences=context.getSharedPreferences("BOOKIDS", 0);
        String initialString=preferences.getString("BOOKIDS","");
        bookIds=new ArrayList<String>(Arrays.asList(initialString.split(",")));
        if(bookIds.size()>0&&bookIds.get(0).equals("")){
            bookIds.remove(0);
        }
        return bookIds;
    }

    public static  void removeBookById(Context context,int id){
        ArrayList<String> myBooksIds=getMyBookIds(context);
        myBooksIds.remove(Integer.toString(id));
        addBookList(context,myBooksIds);
    }
    public static void addBookById(Context context,int id){
        ArrayList<String> currentIds=getMyBookIds(context);
        currentIds.add(Integer.toString(id));
        addBookList(context,currentIds);
    }

    public static void addBookList(Context context,ArrayList<String> idList){
        SharedPreferences preferences=context.getSharedPreferences("BOOKIDS", 0);
        SharedPreferences.Editor editor=preferences.edit();
        StringBuilder result = new StringBuilder();
        for(String string : idList) {
            result.append(string);
            result.append(",");
        }
        String res= result.length() > 0 ? result.substring(0, result.length() - 1): "";
        Log.d("retro","Saving string "+res);
        editor.putString("BOOKIDS", res);
        editor.commit();
    }
}
