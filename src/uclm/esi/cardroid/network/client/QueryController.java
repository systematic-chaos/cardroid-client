package uclm.esi.cardroid.network.client;

import uclm.esi.cardroid.zerocice.CardroidManagerPrx;
import Ice.ObjectPrx;
import android.os.Handler;

/**
 * \class QueryController
 * Responsible for managing the execution and results retrieval of operations 
 * against the platform's server on behalf of this client
 */
public abstract class QueryController {
	
	/**
	 * \interface QueryListener
	 * Specification of the public callback methods susceptible of being 
	 * called upon the completion of an asynchronous operation against the 
	 * platform's server
	 */
	public interface QueryListener {
		void onError();

		void onDataChange(QueryModel data, boolean saved);

		void onDataChange(ObjectPrx data, boolean saved);

		void onDataChange(double data, boolean saved);

		void onDataChange(boolean data, boolean saved);
	};

	/// Handler used by this QueryController instance to post messages and events
	protected Handler _handler;
	/// Proxy to the CardroidManager remote object for the current user session
	protected CardroidManagerPrx _manager;
	/// Reference to the QueryListener whose implementation's methods will be
	/// called upon the completion of any asynchronous operation against the 
	/// platform's server executed by this QueryController instance
	protected QueryListener _listener;
	protected String _lastError;

	/**
	 * Classes subclassing QueryController must specify their behavior
	 * upon any event in the execution of the query being currently performed
	 * @param saved Whether the operation that caused this call has already 
	 * 			successfully finished
	 */
	abstract protected void postDataChanged(final boolean saved);

	/**
	 * Post the provided string error message via this instance's _listener
	 * @param string
	 */
	protected synchronized void postError(final String string) {
		_lastError = string;
		if (_listener != null) {
			_handler.post(new Runnable() {
				public void run() {
					_listener.onError();
				}
			});
		}
	}


	/**
	 * Constructor which creates an empty query
	 */
	public QueryController(Handler handler, CardroidManagerPrx manager) {
		_handler = handler;
		_manager = manager;
	}

	/**
	 * Set the QueryListener for this instance, whose methods implementation
	 * will be provided as the callback methods in the execution of any query
	 * against the platform's server
	 * @param cb The new QueryListener
	 */
	abstract public void setListener(QueryListener cb);

	/**
	 * @return The last error raised in this QueryController , if such
	 * 			an error took place (otherwise, return null)
	 */
	public synchronized String getLastError() {
		return _lastError;
	}

	public synchronized void clearLastError() {
		_lastError = null;
	}

	/**
	 * Classes subclassing QueryController must provide an implementation of 
	 * their behavior when the QueryController context is destroyed
	 * (typically, perform a cleanup and aborting ongoing operations)
	 */
	abstract public void destroy();
}
