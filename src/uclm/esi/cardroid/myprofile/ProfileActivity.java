package uclm.esi.cardroid.myprofile;

import java.util.Enumeration;
import java.util.Hashtable;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.mymessages.MessageTalkActivity;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * \class ProfileActivity
 * Activity displaying the data from a user who does not
 * have any relation with the active user, so just basic information is depicted
 */
public class ProfileActivity extends SessionActivity {

	public static final String EXTRA_USER_EMAIL = "ProfileActivity.USER_EMAIL";

	protected Hashtable<Integer, Integer> mTabs;
	protected UserTypPrx mUser;
	protected TextView mName, mSurname, mReputation;
	protected ImageView mAvatar;
	protected RatingBar mReputationIndicator;

	/**
	 * Extract the User instance from the Bundle contained in the calling Intent
	 * , and call setupWidgets to initialize the UI widgets
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());

		setupWidgets();
	}

	/**
	 * Fill the widgets with the data retrieved from the query thrown after this
	 * activity's SessionController is set
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mUser == null) {
			mUser = getUser();

			displayBasicInformation();

			setupActionBar(mUser._toString(), false);

			if (!mUser.getEmail().equals(_sessionController.getMyLogin()))
				setupSendMessageButton();
		}
	}

	protected int getContentViewId() {
		return R.layout.profile;
	}

	protected UserTypPrx getUser() {
		return _sessionController.getUserFromEmail(getIntent().getStringExtra(
				EXTRA_USER_EMAIL));
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mTabs = new Hashtable<Integer, Integer>(1);

		setupBasicInformationWidgets();
	}

	/**
	 * Initialize the widgets displaying the basic information of the User
	 * instance received in the extra's Intent
	 */
	protected void setupBasicInformationWidgets() {
		mTabs.put(R.id.textBasicInformation, R.id.layoutBasicInformation);

		mAvatar = (ImageView) findViewById(R.id.imageUser);
		mName = (TextView) findViewById(R.id.textViewName);
		mSurname = (TextView) findViewById(R.id.textViewSurname);
		mReputation = (TextView) findViewById(R.id.textViewReputation);
		mReputationIndicator = (RatingBar) findViewById(R.id.ratingBarReputation);
	}

	/**
	 * Fill the UI widgets showing the basic information of mUser
	 */
	protected void displayBasicInformation() {
		byte[] avatarBytes = mUser.getAvatarBytes();
		if (avatarBytes != null && avatarBytes.length > 0)
			mAvatar.setImageBitmap(BitmapFactory.decodeByteArray(avatarBytes,
					0, avatarBytes.length));
		else
			mAvatar.setImageDrawable(getResources().getDrawable(
					R.drawable.car_logo_xhdpi));
		mName.setText(mUser.getName());
		mSurname.setText(mUser.getSurname());
		mReputation.setText(String.valueOf(mUser.getReputation()));
		mReputationIndicator.setRating(mUser.getReputation());
		int ratingColor;
		switch (mUser.getReputation()) {
		case 0:
			ratingColor = android.R.color.holo_red_dark;
			break;
		case 1:
			ratingColor = android.R.color.holo_red_light;
			break;
		case 2:
			ratingColor = android.R.color.holo_orange_dark;
			break;
		case 3:
			ratingColor = android.R.color.holo_orange_light;
			break;
		case 4:
			ratingColor = android.R.color.holo_green_light;
			break;
		case 5:
			ratingColor = android.R.color.holo_green_dark;
			break;
		default:
			ratingColor = android.R.color.primary_text_dark_nodisable;
		}
		mReputation.setTextColor(getResources().getColor(ratingColor));
	}

	private void setupSendMessageButton() {

		LinearLayout layout = (LinearLayout) findViewById(R.id.layoutBasicInformation);
		layout = (LinearLayout) layout.getChildAt(layout.getChildCount() - 1);
		Button sendMessageButton = new Button(this);
		sendMessageButton.setText(R.string.sendMessage);
		sendMessageButton.setPadding(8, 0, 6, 0);
		layout.addView(sendMessageButton, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		sendMessageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProfileActivity.this,
						MessageTalkActivity.class);
				intent.putExtra(MessageTalkActivity.EXTRA_USER_SPEAKER,
						mUser.getEmail());
				startActivity(intent);
			}
		});
	}

	/**
	 * Toggle the visibility (visible/gone) of the View behind the selected View
	 * 
	 * @param v
	 *            The view selected
	 */
	public void onTabToggle(View v) {
		View layout = findViewById(mTabs.get(v.getId()));
		Checkable c = (Checkable) v;
		if (c.isChecked()) {
			c.setChecked(false);
			layout.setVisibility(View.GONE);
		} else {
			c.setChecked(true);
			layout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Show the View behind the View selected and hide the View s behind the
	 * other CheckedTextView instances (tabs)
	 * 
	 * @param activateTab
	 *            The View selected
	 */
	public void switchTabInfoLayouts(View activateTab) {
		switchTabInfoLayouts(activateTab.getId());
	}

	/**
	 * Show the View behind the View selected and hide the View s behind the
	 * other CheckedTextView instances (tabs)
	 * 
	 * @param activateTabId
	 *            The id of the View selected
	 */
	protected void switchTabInfoLayouts(int activateTabId) {
		Enumeration<Integer> tabs = mTabs.keys();
		int tabId, visibility;
		boolean checked;

		while (tabs.hasMoreElements()) {
			tabId = tabs.nextElement();
			if (tabId == activateTabId) {
				checked = true;
				visibility = View.VISIBLE;
			} else {
				checked = false;
				visibility = View.GONE;
			}
			((Checkable) findViewById(tabId)).setChecked(checked);
			findViewById(mTabs.get(tabId)).setVisibility(visibility);
		}
	}

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return _eventStorm;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}
}
