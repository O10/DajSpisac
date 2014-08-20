package com.swmansion.dajspisac.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.octo.android.robospice.SpiceManager;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;
import com.swmansion.dajspisac.tools.ImageHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by olek on 04.08.14.
 */
public class BooksChooseAdapter extends BaseAdapter {
    Context context;
    ArrayList<Book> mBooksArray;
    Bitmap[] bArray;
    SpiceManager spiceManager;
    boolean positionsMarked[];

    String currentUserBooksIDS;
    ArrayList<String> bookIDS;

    static class ViewHolderItem {
        TextView author, title;
        ImageView miniature, sign;
        Button addDeleteButton;
        LinearLayout bookLayout;
        int id;
    }

    public BooksChooseAdapter(Context context, SpiceManager spiceManager) {
        super();
        this.context = context;
        this.mBooksArray = new ArrayList<Book>();
        this.spiceManager = spiceManager;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.single_book_listview_layout, viewGroup, false);
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

        if (book != null) {
            viewHolder.author.setText(book.getAuthors());
            viewHolder.title.setText(book.getName().toUpperCase());
            viewHolder.id = mBooksArray.get(position).getId();
            if (null == bArray[position]) {
                ImageLoader.getInstance().loadImage(String.format("http://dajspisac.pl%s", book.getCover_small()), new MImageLoadingListener(viewHolder.miniature, position));
            } else {
                viewHolder.miniature.setImageBitmap(bArray[position]);
            }

            if (positionsMarked[position]) {
                viewHolder.addDeleteButton.setBackgroundResource(R.drawable.twojeksiazki_plus_lososiowy);
            } else {
                viewHolder.addDeleteButton.setBackgroundResource(R.drawable.twojeksiazki_plus_lightblue);
            }

            viewHolder.addDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (positionsMarked[position]) {
                        viewHolder.addDeleteButton.setBackgroundResource(R.drawable.twojeksiazki_plus_lightblue);
                        DajSpisacUtilities.removeBookById(context, viewHolder.id);
                        positionsMarked[position] = false;
                    } else {
                        bookIDS.add(Integer.toString(viewHolder.id));
                        DajSpisacUtilities.addBookById(context, viewHolder.id);
                        viewHolder.addDeleteButton.setBackgroundResource(R.drawable.twojeksiazki_plus_lososiowy);
                        positionsMarked[position] = true;
                    }
                }
            });
            viewHolder.bookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!positionsMarked[position]) {
                        viewHolder.addDeleteButton.callOnClick();
                    }
                    Intent intent = new Intent(context, SingleBookActivity.class);
                    intent.putExtra("QUERY", "ksiazki/" + viewHolder.id);
                    if (!BitmapLoadSave.saveBitmapToInternal(context, bArray[position], "lastminiature.png")) {
                        Log.d("retro", "Saving miniature failed");
                        return;
                    }
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private void fillMarkedTable() {

        bookIDS = DajSpisacUtilities.getMyBookIds(context);

        for (int i = 0; i < positionsMarked.length; i++) {
            if (bookIDS.contains(Integer.toString(mBooksArray.get(i).getId()))) {
                positionsMarked[i] = true;
            } else {
                positionsMarked[i] = false;
            }
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    void setBooksArray(BookList bookList) {
        this.mBooksArray = bookList;
        bArray = new Bitmap[mBooksArray.size()];
        positionsMarked = new boolean[mBooksArray.size()];
        fillMarkedTable();
        notifyDataSetChanged();
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
            bArray[position] = finalbmp;
            imageView.setImageBitmap(finalbmp);
            notifyDataSetChanged();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    }


}
