package uclm.esi.cardroid.util;

import uclm.esi.cardroid.data.android.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * \class DatePickerDialog
 * DialogFragment that allows the user to select a date
 * and retrieve it from the invoking Activity in the form of a Date instance.
 * This is an abstract class, subclasses extending it should provide a way to
 * retrieve a Date value from the user selection and store it in the mDateTime
 * field.
 */
public abstract class DatePickerDialog extends DialogFragment {
	/// Key for initializing the Dialog to a given value (via an argument)
	protected static final String ARG_DATETIME = "DatePickerDialogFragment.DATETIME";

	protected View mView;
	protected int mLayout;
	protected Date mDateTime;

	/**
	 * Inflate the layout whose id is contained in mLayout and build a dialog
	 * using it. Add positive and negative action buttons, which will call the
	 * corresponding methods of the calling activity, which must implement (be
	 * an instance of) the DateTimePickerDialogListener interface. Call to
	 * setupWidgets and setupArgs , thus setting the values of the UI elements
	 * and the value to be displayed (if provided), fixed by the calling
	 * activity. Subclasses MUST initialize mLayout to their desired layout id
	 * before calling this implementation via super.onCreateDialog
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		/* Inflate and set the layout for the dialog. Pass null as the *
		 * parent view because it's going in the dialog layout         */
		mView = inflater.inflate(this.mLayout, null);
		builder.setView(mView)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						/* Save date and time.							   *
						 * Send the positive button event back to the host *
						 * activity										   */
						mListener.onDialogPositiveClick(DatePickerDialog.this);
					}
				})

				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								/* Send the negative button event back to the *
								 * host activity							  */
								mListener
										.onDialogNegativeClick(DatePickerDialog.this);
							}
						});

		setupWidgets();

		setupArgs();

		// Create the AlertDialog object and return it
		return builder.create();
	}

	/**
	 * @return The date displayed on the UI widgets
	 */
	public Date getDT() {
		return mDateTime;
	}

	/**
	 * @param datetime
	 *            The date to be displayed on the UI widgets
	 */
	public void setDT(Date datetime) {
		mDateTime = datetime;
	}

	/**
	 * Initialize mDateTime to the value provided via the Fragment arguments, if
	 * these are provided
	 */
	protected void setupArgs() {
		Bundle args = getArguments();
		if (args != null) {
			if (!args.isEmpty() && args.containsKey(ARG_DATETIME)) {
				mDateTime = args.getParcelable(ARG_DATETIME);
				setDT(mDateTime);
			}
		}
	}

	/**
	 * Method to be implemented by subclasses extending this one; must setup the
	 * layout widgets used to fill the mDateTime field with the user input
	 */
	protected abstract void setupWidgets();

	/**
	 * \interface DateTimePickerDialogListener
	 * The activity that creates an instance
	 * of this dialog fragment must implement this interface in order to receive
	 * event callbacks. Each method passes the DialogFragment in case the host
	 * needs to query it.
	 */
	public interface DateTimePickerDialogListener {
		public void onDialogPositiveClick(DatePickerDialog dialog);

		public void onDialogNegativeClick(DatePickerDialog dialog);

		public void showDateTimePickerDialog(View v);
	}

	/// Use this instance of the interface to deliver action events
	protected DateTimePickerDialogListener mListener;

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * DateTimePickerDialogListener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			/* Instantiate the DateTimePickerDialogListener so we can send *
			 * events to the host										   */
			mListener = (DateTimePickerDialogListener) activity;
		} catch (ClassCastException cce) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement DateTimePickerDialogListener");
		}
	}
}
