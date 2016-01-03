package uclm.esi.cardroid.network.client;

import java.util.ArrayList;
import java.util.List;

import uclm.esi.cardroid.QueryResultPrx;
import uclm.esi.cardroid.QueryResultPrxHolder;
import uclm.esi.cardroid.ResultSeqHolder;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrx;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.TripRequestTyp;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getMessageTalks;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getMessageTalksSpeakers;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getPassengerTrips;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getUserActivity;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getUserPlaces;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getUserTrips;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_searchTrips;
import uclm.esi.cardroid.zerocice.CardroidManagerPrx;

import android.os.Handler;

import Ice.IntHolder;
import Ice.LocalException;
import Ice.ObjectPrx;

/**
 * \class DBProcedureController
 * Class responsible for managing the execution of operations against the 
 * platform's server which produce an scrollable set of results of an 
 * indeterminate length. These operations are named "queries"
 * Since this operations usually take some time to complete, this class
 * only provides functions to invoke them in an asynchronous way
 */
public class DBQueryController extends QueryController {
	public enum QueryType {
		GET_USER_PLACES, SEARCH_TRIPS, GET_USER_TRIPS, GET_PASSENGER_TRIPS, GET_MESSAGE_TALKS, GET_MESSAGE_TALKS_SPEAKERS, GET_USER_ACTIVITY
	};

	public static final int NO_ENTRY = -1;
	public static final int NEW_ENTRY = -2;

	private ArrayList<ObjectPrx> _data = new ArrayList<ObjectPrx>();
	private int _nrows = 0;
	private int _rowsQueried = 0;
	private QueryResultPrx _query = null;
	private int _currentEntry = NO_ENTRY;

	protected synchronized void postDataChanged(final boolean saved) {
		if (_listener != null) {
			_handler.post(new Runnable() {
				public void run() {
					_listener.onDataChange(getQueryModel(), saved);
				}
			});
		}
	}

	/**
	 * Store the result of the ongoing operation which has finished
	 * and sent us back its result
	 * @param result The operation's result
	 */
	private synchronized void queryResponse(List<ObjectPrx> first, int nrows,
			QueryResultPrx result) {
		_data.clear();
		_nrows = nrows;
		_data.addAll(first);
		_query = result;
		if (_listener != null) {
			postDataChanged(true);
		}
	}

	/**
	 * @return An instance of QueryModel holding the data of the current query
	 */
	private synchronized QueryModel getQueryModel() {
		QueryModel model = new QueryModel(_handler, _manager, _listener);
		model.data = new ArrayList<ObjectPrx>(_data);
		model.nRows = _nrows;
		model.rowsQueried = _rowsQueried;
		if (_currentEntry != NO_ENTRY) {
			model.currentEntry = _data.get(_currentEntry);
		}
		model.query = _query;
		return model;
	}

	/**
	 * Constructor which creates an empty query
	 */
	public DBQueryController(Handler handler, CardroidManagerPrx manager) {
		super(handler, manager);
	}

