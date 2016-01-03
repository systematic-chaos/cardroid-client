package uclm.esi.cardroid.mytrips;

import uclm.esi.cardroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * \class NumberPickerDialog
 * DialogFragment that allows the user to select a
 * number and retrieve it from the invoking Activity in the form of an integer
 */
public class NumberPickerDialog extends DialogFragment {

	/// Keys for initializing the Dialog to some provided values (via argument)
	private static final String ARG_VALUE = "NumberPickerDialogFragment.VALUE";
	private static final String ARG_MIN_VALUE = "NumberPickerDialogFragment.MIN_VALUE";
	private static final String ARG_MAX_VALUE = "NumberPickerDialogFragment.MAX_VALUE";

	private View mView;

	private NumberPicker mNumberPicker;

	/**
	 * Inflate the layout whose id is contained in mLayout and build a dialog
	 * using it. Add positive and negative action buttons, which will call the
	 * corresponding methods of the calling activity, which must implement (be
	 * an instance of) the NumberPickerDialogListener interface. Call to
	 * setupWidgets , thus setting the values of the UI elements.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		/*
		 * Inflate and set the layout for the dialog. Pass null as the parent
		 * view because it's going in the dialog layout.
		 */
		mView = inflater.inflate(R.layout.number_picker, null);
		builder.setView(mView)
				.setTitle(R.string.requestedSeats)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Save value; send the positive button event back to
						// the host
						// activity
						mListener
								.onDialogPositiveClick(NumberPickerDialog.this);
					}
				})

				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Send the negative event button back to the
								// host activity
								mListener
										.onDialogNegativeClick(NumberPickerDialog.this);
							}
						});

		mNumberPicker = (NumberPicker) mView.findViewById(R.id.numberPicker);
		Bundle args = getArguments();
		if (args != null && !args.isEmpty()) {
			if (args.containsKey(ARG_VALUE))
				mNumberPicker.setValue(args.getInt(ARG_VALUE));
			if (args.containsKey(ARG_MIN_VALUE))
				mNumberPicker.setMinValue(args.getInt(ARG_MIN_VALUE));
			if (args.containsKey(ARG_MAX_VALUE))
				mNumberPicker.setMaxValue(args.getInt(ARG_MAX_VALUE));
		}

		// Create the AlertDialog object and return it
		return builder.create();
	}

	/**
	 * Create a new instance of NumberPickerDialog, providing the values
	 * corresponding to the minimum, maximum and displayed value for the
	 * NumberPicker
	 */
	public static NumberPickerDialog newInstance(int value, int minValue,
			int maxValue) {
		if (!(minValue <= value && value <= maxValue))
			return null;

		NumberPickerDialog f = new NumberPickerDialog();

		Bundle args = new Bundle();
		args.putInt(ARG_VALUE, value);
		args.putInt(ARG_MIN_VALUE, minValue);
		args.putInt(ARG_MAX_VALUE, maxValue);
		f.setArguments(args);

		return f;
	}

	/**
	 * Create a new instance of NumberPickerDialog, providing the value
	 * corresponding to the displayed value for the NumberPicker
	 */
	public static NumberPickerDialog newInstance(int value) {
		NumberPickerDialog f = new NumberPickerDialog();

		Bundle args = new Bundle();
		args.putInt(ARG_VALUE, value);
		f.setArguments(args);

		return f;
	}

	/**
	 * @return The value displayed on the UI NumberPicker widget
	 */
	public int getValue() {
		return mNumberPicker.getValue();
	}

	/**
	 * \interface NumberPickerDialogListener
	 * The activity that creates an instance
	 * of this dialog fragment must implement this interface in order to receive
	 * event callbacks. One method passes the value displayed by the
	 * NumberPicker UI widget.
	 */
	public interface NumberPickerDialogListener {
		public void onDialogPositiveClick(NumberPickerDialog dialog);

		public void onDialogNegativeClick(NumberPickerDialog dialog);

		public void showNumberPickerDialog(View v);
	}

	/// Use this instance of the interface to deliver action events
	private NumberPickerDialogListener mListener;

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * NumberPickerDialog listener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NumberPickerDialogListener so we can send
			// events to the host
			mListener = (NumberPickerDialogListener) activity;
		} catch (ClassCastException cce) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NumberPickerDialogListener");
		}
	}
}
