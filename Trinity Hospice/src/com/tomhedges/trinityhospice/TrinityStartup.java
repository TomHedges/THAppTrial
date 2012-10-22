package com.tomhedges.trinityhospice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class TrinityStartup extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trinitystartup);
        
        // start Map
        ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new ImageButton.OnClickListener() {
           public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityMap.class);
                startActivity(intent);
            }
        });
        
        // start Browser - Website Homepage
        ImageButton btnWebsite = (ImageButton) findViewById(R.id.btnWebsite);
        btnWebsite.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityBrowser.class);
                intent.putExtra("TrinityBrowserAddress", "http://www.trinityhospice.org.uk");
                startActivity(intent);
            }
        });
        
        // start Browser - Website Donate
        ImageButton btnDonate = (ImageButton) findViewById(R.id.btnDonate);
        btnDonate.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityBrowser.class);
                intent.putExtra("TrinityBrowserAddress", "http://www.trinityhospice.org.uk/make-single-donation");
                startActivity(intent);
            }
        });
        
        // start Browser - Website Our Care
        ImageButton btnServices = (ImageButton) findViewById(R.id.btnServices);
        btnServices.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityBrowser.class);
                intent.putExtra("TrinityBrowserAddress", "http://www.trinityhospice.org.uk/care");
                startActivity(intent);
            }
        });
        
        // start Browser - Website Campaign
        ImageButton btnCampaign = (ImageButton) findViewById(R.id.btnCampaign);
        btnCampaign.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityBrowser.class);
                intent.putExtra("TrinityBrowserAddress", "http://www.trinityhospice.org.uk/click-a-chick");
                startActivity(intent);
            }
        });
        
        // start twitter?!?
        Button btnTwitter = (Button) findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TrinityStartup.this.getApplication(), TrinityTweets.class);
                startActivity(intent);
            }
        });
    }
}