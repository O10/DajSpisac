package com.example.olek.firsttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.swmansion.dajspisac.book.BookList;
import com.swmansion.dajspisac.book.BooksRequest;


public class HomeActivity extends Activity {
    protected SpiceManager spiceManager = new SpiceManager(com.octo.android.robospice.Jackson2SpringAndroidSpiceService.class);
    String lastRequestCacheKey = new String();
    private Button butFinSol, butLoginFb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        butFinSol = (Button) findViewById(R.id.butfindsolution);
        butLoginFb = (Button) findViewById(R.id.butlogviafb);

        butFinSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        butLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
        //performRequest("ksiazki?class_nr=I+gimnazjum");
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void performRequest(String user) {

        //FollowersRequest request = new FollowersRequest(user);
        BooksRequest request = new BooksRequest("ksiazki?class_nr=I+gimnazjum");
        lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListBooksRequestListener());
    }

    //inner class of your spiced Activity
    private class ListBooksRequestListener implements RequestListener<BookList> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d("retro", "Failure");
        }

        @Override
        public void onRequestSuccess(BookList books) {
            if (books != null)
                Log.d("retro", "Success" + books.get(5).getClass_nr());
            else
                Log.d("retro", "Succes");

        }
    }
}
