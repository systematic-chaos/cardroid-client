package uclm.esi.cardroid;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.myactivity.MyActivityActivity;
import uclm.esi.cardroid.mymessages.MyMessagesActivity;
import uclm.esi.cardroid.myprofile.MyProfileActivity;
import uclm.esi.cardroid.mytrips.MyTripsActivity;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.newtrip.NewTripActivity;
import uclm.esi.cardroid.search.SearchActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Class RootMenuActivity
 * Application startup menu. Provide direct access to the
 * application modules.
 */
public class RootMenuActivity extends SessionActivity {

	/**
	 * Extend the parent class' onCreate, setting the content view with the
	 * proper layout resource and setting the action bar with the activity's
	 * title
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_root_menu);
		setupActionBar(R.string.menu, false);
	}

	@Override
	protected void setupWidgets() {
	}

	/**
	 * Throw an Intent to the SearchActivity class. The FLAG_ACTIVITY_CLEAR_TOP
	 * flag is set, which ensures that no more than one instance of such a class
	 * will be created for the current task stack.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void search(View v) {
		Intent intent = new Intent(RootMenuActivity.this, SearchActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Throw an Intent to the MyTripsActivity class. The FLAG_ACTIVITY_CLEAR_TOP
	 * flag is set, which ensures that no more than one instance of such a class
	 * will be created for the current task stack.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void myTrips(View v) {
		Intent intent = new Intent(RootMenuActivity.this, MyTripsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Throw an Intent to the MyActivityActivity class. The
	 * FLAG_ACTIVITY_CLEAR_TOP flag is set, which ensures that no more than one
	 * instance of such a class will be created for the current task stack.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void myActivity(View v) {
		Intent intent = new Intent(RootMenuActivity.this,
				MyActivityActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Throw an Intent to the MyMessagesActivity class. The
	 * FLAG_ACTIVITY_CLEAR_TOP flag is set, which ensures that no more than one
	 * instance of such a class will be created for the current task stack.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void myMessages(View v) {
		Intent intent = new Intent(RootMenuActivity.this,
				MyMessagesActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Throw an Intent to the NewTripActivity class.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void newTrip(View v) {
		Intent intent = new Intent(RootMenuActivity.this, NewTripActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Throw an Intent to the MyProfileActivity class. The
	 * FLAG_ACTIVITY_CLEAR_TOP flag is set, which ensures that no more than one
	 * instance of such a class will be created for the current task stack.
	 * 
	 * @param v
	 *            If this method was invoked from a View via a
	 *            View.OnClickListener, v references the view which originated
	 *            the invocation. Otherwise, it should be set to null
	 */
	public void myProfile(View v) {
		Intent intent = new Intent(RootMenuActivity.this,
				MyProfileActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		_sessionController.destroy();

		super.onBackPressed();
	}

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return _eventStorm;
	}

	@Override
	public boolean replayEvents() {
		return true;
	}
}
