package uclm.esi.cardroid.mytrips;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * \class MyTripsActivity
 * Activity that displays two instances of
 * ListViewCursorLoaderFragment , corresponding to the trips published by the
 * user and the trips she has subscripted to, on a ViewPager
 */
public class MyTripsActivity extends SessionActivity implements
		ActionBar.TabListener {

	/**
	 * The PagerAdapter that will provide fragments for each of the two primary
	 * sections of the activity. We use a FragmentPagerAdapter derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a FragmentStatePagerAdapter
	 */
	private MyTripsPagerAdapter mMyTripsPagerAdapter;
	private static String[] mPageTitles;

	/**
	 * The ViewPager that will display the two primary sections of the activity,
	 * one at a time.
	 */
	private ViewPager mViewPager;

	/**
	 * Call the layout elements to be set
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrips);

		setupWidgets();

		setupActionBar(R.string.mytrips, true);

		setupViewPager();
	}

	@Override
	protected void setupWidgets() {
		mPageTitles = getResources().getStringArray(R.array.myTrips);

		// Create the adapter that will return a fragment for each of the
		// two primary sections of the activity
		mMyTripsPagerAdapter = new MyTripsPagerAdapter(
				getSupportFragmentManager());
	}

	final private CardroidEventStormListener _mEventStorm = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			_eventStorm._notify(action);
			MyTripsPublishedFragment publishedTrips = ((MyTripsPublishedFragment) mMyTripsPagerAdapter
					.getItem(0));
			publishedTrips.clearAdapterData();
			publishedTrips.updateListAdapterData();
			MyTripsSubscriptedFragment subscriptedTrips = ((MyTripsSubscriptedFragment) mMyTripsPagerAdapter
					.getItem(1));
			subscriptedTrips.clearAdapterData();
			subscriptedTrips.updateListAdapterData();
		}

		@Override
		public void message(MessageTypPrx msg) {
			_eventStorm.message(msg);
		}

		@Override
		public void inactivity() {
			_eventStorm.inactivity();
		}

		@Override
		public void error() {
			_eventStorm.error();
		}
	};

	/**
	 * Setup action bar. In addition to the functionality provided by the super
	 * implementation, set the ActionBar navigation mode to
	 * NAVIGATION_MODE_TABS, which allows the user to navigate between the
	 * Fragment s provided in mViewPager by swipe gestures and tapping the
	 * ActionBar
	 * 
	 * @param title
	 *            Title to set
	 * @param enableHomeButton
	 *            Whether the Home/Up button should be enabled, in order to
	 *            provide hierarchical navigation features
	 * @return A reference to this activity's ActionBar
	 */
	@Override
	protected ActionBar setupActionBar(CharSequence title,
			boolean enableHomeButton) {
		ActionBar actionBar = super.setupActionBar(title, enableHomeButton);

		// Specify that tabs should be displayed in the action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		return actionBar;
	}

	/**
	 * Set up the ViewPager, attaching the adapter and setting up a listener for
	 * when the user swipes between sections
	 */
	private void setupViewPager() {
		final ActionBar actionBar = getActionBar();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mMyTripsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					/*
					 * When swiping different activity sections, select the
					 * corresponding tab via its positional index
					 */
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		/*
		 * For each of the sections in the activity, add a tab to the action bar
		 */
		for (int i = 0; i < mMyTripsPagerAdapter.getCount(); i++) {
			/*
			 * Add a tab, setting its text to the page title defined by the
			 * adapter. Also specify this FragmentActivity, which implements the
			 * TabListener interface, as the listener for when this tab is
			 * selected
			 */
			actionBar.addTab(actionBar.newTab()
					.setText(mMyTripsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	/**
	 * Hide the given tab
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	/**
	 * Show the given tab
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	/**
	 * Probably ignore this event
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	/**
	 * \class MyTripsPagerAdapter A FragmentPagerAdapter that returns a fragment
	 * corresponding to one of the primary sections of the activity
	 */
	public static class MyTripsPagerAdapter extends FragmentPagerAdapter {

		private String[] titles;

		public MyTripsPagerAdapter(FragmentManager fm) {
			super(fm);
			titles = mPageTitles;
		}

		/**
		 * Return the Fragment associated with a specified position.
		 */
		@Override
		public Fragment getItem(int i) {
			Fragment fragment;
			switch (i) {
			case 0:
				fragment = new MyTripsPublishedFragment();
				return fragment;
			case 1:
				fragment = new MyTripsSubscriptedFragment();
				return fragment;
			default:
				return null;
			}
		}

		/**
		 * Return the number of views available.
		 */
		@Override
		public int getCount() {
			return titles.length;
		}

		/**
		 * This method may be called by the ViewPager to obtain a title string
		 * to describe the specified page. This method may return null
		 * indicating no title for this page. The default implementation returns
		 * null.
		 * 
		 * @param position
		 *            The position of the title requested
		 * @return A title for the requested page
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return _mEventStorm;
	}

	@Override
	public boolean replayEvents() {
		// return true;
		return false;
	}
}
