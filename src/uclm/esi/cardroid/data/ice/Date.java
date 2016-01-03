package uclm.esi.cardroid.data.ice;

import java.text.SimpleDateFormat;

import Ice.Current;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.zerocice.DateTyp;

/**
 * \class Date
 * Domain class implementing a Date for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Date extends DateTyp implements IDate, Comparable<Date>,
		ObjectFactory {
	private static final long serialVersionUID = -3991148834484088982L;

	public Date() {
	}

	/**
	 * Default constructor
	 */
	public Date(long datetime) {
		super(datetime);
	}

	public Date(DateTyp date) {
		this(date.datetime);
	}

	/* IDate interface */

	public Date newInstance(IDate dateObject) {
		if (dateObject == null)
			return null;
		if (dateObject instanceof Date)
			return (Date) dateObject;
		long datetime = dateObject.getTimeInMillis();
		return new Date(datetime);
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

	@Override
	public String _toString(Current __current) {
		return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(
				new java.util.Date(datetime));
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
	public Date create(String type) {
		if (type.equals(ice_staticId())) {
			return new Date();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
