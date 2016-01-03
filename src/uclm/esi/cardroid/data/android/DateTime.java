package uclm.esi.cardroid.data.android;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import uclm.esi.cardroid.data.IDateTime;

/**
 * \class DateTime
 * Persistence class that stores the data for a date and time
 */
public class DateTime extends Date implements IDateTime {

	private int[] time;

	/**
	 * Default constructor.
	 */
	public DateTime(int year, int month, int day, int hour, int minute) {
		super(year, month, day);
		time = new int[2];
		time[0] = hour;
		time[1] = minute;
	}

	public DateTime() {
		super();
	}

	public DateTime newInstance(IDateTime dateTimeObject) {
		if (dateTimeObject == null)
			return null;
		if (dateTimeObject instanceof DateTime)
			return (DateTime) dateTimeObject;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTimeObject.getTimeInMillis());

		return new DateTime(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
	}

	/**
	 * @return Stored time, in the form of an array containing hour and minute
	 */
	public int[] getTime() {
		return time;
	}

	/**
	 * @param hour
	 *            Hour of the time to be stored
	 * @param minute
	 *            Minute of the time to be stored
	 */
	public void setTime(int hour, int minute) {
		time[0] = hour;
		time[1] = minute;
	}

	@Override
	public void setTimeInMillis(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		setTime(calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));
	}

	@Override
	public long getTimeInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(date[0], date[1], date[2], time[0], time[1]);
		return calendar.getTimeInMillis();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeIntArray(time);
	}

	public static final Parcelable.Creator<DateTime> CREATOR = new Parcelable.Creator<DateTime>() {

		public DateTime createFromParcel(Parcel in) {
			return new DateTime(in);
		}

		public DateTime[] newArray(int size) {
			return new DateTime[size];
		}
	};

	protected DateTime(Parcel in) {
		super(in);
		time = in.createIntArray();
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof DateTime) {
			DateTime dt = (DateTime) o;
			/**
			 * Two dates comparison is done in terms of their date(year, month
			 * and day) and time (hour and minute)
			 */
			eq = Arrays.equals(date, dt.getDate())
					&& Arrays.equals(time, dt.getTime());
		}
		return eq;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT, Locale.getDefault()).format(
				new GregorianCalendar(date[0], date[1], date[2], time[0],
						time[1]).getTime());
	}

	@Override
	public int compareTo(Date d) {
		int comp = 0;
		if (d != null && d instanceof DateTime) {
			DateTime dt = (DateTime) d;
			int[] dtDate = dt.getDate(), dtTime = dt.getTime();
			// / Two dates are chronologically compared
			comp = new GregorianCalendar(date[0], date[1], date[2], time[0],
					time[1]).compareTo(new GregorianCalendar(dtDate[0],
					dtDate[1], dtDate[2], dtTime[0], dtTime[1]));
		}
		return comp;
	}
}
