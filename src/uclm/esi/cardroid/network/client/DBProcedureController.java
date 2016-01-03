package uclm.esi.cardroid.network.client;

import uclm.esi.cardroid.data.ice.Car;
import uclm.esi.cardroid.data.ice.TripOffer;
import uclm.esi.cardroid.data.ice.TripRequest;
import uclm.esi.cardroid.data.ice.User;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.Fuel;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.TripRequestTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_addCar;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_addCarEmail;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_calculatePriceEstimation;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getCarFromPlate;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getCarFromPlateEmail;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getTripFromId;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getTripOfferFromId;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getTripRequestFromId;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_getUserFromEmail;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_joinTrip;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_newMessage;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_newTripOffer;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_newTripRequest;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_organizeTrip;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_removeCar;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_removeCarPlateEmail;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_updateCarData;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_updateCarDataEmail;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_updateUserData;
import uclm.esi.cardroid.zerocice.Callback_CardroidManager_userTripRegistered;
import uclm.esi.cardroid.zerocice.CardroidManagerPrx;
import android.os.Handler;
import Ice.LocalException;
import Ice.ObjectPrx;

/**
 * \class DBProcedureController
 * Class responsible for managing the execution of operations against the 
 * platform's server which produce a single result, if any, instead of
 * an scrollable set of results of an indeterminate length 
 * (is DBQueryController who manages that)
 * This class introduces two terms which must be noted to understand its 
 * behavior:
 *  - Operations which just perform a query on the server, always 
 *    getting a simple result back and not causing any data modification in 
 *    the server, are called "functions"
 *  - Operations which perform an operation on the server, causing a data 
 *    modification in it, and not always get a result back, are called
 *    "procedures"
 * This class provides operations to invoke functions and procedures in both 
 * synchronous and asynchronous ways, attending to the needs of the developer
 */
public class DBProcedureController extends QueryController {
	public enum FunctionType {
		GET_TRIP_FROM_ID, GET_TRIP_OFFER_FROM_ID, GET_TRIP_REQUEST_FROM_ID, CALCULATE_PRICE_ESTIMATION, GET_USER_FROM_EMAIL, GET_CAR_FROM_PLATE, GET_CAR_FROM_PLATE_EMAIL, USER_TRIP_REGISTERED
	};

	public enum ProcedureType {
		JOIN_TRIP, ORGANIZE_TRIP, NEW_TRIP_OFFER, NEW_TRIP_REQUEST, NEW_MESSAGE, UPDATE_USER_DATA, UPDATE_CAR_DATA, UPDATE_CAR_DATA_EMAIL, ADD_CAR, ADD_CAR_EMAIL, REMOVE_CAR, REMOVE_CAR_PLATEEMAIL
	};

	private ObjectPrx _dataResult;
	private double _doubleResult;
	private boolean _boolResult;
	protected boolean _saved;
	protected FunctionType _functionType;
	protected ProcedureType _procedureType;

	protected synchronized void postDataChanged(final boolean saved) {
		if (_listener != null) {
			_handler.post(new Runnable() {
				public void run() {
					if (_functionType == FunctionType.CALCULATE_PRICE_ESTIMATION)
						_listener.onDataChange(_doubleResult, saved);
					else if (_functionType == FunctionType.USER_TRIP_REGISTERED)
						_listener.onDataChange(_boolResult, saved);
					else
						_listener.onDataChange(_dataResult, saved);
				}
			});
		}
	}

	/**
	 * Store the result of the ongoing operation which has finished
	 * and sent back an ObjectPrx result
	 * @param result The operation's result
	 */
	private synchronized void procedureResponse(ObjectPrx result) {
		_dataResult = result;
		if (_listener != null) {
			postDataChanged(true);
		}
	}

	/**
	 * Store the result of the ongoing operation which has finished
	 * and sent back a double precision number result 
	 * @param result The operation's result
	 */
	private synchronized void functionResponse(double result) {
		_doubleResult = result;
		if (_listener != null) {
			postDataChanged(true);
		}
	}

	/**
	 * Store the result of the ongoing operation which has finished
	 * and sent back a boolean result
	 * @param result The operation's result
	 */
	private synchronized void functionResponse(boolean result) {
		_boolResult = result;
		if (_listener != null) {
			postDataChanged(true);
		}
	}

