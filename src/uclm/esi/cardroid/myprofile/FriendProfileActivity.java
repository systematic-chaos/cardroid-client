package uclm.esi.cardroid.myprofile;

import uclm.esi.cardroid.R;
import android.widget.TextView;

/**
 * \class FriendProfileActivity
 * Activity displaying the data from a user who
 * does have a friend or trip sharing relation with the active user, depicting
 * some contact data in addition to the user's basic information
 */
public class FriendProfileActivity extends ProfileActivity {

	protected TextView mHome, mPhone, mEmail;

	/**
	 * Fill those widgets which were not placed in the superclass's layout with
	 * the data retrieved from the query thrown after this activity's
	 * SessionController is set
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mUser != null) {
			displayContactData();
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.friend_profile;
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();

		setupContactDataWidgets();
	}

	/**
	 * Initialize the widgets displaying the contact data of the User instance
	 * received in the extra's Intent
	 */
	protected void setupContactDataWidgets() {
		mTabs.put(R.id.textContactData, R.id.layoutContactData);

		mHome = (TextView) findViewById(R.id.textViewHome);
		mPhone = (TextView) findViewById(R.id.textViewPhone);
		mEmail = (TextView) findViewById(R.id.textViewEmail);
	}

	/**
	 * Fill the UI widgets showing the contact data of mUser
	 */
	protected void displayContactData() {
		mHome.setText(mUser.getUserHome().getName());
		mPhone.setText(String.valueOf(mUser.getTelephone()));
		mEmail.setText(mUser.getEmail());
	}
}
