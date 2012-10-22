package com.tomhedges.trinityhospice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrinityTweets extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trinitytweetsmain);
        
        ArrayList<Tweet> tweets = getTweets();
        
        ListView listView = (ListView) findViewById(R.id.ListViewId);
        listView.setAdapter(new UserItemAdapter(this, R.layout.trinitytweetslistview, tweets));
    }

	public class UserItemAdapter extends ArrayAdapter<Tweet> {
		private ArrayList<Tweet> tweets;

		public UserItemAdapter(Context context, int textViewResourceId, ArrayList<Tweet> tweets) {
			super(context, textViewResourceId, tweets);
			this.tweets = tweets;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.trinitytweetslistview, null);
			}

			Tweet tweet = tweets.get(position);
			if (tweet != null) {
				TextView username = (TextView) v.findViewById(R.id.username);
				TextView message = (TextView) v.findViewById(R.id.message);
				TextView date = (TextView) v.findViewById(R.id.date);
				ImageView image = (ImageView) v.findViewById(R.id.avatar);

				if (username != null) {
					username.setText(tweet.username);
				}

				if(message != null) {
					message.setText(tweet.message);
				}

				if(message != null) {
					date.setText(tweet.date);
				}
				
				if(image != null) {
					image.setImageBitmap(getBitmap(tweet.image_url));
				}
			}
			return v;
		}
	}

	public Bitmap getBitmap(String bitmapUrl) {
		try {
			URL url = new URL(bitmapUrl);
			return BitmapFactory.decodeStream(url.openConnection() .getInputStream()); 
		}
		catch(Exception ex) {return null;}
	}
	
	public ArrayList<Tweet> getTweets() {
		//String searchUrl = "http://twitter.com/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=" 
		//	+ searchTerm + "&count=" + page;
		
		String TweetString = "http://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=trinityhospice";
		
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		HttpClient client = new  DefaultHttpClient();
		HttpGet get = new HttpGet(TweetString);
        
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String responseBody = null;
		try{
			responseBody = client.execute(get, responseHandler);
			Toast.makeText(TrinityTweets.this, "Online Load OK", 1000000).show();
		    
	        try {
	        	FileOutputStream saveTweets = openFileOutput("trinityhospicetweets.dat", Context.MODE_PRIVATE);
	        	OutputStreamWriter osw = new OutputStreamWriter(saveTweets);
	        	osw.write(responseBody);
	        	osw.flush();
	            osw.close();
	            
	        } catch (IOException e) {
		        Toast.makeText(TrinityTweets.this, "error saving", 1000000).show();
			}
		}catch(Exception ex) {
			try {	
	        	FileInputStream openTweets = openFileInput("trinityhospicetweets.dat");
	        	InputStreamReader isr = new InputStreamReader(openTweets);
	            /* Prepare a char-Array that will
	            * hold the chars we read back in. */
	            char[] inputBuffer = new char[16384];
	            // Fill the Buffer with data from the file
	            isr.read(inputBuffer);
	            // Transform the chars to a String
	            responseBody = new String(inputBuffer);
	            isr.close();
	            
	            Toast.makeText(TrinityTweets.this, "Unable to update Trinity Hospice tweets via the internet.\nWill try to update again on next visit to this page.", 1000000).show();
		    } catch (IOException e) {
		    	Toast.makeText(TrinityTweets.this, "NO ONLINE. NO FILE. FAIL", 1000000).show();
			}
	        Toast.makeText(TrinityTweets.this, ex.toString(), 1000000).show();
		}
        
		responseBody="{\"tweetobject\": {\"tweets\":" + responseBody + "} }";

        try {
        	JSONObject jObject = new JSONObject(responseBody);
			JSONObject trinitytweetsObject = jObject.getJSONObject("tweetobject");
			JSONArray trinitytweetsArray = trinitytweetsObject.getJSONArray("tweets");
	
			for (int i = 0; i < trinitytweetsArray.length(); i++) {
	
				//parse location details
			    String strText = trinitytweetsArray.getJSONObject(i).getString("text").toString();
			    String strDate = trinitytweetsArray.getJSONObject(i).getString("created_at").toString();

			    String strUser = trinitytweetsArray.getJSONObject(i).getString("user").toString();
			    strUser="{\"tweetobject\": {\"tweets\": [" + strUser + "] } }";
	        	JSONObject jObjectUser = new JSONObject(strUser);
				JSONObject trinitytweetsObjectUser = jObjectUser.getJSONObject("tweetobject");
				JSONArray trinitytweetsArrayUser = trinitytweetsObjectUser.getJSONArray("tweets");

			    String strName = trinitytweetsArrayUser.getJSONObject(0).getString("name").toString();
			    String strURL = trinitytweetsArrayUser.getJSONObject(0).getString("profile_image_url").toString();
			    
			    
			    try {
			    	String strUserReTweet = trinitytweetsArray.getJSONObject(i).getString("retweeted_status").toString();

				    strUserReTweet="{\"tweetobject\": {\"tweets\": [" + strUserReTweet + "] } }";
		        	JSONObject jObjectUserRT = new JSONObject(strUserReTweet);
					JSONObject trinitytweetsObjectUserRT = jObjectUserRT.getJSONObject("tweetobject");
					JSONArray trinitytweetsArrayUserRT = trinitytweetsObjectUserRT.getJSONArray("tweets");

					strText = trinitytweetsArrayUserRT.getJSONObject(0).getString("text").toString();
					strDate = trinitytweetsArrayUserRT.getJSONObject(0).getString("created_at").toString();

			    	String strUserReTweetU = trinitytweetsArrayUserRT.getJSONObject(0).getString("user").toString();
				    strUserReTweetU="{\"tweetobject\": {\"tweets\": [" + strUserReTweetU + "] } }";
		        	JSONObject jObjectUserRTU = new JSONObject(strUserReTweetU);
					JSONObject trinitytweetsObjectUserRTU = jObjectUserRTU.getJSONObject("tweetobject");
					JSONArray trinitytweetsArrayUserRTU = trinitytweetsObjectUserRTU.getJSONArray("tweets");
					
				    strName = trinitytweetsArrayUserRTU.getJSONObject(0).getString("screen_name").toString() + " (Retweeted by " + strName + ")";
				    strURL = trinitytweetsArrayUserRTU.getJSONObject(0).getString("profile_image_url").toString();
		    	} catch (Exception e) {
		    	}
		    	
				Tweet tweet = new Tweet(
						strName,
						strText,
						strDate,
						strURL
						);
				tweets.add(tweet);
			}

		} catch (JSONException e) {
	        Toast.makeText(TrinityTweets.this, "error tweeting: " + e, 1000000).show();
		}
		return tweets;
	}
	
	public class Tweet {
		public String username;
		public String message;
		public String date;
		public String image_url;
		
		public Tweet(String username, String message, String date, String url) {
			this.username = username;
			this.message = message;
			this.date = date;
			this.image_url = url;
		}
	}
}