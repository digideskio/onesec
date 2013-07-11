package com.example.onesec_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.cake.Batter;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.http.TokenManager;
import com.example.onesec.impl.second.Second;
import com.example.onesec_app.adapters.SecondsCursorAdapter;

public class ViewSecondsActivity extends Activity {

//	private ListView secondsListView;
	private Batter batter;
	private boolean selectorOn;
//	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("My Seconds");
		setContentView(R.layout.activity_view_seconds);
		// Show the Up button in the action bar.
		setupActionBar();
		
		batter = new Batter();
		selectorOn = false;
		Button selectButton = (Button)findViewById(R.id.select_seconds);
		selectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectorOn = !selectorOn;
			}
		});
		
		showSeconds();
		
		Log.v("onCreate", "about to handle intent");
		handleIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		Log.v("onNewIntent", "handling intent");
		handleIntent(intent);
	}
	
	@Override
	public boolean onSearchRequested() {
//	    pauseSomeStuff();
		Log.v("onSearchRequested", "here!");
	    return super.onSearchRequested();
	}
	
	private void handleIntent(Intent intent) {
		Log.v("handleIntent", "intent action is " + intent.getAction());
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String sprinkle = intent.getStringExtra(SearchManager.QUERY);
			Log.v("handleIntent", "searching for sprinkle " + sprinkle);
			// Get Seconds with queried sprinkle
		}
	}
	
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_seconds, menu);
        
        // Associate searchable configuration with the SearchView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	Log.v("onCreateOptionsMenu", "searchable");
        	SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        	SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        	searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			// show settings
			break;
		case R.id.action_signout:
			signout();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	/*
	 * Loads all Seconds
	 */
	private void showSeconds() {
		final Cursor c = Kitchen.getSecondsCursor(this);
		
		String[] fromColumns = {
				KitchenContract.SecondEntry.COLUMN_NAME_DATE,
				KitchenContract.SecondEntry.COLUMN_NAME_THUMBNAIL_PATH };
		int[] toViews = {
				R.id.secondDate,
				R.id.secondThumbnail };
		c.moveToFirst();
		SecondsCursorAdapter adapter = new SecondsCursorAdapter(this, 
		        R.layout.listview_seconds_row, c, fromColumns, toViews, 0);
		ListView listView = (ListView)findViewById(R.id.secondsListView);
		listView.setAdapter(adapter);
		//listView.setLongClickable(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				c.moveToPosition(pos);
				Second second = new Second(c);
				if(selectorOn)
				{
					//SecondsCursorAdapter adapter = (SecondsCursorAdapter) adapterView.getAdapter();
					batter.addSecond(second);
				}
				else{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(second.getVideoUri(), "video/mp4");
					startActivity(intent);
				}
			}
		});
	}
	
	public void bakeCake(View v) {
		Cake cake = batter.bake(this);
		
		// Save cake and its batter locally
		//Kitchen.saveCakeToLocalDb(this, cake);
		Kitchen.writeBatterToFile(this, batter);
		Kitchen.saveCakeToLocalDb(this, cake);
		
		Intent intent = new Intent(this, NewCakeActivity.class);
		intent.putExtra("cake_uid", cake.getId());
    	startActivity(intent);
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
    private void signout() {
    	TokenManager.forgetToken(this);
    	Log.v("signout", "forgetting token");
    }

}
