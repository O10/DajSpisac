package com.swmansion.dajspisac.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.swmansion.dajspisac.tools.BitmapLoadSave;
import com.swmansion.dajspisac.tools.ImageHelper;
import com.example.olek.firsttest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

/**
 * Created by olek on 04.08.14.
 */
public class BooksAdapter extends BaseAdapter {
    Context context;
    ArrayList<Book> mBooksArray;
    Bitmap[] bArray;
    SpiceManager spiceManager;

    static class ViewHolderItem {
        TextView author, title;
        ImageView miniature;
        Button addDeleteButton;
        LinearLayout bookLayout;
        int id;
        TextView t;
    }

    public BooksAdapter(Context context, SpiceManager spiceManager) {
        super();
        this.context = context;
        this.mBooksArray = new ArrayList<Book>();
        this.spiceManager = spiceManager;

        //query("ksiazki?class_nr=I+gimnazjum");
    }

    @Override
    public int getCount() {
        return mBooksArray.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooksArray.get(i);
    }

    public void query(String query) {
        BooksRequest request = new BooksRequest(query);
        String lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListBooksRequestListener());
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
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
                ImageLoader.getInstance().loadImage(String.format("http://dajspisac.pl%s", book.getCover_small()), new MImageLoadingListener(viewHolder.miniature, position));
            } else {
                viewHolder.miniature.setImageBitmap(bArray[position]);
            }

            viewHolder.addDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Worktin", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.bookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleBookActivity.class);
                    intent.putExtra("QUERY", "ksiazki/" + viewHolder.id);
                    if(!BitmapLoadSave.saveImageToExternalStorage(context,bArray[position])){
                        return;
                    }
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ListBooksRequestListener implements RequestListener<BookList> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d("retro", "Failure");
            Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(BookList books) {
            if (books != null) {
                BooksAdapter.this.mBooksArray = books;
                bArray = new Bitmap[mBooksArray.size()];
                notifyDataSetChanged();
                Log.d("retro", "Success" + books.get(5).getClass_nr());
            } else
                Log.d("retro", "Succes");

        }
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