	/**
	 * Constructor which creates and begins the asynchronous execution of a 
	 * query against the platform server
	 * @param handler	The handler used by this QueryController instance to 
	 * 					post messages and events
	 * @param manager	Proxy to the CardroidManager remote object of the 
	 * 					current user session
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation to be
	 * 					executed
	 * @param type		Type of the query to be executed
	 * @param params	Arguments to be passed to the operation invocation
	 */
	public DBQueryController(Handler handler, CardroidManagerPrx manager,
			final QueryListener listener, final QueryType type,
			final Object... params) {
		this(handler, manager);
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		switch (type) {
		case GET_USER_PLACES: {
			Callback_CardroidManager_getUserPlaces cb = new Callback_CardroidManager_getUserPlaces() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, nrows, result);
				}
			};
			_manager.begin_getUserPlaces((UserTypPrx) params[0], 10, cb);
		}
			break;

		case SEARCH_TRIPS: {
			Callback_CardroidManager_searchTrips cb = new Callback_CardroidManager_searchTrips() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			_manager.begin_searchTrips((TripRequestTyp) params[0], 10, cb);
		}
			break;

		case GET_USER_TRIPS: {
			Callback_CardroidManager_getUserTrips cb = new Callback_CardroidManager_getUserTrips() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			_manager.begin_getUserTrips((UserTypPrx) params[0], 10, cb);
		}
			break;

		case GET_PASSENGER_TRIPS: {
			Callback_CardroidManager_getPassengerTrips cb = new Callback_CardroidManager_getPassengerTrips() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			_manager.begin_getPassengerTrips((UserTypPrx) params[0], 10, cb);
		}
			break;

		case GET_MESSAGE_TALKS: {
			Callback_CardroidManager_getMessageTalks cb = new Callback_CardroidManager_getMessageTalks() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			_manager.begin_getMessageTalks((UserTypPrx) params[0],
					(UserTypPrx) params[1], 10, cb);
		}
			break;

		case GET_MESSAGE_TALKS_SPEAKERS: {
			Callback_CardroidManager_getMessageTalksSpeakers cb = new Callback_CardroidManager_getMessageTalksSpeakers() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			manager.begin_getMessageTalksSpeakers((UserTypPrx) params[0], 10,
					cb);
		}
			break;

		case GET_USER_ACTIVITY: {
			Callback_CardroidManager_getUserActivity cb = new Callback_CardroidManager_getUserActivity() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(List<ObjectPrx> first, int nrows,
						QueryResultPrx result) {
					queryResponse(first, 10, result);
				}
			};
			_manager.begin_getUserActivity((UserTypPrx) params[0], 10, cb);
		}
			break;
		}
	}

	public synchronized void destroy() {
		//getQueryModel().destroy();
		_query = null;
	}

	public synchronized void setListener(QueryListener cb) {
		_listener = cb;
		_listener.onDataChange(getQueryModel(), false);
		if (_lastError != null) {
			_listener.onError();
		}
	}

	public synchronized boolean setCurrent(int row) {
		if (row < _data.size()) {
			_currentEntry = row;
			return true;
		}
		return false;
	}

	/**
	 * Execute the "getPlaces" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getUserPlaces(QueryListener listener,
			UserTypPrx usr) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getUserPlaces cb = new Callback_CardroidManager_getUserPlaces() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, nrows, result);
			}
		};
		_manager.begin_getUserPlaces(usr, 10, cb);
	}

	/**
	 * Execute the "searchTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void searchTrips(QueryListener listener,
			TripRequestTyp tRequest) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_searchTrips cb = new Callback_CardroidManager_searchTrips() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_searchTrips(tRequest, 10, cb);
	}

	/**
	 * Execute the "getUserTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getUserTrips(QueryListener listener, UserTypPrx usr) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getUserTrips cb = new Callback_CardroidManager_getUserTrips() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_getUserTrips(usr, 10, cb);
	}

	/**
	 * Execute the "getPassengerTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getPassengerTrips(QueryListener listener,
			UserTypPrx passenger) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getPassengerTrips cb = new Callback_CardroidManager_getPassengerTrips() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_getPassengerTrips(passenger, 10, cb);
	}

	/**
	 * Execute the "getMessageTalks" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getMessageTalks(QueryListener listener,
			UserTypPrx usr1, UserTypPrx usr2) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getMessageTalks cb = new Callback_CardroidManager_getMessageTalks() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_getMessageTalks(usr1, usr2, 10, cb);
	}

	/**
	 * Execute the "getMessageTalksSpeakers" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getMessageTalksSpeakers(QueryListener listener,
			UserTypPrx usr) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getMessageTalksSpeakers cb = new Callback_CardroidManager_getMessageTalksSpeakers() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_getMessageTalksSpeakers(usr, 10, cb);
	}

	/**
	 * Execute the "getUserActivity" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getUserActivity(QueryListener listener,
			UserTypPrx usr) {
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(getQueryModel(), false);
		_rowsQueried = 10;

		Callback_CardroidManager_getUserActivity cb = new Callback_CardroidManager_getUserActivity() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(List<ObjectPrx> first, int nrows,
					QueryResultPrx result) {
				queryResponse(first, 10, result);
			}
		};
		_manager.begin_getUserActivity(usr, 10, cb);
	}

	/**
	 * Execute the "getUserPlaces" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<PlaceTypPrx> getUserPlaces(UserTypPrx usr, int n) {
		_rowsQueried = n;
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getUserPlaces(usr, n, first, nRows, new QueryResultPrxHolder());
		List<PlaceTypPrx> result = new ArrayList<PlaceTypPrx>(nRows.value);
		for (ObjectPrx o : first.value)
			result.add((PlaceTypPrx) o);
		return result;
	}

	/**
	 * Execute the "searchTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<TripTypPrx> searchTrips(TripRequestTyp tRequest,
			int n) {
		_rowsQueried = n;
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.searchTrips(tRequest, n, first, nRows,
				new QueryResultPrxHolder());
		List<TripTypPrx> result = new ArrayList<TripTypPrx>(nRows.value);
		for (ObjectPrx o : first.value)
			result.add((TripTypPrx) o);
		return result;
	}

	/**
	 * Execute the "getUserTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<TripOfferTypPrx> getUserTrips(UserTypPrx usr, int n) {
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getUserTrips(usr, n, first, nRows, new QueryResultPrxHolder());
		List<TripOfferTypPrx> result = new ArrayList<TripOfferTypPrx>(
				nRows.value);
		for (ObjectPrx o : first.value)
			result.add((TripOfferTypPrx) o);
		return result;
	}

	/**
	 * Execute the "getPassengerTrips" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<TripOfferTypPrx> getPassengerTrips(
			UserTypPrx passenger, int n) {
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getPassengerTrips(passenger, n, first, nRows,
				new QueryResultPrxHolder());
		List<TripOfferTypPrx> result = new ArrayList<TripOfferTypPrx>(
				nRows.value);
		for (ObjectPrx o : first.value)
			result.add((TripOfferTypPrx) o);
		return result;
	}

	/**
	 * Execute the "getMessageTalks" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<MessageTypPrx> getMessageTalks(UserTypPrx usr1,
			UserTypPrx usr2, int n) {
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getMessageTalks(usr1, usr2, n, first, nRows,
				new QueryResultPrxHolder());
		List<MessageTypPrx> result = new ArrayList<MessageTypPrx>(nRows.value);
		for (ObjectPrx o : first.value)
			result.add((MessageTypPrx) o);
		return result;
	}

	/**
	 * Execute the "getMessageTalksSpeakers" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<UserTypPrx> getMessageTalksSpeakers(
			UserTypPrx usr, int n) {
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getMessageTalksSpeakers(usr, n, first, nRows,
				new QueryResultPrxHolder());
		List<UserTypPrx> result = new ArrayList<UserTypPrx>(nRows.value);
		for (ObjectPrx o : first.value)
			result.add((UserTypPrx) o);
		return result;
	}

	/**
	 * Execute the "getUserActivity" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized List<UserActivityTypPrx> getUserActivity(
			UserTypPrx usr, int n) {
		ResultSeqHolder first = new ResultSeqHolder();
		IntHolder nRows = new IntHolder();
		_manager.getUserActivity(usr, n, first, nRows,
				new QueryResultPrxHolder());
		List<UserActivityTypPrx> result = new ArrayList<UserActivityTypPrx>(
				nRows.value);
		for (ObjectPrx o : first.value)
			result.add((UserActivityTypPrx) o);
		return result;
	}
}
