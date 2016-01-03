package uclm.esi.cardroid.myprofile;

import java.util.List;

import Ice.ObjectPrx;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.android.Car;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.myprofile.CarDialog.CarDialogListener;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;

/**
 * \class CarDetailsActivity
 * Activity that displays the data from a Car instance
 */
public class CarDetailsActivity extends SessionActivity implements
		CarDialogListener {

	public static final String EXTRA_CAR_PLATE = "CarDetails.CAR_PLATE";
	public static final String EXTRA_CAR_OWNER_EMAIL = "CarDetails.CAR_OWNER_EMAIL";

	private CarTypPrx mCar;
	private TextView mHeaderText, mBrandText, mModelText, mColorText,
			mSeatsText, mPlateText, mFuelText, mConsumptionText;

	/**
	 * Extract the Car instance from the Bundle contained in the calling Intent
	 * , and call setupWidgets to initialize the UI widgets
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_details);

		setupWidgets();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Bundle extras = getIntent().getExtras();
		mCar = _sessionController.getCarFromPlateEmail(
				extras.getString(EXTRA_CAR_PLATE),
				extras.getString(EXTRA_CAR_OWNER_EMAIL));

		displayCarData();
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mHeaderText = (TextView) findViewById(R.id.textViewHeader);
		mBrandText = (TextView) findViewById(R.id.textViewBrand);
		mModelText = (TextView) findViewById(R.id.textViewModel);
		mColorText = (TextView) findViewById(R.id.textViewColor);
		mSeatsText = (TextView) findViewById(R.id.textViewSeats);
		mPlateText = (TextView) findViewById(R.id.textViewPlate);
		mFuelText = (TextView) findViewById(R.id.textViewFuel);
		mConsumptionText = (TextView) findViewById(R.id.textViewConsumption);
	}

	/**
	 * Fill the UI widgets
	 */
	private void displayCarData() {
		mHeaderText.setText(mCar.getBrand() + " " + mCar.getModel());
		mBrandText.setText(mCar.getBrand());
		mModelText.setText(mCar.getModel());
		mColorText.setText(mCar.getColor());
		mSeatsText.setText(String.valueOf(mCar.getNSeats()));
		mPlateText.setText(mCar.getPlate());
		mFuelText.setText(mCar.getCarFuel().name());
		mConsumptionText.setText(String.valueOf(mCar.getConsumptionPerKm()));
	}

	/**
	 * User touched the dialog's positive button. Assign the new value to mCar
	 * and fill the UI widgets with its data
	 * 
	 * @param dialog
	 *            The CarDialog to be queried about the Car data it stores
	 */
	@Override
	public void onDialogPositiveClick(CarDialog dialog) {
		Car updated = dialog.getCarData();
		QueryListener carUpdated = new QueryListener() {

			@Override
			public void onError() {
				Toast.makeText(CarDetailsActivity.this, R.string.updateError,
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
					mCar = (CarTypPrx) data;
					UserTypPrx user = _sessionController.getMyUser();
					List<CarTypPrx> userCars = user.getUserCars();
					for (int n = 0; n < userCars.size(); n++)
						if (userCars.get(n).getPlate().equals(mCar.getPlate()))
							userCars.set(n, mCar);
					user.setUserCars(userCars);
					displayCarData();
					Toast.makeText(CarDetailsActivity.this,
							R.string.dataUpdated, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};
		_sessionController.updateCarDataEmail(carUpdated,
				new uclm.esi.cardroid.data.ice.Car().newInstance(updated),
				getIntent().getStringExtra(EXTRA_CAR_OWNER_EMAIL));
	}

	/**
	 * User touched the dialog's negative button. Dismiss dialog
	 */
	@Override
	public void onDialogNegativeClick(CarDialog dialog) {
		dialog.getDialog().cancel();
	}

	/**
	 * Create an instance of the Dialog fragment and show it
	 */
	@Override
	public void showCarDialog(View v) {
		DialogFragment carDialog = CarDialog
				.newInstance(new Car()
						.newInstance(uclm.esi.cardroid.data.ice.Car
								.extractObject(mCar)));
		carDialog.show(getSupportFragmentManager(),
				CarDialog.class.getSimpleName());
	}

	/**
	 *  Remove a car from the user's profile
	 */
	public void removeCar(View v) {
		QueryListener carRemoved = new QueryListener() {

			@Override
			public void onError() {
				Toast.makeText(CarDetailsActivity.this, R.string.updateError,
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
					UserTypPrx user = _sessionController.getMyUser();
					List<CarTypPrx> userCars = user.getUserCars();
					for (int n = 0; n < userCars.size(); n++)
						if (userCars.get(n).getPlate().equals(mCar.getPlate()))
							userCars.remove(n);
					user.setUserCars(userCars);
					Toast.makeText(CarDetailsActivity.this,
							R.string.dataUpdated, Toast.LENGTH_SHORT).show();
					finish();
				}
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};
		_sessionController.removeCarPlateEmail(carRemoved, mCar.getPlate(),
				_sessionController.getMyLogin());
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
