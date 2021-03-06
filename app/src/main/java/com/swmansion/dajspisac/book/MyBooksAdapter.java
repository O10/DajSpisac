package com.swmansion.dajspisac.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swmansion.dajspisac.R;
import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.DajSpisacUtilities;
import com.swmansion.dajspisac.tools.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by olek on 13.08.14.
 */
public class MyBooksAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Book> mBooksArray = new ArrayList<Book>();
    HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    static class ViewHolderItem {
        TextView author, title;
        ImageView miniature;
        Button addDeleteButton;
        LinearLayout bookLayout;
        int id;
    }

    void addBook(Book book) {
        for (Book tempBook : mBooksArray) {
            if (book.getId() == tempBook.getId()) {
                return;
            }
        }
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

        if (book != null) {
            viewHolder.author.setText(book.getAuthors());
            viewHolder.title.setText(book.getName().toUpperCase());
            viewHolder.id = mBooksArray.get(position).getId();
            if (null == bitmapMap.get(position)) {
                ImageLoader.getInstance().loadImage(String.format("http://dajspisac.pl%s", book.getCover_small()), new MImageLoadingListener(viewHolder.miniature, position));
            } else {
                viewHolder.miniature.setImageBitmap(bitmapMap.get(position));
            }
            viewHolder.addDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DajSpisacUtilities.removeBookById(context, viewHolder.id);
                    mBooksArray.remove(position);
                    bitmapMap.clear();
                    notifyDataSetChanged();
                }
            });
            viewHolder.bookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleBookActivity.class);
                    intent.putExtra("QUERY", "ksiazki/" + viewHolder.id);
                    intent.putExtra("CURBOOKID",viewHolder.id);
                    if (!BitmapLoadSave.saveBitmapToInternal(context, bitmapMap.get(position), "lastminiature.png")) {
                        return;
                    }
                    context.startActivity(intent);
                }
            });
        }

        return view;
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
            bitmapMap.put(position, finalbmp);
            imageView.setImageBitmap(finalbmp);
            notifyDataSetChanged();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    }
}