	/**
	 * Constructor which creates an empty query
	 */
	public DBProcedureController(Handler handler, CardroidManagerPrx manager) {
		super(handler, manager);
	}

	/**
	 * Constructor which creates and begins the asynchronous execution of a 
	 * function against the platform's server
	 * @param handler	The handler used by this QueryController instance to 
	 * 					post messages and events
	 * @param manager	Proxy to the CardroidManager remote object of the 
	 * 					current user session
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation to be
	 * 					executed
	 * @param type		Type of the function to be executed
	 * @param params	Arguments to be passed to the operation invocation
	 */
	public DBProcedureController(Handler handler, CardroidManagerPrx manager,
			final QueryListener listener, final FunctionType type,
			final Object... params) {
		this(handler, manager);
		_listener = listener;
		_functionType = type;
		_procedureType = null;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		switch (type) {
		case CALCULATE_PRICE_ESTIMATION: {
			Callback_CardroidManager_calculatePriceEstimation cb = new Callback_CardroidManager_calculatePriceEstimation() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(double ret) {
					functionResponse(ret);
				}
			};
			_manager.begin_calculatePriceEstimation((Fuel) params[0],
					(Integer) params[1], cb);
		}
			break;
		case GET_CAR_FROM_PLATE: {
			Callback_CardroidManager_getCarFromPlate cb = new Callback_CardroidManager_getCarFromPlate() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_getCarFromPlate((String) params[0],
					(UserTypPrx) params[1], cb);
		}
			break;
		case GET_CAR_FROM_PLATE_EMAIL: {
			Callback_CardroidManager_getCarFromPlateEmail cb = new Callback_CardroidManager_getCarFromPlateEmail() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_getCarFromPlateEmail((String) params[0],
					(String) params[1], cb);
		}
			break;
		case GET_TRIP_FROM_ID: {
			Callback_CardroidManager_getTripFromId cb = new Callback_CardroidManager_getTripFromId() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_getTripFromId((Integer) params[0], cb);
		}
			break;
		case GET_TRIP_OFFER_FROM_ID: {
			Callback_CardroidManager_getTripOfferFromId cb = new Callback_CardroidManager_getTripOfferFromId() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripOfferTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_getTripOfferFromId((Integer) params[0], cb);
		}
			break;
		case GET_TRIP_REQUEST_FROM_ID: {
			Callback_CardroidManager_getTripRequestFromId cb = new Callback_CardroidManager_getTripRequestFromId() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripRequestTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_getTripRequestFromId((Integer) params[0], cb);
		}
		case GET_USER_FROM_EMAIL: {
			Callback_CardroidManager_getUserFromEmail cb = new Callback_CardroidManager_getUserFromEmail() {

				@Override
				public void exception(LocalException __ex) {
					postError(__ex.toString());
				}

				@Override
				public void response(UserTypPrx __ret) {
					procedureResponse(__ret);
				}
			};
			_manager.begin_getUserFromEmail((String) params[0], cb);
		}
			break;
		case USER_TRIP_REGISTERED: {
			Callback_CardroidManager_userTripRegistered cb = new Callback_CardroidManager_userTripRegistered() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(boolean ret) {
					functionResponse(ret);
				}
			};
			_manager.begin_userTripRegistered((UserTypPrx) params[0],
					(TripTypPrx) params[1], cb);
		}
			break;
		}
	}

	/**
	 * Constructor which creates and begins the asynchronous execution of a 
	 * procedure against the platform's server
	 * @param handler	The handler used by this QueryController instance to 
	 * 					post messages and events
	 * @param manager	Proxy to the CardroidManager remote object of the 
	 * 					current user session
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation to be
	 * 					executed
	 * @param type		Type of the procedure to be executed
	 * @param params	Arguments to be passed to the operation invocation
	 */
	public DBProcedureController(Handler handler, CardroidManagerPrx manager,
			final QueryListener listener, final ProcedureType type,
			final Object... params) {
		this(handler, manager);
		_listener = listener;
		_procedureType = type;
		_functionType = null;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		switch (type) {
		case JOIN_TRIP: {
			Callback_CardroidManager_joinTrip cb = new Callback_CardroidManager_joinTrip() {
				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response() {
					procedureResponse((TripOfferTypPrx) params[0]);
				}
			};
			_manager.begin_joinTrip((TripOfferTypPrx) params[0],
					(UserTypPrx) params[1], (Integer) params[2], cb);
		}
			break;

		case ORGANIZE_TRIP: {
			Callback_CardroidManager_organizeTrip cb = new Callback_CardroidManager_organizeTrip() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripOfferTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_organizeTrip((TripRequestTypPrx) params[0],
					(TripOffer) params[1], cb);
		}
			break;
		case NEW_MESSAGE: {
			Callback_CardroidManager_newMessage cb = new Callback_CardroidManager_newMessage() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(MessageTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_newMessage((UserTypPrx) params[0],
					(UserTypPrx) params[1], (String) params[2], cb);
		}
			break;
		case NEW_TRIP_OFFER: {
			Callback_CardroidManager_newTripOffer cb = new Callback_CardroidManager_newTripOffer() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripOfferTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_newTripOffer((TripOffer) params[0], cb);
		}
			break;
		case NEW_TRIP_REQUEST: {
			Callback_CardroidManager_newTripRequest cb = new Callback_CardroidManager_newTripRequest() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(TripRequestTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_newTripRequest((TripRequest) params[0], cb);
		}
			break;
		case UPDATE_CAR_DATA: {
			Callback_CardroidManager_updateCarData cb = new Callback_CardroidManager_updateCarData() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_updateCarData((Car) params[0], (User) params[1], cb);
		}
			break;
		case UPDATE_CAR_DATA_EMAIL: {
			Callback_CardroidManager_updateCarDataEmail cb = new Callback_CardroidManager_updateCarDataEmail() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_updateCarDataEmail((Car) params[0],
					(String) params[1], cb);
		}
			break;
		case UPDATE_USER_DATA: {
			Callback_CardroidManager_updateUserData cb = new Callback_CardroidManager_updateUserData() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(UserTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_updateUserData((User) params[0], cb);
		}
			break;
		case ADD_CAR: {
			Callback_CardroidManager_addCar cb = new Callback_CardroidManager_addCar() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_addCar((Car) params[0], (UserTypPrx) params[1], cb);
		}
			break;
		case ADD_CAR_EMAIL: {
			Callback_CardroidManager_addCarEmail cb = new Callback_CardroidManager_addCarEmail() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response(CarTypPrx ret) {
					procedureResponse(ret);
				}
			};
			_manager.begin_addCarEmail((Car) params[0], (String) params[1], cb);
		}
			break;
		case REMOVE_CAR: {
			Callback_CardroidManager_removeCar cb = new Callback_CardroidManager_removeCar() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response() {
					procedureResponse(null);
				}
			};
			_manager.begin_removeCar((CarTypPrx) params[0],
					(UserTypPrx) params[1], cb);
		}
			break;
		case REMOVE_CAR_PLATEEMAIL: {
			Callback_CardroidManager_removeCarPlateEmail cb = new Callback_CardroidManager_removeCarPlateEmail() {

				@Override
				public void exception(LocalException ex) {
					postError(ex.toString());
				}

				@Override
				public void response() {
					procedureResponse(null);
				}
			};
			_manager.begin_removeCarPlateEmail((String) params[0],
					(String) params[1], cb);
		}
			break;
		}
	}

	public synchronized void setListener(QueryListener cb) {
		_listener = cb;
		_listener.onDataChange(_dataResult, _saved);
		if (_lastError != null) {
			_listener.onError();
		}
	}

	public synchronized void destroy() {
		_dataResult = null;
		_doubleResult = 0.;
		_boolResult = false;
	}

	public synchronized void getTripFromId(QueryListener listener, int tripId) {
		_functionType = FunctionType.GET_TRIP_FROM_ID;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getTripFromId cb = new Callback_CardroidManager_getTripFromId() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_getTripFromId(tripId, cb);
	}

	public synchronized void getTripOfferFromId(QueryListener listener,
			int tripId) {
		_functionType = FunctionType.GET_TRIP_OFFER_FROM_ID;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getTripOfferFromId cb = new Callback_CardroidManager_getTripOfferFromId() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripOfferTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_getTripOfferFromId(tripId, cb);
	}

	public synchronized void getTripRequestFromId(QueryListener listener,
			int tripId) {
		_functionType = FunctionType.GET_TRIP_REQUEST_FROM_ID;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getTripRequestFromId cb = new Callback_CardroidManager_getTripRequestFromId() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripRequestTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_getTripRequestFromId(tripId, cb);
	}

	/**
	 * Execute the "calculatePriceEstimation" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void calculatePriceEstimation(QueryListener listener,
			Fuel fuel, int distance) {
		_functionType = FunctionType.CALCULATE_PRICE_ESTIMATION;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_calculatePriceEstimation cb = new Callback_CardroidManager_calculatePriceEstimation() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(double ret) {
				functionResponse(ret);
			}
		};
		_manager.begin_calculatePriceEstimation(fuel, distance, cb);
	}

	/**
	 * Execute the "getUserFromEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getUserFromEmail(QueryListener listener,
			String email) {
		_functionType = FunctionType.GET_USER_FROM_EMAIL;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getUserFromEmail cb = new Callback_CardroidManager_getUserFromEmail() {

			@Override
			public void exception(LocalException __ex) {
				postError(__ex.toString());
			}

			@Override
			public void response(UserTypPrx __ret) {
				procedureResponse(__ret);
			}
		};
		_manager.begin_getUserFromEmail(email, cb);
	}

	/**
	 * Execute the "userTripRegistered" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void userTripRegistered(QueryListener listener,
			UserTypPrx usr, TripTypPrx trip) {
		_functionType = FunctionType.USER_TRIP_REGISTERED;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(false, false);

		Callback_CardroidManager_userTripRegistered cb = new Callback_CardroidManager_userTripRegistered() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(boolean ret) {
				functionResponse(ret);
			}
		};
		_manager.begin_userTripRegistered(usr, trip, cb);
	}

	/**
	 * Execute the "getCarFromPlate" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getCarFromPlate(QueryListener listener,
			String plate, UserTypPrx owner) {
		_functionType = FunctionType.GET_CAR_FROM_PLATE;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getCarFromPlate cb = new Callback_CardroidManager_getCarFromPlate() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_getCarFromPlate(plate, owner, cb);
	}

	/**
	 * Execute the "getCarFromPlateEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void getCarFromPlateEmail(QueryListener listener,
			String plate, String ownerEmail) {
		_functionType = FunctionType.GET_CAR_FROM_PLATE_EMAIL;
		_procedureType = null;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_getCarFromPlateEmail cb = new Callback_CardroidManager_getCarFromPlateEmail() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_getCarFromPlateEmail(plate, ownerEmail, cb);
	}

	/**
	 * Execute the "joinTrip" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void joinTrip(QueryListener listener,
			final TripOfferTypPrx trip, UserTypPrx passenger, int nSeats) {
		_functionType = null;
		_procedureType = ProcedureType.JOIN_TRIP;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_joinTrip cb = new Callback_CardroidManager_joinTrip() {
			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response() {
				procedureResponse(trip);
			}
		};
		_manager.begin_joinTrip(trip, passenger, nSeats, cb);
	}

	/**
	 * Execute the "organizeTrip" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void organizeTrip(QueryListener listener,
			TripRequestTypPrx tripRequest, TripOffer tripOffer) {
		_functionType = null;
		_procedureType = ProcedureType.ORGANIZE_TRIP;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_organizeTrip cb = new Callback_CardroidManager_organizeTrip() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripOfferTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_organizeTrip(tripRequest, tripOffer, cb);
	}

	/**
	 * Execute the "newTripOffer" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void newTripOffer(QueryListener listener,
			TripOffer tripOffer) {
		_functionType = null;
		_procedureType = ProcedureType.NEW_TRIP_OFFER;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_newTripOffer cb = new Callback_CardroidManager_newTripOffer() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripOfferTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_newTripOffer(tripOffer, cb);
	}

	/**
	 * Execute the "newTripRequest" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void newTripRequest(QueryListener listener,
			TripRequest tripRequest) {
		_functionType = null;
		_procedureType = ProcedureType.NEW_TRIP_REQUEST;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_newTripRequest cb = new Callback_CardroidManager_newTripRequest() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(TripRequestTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_newTripRequest(tripRequest, cb);
	}

	/**
	 * Execute the "newMessage" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void newMessage(QueryListener listener,
			UserTypPrx user1, UserTypPrx user2, String message) {
		_functionType = null;
		_procedureType = ProcedureType.NEW_MESSAGE;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_newMessage cb = new Callback_CardroidManager_newMessage() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(MessageTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_newMessage(user1, user2, message, cb);
	}

	/**
	 * Execute the "updateUserData" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void updateUserData(QueryListener listener, User user) {
		_functionType = null;
		_procedureType = ProcedureType.UPDATE_USER_DATA;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_updateUserData cb = new Callback_CardroidManager_updateUserData() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(UserTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_updateUserData(user, cb);
	}

	/**
	 * Execute the "updateCarData" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void updateCarData(QueryListener listener, Car car,
			User owner) {
		_functionType = null;
		_procedureType = ProcedureType.UPDATE_CAR_DATA;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_updateCarData cb = new Callback_CardroidManager_updateCarData() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_updateCarData(car, owner, cb);
	}

	/**
	 * Execute the "updateCarDataEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void updateCarDataEmail(QueryListener listener,
			Car car, String ownerEmail) {
		_functionType = null;
		_procedureType = ProcedureType.UPDATE_CAR_DATA_EMAIL;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_updateCarDataEmail cb = new Callback_CardroidManager_updateCarDataEmail() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_updateCarDataEmail(car, ownerEmail, cb);
	}

	/**
	 * Execute the "addCar" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void addCar(QueryListener listener, Car car,
			UserTypPrx usr) {
		_functionType = null;
		_procedureType = ProcedureType.UPDATE_CAR_DATA;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_addCar cb = new Callback_CardroidManager_addCar() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_addCar(car, usr, cb);
	}

	/**
	 * Execute the "addCarEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void addCarEmail(QueryListener listener, Car car,
			String usrEmail) {
		_functionType = null;
		_procedureType = ProcedureType.UPDATE_CAR_DATA_EMAIL;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_addCarEmail cb = new Callback_CardroidManager_addCarEmail() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response(CarTypPrx ret) {
				procedureResponse(ret);
			}
		};
		_manager.begin_addCarEmail(car, usrEmail, cb);
	}

	/**
	 * Execute the "removeCar" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void removeCar(QueryListener listener, CarTypPrx car,
			UserTypPrx usr) {
		_functionType = null;
		_procedureType = ProcedureType.REMOVE_CAR;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_removeCar cb = new Callback_CardroidManager_removeCar() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response() {
				procedureResponse(null);
			}
		};
		_manager.begin_removeCar(car, usr, cb);
	}

	/**
	 * Execute the "removeCarPlateEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 */
	public synchronized void removeCarPlateEmail(QueryListener listener,
			String plate, String ownerEmail) {
		_functionType = null;
		_procedureType = ProcedureType.REMOVE_CAR_PLATEEMAIL;
		_listener = listener;

		// Send the initial data change notification
		_listener.onDataChange(_dataResult, false);

		Callback_CardroidManager_removeCarPlateEmail cb = new Callback_CardroidManager_removeCarPlateEmail() {

			@Override
			public void exception(LocalException ex) {
				postError(ex.toString());
			}

			@Override
			public void response() {
				procedureResponse(null);
			}
		};
		_manager.begin_removeCarPlateEmail(plate, ownerEmail, cb);
	}

	/**
	 * Execute the "getTripFromId" operation synchronously
	 */
	public synchronized TripTypPrx getTripFromId(int tripId) {
		return _manager.getTripFromId(tripId);
	}

	/**
	 * Execute the "getTripOfferFromId" operation synchronously
	 */
	public synchronized TripOfferTypPrx getTripOfferFromId(int tripId) {
		return _manager.getTripOfferFromId(tripId);
	}

	/**
	 * Execute the "getTripRequestFromId" operation synchronously
	 */
	public synchronized TripRequestTypPrx getTripRequestFromId(int tripId) {
		return _manager.getTripRequestFromId(tripId);
	}

	/**
	 * Execute the "calculatePriceEstimation" operation synchronously
	 */
	public synchronized double calculatePriceEstimation(Fuel fuel, int distance) {
		return _manager.calculatePriceEstimation(fuel, distance);
	}

	/**
	 * Execute the "getUserFromEmail" operation synchronously
	 */
	public synchronized UserTypPrx getUserFromEmail(String email) {
		return _manager.getUserFromEmail(email);
	}

	/**
	 * Execute the "userTripRegistered" operation synchronously
	 */
	public synchronized boolean userTripRegistered(UserTypPrx usr,
			TripTypPrx trip) {
		return _manager.userTripRegistered(usr, trip);
	}

	/**
	 * Execute the "getCarFromPlate" operation synchronously
	 */
	public synchronized CarTypPrx getCarFromPlate(String plate, UserTypPrx owner) {
		return _manager.getCarFromPlate(plate, owner);
	}

	/**
	 * Execute the "getCarFromPlateEmail" operation synchronously
	 */
	public synchronized CarTypPrx getCarFromPlateEmail(String plate,
			String ownerEmail) {
		return _manager.getCarFromPlateEmail(plate, ownerEmail);
	}

	/**
	 * Execute the "joinTrip" operation synchronously
	 */
	public synchronized TripOfferTypPrx joinTrip(TripOfferTypPrx trip,
			UserTypPrx passenger, int nSeats) {
		_manager.joinTrip(trip, passenger, nSeats);
		return trip;
	}

	/**
	 * Execute the "organizeTrip" operation synchronously
	 */
	public synchronized TripOfferTypPrx organizeTrip(
			TripRequestTypPrx tripRequest, TripOffer tripOffer) {
		return _manager.organizeTrip(tripRequest, tripOffer);
	}

	/**
	 * Execute the "newTripOffer" operation synchronously
	 */
	public synchronized TripOfferTypPrx newTripOffer(TripOffer tripOffer) {
		return _manager.newTripOffer(tripOffer);
	}

	/**
	 * Execute the "newTripRequest" operation synchronously
	 */
	public synchronized TripRequestTypPrx newTripRequest(TripRequest tripRequest) {
		return _manager.newTripRequest(tripRequest);
	}

	/**
	 * Execute the "newMessage" operation synchronously
	 */
	public synchronized MessageTypPrx newMessage(UserTypPrx user1,
			UserTypPrx user2, String message) {
		return _manager.newMessage(user1, user2, message);
	}

	/**
	 * Execute the "updateUserData" operation synchronously
	 */
	public synchronized UserTypPrx updateUserData(User user) {
		return _manager.updateUserData(user);
	}

	/**
	 * Execute the "updateCarData" operation synchronously
	 */
	public synchronized CarTypPrx updateCarData(Car car, User owner) {
		return _manager.updateCarData(car, owner);
	}

	/**
	 * Execute the "updateCarDataEmail" operation synchronously
	 */
	public synchronized CarTypPrx updateCarDataEmail(Car car, String ownerEmail) {
		return _manager.updateCarDataEmail(car, ownerEmail);
	}

	/**
	 * Execute the "addCar" operation synchronously
	 */
	public synchronized CarTypPrx addCar(Car car, UserTypPrx usr) {
		return _manager.addCar(car, usr);
	}

	/**
	 * Execute the "addCarEmail" operation synchronously
	 */
	public synchronized CarTypPrx addCarEmail(Car car, String usrEmail) {
		return _manager.addCarEmail(car, usrEmail);
	}

	/**
	 * Execute the "removeCar" operation synchronously
	 */
	public synchronized void removeCar(CarTypPrx car, UserTypPrx usr) {
		_manager.removeCar(car, usr);
	}

	/**
	 * Execute the "removeCarPlateEmail" operation synchronously
	 */
	public synchronized void removeCarPlateEmail(String plate, String ownerEmail) {
		_manager.removeCarPlateEmail(plate, ownerEmail);
	}
}
