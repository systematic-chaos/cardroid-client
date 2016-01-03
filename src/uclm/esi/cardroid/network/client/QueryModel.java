package uclm.esi.cardroid.network.client;

import java.util.List;

import uclm.esi.cardroid.Callback_QueryResult_destroy;
import uclm.esi.cardroid.Callback_QueryResult_next;
import uclm.esi.cardroid.QueryResultPrx;
import uclm.esi.cardroid.zerocice.CardroidManagerPrx;
import android.os.Handler;
import Ice.LocalException;
import Ice.ObjectPrx;

public class QueryModel extends QueryController {
	public List<ObjectPrx> data;
	public int nRows;
	public int rowsQueried;
	public ObjectPrx currentEntry;
	public QueryResultPrx query;

	public QueryModel(Handler handler, CardroidManagerPrx manager,
			final QueryListener listener) {
		super(handler, manager);
		_listener = listener;
	}

	public synchronized void setListener(QueryListener cb) {
		_listener = cb;
		_listener.onDataChange(this, false);
		if (_lastError != null) {
			_listener.onError();
		}
	}
	
	public synchronized void getMore(int position) {
		assert position < nRows;
		if (position < rowsQueried) {
			return;
		}

		Callback_QueryResult_next cb = new Callback_QueryResult_next() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(final List<ObjectPrx> ret,
					final boolean destroyed) {
				synchronized (QueryModel.this) {
					data = ret;
					nRows += ret.size();
					postDataChanged(true);
				}
			}
		};
		query.begin_next(10, cb);
		rowsQueried += 10;
	}

	protected synchronized void postDataChanged(final boolean saved) {
		if (_listener != null) {
			_handler.post(new Runnable() {
				public void run() {
					_listener.onDataChange(QueryModel.this, saved);
				}
			});
		}
	}

	public synchronized void destroy() {
		if (query != null) {
			query.begin_destroy(new Callback_QueryResult_destroy() {

				@Override
				public void response() {
				}

				@Override
				public void exception(LocalException ex) {
				}
			});
			query = null;
		}
	}
}
