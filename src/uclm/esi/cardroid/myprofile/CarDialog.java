package uclm.esi.cardroid.myprofile;

import java.util.Hashtable;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.ICar.Fuel;
import uclm.esi.cardroid.data.android.Car;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * \class CarDialog
 * DialogFragment that allows the user to set the attributes
 * and details of a new car, or modify the data of an existing one
 */
public class CarDialog extends DialogFragment {
	public static final String ARG_CAR = "CarDialogFragment.CAR";

	private Car mCar;
	private View mView;

	private EditText mBrandText, mModelText, mColorText, mSeatsText,
			mPlateText, mConsumptionText;
	private Spinner mFuelSpinner;
	private Hashtable<String, Fuel> mFuelTypes;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		/*
		 * Inflate and set the layout for the dialog. Pass null as the parent
		 * view because it's going to the dialog layout.
		 */
		int layout = R.layout.car_dialog;
		mView = inflater.inflate(layout, null);

		setupWidgets();

		Bundle args = getArguments();
		if (args != null) {
			if (!args.isEmpty() && args.containsKey(ARG_CAR)) {
				setCarData((Car) args.getParcelable(ARG_CAR));
			}
		}

		builder.setView(mView)
				// Add title
				.setTitle(mCar == null ? R.string.addCar : R.string.editCarData)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Save car data
						if (updateCarData()) {
							// Since changes in the car data were made, send the
							// positive button event back to the host activity
							mListener.onDialogPositiveClick(CarDialog.this);
						} else {
							// Otherwise, send the negative button event back,
							// to discard an update of the user data in the host
							// activity
							mListener.onDialogNegativeClick(CarDialog.this);
						}
					}
				})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Send the negative button event back to the
								// host activity
								mListener.onDialogNegativeClick(CarDialog.this);
							}
						});

		// Create the AlertDialog object and return it
		return builder.create();
	}

	/**
	 * Create a new instance of CarDialogFragment
	 * 
	 * @param car
	 *            Input argument
	 */
	public static CarDialog newInstance(Car car) {
		CarDialog f = new CarDialog();

		// Supply received car as an argument
		Bundle args = new Bundle();
		args.putParcelable(ARG_CAR, car);
		f.setArguments(args);

		return f;
	}

	/**
	 * @return Instance of Car containing the data depicted by this Dialog
	 */
	public Car getCarData() {
		return mCar;
	}

	/**
	 * Initialize the mCar instance and the UI widgets with the data received
	 * 
	 * @param car
	 *            The Car instance whose data will be used for proper
	 *            initializations
	 */
	public void setCarData(Car car) {
		mCar = car;
		mBrandText.setText(car.getBrand());
		mModelText.setText(car.getModel());
		mColorText.setText(car.getColor());
		mSeatsText.setText(String.valueOf(car.getNSeats()));
		mPlateText.setText(car.getPlate());
		mPlateText.setEnabled(false);
		mFuelSpinner.setSelection(car.getFuel().ordinal());
		mConsumptionText.setText(String.valueOf(car.getConsumptionPerKm()));
	}

	/**
	 * Update those fields of mCar whose value doesn't match with the
	 * corresponding one in the UI widgets, making its state consistent and
	 * ensuring that mCar reflects the information displayed on the UI
	 * 
	 * @return Whether any update operation had to be performed on mCar to keep
	 *         its data consistent and updated
	 */
	private boolean updateCarData() {
		boolean dataChanged = false;
		if (mCar == null) {
			dataChanged = initializeCarData();
		} else {
			String str = mBrandText.getText().toString();
			if (!mCar.getBrand().equals(str) && str.trim().length() > 0) {
				mCar.setBrand(str);
				dataChanged = true;
			}
			str = mModelText.getText().toString();
			if (!mCar.getModel().equals(str) && str.trim().length() > 0) {
				mCar.setModel(str);
				dataChanged = true;
			}
			str = mColorText.getText().toString();
			if (!mCar.getColor().equals(str) && str.trim().length() > 0) {
				mCar.setColor(str);
				dataChanged = true;
			}
			str = mSeatsText.getText().toString();
			if (!String.valueOf(mCar.getNSeats()).equals(str)
					&& str.trim().length() > 0) {
				mCar.setNSeats(Integer.parseInt(str));
				dataChanged = true;
			}
			str = mPlateText.getText().toString();
			if (!mCar.getPlate().equals(str) && str.trim().length() > 0) {
				mCar.setPlate(str);
				dataChanged = true;
			}
			if (!mFuelTypes.get(mFuelSpinner.getSelectedItem()).equals(
					mCar.getFuel())) {
				mCar.setFuel(mFuelTypes.get(mFuelSpinner.getSelectedItem()));
				dataChanged = true;
			}
			str = mConsumptionText.getText().toString();
			if (!String.valueOf(mCar.getConsumptionPerKm()).equals(str)
					&& str.trim().length() > 0) {
				mCar.setConsumptionPerKm(Double.parseDouble(str));
				dataChanged = true;
			}
		}
		return dataChanged;
	}

	/**
	 * Checks if a Car instance can be right initialized with the data currently
	 * contained in the UI widgets. If such an initialization can be performed,
	 * mCar is assigned the value of the new initialization. Otherwise, a Dialog
	 * detailing whose fields of the UI don't have a right value (or even don't
	 * have one) is prompted up
	 * 
	 * @return Whether the Car instance initialization could be performed
	 *         successfully
	 */
	private boolean initializeCarData() {
		boolean canInitialize = true;
		String[] carData = new String[6];
		Fuel carFuel;
		carData[0] = mBrandText.getText().toString();
		carData[1] = mModelText.getText().toString();
		carData[2] = mColorText.getText().toString();
		carData[3] = mSeatsText.getText().toString();
		carData[4] = mPlateText.getText().toString();
		carData[5] = mConsumptionText.getText().toString();
		carFuel = mFuelTypes.get(mFuelSpinner.getSelectedItem());
		for (int n = 0; n < carData.length && canInitialize; n++) {
			canInitialize = carData[n].trim().length() > 0;
		}
		canInitialize = canInitialize && carFuel != null;
		if (canInitialize) {
			mCar = new Car(carData[0], carData[1], carData[2],
					Integer.parseInt(carData[3]), carData[4], carFuel,
					Double.parseDouble(carData[5]));
		} else {
			showErrorDialog(getString(R.string.fillingError));
		}
		return canInitialize;
	}

	/**
	 * Initialize the UI widgets
	 */
	private void setupWidgets() {
		mBrandText = (EditText) mView.findViewById(R.id.editTextBrand);
		mModelText = (EditText) mView.findViewById(R.id.editTextModel);
		mColorText = (EditText) mView.findViewById(R.id.editTextColor);
		mSeatsText = (EditText) mView.findViewById(R.id.editTextSeats);
		mPlateText = (EditText) mView.findViewById(R.id.editTextPlate);
		mConsumptionText = (EditText) mView
				.findViewById(R.id.editTextConsumption);

		mFuelSpinner = (Spinner) mView.findViewById(R.id.spinnerFuel);
		Fuel[] fuelTypes = Fuel.values();
		String[] fuelNames = getResources().getStringArray(R.array.fuelTypes);
		mFuelTypes = new Hashtable<String, Car.Fuel>(fuelNames.length, 1);
		for (int n = 0; n < fuelTypes.length; n++) {
			mFuelTypes.put(fuelNames[n], fuelTypes[n]);
		}
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> fuelAdapter = new ArrayAdapter<CharSequence>(
				getActivity(), android.R.layout.simple_spinner_item, fuelNames);
		// Specify the layout to use when the list of choices appears
		fuelAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mFuelSpinner.setAdapter(fuelAdapter);
	}

	/**
	 * Show an error AlertDialog displaying a message
	 * 
	 * @param msg
	 *            The message to be displayed in the AlertDialog created
	 */
	private void showErrorDialog(String msg) {
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog characteristics
		builder.setMessage(msg);
		// Add the dialog's button
		builder.setNegativeButton("Volver",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * \interface CarDialogListener
	 * The activity that creates an instance of this
	 * dialog fragment must implement this interface in order to receive event
	 * callbacks. Each method passes the DialogFragment in case the host
	 * activity needs to query it.
	 */
	public interface CarDialogListener {
		public void onDialogPositiveClick(CarDialog dialog);

		public void onDialogNegativeClick(CarDialog dialog);

		public void showCarDialog(View v);
	}

	/// Use this instance of the interface to deliver action events
	private CarDialogListener mListener;

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * CarDialogListener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the CarDialogListener so we can send events back to
			// the host activity
			mListener = (CarDialogListener) activity;
		} catch (ClassCastException cce) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement CarDialogListener");
		}
	}
}
