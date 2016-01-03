package uclm.esi.cardroid.newtrip;

import uclm.esi.cardroid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * \class NewTripActivity
 * Simple class that allows the user to choose between
 * publish a new trip or request a new trip. Depending on the result of this
 * choice, one of the NewTripOfferActivity and NewTripRequestActivity activities
 * will be launched
 */
public class NewTripActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtrip_menu);
	}

	/**
	 * Launch a new NewTripOfferActivity
	 */
	public void newTripOffer(View v) {
		startActivity(new Intent(NewTripActivity.this,
				NewTripOfferActivity.class));
		finish();
	}

	/**
	 * Launch a new NewTripRequestActivity
	 */
	public void newTripRequest(View v) {
		startActivity(new Intent(NewTripActivity.this,
				NewTripRequestActivity.class));
		finish();
	}
}
