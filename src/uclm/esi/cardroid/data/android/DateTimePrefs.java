package uclm.esi.cardroid.data.android;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import uclm.esi.cardroid.data.IDateTimePrefs;

/**
 * \class DateTimePrefs
 * Persistence class that store the data for a date and
 * preferences about date and time
 */
public class DateTimePrefs extends Date implements IDateTimePrefs {

	private int toleranceDays;
	private TimePreferences timePreferences;

	// / Constructor that takes a simple date and not date/time preferences.
	public DateTimePrefs(int year, int month, int day) {
		super(year, month, day);
		toleranceDays = 0;
		timePreferences = TimePreferences.ANY;
	}

	/**
	 * Constructor that takes a date along with the number of days the provided
	 * date may vary on.
	 */
	public DateTimePrefs(int year, int month, int day, int toleranceDays) {
		super(year, month, day);
		this.toleranceDays = toleranceDays;
		timePreferences = TimePreferences.ANY;
	}

	/**
	 * Constructor that takes a date along with the preferred time (an instance
	 * of TimePreferences).
	 */
	public DateTimePrefs(int year, int month, int day,
			TimePreferences timePreferences) {
		super(year, month, day);
		this.timePreferences = timePreferences;
		toleranceDays = 0;
	}

	/**
	 * Constructor that takes a date along with the number of days the provided
	 * date may vary on and the preferred time (an instance of TimePreferences).
	 */
	public DateTimePrefs(int year, int month, int day, int toleranceDays,
			TimePreferences timePreferences) {
		super(year, month, day);
		this.toleranceDays = toleranceDays;
		this.timePreferences = timePreferences;
	}

	public DateTimePrefs() {
	}

	public DateTimePrefs newInstance(IDateTimePrefs dateTimePrefsObject) {
		if (dateTimePrefsObject == null)
			return null;
		if (dateTimePrefsObject instanceof DateTimePrefs)
			return (DateTimePrefs) dateTimePrefsObject;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTimePrefsObject.getTimeInMillis());

		return new DateTimePrefs(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				dateTimePrefsObject.getToleranceDays(),
				dateTimePrefsObject.getTimePreferences());
	}

	/**
	 * @return The number of days the stored date may vary
	 */
	public int getToleranceDays() {
		return toleranceDays;
	}

	/**
	 * @param toleranceDays
	 *            The number of days the stored date may vary
	 */
	public void setToleranceDays(int toleranceDays) {
		this.toleranceDays = toleranceDays;
	}

	/**
	 * @return The preferred time
	 */
	public TimePreferences getTimePreferences() {
		return timePreferences;
	}

	/**
	 * @param timePreferences
	 *            The preferred time
	 */
	public void setTimePreferences(TimePreferences timePreferences) {
		this.timePreferences = timePreferences;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(toleranceDays);
		out.writeString(timePreferences.name());
	}

	public static final Parcelable.Creator<DateTimePrefs> CREATOR = new Parcelable.Creator<DateTimePrefs>() {

		public DateTimePrefs createFromParcel(Parcel in) {
			return new DateTimePrefs(in);
		}

		public DateTimePrefs[] newArray(int size) {
			return new DateTimePrefs[size];
		}
	};

	protected DateTimePrefs(Parcel in) {
		super(in);
		toleranceDays = in.readInt();
		timePreferences = TimePreferences.valueOf(in.readString());
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof DateTimePrefs) {
			DateTimePrefs dtp = (DateTimePrefs) o;
			/**
			 * Two dates comparison is done in terms of their date(year, month
			 * and day) and time (hour and minute)
			 */
			eq = Arrays.equals(date, dtp.getDate())
					&& toleranceDays == dtp.getToleranceDays()
					&& timePreferences.equals(dtp.getTimePreferences());
		}
		return eq;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
				.format(new GregorianCalendar(date[0], date[1], date[2])
						.getTime()));
		if (toleranceDays > 0) {
			sb.append("\t~" + toleranceDays + " días");
		}
		switch (timePreferences) {
		case ANY:
			sb.append("\tCualquier hora");
			break;
		case MORNING:
			sb.append("\tMañana");
			break;
		case AFTERNOON:
			sb.append("\tTarde");
			break;
		case NIGHT:
			sb.append("\tNoche");
			break;
		}
		return sb.toString();
	}
}
