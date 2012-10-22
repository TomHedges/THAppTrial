package com.tomhedges.trinityhospice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class TrinityBrowser extends Activity {
	private WebView wvBrowser;

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    String BrowserAddress;
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trinitybrowser);
        
        Bundle extras = getIntent().getExtras(); {
        	BrowserAddress = extras.getString("TrinityBrowserAddress");
        }
        
        // Find the Views in the layout file
        wvBrowser = (WebView) findViewById(R.id.wvBrowser);
		wvBrowser.loadUrl(BrowserAddress);
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.browsermenu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
        case R.id.BrowserBack:
            Toast.makeText(TrinityBrowser.this, "Menu Item 1 was pressed", 3000).show();
            return true;
        case R.id.BrowserOpen:
            Toast.makeText(TrinityBrowser.this, "Menu Item 2 was pressed", 3000).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}