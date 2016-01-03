package uclm.esi.cardroid.data.ice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Ice.Current;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IDateTimePrefs;
import uclm.esi.cardroid.data.zerocice.DateTimePrefsTyp;

/**
 * \class DateTimePrefs
 * Domain class implementing a DateTimePrefs for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class DateTimePrefs extends DateTimePrefsTyp implements IDateTimePrefs,
		Comparable<Date>, ObjectFactory {
	private static final long serialVersionUID = 8438816444504630535L;

	public DateTimePrefs() {
	}

	/**
	 * Default constructor
	 */
	public DateTimePrefs(long datetime, int toleranceDays,
			uclm.esi.cardroid.data.zerocice.TimePreferences timePrefs) {
		super(datetime, toleranceDays, timePrefs);
	}

	public DateTimePrefs(DateTimePrefsTyp dateTimePrefs) {
		this(dateTimePrefs.datetime, dateTimePrefs.toleranceDays,
				dateTimePrefs.timePrefs);
	}

	/* IDateTimePrefs interface */

	public Date newInstance(IDate dateObject) {
		return new Date().newInstance(dateObject);
	}

	public DateTimePrefs newInstance(IDateTimePrefs dateTimePrefsObject) {
		if (dateTimePrefsObject == null)
			return null;
		if (dateTimePrefsObject instanceof DateTimePrefs)
			return (DateTimePrefs) dateTimePrefsObject;
		long datetime = dateTimePrefsObject.getTimeInMillis();
		int toleranceDays = dateTimePrefsObject.getToleranceDays();
		uclm.esi.cardroid.data.zerocice.TimePreferences timePreferences = uclm.esi.cardroid.data.zerocice.TimePreferences
				.valueOf(dateTimePrefsObject.getTimePreferences().name());
		return new DateTimePrefs(datetime, toleranceDays, timePreferences);
	}

	public void setTimePreferences(TimePreferences timePreferences) {
		setTimePrefs(uclm.esi.cardroid.data.zerocice.TimePreferences
				.valueOf(timePreferences.name()));
	}

	public TimePreferences getTimePreferences() {
		return IDateTimePrefs.TimePreferences.valueOf(timePrefs.name());
	}

	/* Overriding superclass */

	@Override
	public int getToleranceDays(Current __current) {
		return toleranceDays;
	}

	@Override
	public void setToleranceDays(int toleranceDays, Current __current) {
		this.toleranceDays = toleranceDays;
	}

	@Override
	public uclm.esi.cardroid.data.zerocice.TimePreferences getTimePrefs(
			Current __current) {
		return timePrefs;
	}

	@Override
	public void setTimePrefs(
			uclm.esi.cardroid.data.zerocice.TimePreferences timePrefs,
			Current __current) {
		this.timePrefs = timePrefs;
	}

	@Override
	public long getTimeInMillis(Current __current) {
		return datetime;
	}

	@Override
	public void setTimeInMillis(long datetime, Current __current) {
		this.datetime = datetime;
	}

	public int[] getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(datetime);
		int[] date = new int[3];
		date[0] = calendar.get(Calendar.YEAR);
		date[1] = calendar.get(Calendar.MONTH);
		date[2] = calendar.get(Calendar.DAY_OF_MONTH);
		return date;
	}

	public void setDate(int[] date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(datetime);
		calendar.set(date[0], date[1], date[2]);
		datetime = calendar.getTimeInMillis();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM)
				.format(new java.util.Date(datetime)));
		switch(timePrefs) {
		case MORNING:
			builder.append(" " + "Mañana");
			break;
		case AFTERNOON:
			builder.append(" " + "Tarde");
			break;
		case NIGHT:
			builder.append(" " + "Noche");
			break;
		}
		return builder.toString();
	}

	/* Comparable<Date> interface */

	@Override
	public int compareTo(Date d) {
		int comp = 0;
		if (d != null) {
			if (datetime < d.getTimeInMillis())
				comp = -1;
			if (datetime == d.getTimeInMillis())
				comp = 0;
			if (datetime > d.getTimeInMillis())
				comp = 1;
		}
		return comp;
	}

	/* ObjectFactory interface */

	@Override
	public DateTimePrefs create(String type) {
		if (type.equals(ice_staticId())) {
			return new DateTimePrefs();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
