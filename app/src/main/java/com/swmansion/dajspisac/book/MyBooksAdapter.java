package com.swmansion.dajspisac.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.olek.firsttest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.ImageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by olek on 13.08.14.
 */
public class MyBooksAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Book> mBooksArray = new ArrayList<Book>();
    HashMap<Integer,Bitmap> bitmapMap=new HashMap<Integer,Bitmap>();

    static class ViewHolderItem {
        TextView author, title;
        ImageView miniature,sign;
        Button addDeleteButton;
        LinearLayout bookLayout;
        int id;
    }

    void addBook(Book book){
        mBooksArray.add(book);
        notifyDataSetChanged();
    }

    public MyBooksAdapter(Context context) {
        super();
        this.context = context;
        this.mBooksArray = new ArrayList<Book>();
    }

    @Override
    public int getCount() {
        return mBooksArray.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooksArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.mybook_single_listview_layout, viewGroup, false);
            viewHolder = new ViewHolderItem();
            viewHolder.author = (TextView) view.findViewById(R.id.tVAuthor);
            viewHolder.title = (TextView) view.findViewById(R.id.tVtitle);
            viewHolder.miniature = (ImageView) view.findViewById(R.id.iViewMin);
            viewHolder.id = mBooksArray.get(position).getId();
            viewHolder.addDeleteButton = (Button) view.findViewById(R.id.buttonAddDeleteBook);
            viewHolder.bookLayout = (LinearLayout) view.findViewById(R.id.booksLayoutSingleItem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) view.getTag();
        }
        Book book = mBooksArray.get(position);

        if(book!=null){
            viewHolder.author.setText(book.getAuthors());
            viewHolder.title.setText(book.getName().toUpperCase());
            viewHolder.id = mBooksArray.get(position).getId();
            if (null == bitmapMap.get(position)) {
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
                ImageLoader.getInstance().loadImage(String.format("http://dajspisac.pl%s", book.getCover_small()), new MImageLoadingListener(viewHolder.miniature, position));
            } else {
                viewHolder.miniature.setImageBitmap(bitmapMap.get(position));
            }
            viewHolder.addDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeIdFromPreferences(viewHolder.id);
                    mBooksArray.remove(position);
                    notifyDataSetChanged();
                }
            });
            viewHolder.bookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleBookActivity.class);
                    intent.putExtra("QUERY", "ksiazki/" + viewHolder.id);
                    if(!BitmapLoadSave.saveImageToExternalStorage(context,bitmapMap.get(position))){
                        return;
                    }
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private void removeIdFromPreferences(int id){
        SharedPreferences preferences=context.getSharedPreferences("BOOKIDS", 0);
        String initialString=preferences.getString("BOOKIDS","");
        ArrayList<String> myBooksIds=new ArrayList<String>(Arrays.asList(initialString.split(",")));
        myBooksIds.remove(Integer.toString(id));

        SharedPreferences.Editor editor=preferences.edit();
        StringBuilder result = new StringBuilder();
        for(String string : myBooksIds) {
            result.append(string);
            result.append(",");
        }
        String res= result.length() > 0 ? result.substring(0, result.length() - 1): "";
        editor.putString("BOOKIDS",res);
        editor.commit();
    }


    private class MImageLoadingListener implements ImageLoadingListener {
        ImageView imageView;
        int position;

        public MImageLoadingListener(ImageView imageView, int position) {
            this.imageView = imageView;
            this.position = position;
        }

        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            Bitmap finalbmp = ImageHelper.getRoundedCornerBitmap(bitmap, 8);
            bitmapMap.put(position,finalbmp);
            imageView.setImageBitmap(finalbmp);
            notifyDataSetChanged();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    }
}
