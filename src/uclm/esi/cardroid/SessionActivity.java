package uclm.esi.cardroid;

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
import uclm.esi.cardroid.util.MyFragmentActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * \class SessionActivity
 * Convenience class that extends the MyFragmentActivity class, by adding some 
 * methods that most of the activity classes in the project working in the 
 * context of an user session will make use of. The most relevant features are the
 * inclusion of the session and query controllers, which will be used by any
 * activity created after a session has been established
 */
public abstract class SessionActivity extends MyFragmentActivity {

	protected static final int DIALOG_ERROR = 0;
	protected static final int DIALOG_FATAL = 1;
	protected static final int DIALOG_NEXT = 2;

	protected SessionController _sessionController;
	protected QueryController _queryController;

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

		_sessionController.setSubscriber(getCardroidEventStormListener(),
				replayEvents());
	}

	@Override
	protected void onPause() {
		_sessionController.clearSubscriber();

		super.onPause();
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
			// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is NOT part of this app's stack, so create a
				// new task
				// when navigating up, with a synthesized back stack
				TaskStackBuilder.create(this)
				// Add all of this activity's parents to the back stack
						.addNextIntentWithParentStack(upIntent)
						// Navigate up to the closest parent
						.startActivities();
			} else {
				// This activity is part of this app's task, so simply
				// navigate up to the logical parent activity
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** A default CardroidEventStormListener , providing the implementation
	 * for the methods called upon the arrival of a new event notification 
	 * which most of the SessionActivity subclasses will make use of.
	 * Whenever a subclass of SessionActivity needs to provide a different
	 * implementation for this feature, it should override this data member.
	 * A reference to the current implementation of this field can be 
	 * retrieved by calling the getCardroidEventStormListener() abstract method
	 */
	final protected CardroidEventStormListener _eventStorm = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			TaskStackBuilder stackBuilder = TaskStackBuilder
					.create(SessionActivity.this);
			Intent resultIntent;
			resultIntent = new Intent(SessionActivity.this,
					TripDetailsActivity.class);
			resultIntent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, action
					.getActivityTrip().getTripId());
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Adds the back stack
			stackBuilder.addParentStack(TripDetailsActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(resultIntent);

			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					SessionActivity.this)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle(getString(R.string.notification))
					.setContentText(action._toString())
					.setContentIntent(resultPendingIntent).setAutoCancel(true);

			// Sets an ID for the notification, so it can be updated
			int notifyID = 1;
			// Because the ID remains unchanged, the existing notification is
			// updated
			notificationManager.notify(notifyID, notificationBuilder.build());
		}

		@Override
		public void message(MessageTypPrx msg) {
			TaskStackBuilder stackBuilder = TaskStackBuilder
					.create(SessionActivity.this);
			Intent resultIntent;
			resultIntent = new Intent(SessionActivity.this,
					MessageTalkActivity.class);
			String speakerEmail = msg.getUser1().getEmail()
					.equals(_sessionController.getMyLogin()) ? msg.getUser2()
					.getEmail() : msg.getUser1().getEmail();
			resultIntent.putExtra(MessageTalkActivity.EXTRA_USER_SPEAKER,
					speakerEmail);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Adds the back stack
			stackBuilder.addParentStack(MessageTalkActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(resultIntent);

			// Gets a Pending Intent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					1, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					SessionActivity.this)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle(getString(R.string.message))
					.setContentText(msg._toString())
					.setContentIntent(resultPendingIntent).setAutoCancel(true);

			// Sets an ID for the notification, so it can be updated
			int notifyID = 2;
			// Because the ID remains unchanged, the existing notification is
			// updated
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

	/**
	 * @return A reference to the object in this class implementing the methods
	 * 			called upon the arrival of a new event notification
	 */
	abstract public CardroidEventStormListener getCardroidEventStormListener();

	/**
	 * @return Whether the events notifications received by this class should
	 * 			be replayed after their occurrence have been processed by
	 * 			this SessionActivity 's CardroidEventStormListener
	 */
	abstract public boolean replayEvents();

	/**
	 * Show a alert dialog
	 * @param id The id identifying the type of dialog: DIALOG_ERROR, 
	 * 				DIALOG_FATAL, DIALOG_NEXT
	 * @param msg An optional message to be displayed in the dialog fragment
	 */
	protected void showAlertDialog(int id, String msg) {
		/*
		 * DialogFragment.show() will take care of adding the fragment in a
		 * transaction. We also want to remove any currently showing dialog, so
		 * make our own transaction and take care of that there.
		 */
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog
		DialogFragment newFragment = MyDialogFragment.newInstance(id, msg);
		newFragment.show(ft, "dialog");
	}

	/**
	 * Show a alert dialog
	 * @param id The id identifying the type of dialog: DIALOG_ERROR, 
	 * 				DIALOG_FATAL, DIALOG_NEXT
	 */
	protected void showAlertDialog(int id) {
		showAlertDialog(id, null);
	}

	/**
	 * Called upon the dismissal of the recoverable error dialog
	 */
	public void onDialogErrorPositiveClick() {
		_queryController.clearLastError();
	}

	/**
	 * Called upon the dismissal of the fatal dialog.
	 * Destroy the session and finish this Activity
	 */
	public void onDialogFatalPositiveClick() {
		CardroidApp app = (CardroidApp) getApplication();
		app.logout();
		finish();
	}

	/**
	 * \class MyDialogFragment
	 * DialogFragment allowing the configuration of the Dialog according to an
	 * integer identifier (the constant values DIALOG_ERROR, DIALOG_FATAL, 
	 * and DIALOG_NEXT) and an optional textual message
	 */
	public static class MyDialogFragment extends DialogFragment {

		private static final String LAST_ERROR_ARG = "LAST_ERROR";

		/*
		 * Create a new instance of MyDialogFragment, providing "id" and
		 * "lastError" (if present) as arguments
		 */
		public static MyDialogFragment newInstance(int id, String lastError) {
			MyDialogFragment f = new MyDialogFragment();

			// Supply id input as an argument
			Bundle args = new Bundle();
			args.putInt("id", id);
			// If lastError input is present (has a non-null value), supply it
			// as an argument
			if (lastError != null)
				args.putString(LAST_ERROR_ARG, lastError);
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
						.getString(LAST_ERROR_ARG));
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
				fatalBuilder.setMessage(R.string.loginAgain);
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
