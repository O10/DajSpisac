package com.swmansion.dajspisac.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by olek on 11.08.14.
 */
public class BitmapLoadSave {

    public static boolean saveBitmapToInternal(Context context, Bitmap image, String filename) {
        FileOutputStream out;
        boolean result = true;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("retro", "File not found exception");
            return false;
        }

        try {
            image.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        }
        Log.d("retro", "Returning " + Boolean.toString(result) + " filename " + filename);
        return result;
    }

    public static Bitmap loadBitmapFromInternal(Context context, String filename) {
        try {
            Bitmap tmp = BitmapFactory.decodeFile(context.getFilesDir().getAbsolutePath() + "/" + filename);
            Log.d("retro", "returning bitmap " + filename);
            return tmp;
        } catch (Exception e) {
            Log.d("retro", "Loading null " + filename);
            return null;
        }
    }

}
