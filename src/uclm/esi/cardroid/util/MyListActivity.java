package uclm.esi.cardroid.util;

import uclm.esi.cardroid.CardroidApp;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.RootMenuActivity;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.myactivity.MyActivityActivity;
import uclm.esi.cardroid.mymessages.MessageTalkActivity;
import uclm.esi.cardroid.mymessages.MyMessagesActivity;
import uclm.esi.cardroid.myprofile.MyProfileActivity;
import uclm.esi.cardroid.mytrips.MyTripsActivity;
import uclm.esi.cardroid.mytrips.TripDetailsActivity;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryController;
import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.network.client.SessionController.SessionListener;
import uclm.esi.cardroid.newtrip.NewTripActivity;
import uclm.esi.cardroid.search.SearchActivity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * \class MyListActivity
 * Convenience class extending ListActivity and providing the functionalities 
 * available in SessionActivity (some of which are inherited from its parent 
 * class, MyFragmentActivity ). This class also provides an ScrollListener and 
 * implements the OnItemClickListener interface, thus forcing classes 
 * subclassing MyListActivity to provide an implementation of the methods 
 * required by this interface
 */
public abstract class MyListActivity extends ListActivity implements
		OnItemClickListener {
	protected static final int DIALOG_ERROR = 0;
	protected static final int DIALOG_FATAL = 1;
	protected static final int DIALOG_NEXT = 2;

	protected SessionController _sessionController;
	protected QueryController _queryController;

	protected ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_loader);

		mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoader);

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnItemClickListener(this);
		getListView().setOnScrollListener(scrollListener);

		setupListView();

		/* Register mNetworkChangeReceiver to receive changes in the network *
		 * connectivity status												 */
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

		CardroidApp app = (CardroidApp) getApplication();
		_sessionController = app.getSessionController();
		if (_sessionController == null) {
			finish();
			return;
		}

		_sessionController.setSessionListener(new SessionListener() {
			public void onDestroy() {
				showAlertDialog(DIALOG_FATAL, _queryController.getLastError());
				showAlertDialog(DIALOG_FATAL);
			}
		});

		_queryController = _sessionController.getCurrentQuery();

		_sessionController.setSubscriber(getCardroidCallbackListener(),
				replayEvents());

		registerReceiver(mNetworkChangeReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	/**
	 * When the activity is paused, unregister mNetworkChangeReceiver, since the
	 * activity is not visible
	 */
	@Override
	protected void onPause() {
		_sessionController.clearSubscriber();

		unregisterReceiver(mNetworkChangeReceiver);
		super.onPause();
	}

	/**
	 * Set a callback listener which will initialize (if necessary) and update
	 * the adapter's data upon the availability of the query results
	 */
	abstract protected void setupListView();

	/**
	 *  This ScrollListener attempts to query for more data when this 
	 *  ListActivity 's scroll reaches its bottom. If such a behavior is not
	 *  desired, this data member can be overridden by subclasses of 
	 *  MyListActivity 
	 */
	protected final OnScrollListener scrollListener = new OnScrollListener() {

		private int currentFirstVisibleItem, currentVisibleItemCount,
				currentTotalItemCount;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			currentFirstVisibleItem = firstVisibleItem;
			currentVisibleItemCount = visibleItemCount;
			currentTotalItemCount = totalItemCount;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (currentFirstVisibleItem + currentVisibleItemCount == currentTotalItemCount
					&& scrollState == SCROLL_STATE_IDLE) {
				// Scroll has completed
				queryMoreData(currentTotalItemCount);
			}
		}
	};

	/**
	 * Query the server's result set for more data
	 * @param position The position of the last entry retrieved from the server
	 */
	abstract protected void queryMoreData(int position);

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
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}

	/**
	 * When the activity's Internet connectivity has been interrupted, launch an
	 * informative toast
	 */
	protected void onDisconnected() {
		Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
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
	 * @return The View displayed by the ListView when it does not have an
	 *         Adapter set or such an Adapter is empty (does not contain any
	 *         elements)
	 */
	public View getEmptyView() {
		return getListView().getEmptyView();
	}

	/**
	 * @param text
	 *            The text to be displayed by the ListView when it does not have
	 *            an Adapter set or such an Adapter is empty (does not contain
	 *            any elements)
	 */
	protected void setEmptyText(CharSequence text) {
		((TextView) getEmptyView().findViewById(R.id.textViewEmpty))
				.setText(text);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_rootmenu:
			intent = new Intent(this, RootMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_search:
			intent = new Intent(this, SearchActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_mytrips:
			intent = new Intent(this, MyTripsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_newtrip:
			intent = new Intent(this, NewTripActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_messages:
			intent = new Intent(this, MyMessagesActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_activity:
			intent = new Intent(this, MyActivityActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_profile:
			intent = new Intent(this, MyProfileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				/* This activity is NOT part of this app's stack, so create a *
				 * new task	when navigating up, with a synthesized back stack */
				TaskStackBuilder.create(this)
				// Add all of this activity's parents to the back stack
						.addNextIntentWithParentStack(upIntent)
						// Navigate up to the closest parent
						.startActivities();
			} else {
				/* This activity is part of this app's task, so simply *
				 * navigate up to the logical parent activity		   */
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	final protected CardroidEventStormListener _callbackListener = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			TaskStackBuilder stackBuilder = TaskStackBuilder
					.create(MyListActivity.this);
			Intent resultIntent;
			resultIntent = new Intent(MyListActivity.this,
					TripDetailsActivity.class);
			resultIntent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, action
					.getActivityTrip().getTripId());

			// Adds the back stack
			stackBuilder.addParentStack(TripDetailsActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(resultIntent);

			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					MyListActivity.this)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle("Cardroid push notification")
					.setContentText(action._toString())
					.setContentIntent(resultPendingIntent).setAutoCancel(true);

			// Sets an ID for the notification, so it can be updated
			int notifyID = 1;

			/* Because the ID remains unchanged, the existing notification *
			 * is updated												   */
			notificationManager.notify(notifyID, notificationBuilder.build());
		}

		@Override
		public void message(MessageTypPrx msg) {
			TaskStackBuilder stackBuilder = TaskStackBuilder
					.create(MyListActivity.this);
			Intent resultIntent;
			resultIntent = new Intent(MyListActivity.this,
					MessageTalkActivity.class);
			String speakerEmail = msg.getUser1().getEmail()
					.equals(_sessionController.getMyLogin()) ? msg.getUser2()
					.getEmail() : msg.getUser1().getEmail();
			resultIntent.putExtra(MessageTalkActivity.EXTRA_USER_SPEAKER,
					speakerEmail);

			// Adds the back stack
			stackBuilder.addParentStack(MessageTalkActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(resultIntent);

			// Gets a Pending Intent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					1, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					MyListActivity.this)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle("Cardroid Incoming Message")
					.setContentText(msg._toString())
					.setContentIntent(resultPendingIntent).setAutoCancel(true);

			// Sets an ID for the notification, so it can be updated
			int notifyID = 2;
			/* Because the ID remains unchanged, the existing notification * 
			 * is updated												   */
			notificationManager.notify(notifyID, notificationBuilder.build());
		}

		@Override
		public void inactivity() {
			showAlertDialog(DIALOG_FATAL);
		}

		@Override
		public void error() {
			showAlertDialog(DIALOG_FATAL);
		}
	};

	abstract public CardroidEventStormListener getCardroidCallbackListener();

	abstract public boolean replayEvents();

	/**
	 * DialogFragment.show() will take care of adding the fragment in a
	 * transaction. We also want to remove any currently showing dialog, so
	 * make our own transaction and take care of that there.
	 */
	protected void showAlertDialog(int id, String msg) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog
		DialogFragment newFragment = MyDialogFragment.newInstance(id, msg);
		newFragment.show(ft, "dialog");
	}

	protected void showAlertDialog(int id) {
		showAlertDialog(id, null);
	}

	public void onDialogErrorPositiveClick() {
		_queryController.clearLastError();
	}

	public void onDialogFatalPositiveClick() {
		CardroidApp app = (CardroidApp) getApplication();
		app.logout();
		finish();
	}

	public static class MyDialogFragment extends DialogFragment {

		/**
		 * Create a new instance of MyDialogFragment, providing "id" and
		 * "lastError" (if present) as arguments
		 */
		public static MyDialogFragment newInstance(int id, String lastError) {
			MyDialogFragment f = new MyDialogFragment();

			// Supply id input as an argument
			Bundle args = new Bundle();
			args.putInt("id", id);
			/* If lastError input is present (has a non-null value), *
			 * supply it as an argument								 */
			if (lastError != null)
				args.putString("lastError", lastError);
			f.setArguments(args);

			return f;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int id = savedInstanceState.getInt("id");

			switch (id) {
			case DIALOG_ERROR:
				AlertDialog.Builder errorBuilder = new AlertDialog.Builder(
						getActivity());
				errorBuilder.setTitle("Error");
				errorBuilder.setMessage(savedInstanceState
						.getString("lastError"));
				errorBuilder.setCancelable(false);
				errorBuilder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((SessionActivity) getActivity())
										.onDialogErrorPositiveClick();
							}
						});
				return errorBuilder.create();

			case DIALOG_FATAL:
				AlertDialog.Builder fatalBuilder = new AlertDialog.Builder(
						getActivity());
				fatalBuilder.setTitle("Error");
				fatalBuilder
						.setMessage("The session was lost. Please login again.");
				fatalBuilder.setCancelable(false);
				fatalBuilder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((SessionActivity) getActivity())
										.onDialogFatalPositiveClick();
							}
						});
				return fatalBuilder.create();
			}

			return null;
		}
	}
}
