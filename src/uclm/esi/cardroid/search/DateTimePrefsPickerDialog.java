package uclm.esi.cardroid.search;

import java.util.Calendar;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.IDateTimePrefs.TimePreferences;
import uclm.esi.cardroid.data.android.DateTimePrefs;
import uclm.esi.cardroid.util.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

/**
 * \class DateTimePrefsPickerDialog
 * DialogFragment that allows the user to
 * select a date and time preferences and retrieve it from the invoking Activity
 * in the form of a DateTimePrefs instance.
 */
public class DateTimePrefsPickerDialog extends DatePickerDialog {

	private DatePicker mDatepicker;
	private CheckBox mCheckboxDatetolerance;
	private NumberPicker mNumberpickerTolerancedays;
	private RadioGroup mRadiogroupTime;

	/**
	 * Initialize mLayout and call the super implementation to inflate the
	 * layout and build a dialog using it
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mLayout = R.layout.search_dialog_datetime;
		return super.onCreateDialog(savedInstanceState);
	}

	/**
	 * Create a new instance of DateTimePrefsPickerDialogFragment, providing
	 * "datetime" as an argument
	 * 
	 * @param datetime
	 *            Input argument
	 */
	public static DateTimePrefsPickerDialog newInstance(DateTimePrefs datetime) {
		DateTimePrefsPickerDialog f = new DateTimePrefsPickerDialog();

		// Supply datetime input as an argument
		Bundle args = new Bundle();
		args.putParcelable(ARG_DATETIME, datetime);
		f.setArguments(args);

		return f;
	}

	/**
	 * Setup the layout widgets used to fill the mDateTime field with the user
	 * input
	 */
	protected void setupWidgets() {
		mDatepicker = (DatePicker) mView.findViewById(R.id.datePicker);
		mCheckboxDatetolerance = (CheckBox) mView
				.findViewById(R.id.checkBoxDateTolerance);
		mNumberpickerTolerancedays = (NumberPicker) mView
				.findViewById(R.id.numberPickerToleranceDays);
		mRadiogroupTime = (RadioGroup) mView.findViewById(R.id.radioGroupTime);

		Calendar today = Calendar.getInstance();
		today.clear(Calendar.HOUR_OF_DAY);
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		mDatepicker.setMinDate(today.getTimeInMillis());
		mCheckboxDatetolerance.setChecked(false);
		mNumberpickerTolerancedays.setEnabled(false);
		mNumberpickerTolerancedays.setMaxValue(4);

		mCheckboxDatetolerance
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mNumberpickerTolerancedays.setEnabled(isChecked);
					}
				});
	}

	/**
	 * Initialize mDateTime to the value provided via the Fragment arguments, if
	 * these are provided
	 */
	@Override
	protected void setupArgs() {
		Bundle args = getArguments();
		if (args != null) {
			if (!args.isEmpty() && args.containsKey(ARG_DATETIME)) {
				mDateTime = args.getParcelable(ARG_DATETIME);
				setDateTimePrefs((DateTimePrefs) mDateTime);
			}
		}
	}

	/**
	 * @return The date and time preferences displayed on the UI widgets
	 */
	public DateTimePrefs getDateTimePrefs() {
		int[] date = getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.set(date[0], date[1], date[2]);
		mDateTime = new DateTimePrefs(date[0], date[1], date[2],
				getDateTolerance() ? getToleranceDays() : 0,
				getTimePreferences());
		return (DateTimePrefs) mDateTime;
	}

	/**
	 * @param datetime
	 *            The date and time preferences to be displayed on the UI
	 *            widgets
	 */
	public void setDateTimePrefs(DateTimePrefs datetime) {
		int[] date = datetime.getDate();
		mDatepicker.updateDate(date[0], date[1], date[2]);
		mCheckboxDatetolerance.setChecked(datetime.getToleranceDays() != 0);
		mNumberpickerTolerancedays.setEnabled(mCheckboxDatetolerance
				.isChecked());
		mNumberpickerTolerancedays.setValue(datetime.getToleranceDays());
		mRadiogroupTime.check(mRadiogroupTime.getChildAt(
				datetime.getTimePreferences().ordinal()).getId());
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
	 * @return Whether days tolerance over the date are allowed
	 */
	public boolean getDateTolerance() {
		return mCheckboxDatetolerance.isChecked();
	}

	/**
	 * @return The number of days tolerance over the date displayed on the UI
	 *         widgets
	 */
	public int getToleranceDays() {
		int toleranceDays = 0;
		if (getDateTolerance()) {
			toleranceDays = mNumberpickerTolerancedays.getValue();
		}
		return toleranceDays;
	}

	/**
	 * @return The time preferences displayed on the UI widgets
	 */
	public TimePreferences getTimePreferences() {
		TimePreferences timePreferences;
		switch (mRadiogroupTime.getCheckedRadioButtonId()) {
		case R.id.radioTimeMorning:
			timePreferences = TimePreferences.MORNING;
			break;
		case R.id.radioTimeAfternoon:
			timePreferences = TimePreferences.AFTERNOON;
			break;
		case R.id.radioTimeNight:
			timePreferences = TimePreferences.NIGHT;
			break;
		case R.id.radioTimeAny:
		default:
			timePreferences = TimePreferences.ANY;
			break;
		}
		return timePreferences;
	}
}
