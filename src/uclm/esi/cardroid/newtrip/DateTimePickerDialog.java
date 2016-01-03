package uclm.esi.cardroid.newtrip;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.DateTime;
import uclm.esi.cardroid.data.android.DateTimePrefs;
import uclm.esi.cardroid.util.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * \class DateTimePickerDialog
 * DialogFragment that allows the user to select a
 * date and a time and retrieve it from the invoking Activity in the form of a
 * DateTime instance.
 */
public class DateTimePickerDialog extends DatePickerDialog {

	private DatePicker mDatepicker;
	private TimePicker mTimepicker;

	private static final String ARG_DATETIME_PREFS = "DateTimePickerDialog.DATETIME_PREFS";

	/**
	 * Initialize mLayout and call the super implementation to inflate the
	 * layout and build a dialog using it
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mLayout = R.layout.newtrip_dialog_datetime;
		return super.onCreateDialog(savedInstanceState);
	}

	/**
	 * Create a new instance of DateTimePickerDialogFragment, providing
	 * "datetime" as an argument
	 * 
	 * @param datetime
	 *            Input argument
	 */
	public static DateTimePickerDialog newInstance(DateTime datetime) {
		DateTimePickerDialog f = new DateTimePickerDialog();

		Bundle args = new Bundle();
		args.putParcelable(ARG_DATETIME, datetime);
		f.setArguments(args);

		return f;
	}

	/**
	 * Create a new instance of DateTimePickerDialogFragment, setting the UI
	 * widgets limits to the preferences expressed by the argument provided in
	 * "datetimePrefs"
	 * 
	 * @param datetimePrefs
	 *            Preferences input argument
	 */
	public static DateTimePickerDialog newInstance(DateTimePrefs datetimePrefs) {
		DateTimePickerDialog f = new DateTimePickerDialog();

		Bundle args = new Bundle();
		args.putParcelable(ARG_DATETIME_PREFS, datetimePrefs);
		f.setArguments(args);

		return f;
	}

	/**
	 * Setup the layout widgets used to fill the mDateTime field with the user
	 * input
	 */
	@Override
	protected void setupWidgets() {
		mDatepicker = (DatePicker) mView.findViewById(R.id.datePicker);
		mTimepicker = (TimePicker) mView.findViewById(R.id.timePicker);

		Calendar today = Calendar.getInstance();
		today.clear(Calendar.SECOND);
		mDatepicker.setMinDate(today.getTimeInMillis());
		mTimepicker.setIs24HourView(true);
	}

	/**
	 * Initialize mDateTime to the value provided via the Fragment arguments, if
	 * these are provided
	 */
	@Override
	protected void setupArgs() {
		Bundle args = getArguments();
		if (args != null && !args.isEmpty()) {
			if (args.containsKey(ARG_DATETIME)) {
				mDateTime = (DateTime) args.getParcelable(ARG_DATETIME);
				setDateTime((DateTime) mDateTime);
			}
			if (args.containsKey(ARG_DATETIME_PREFS)) {
				DateTimePrefs datetimePrefs = args
						.getParcelable(ARG_DATETIME_PREFS);
				mDatepicker
						.setMinDate(datetimePrefs.getTimeInMillis()
								- TimeUnit.MILLISECONDS.convert(
										datetimePrefs.getToleranceDays(),
										TimeUnit.DAYS));
				mDatepicker
						.setMaxDate(datetimePrefs.getTimeInMillis()
								+ TimeUnit.MILLISECONDS.convert(
										datetimePrefs.getToleranceDays(),
										TimeUnit.DAYS));
				int[] date = datetimePrefs.getDate();
				int time;
				switch (datetimePrefs.getTimePreferences()) {
				case NIGHT:
					time = 0;
					break;
				case AFTERNOON:
					time = 16;
					break;
				case MORNING:
				case ANY:
				default:
					time = 8;
				}
				mDateTime = new DateTime(date[0], date[1], date[2], time, 0);
				setDateTime((DateTime) mDateTime);
			}
		}
	}

	/**
	 * @return The date and time displayed on the UI widgets
	 */
	public DateTime getDateTime() {
		int[] date = getDate();
		int[] time = getTime();
		mDateTime = new DateTime(date[0], date[1], date[2], time[0], time[1]);
		return (DateTime) mDateTime;
	}

	/**
	 * @param datetime
	 *            The date and time to be displayed on the UI widgets
	 */
	public void setDateTime(DateTime datetime) {
		int[] date = datetime.getDate();
		int[] time = datetime.getTime();
		mDatepicker.updateDate(date[0], date[1], date[2]);
		mTimepicker.setCurrentHour(time[0]);
		mTimepicker.setCurrentMinute(time[1]);
		mDateTime = datetime;
	}

	/**
	 * @return The date displayed on the UI widgets
	 */
	public int[] getDate() {
		int[] date = { mDatepicker.getYear(), mDatepicker.getMonth(),
				mDatepicker.getDayOfMonth() };
		return date;
	}

	/**
	 * @return The time displayed on the UI widgets
	 */
	public int[] getTime() {
		int[] time = { mTimepicker.getCurrentHour(),
				mTimepicker.getCurrentMinute() };
		return time;
	}
}
