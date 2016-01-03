package uclm.esi.cardroid.util;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class ResultSetCursor implements Cursor {
	private ResultSet rs = null;

	public ResultSetCursor() {
	}

	public ResultSetCursor(ResultSet resultSet) {
		rs = resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		rs = resultSet;
	}

	public ResultSet getResultSet() {
		return rs;
	}

	public void close() {
		try {
			rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		String string = getString(columnIndex);
		buffer.data = string.toCharArray();
		buffer.sizeCopied = string.length();
	}

	@Deprecated
	public void deactivate() {
		// TODO Auto-generated method stub
	}

	public byte[] getBlob(int columnIndex) {
		byte[] result = null;
		try {
			Blob blob = rs.getBlob(columnIndex);
			result = blob.getBytes(1, (int) blob.length());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public int getColumnCount() {
		int columnCount = -1;
		try {
			columnCount = rs.getMetaData().getColumnCount();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return columnCount;
	}

	public int getColumnIndex(String columnName) {
		int columnIndex = -1;
		try {
			columnIndex = rs.findColumn(columnName);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return columnIndex;
	}

	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		int columnIndex = -1;
		try {
			columnIndex = rs.findColumn(columnName);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		if (columnIndex <= 0)
			throw new IllegalArgumentException();
		return columnIndex;
	}

	public String getColumnName(int columnIndex) {
		String columnName = null;
		try {
			columnName = rs.getMetaData().getColumnName(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return columnName;
	}

	public String[] getColumnNames() {
		String[] columnNames = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			columnNames = new String[rsmd.getColumnCount()];
			for (int n = 0; n < columnNames.length; n++)
				columnNames[n] = rsmd.getColumnName(n + 1);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return columnNames;
	}

	public int getCount() {
		int count = -1;
		try {
			int row = rs.getRow();
			if (rs.last())
				count = rs.getRow();
			rs.absolute(row);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return count;
	}

	public double getDouble(int columnIndex) {
		double result = 0.;
		try {
			result = rs.getDouble(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public Bundle getExtras() {
		return new Bundle();
	}

	public float getFloat(int columnIndex) {
		float result = 0;
		try {
			result = rs.getFloat(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public int getInt(int columnIndex) {
		int result = 0;
		try {
			result = rs.getInt(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public long getLong(int columnIndex) {
		long result = 0;
		try {
			result = rs.getLong(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public int getPosition() {
		int position = -1;
		try {
			position = rs.getRow();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return position;
	}

	public short getShort(int columnIndex) {
		short result = 0;
		try {
			result = rs.getShort(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public String getString(int columnIndex) {
		String result = null;
		try {
			result = rs.getString(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

	public int getType(int columnIndex) {
		int type = -1;
		try {
			type = rs.getMetaData().getColumnType(columnIndex);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return type;
	}

	public boolean getWantsAllOnMoveCalls() {
		return false;
	}

	public boolean isAfterLast() {
		boolean afterLast = false;
		try {
			afterLast = rs.isAfterLast();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return afterLast;
	}

	public boolean isBeforeFirst() {
		boolean beforeFirst = false;
		try {
			beforeFirst = rs.isBeforeFirst();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return beforeFirst;
	}

	public boolean isClosed() {
		boolean closed = false;
		try {
			rs.isClosed();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return closed;
	}

	public boolean isFirst() {
		boolean first = false;
		try {
			first = rs.isFirst();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return first;
	}

	public boolean isLast() {
		boolean last = false;
		try {
			last = rs.isLast();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return last;
	}

	public boolean isNull(int columnIndex) {
		boolean isNull = false;
		try {
			isNull = rs.getMetaData().isNullable(columnIndex) != 0;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return isNull;
	}

	public boolean move(int offset) {
		boolean movement = false;
		try {
			movement = rs.relative(offset);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public boolean moveToFirst() {
		boolean movement = false;
		try {
			movement = rs.first();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public boolean moveToLast() {
		boolean movement = false;
		try {
			movement = rs.last();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public boolean moveToNext() {
		boolean movement = false;
		try {
			movement = rs.next();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public boolean moveToPosition(int position) {
		boolean movement = false;
		try {
			movement = rs.absolute(position);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public boolean moveToPrevious() {
		boolean movement = false;
		try {
			movement = rs.previous();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return movement;
	}

	public void registerContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
	}

	@Deprecated
	public boolean requery() {
		// TODO Auto-generated method stub
		return false;
	}

	public Bundle respond(Bundle extras) {
		return extras;
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		// TODO Auto-generated method stub
	}

	public void unregisterContentObserver(ContentObserver observer) {
		// TODO Auto-generated method stub
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
	}
}
