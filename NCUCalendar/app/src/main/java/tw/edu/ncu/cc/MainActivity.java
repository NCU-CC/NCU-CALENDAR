package tw.edu.ncu.cc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.android.volley.VolleyError;
import com.wuman.android.auth.OAuthManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tw.edu.ncu.cc.NCUCalendar.AllEvents;
import tw.edu.ncu.cc.NCUCalendar.Categories;
import tw.edu.ncu.cc.NCUCalendar.EventConfig;
import tw.edu.ncu.cc.NCUCalendar.NCUCalendar;
import tw.edu.ncu.cc.NCUCalendar.ResponseListener;
import tw.edu.ncu.cc.Oauth.AndroidOauthBuilder;
import tw.edu.ncu.cc.adapter.NavDrawerListAdapter;
import tw.edu.ncu.cc.model.NavDrawerItem;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private Fragment fragment = null;
	private boolean ShowDrawer = true;

	private boolean auth = false;
	private CookieManager cookieManager;
	public static NCUCalendar NCUCalendar;
	public static ArrayList<String> CategoryList;
	public static List<WeekViewEvent> eventslist;
	public static ArrayList<String> ComparedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CookieSyncManager.createInstance(this);
		cookieManager = CookieManager.getInstance();

		NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.calendar);
			builder.setTitle(R.string.network_unreachable);
			builder.setMessage(R.string.open_network_message);
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.setPositiveButton(R.string.network_settings, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					startActivity(new Intent(Settings.ACTION_SETTINGS));
					finish();
				}
			});
			builder.show();
		} else {
			EventConfig EventConfig = new EventConfig("http://140.115.3.188/calendar/v1/", getString(R.string.language));

			AndroidOauthBuilder oauthBuilder = AndroidOauthBuilder.initContext(this)
					.clientID(getString(R.string.oauth_id))
					.clientSecret(getString(R.string.oauth_secret))
					.callback(getString(R.string.callback))
					.scope("calendar.event.read", "calendar.event.write")
					.fragmentManager(getFragmentManager());
			OAuthManager oAuthManager = oauthBuilder.build();
			NCUCalendar = new NCUCalendar(EventConfig, oAuthManager, this);

			new AuthTask().execute();
		}


		mTitle = mDrawerTitle = getTitle();
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.app_name,
				R.string.app_name
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clickMenuItem(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_eventcreate:
				Intent intent = new Intent(this, EventCreate.class);
				startActivity(intent);
				break;
			default:
				finish();
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (!ShowDrawer) {
			menu.findItem(R.id.menu_eventcreate).setVisible(ShowDrawer);
		} else {
			menu.findItem(R.id.menu_eventcreate).setVisible(!drawerOpen);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public void displayView(int position) {
		switch (position) {
			case 0:
				fragment = new DayCalendar();
				ShowDrawer = true;
				break;
			case 1:
				fragment = new WeekCalendar();
				ShowDrawer = true;
				break;
			case 2:
				fragment = new MonthlyCalendar();
				ShowDrawer = true;
				break;
			case 3:
				fragment = new PatchFragment();
				ShowDrawer = false;
				break;
			case 4:
				fragment = new SystemFragment();
				ShowDrawer = false;
				break;

			default:
				break;
		}

		if (fragment != null && auth) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class AuthTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			cookieManager.setCookie("portal.ncu.edu.tw", "JSESSIONID=");
			try {
				NCUCalendar.initAccessToken();
			} catch (Exception e) {
				finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			getCategories();
			auth = true;
		}
	}

	private void getCategories() {
		NCUCalendar.getCategories(new ResponseListener<Categories>() {
			public void onResponse(Categories responses) {
				int i = 0;
				CategoryList = new ArrayList<String>();
				Categories.Category[] categories = responses.getCategories();
				for (Categories.Category category : categories) {
					if (category.getAddible()) {
						CategoryList.add(i, category.getName());
						i++;
					}
				}
				getAllEvents();
			}

			@Override
			public void onError(VolleyError error) {
			}
		});
	}

	public void getAllEvents() {
		Calendar c = Calendar.getInstance();
		Calendar sCalendar = Calendar.getInstance();
		Calendar eCalendar = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH) + 1;
		if (mMonth >= 2 && mMonth <= 6) {
			sCalendar.set(Calendar.MONTH, 1);
			sCalendar.set(Calendar.DAY_OF_MONTH, 1);
			eCalendar.set(Calendar.YEAR, mYear + 1);
			eCalendar.set(Calendar.MONTH, 0);
			eCalendar.set(Calendar.DAY_OF_MONTH, 31);
		} else {
			sCalendar.set(Calendar.MONTH, 6);
			sCalendar.set(Calendar.DAY_OF_MONTH, 1);
			eCalendar.set(Calendar.YEAR, mYear + 1);
			eCalendar.set(Calendar.MONTH, 5);
			eCalendar.set(Calendar.DAY_OF_MONTH, 31);
		}
		String myFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
		NCUCalendar.getAllEvents(new ResponseListener<AllEvents>() {
			public void onResponse(AllEvents responses) {
				int i = 0;
				eventslist = new ArrayList<WeekViewEvent>();
				ComparedList = new ArrayList<String>();
				AllEvents.Events[] events = responses.getEvents();
				for (AllEvents.Events event : events) {
					ComparedList.add(i, event.getId());
					String[] StartTime = event.getStart().split("T");
					String[] StartTime_YYYYMMDD = StartTime[0].split("-");
					String[] StartTime_HHMM = StartTime[1].split(":");

					Calendar startTime = Calendar.getInstance();
					startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(StartTime_HHMM[0]));
					startTime.set(Calendar.MINUTE, Integer.parseInt(StartTime_HHMM[1]));
					startTime.set(Calendar.YEAR, Integer.parseInt(StartTime_YYYYMMDD[0]));
					startTime.set(Calendar.MONTH, Integer.parseInt(StartTime_YYYYMMDD[1]) - 1);
					startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(StartTime_YYYYMMDD[2]));

					String[] EndTime = event.getEnd().split("T");
					String[] EndTime_YYYYMMDD = EndTime[0].split("-");
					String[] EndTime_HHMM = EndTime[1].split(":");

					Calendar endTime = Calendar.getInstance();
					endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(EndTime_HHMM[0]));
					endTime.set(Calendar.MINUTE, Integer.parseInt(EndTime_HHMM[1]));
					endTime.set(Calendar.MONTH, Integer.parseInt(EndTime_YYYYMMDD[1]) - 1);
					endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(EndTime_YYYYMMDD[2]));

					WeekViewEvent eachevent = new WeekViewEvent(i, getEventTitle(event, StartTime_HHMM, EndTime_HHMM), startTime, endTime);
					int EventColorType = i % 4;
					switch (EventColorType) {
						case 0:
							eachevent.setColor(getResources().getColor(R.color.event_color_01));
							break;
						case 1:
							eachevent.setColor(getResources().getColor(R.color.event_color_02));
							break;
						case 2:
							eachevent.setColor(getResources().getColor(R.color.event_color_03));
							break;
						case 3:
							eachevent.setColor(getResources().getColor(R.color.event_color_04));
							break;
						default:
							eachevent.setColor(getResources().getColor(R.color.event_color_01));
							break;
					}
					eventslist.add(eachevent);
					i++;
				}
				displayView(0);
			}

			@Override
			public void onError(VolleyError error) {
				if (error != null) {
					if (error.networkResponse.statusCode == 404) {
						Toast.makeText(MainActivity.this, "您尚未新增任何活動。", Toast.LENGTH_LONG).show();
						eventslist = new ArrayList<WeekViewEvent>();
						displayView(0);
					} else {
						Toast.makeText(MainActivity.this, "活動資料載入失敗！\n請確認您的網路連線穩定，並重新登入再試一次。", Toast.LENGTH_LONG).show();
						NCUCalendar.deleteAccessToken();
					}
				} else {
					Toast.makeText(MainActivity.this, "伺服器出現錯誤，請重新開啟中大行事曆。", Toast.LENGTH_LONG).show();
				}
			}
		}, sdf.format(sCalendar.getTime()), sdf.format(eCalendar.getTime()));

		EventDisplay.mainRefrence = this;
		EventCreate.mainRefrence = this;
		EventUpdate.mainRefrence = this;
		MonthlyCalendar.mainRefrence = this;
	}

	private String getEventTitle(AllEvents.Events event, String[] StartTime_HHMM, String[] EndTime_HHMM) {
		return String.format(event.getSummary() +
				"\n時間：" + StartTime_HHMM[0] + ":" + StartTime_HHMM[1] + " ~ " + EndTime_HHMM[0] + ":" + EndTime_HHMM[1] +
				"\n地點：" + event.getLocation());
	}
}