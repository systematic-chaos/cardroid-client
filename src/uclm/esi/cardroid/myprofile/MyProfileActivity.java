package uclm.esi.cardroid.myprofile;

import java.util.List;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.data.ice.Car;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.myprofile.CarDialog.CarDialogListener;
import uclm.esi.cardroid.myprofile.UserDialog.UserDialogListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.ListDialogFragment.ListDialogListener;
import Ice.ObjectPrx;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * \class MyProfileActivity
 * Activity displaying the data from the active user,
 * depicting all her data
 */
public class MyProfileActivity extends FriendProfileActivity implements
		UserDialogListener, CarDialogListener, ListDialogListener {

	protected TableLayout mCarsTable;
	private UserDialog mUserDialog;
	private CarDialog mCarDialog;

	/**
	 * Fill those widgets which were not placed in the superclass's layout with
	 * the data retrieved from the query thrown after this activity's
	 * SessionController is set
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mUser != null) {
			mCarsTable.removeAllViews();
			displayCarListData();

			setupActionBar(mUser._toString(), true);
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.my_profile;
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();

		setupCarList();
		setupEditButtons();
	}

	@Override
	protected UserTypPrx getUser() {
		return _sessionController.getMyUser();
	}

	protected void displayCarListData() {
		for (CarTypPrx car : mUser.getUserCars())
			addCarToList(car);
	}

	private void addCarToList(final CarTypPrx car) {
		TableRow row = new TableRow(this);
		TextView carDescription = new TextView(this);
		carDescription.setText(car._toString());
		carDescription.setTextAppearance(this,
				android.R.style.TextAppearance_Medium);
		row.addView(carDescription);
		mCarsTable.addView(row);
		row.setClickable(true);
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MyProfileActivity.this,
						CarDetailsActivity.class);
				Bundle extras = new Bundle();
				extras.putString(CarDetailsActivity.EXTRA_CAR_PLATE,
						car.getPlate());
				extras.putString(CarDetailsActivity.EXTRA_CAR_OWNER_EMAIL,
						mUser.getEmail());
				i.putExtras(extras);
				startActivity(i);
			}
		});
	}

	/**
	 * Initialize the widgets displaying the car list of the User instance
	 * received in the extra's Intent
	 */
	protected void setupCarList() {
		mTabs.put(R.id.textCarsList, R.id.tableLayoutCars);

		mCarsTable = (TableLayout) findViewById(R.id.tableLayoutCars);
	}

	/**
	 * Setup the buttons used to launch dialogs and edit mUser 's data
	 */
	protected void setupEditButtons() {
		ImageButton editButton = (ImageButton) findViewById(R.id.imageButtonEditBasicInformation);
		editButton.setVisibility(View.VISIBLE);
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUserDialog(v);
			}
		});

		editButton = (ImageButton) findViewById(R.id.imageButtonEditContactData);
		editButton.setVisibility(View.VISIBLE);
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUserDialog(v);
			}
		});

		editButton = (ImageButton) findViewById(R.id.imageButtonAddCar);
		editButton.setVisibility(View.VISIBLE);
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCarDialog(v);
			}
		});
	}

	/**
	 * Create an instance of the Dialog fragment and show it
	 */
	@Override
	public void showUserDialog(View v) {
		mUserDialog = UserDialog.newInstance(new User()
				.newInstance(uclm.esi.cardroid.data.ice.User
						.extractObject(mUser)));
		mUserDialog.show(getSupportFragmentManager(),
				UserDialog.class.getSimpleName());
	}

	/**
	 * The dialog fragment receives a reference to this Activity through the
	 * Fragment.onAttach() callback, which it uses to call the following methods
	 * defined by the UserDialogFragment.UserDialogListener interface
	 */
	/// User touched the dialog's positive button
	@Override
	public void onDialogPositiveClick(UserDialog dialog) {
		QueryListener profileUpdated = new QueryListener() {

			@Override
			public void onError() {
				Toast.makeText(MyProfileActivity.this, R.string.updateError,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDataChange(double data, boolean saved) {
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
				if (saved) {
					mUser = (UserTypPrx) data;
					setupActionBar(mUser._toString(), true);
					displayBasicInformation();
					displayContactData();
					Toast.makeText(MyProfileActivity.this,
							R.string.dataUpdated, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};
		_sessionController.updateUserData(profileUpdated,
				new uclm.esi.cardroid.data.ice.User().newInstance(dialog
						.getUserData()));
	}

	// / User touched the dialog's negative button
	@Override
	public void onDialogNegativeClick(UserDialog dialog) {
		dialog.getDialog().cancel();
	}

	// / Create an instance of the Dialog fragment and show it
	@Override
	public void showCarDialog(View v) {
		mCarDialog = new CarDialog();
		mCarDialog.show(getSupportFragmentManager(),
				CarDialog.class.getSimpleName());
	}

	/**
	 * The dialog fragment receives a references to this Activity through the
	 * Fragment.onAttach() callback, which it uses to call the following methods
	 * defined by the CarDialogFragment.CarDialogListener interface
	 */
	/// User touched the dialog's positive button
	@Override
	public void onDialogPositiveClick(CarDialog dialog) {
		QueryListener carAdded = new QueryListener() {

			@Override
			public void onError() {
				Toast.makeText(MyProfileActivity.this, R.string.updateError,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDataChange(double data, boolean saved) {
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
				if (saved) {
					CarTypPrx car = (CarTypPrx) data;
					List<CarTypPrx> userCars = mUser.getUserCars();
					userCars.add(car);
					mUser.setUserCars(userCars);
					addCarToList(car);
				}
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};
		_sessionController.addCarEmail(carAdded,
				new Car().newInstance(dialog.getCarData()), mUser.getEmail());
	}

	/// User touched the dialog's negative button
	@Override
	public void onDialogNegativeClick(CarDialog dialog) {
		dialog.getDialog().cancel();
	}

	/**
	 * Pass the received event to the UserDialogFragment. Communication between
	 * fragments must be performed via the underlying activity
	 */
	@Override
	public void onItemSelected(int position) {
		mUserDialog.onItemSelected(position);
	}
}
