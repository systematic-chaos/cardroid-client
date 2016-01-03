package uclm.esi.cardroid.data.android;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;

import uclm.esi.cardroid.data.IDate;

/**
 * \class Date
 * Persistence class that stores the data for a basic date
 */
public class Date implements Parcelable, Comparable<Date>, IDate {

	protected int[] date;

	/**
	 * Default constructor.
	 */
	public Date(int year, int month, int day) {
		date = new int[3];
		date[0] = year;
		date[1] = month;
		date[2] = day;
	}

	public Date() {
	}

	public Date newInstance(IDate dateObject) {
		if (dateObject == null)
			return null;
		if (dateObject instanceof Date)
			return (Date) dateObject;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateObject.getTimeInMillis());

		return new Date(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * @return Stored date, in the form of an integer array containing year,
	 *         month and day
	 */
	public int[] getDate() {
		return date;
	}

	/**
	 * @param year
	 *            Year of the date to be stored
	 * @param month
	 *            Month of the date to be stored
	 * @param day
	 *            Day of the date to be stored
	 */
	public void setDate(int year, int month, int day) {
		date = new int[3];
		date[0] = year;
		date[1] = month;
		date[2] = day;
	}

	public void setTimeInMillis(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	public long getTimeInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(date[0], date[1], date[2]);
		return calendar.getTimeInMillis();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeIntArray(date);
	}

	public static final Parcelable.Creator<Date> CREATOR = new Parcelable.Creator<Date>() {

		public Date createFromParcel(Parcel in) {
			return new Date(in);
		}

		public Date[] newArray(int size) {
			return new Date[size];
		}
	};

	protected Date(Parcel in) {
		date = in.createIntArray();
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof Date) {
			// / Two dates comparison is done in terms of their year, month and
			// day
			eq = Arrays.equals(date, ((Date) o).getDate());
		}
		return eq;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Arrays.hashCode(this.date);
		return hash;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT, Locale.getDefault()).format(
				new GregorianCalendar(date[0], date[1], date[2]).getTime());
	}

	@Override
	public int compareTo(Date d) {
		int comp = 0;
		if (d != null) {
			int[] dDate = d.getDate();
			// / Two dates are chronologically compared
			comp = new GregorianCalendar(date[0], date[1], date[2])
					.compareTo(new GregorianCalendar(dDate[0], dDate[1],
							dDate[2]));
		}
		return comp;
	}
}
