package com.swmansion.dajspisac.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by olek on 11.08.14.
 */
public class BitmapLoadSave {
    public final static String APP_PATH_SD_CARD = "/DesiredSubfolderName/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    public static boolean saveImageToExternalStorage(Context context, Bitmap image) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, "lastmin.png");
            file.createNewFile();
            fOut = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean saveBitmapToInternal(Context context, Bitmap image, String filename) {
        FileOutputStream out = null;
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

    public static boolean isSdReadable() {

        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }


    public static Bitmap getThumbnail(Context context, String filename) {

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
        Bitmap thumbnail = null;
        try {
            if (isSdReadable() == true) {
                thumbnail = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("getThumbnail() on external storage", e.getMessage());
        }

        if (thumbnail == null) {
            try {
                File filePath = context.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("getThumbnail() on internal storage", ex.getMessage());
            }
        }
        return thumbnail;
    }
}
