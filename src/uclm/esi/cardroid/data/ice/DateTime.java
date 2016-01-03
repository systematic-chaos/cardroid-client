package uclm.esi.cardroid.data.ice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Ice.Current;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IDateTime;
import uclm.esi.cardroid.data.zerocice.DateTimeTyp;

/**
 * \class DateTime
 * Domain class implementing a DateTime for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class DateTime extends DateTimeTyp implements IDateTime,
		Comparable<Date>, ObjectFactory {
	private static final long serialVersionUID = 6326227506434498485L;

	public DateTime() {
	}

	/**
	 * Default constructor
	 */
	public DateTime(long datetime) {
		super(datetime);
	}

	public DateTime(DateTimeTyp dateTime) {
		this(dateTime.datetime);
	}

	/* IDateTime interface */

	public Date newInstance(IDate dateObject) {
		return new Date().newInstance(dateObject);
	}

	public DateTime newInstance(IDateTime dateTimeObject) {
		if (dateTimeObject == null)
			return null;
		if (dateTimeObject instanceof DateTime)
			return (DateTime) dateTimeObject;
		long datetime = dateTimeObject.getTimeInMillis();
		return new DateTime(datetime);
	}

	/* Overriding superclass */

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

	public int[] getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(datetime);
		int[] time = new int[2];
		time[0] = calendar.get(Calendar.HOUR_OF_DAY);
		time[1] = calendar.get(Calendar.MINUTE);
		return time;
	}

	public void setDate(int[] date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(datetime);
		calendar.set(date[0], date[1], date[2]);
		datetime = calendar.getTimeInMillis();
	}

	public void setTime(int[] time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(datetime);
		calendar.set(Calendar.HOUR_OF_DAY, time[0]);
		calendar.set(Calendar.MINUTE, time[1]);
		datetime = calendar.getTimeInMillis();
	}

	@Override
	public String _toString(Current __current) {
		return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,
				SimpleDateFormat.SHORT).format(new java.util.Date(datetime));
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
	public DateTime create(String type) {
		if (type.equals(ice_staticId())) {
			return new DateTime();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
