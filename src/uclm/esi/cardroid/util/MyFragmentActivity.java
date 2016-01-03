package uclm.esi.cardroid.util;

import uclm.esi.cardroid.R;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * \class MyFragmentActivity
 * Convenience class that extends the default FragmentActivity class, 
 * by adding some methods that most of the activity classes in the project 
 * will make use of. The most relevant features are the inclusion of a 
 * connectivity change receiver, so extending classes will be able to know 
 * if they can safely try to retrieve remote data, and the application's 
 * action bar, which provides direct access to the different modules of the 
 * application.
 */
public abstract class MyFragmentActivity extends FragmentActivity {

	/**
	 * Extension of the FragmentActivity 's onCreate, including a call to the
	 * abstract method setupLayout and the register of mNetworkChangeReceiver
	 * when the activity is started.
	 * 
	 * @param savedInstanceState
	 *            A previously saved state snapshot of the activity, if it is
	 *            been recreated
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Register mNetworkChangeReceiver to receive changes in the network
		// connectivity status
		registerReceiver(mNetworkChangeReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	/**
	 * When the activity is resumed, register mNetworkChangeReceiver to receive
	 * changes in the network connectivity status
	 */
	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(mNetworkChangeReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	/**
	 * When the activity is paused, unregister mNetworkChangeReceiver, since the
	 * activity is not visible
	 */
	@Override
	protected void onPause() {
		unregisterReceiver(mNetworkChangeReceiver);

		super.onPause();
	}

	/// Handler for received intents for the CONNECTIVITY_CHANGE event
	protected BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean connected = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false) ? false
					: isConnected();
			// Display the connection status
			if (connected) {
				onConnected();
			} else {
				onDisconnected();
			}
		}
	};

	/**
	 * When the activity's Internet connectivity has been established, launch an
	 * informative toast
	 */
	protected void onConnected() {
		Toast.makeText(MyFragmentActivity.this, R.string.connected,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * When the activity's Internet connectivity has been interrupted, launch an
	 * informative toast
	 */
	protected void onDisconnected() {
		Toast.makeText(MyFragmentActivity.this, R.string.disconnected,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Returns details about the currently active default data network.
	 * 
	 * @return A NetworkInfo object for the current default network or null if
	 *         no network default network is currently active
	 */
	public NetworkInfo getActiveNetworkInfo() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * Indicates whether network connectivity exists or is in the process of
	 * being established.
	 * 
	 * @return true if network connectivity exists or is in the process of being
	 *         established, false otherwise
	 */
	public boolean isConnected() {
		NetworkInfo activeNetwork = getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public boolean isWifiConnected() {
		NetworkInfo activeNetwork = getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting()
				&& activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * Setup action bar
	 * 
	 * @param title
	 *            Title to set
	 * @param enableHomeButton
	 *            Whether the Home/Up button should be enabled, in order to
	 *            provide hierarchical navigation features
	 * @return A reference to this activity's ActionBar
	 */
	protected ActionBar setupActionBar(CharSequence title,
			boolean enableHomeButton) {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(title);
		actionBar.setDisplayUseLogoEnabled(true);

		/* Specify the enabled status of the Home/Up button, depending of the *
		 * availability of a hierarchical parent activity, which is specified *
		 * by the enableHomeButton parameter								  */
		actionBar.setHomeButtonEnabled(enableHomeButton);

		return actionBar;
	}

	/**
	 * Setup action bar
	 * 
	 * @param titleRes
	 *            Resource ID of title string to set
	 * @param enableHomeButton
	 *            Whether the Home/Up button should be enabled, in order to
	 *            provide hierarchical navigation features
	 * 
	 * @return A reference to this activity's ActionBar
	 */
	protected ActionBar setupActionBar(int titleRes, boolean enableHomeButton) {
		return setupActionBar(getString(titleRes), enableHomeButton);
	}

	abstract protected void setupWidgets();
}
