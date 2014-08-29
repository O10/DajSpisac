package com.swmansion.dajspisac.book;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;
import com.swmansion.dajspisac.tools.ImageHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by olek on 04.08.14.
 */
public class BooksChooseAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Book> mBooksArray;
    Bitmap[] bArray;
    boolean positionsMarked[];

    LinkedHashSet<String> bookIDS;

    static class ViewHolderItem {
        TextView author, title;
        ImageView miniature;
        LinearLayout bookLayout;
        int id;
    }

    public BooksChooseAdapter(Activity activity) {
        super();
        this.activity = activity;
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

    private void addDeleteButtonClicked(int position, ViewHolderItem viewHolder) {
        bookIDS.add(Integer.toString(viewHolder.id));
        DajSpisacUtilities.addBookById(activity, viewHolder.id);
        activity.finish();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            view = inflater.inflate(R.layout.single_book_listview_layout, viewGroup, false);
            viewHolder = new ViewHolderItem();
            viewHolder.author = (TextView) view.findViewById(R.id.tVAuthor);
            viewHolder.title = (TextView) view.findViewById(R.id.tVtitle);
            viewHolder.miniature = (ImageView) view.findViewById(R.id.iViewMin);
            viewHolder.id = mBooksArray.get(position).getId();
            view.findViewById(R.id.buttonAddDeleteBook).setVisibility(View.GONE);
            view.findViewById(R.id.gradientDivider).setVisibility(View.GONE);
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

            viewHolder.bookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDeleteButtonClicked(position, viewHolder);
                }
            });
        }

        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    void setBooksArray(BookList bookList) {
        bookIDS = new LinkedHashSet<String>(DajSpisacUtilities.getMyBookIds(activity));
        mBooksArray = new ArrayList<Book>(bookList);
        mBooksArray.removeAll(bookIDS);
        bArray = new Bitmap[mBooksArray.size()];
        positionsMarked = new boolean[mBooksArray.size()];
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
            Bitmap finalBmp = ImageHelper.getRoundedCornerBitmap(bitmap, 8);
            bArray[position] = finalBmp;
            imageView.setImageBitmap(finalBmp);
            notifyDataSetChanged();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    }


}
