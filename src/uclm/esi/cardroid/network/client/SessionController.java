package uclm.esi.cardroid.network.client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import uclm.esi.cardroid.data.ice.Car;
import uclm.esi.cardroid.data.ice.Date;
import uclm.esi.cardroid.data.ice.DateTime;
import uclm.esi.cardroid.data.ice.DateTimePrefs;
import uclm.esi.cardroid.data.ice.Message;
import uclm.esi.cardroid.data.ice.Passenger;
import uclm.esi.cardroid.data.ice.Place;
import uclm.esi.cardroid.data.ice.Trip;
import uclm.esi.cardroid.data.ice.TripOffer;
import uclm.esi.cardroid.data.ice.TripRequest;
import uclm.esi.cardroid.data.ice.User;
import uclm.esi.cardroid.data.ice.UserActivity;
import uclm.esi.cardroid.data.ice.Waypoint;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.Fuel;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrx;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.TripRequestTyp;
import uclm.esi.cardroid.data.zerocice.TripRequestTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.network.client.DBProcedureController.FunctionType;
import uclm.esi.cardroid.network.client.DBProcedureController.ProcedureType;
import uclm.esi.cardroid.network.client.DBQueryController.QueryType;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.network.zerocice.CardroidEventStormPrx;
import uclm.esi.cardroid.network.zerocice.CardroidEventStormPrxHelper;
import uclm.esi.cardroid.network.zerocice._CardroidEventStormDisp;
import uclm.esi.cardroid.network.zerocice._CardroidEventStormOperationsNC;
import Ice.Communicator;
import Ice.Current;
import Ice.Identity;
import Ice.LocalException;
import Ice.LocalObjectHolder;
import Ice.ObjectAdapter;
import Ice.ServantLocator;
import IceStorm.AlreadySubscribed;
import android.os.Handler;

/**
 * \class SessionController
 * Responsible for managing an established user session. Provides the 
 * operational interface used by client applications to interact with
 * the platform's server
 */
public class SessionController {
	private Handler _handler;
	private Communicator _communicator;
	private ObjectAdapter _adapter;
	private QueryController _queryController;
	private SessionListener _listener;
	private boolean _fatal = false;
	private boolean _destroyed = false;

	private SessionAdapter _session;
	private SessionRefreshThread _refresh;

	private UserTypPrx _myUser;

	private CardroidEventStormListener _eventListener;
	private CardroidEventStormPrx _eventProxy;
	private LinkedList<CardroidEventReplay> _replay = new LinkedList<CardroidEventReplay>();

	private static final int MAX_MESSAGES = 128;

	/**
	 * SessionController constructor method
	 * @param handler Handler for posting event messages
	 * @param communicator The communicator the session operates on
	 * @param session The SessionAdapter managing this session
	 * @param refreshTimeout The period on which the session will be refreshed 
	 * 							to avoid its destruction due the lack of 
	 * 							activity in the client, expressed in
	 * 							milliseconds
	 */
	public SessionController(Handler handler, Communicator communicator,
			SessionAdapter session, long refreshTimeout) {
		_communicator = communicator;
		_session = session;
		_handler = handler;

		/* Initialize and start the thread responsible for refreshing the *
		 * user session on the server                                     */
		_refresh = new SessionRefreshThread(refreshTimeout);
		_refresh.start();

		/* Initialize the QueryController used to query the server *
		 * (by means of the public operational interface provided) */
		_queryController = new DBQueryController(_handler,
				_session.getCardroidManager());

		// Setup the event monitor servant
		Identity eventStormId = new Identity();
		eventStormId.name = UUID.randomUUID().toString();
		Ice.RouterPrx r = _communicator.getDefaultRouter();
		
		/* Create an object adapter from the user session's Communicator,   *
		 * attending to whether it makes use of a router to redirect the    *
		 * data it carries. This will be the object adapter for the client, *
		 * responsible for providing incarnations to the proxies received   *
		 * from the server which may require it								*/
		if (r != null) {
			Glacier2.RouterPrx router = Glacier2.RouterPrxHelper.checkedCast(r);
			_adapter = _communicator.createObjectAdapterWithRouter(
					"CardroidClient", router);
			eventStormId.category = router.getCategoryForClient();
		} else {
			_adapter = _communicator.createObjectAdapter("CardroidClient");
			eventStormId.category = "";
		}

		/* Add a ServantLocator to the client's ObjectAdapter for every    *
		 * object category, mapping them to the corresponding datatypes,   *
		 * implemented on object classes. Whenever a proxy is added to the *
		 * client's ObjectAdapter, a servant incarnating the object        *
		 * referenced by such a proxy will be created					   */
		_adapter.addServantLocator(new LocatorI(new Place()), "place");
		_adapter.addServantLocator(new LocatorI(new Car()), "car");
		_adapter.addServantLocator(new LocatorI(new User()), "user");
		_adapter.addServantLocator(new LocatorI(new Trip()), "trip");
		_adapter.addServantLocator(new LocatorI(new TripOffer(this)),
				"trip_offer");
		_adapter.addServantLocator(new LocatorI(new TripRequest(this)),
				"trip_request");
		_adapter.addServantLocator(new LocatorI(new UserActivity(this)),
				"user_activity");
		_adapter.addServantLocator(new LocatorI(new Message(this)), "message");

		/* Add an ObjectFactory for each of the domain class datatypes the   *
		 * session's Communicator will make use of. Whenever the IceRuntime  *
		 * needs to instantiate a class characterized by an ID, it will call *
		 * the create(String) method of the ObjectFactory registered along   *
		 * with such an ID												 	 */
		_communicator.addObjectFactory(new Car(), Car.ice_staticId());
		_communicator.addObjectFactory(new Date(), Date.ice_staticId());
		_communicator.addObjectFactory(new DateTime(), DateTime.ice_staticId());
		_communicator.addObjectFactory(new DateTimePrefs(),
				DateTimePrefs.ice_staticId());
		_communicator.addObjectFactory(new Message(this),
				Message.ice_staticId());
		_communicator.addObjectFactory(new Passenger(this),
				Passenger.ice_staticId());
		_communicator.addObjectFactory(new Place(), Place.ice_staticId());
		_communicator.addObjectFactory(new Trip(), Trip.ice_staticId());
		_communicator.addObjectFactory(new TripOffer(this),
				TripOffer.ice_staticId());
		_communicator.addObjectFactory(new TripRequest(this),
				TripRequest.ice_staticId());
		_communicator.addObjectFactory(new User(), User.ice_staticId());
		_communicator.addObjectFactory(new UserActivity(this),
				UserActivity.ice_staticId());
		_communicator.addObjectFactory(new Waypoint(), Waypoint.ice_staticId());

		// Add the event monitor object to this client's ObjectAdapter
		_eventProxy = CardroidEventStormPrxHelper.uncheckedCast(_adapter.add(
				new CardroidEventStormI(), eventStormId));
		_adapter.activate();

		/* Associate the Connection used by this user session's       *
		 * CardroidManager instance with this client's ObjectAdapter, *
		 * in order to enable a bidirectional connection 			  */
		session.getCardroidManager().ice_getConnection().setAdapter(_adapter);

		// Subscribe the event monitor to the user's topic
		try {
			session.subscribeTopic(_eventProxy);
		} catch (AlreadySubscribed as) {
			// Already subscribed to topic
		}

		setupMyUser();
	}

	private synchronized void setupMyUser() {
		DBProcedureController functionController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		
		// Retrieve and keep a proxy to the user owning the current session 
		_myUser = functionController.getUserFromEmail(_session.getUserLogin());
		
		functionController.destroy();
	}

	/**
	 * Destroy the current user session
	 */
	public synchronized void destroy() {
		if (_destroyed) {
			return;
		}
		_destroyed = true;

		/* Since session destruction may take some time, the following tasks * 
		 * will be carried out in a parallel thread, so the normal operation *
		 * of the application will not be affected							 */
		new Thread(new Runnable() {
			public void run() {
				// Destroy the QueryController used by this SessionController
				_queryController.destroy();

				/* Unsubscribe the event monitor from the topic delivering *
				 * notifications related to the user who owns this session */
				_session.unsubscribeTopic(_eventProxy);

				/* Terminate the thread responsible for refreshing the	* 
				 * current user session on the server, wait until it is *
				 * actually destroyed							   		*/
				_refresh.terminate();
				while (_refresh.isAlive()) {
					try {
						_refresh.join();
					} catch (InterruptedException e) {
					}
				}

				/* Destroy the user session, via the SessionAdapter held by *
				 * this instance of SessionController	 				    */
				try {
					_session.destroy();
				} catch (Exception e) {
				}

				// Destroy the Communicator used by this user session
				try {
					_communicator.destroy();
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * Set the SessionListener for this SessionController
	 * @param listener The object implementing the callback methods called  
	 * 				    upon the occurrence of events related to the current 
	 * 					user session
	 */
	public synchronized void setSessionListener(SessionListener listener) {
		_listener = listener;
		if (_fatal) {
			listener.onDestroy();
		}
	}

	/**
	 * Create a new QueryController context for the execution of a query
	 * @param listener 	The object implementing the callback methods called 
	 * 						upon the asynchronous completion of the query
	 * @param t			The type of the query, according to QueryType
	 * @param q			The parameters to be passed as arguments to the query
	 * @return	A new QueryController holding a query to be performed against 
	 * 			the platform's server
	 */
	public synchronized QueryController createQuery(
			QueryController.QueryListener listener, QueryType t, Object... q) {
		_queryController.destroy();
		_queryController = new DBQueryController(_handler,
				_session.getCardroidManager(), listener, t, q);
		return _queryController;
	}

	/**
	 * Create a new QueryController context for the execution of a procedure
	 * @param listener 	The object implementing the callback methods called 
	 * 						upon the asynchronous completion of the procedure
	 * @param t			The type of the procedure, according to ProcedureType
	 * @param q			The parameters to be passed as arguments to the 
	 * 						procedure call
	 * @return	A new QueryController holding a procedure call to be performed 
	 * 			against the platform's server
	 */
	public synchronized QueryController createQuery(
			QueryController.QueryListener listener, ProcedureType t,
			Object... q) {
		_queryController.destroy();
		_queryController = new DBProcedureController(_handler,
				_session.getCardroidManager(), listener, t, q);
		return _queryController;
	}

	/**
	 * Create a new QueryController context for the execution of a function
	 * @param listener 	The object implementing the callback methods called 
	 * 						upon the asynchronous completion of the function
	 * @param t			The type of the procedure, according to FunctionType
	 * @param q			The parameters to be passed as arguments to the 
	 * 						function call
	 * @return	A new QueryController holding a function call to be performed 
	 * 			against the platform's server
	 */
	public synchronized QueryController createQuery(
			QueryController.QueryListener listener, FunctionType t, Object... q) {
		_queryController.destroy();
		_queryController = new DBProcedureController(_handler,
				_session.getCardroidManager(), listener, t, q);
		return _queryController;
	}

	/**
	 * @return An empty QueryController for calling a procedure or function
	 */
	public synchronized DBProcedureController createEmptyQuery() {
		_queryController.destroy();
		_queryController = new DBProcedureController(_handler,
				_session.getCardroidManager());
		return (DBProcedureController) _queryController;
	}

	/**
	 * @return The current query context
	 */
	public synchronized QueryController getCurrentQuery() {
		return _queryController;
	}

	/**
	 * @return A proxy to the remote object holding the user
	 * 			who owns this session
	 */
	public synchronized UserTypPrx getMyUser() {
		return _myUser;
	}

	/**
	 * @return The login (id) of the user who owns this session
	 */
	public synchronized String getMyLogin() {
		return _session.getUserLogin();
	}

	/**
	 * @return The ObjectAdapter used by this client for the sake of
	 * 			bidirectional communications over the current user session's
	 * 			Communicator. It provides ServantLocator s for the allocation
	 * 			of servants to every domain class the category of any received
	 * 			proxy may need to be incarnated in
	 */
	public synchronized ObjectAdapter getAdapter() {
		return _adapter;
	}

	/** Set the object providing the callback methods called by the monitor
	 * subscribed to the publishing of notifications in the topic corresponding
	 * to the current session's owner events. This allow the referenced methods
	 * implementation to vary without the need to unsubscribe the existing 
	 * subscriber to the topic and subscribe a new one with the new
	 * implementation. This method is only called by the UI thread.
	 * @param cess The object providing the callback methods called by the 
	 * 				monitor subscribed to the publishing of notifications in 
	 * 				the topic corresponding	to the current session's owner
	 * 				events 
	 * @param replay Whether the notifications delivered to the topic 
	 * 					subscriber should be replayed after the processing
	 * 					of its occurrence is completed (this is not likely to
	 * 					be the case, but might reveal to be useful under 
	 * 					certain circumstances)
	 */
	public synchronized void setSubscriber(CardroidEventStormListener cess,
			boolean replay) {

		_eventListener = cess;

		if (replay) {
			// Replay the entire state.
			for (CardroidEventReplay r : _replay) {
				r.replay(cess);
			}
		}

		if (_fatal) {
			cess.error();
		}
	}

	/**
	 * Remove the object providing the callback methods called by the monitor
	 * subscribed to the publishing of notifications in the topic corresponding
	 * to the current session's owner events, so no operation will be performed
	 * upon the publishing of notifications in the topic this SessionController
	 * is subscripted to
	 */
	public synchronized void clearSubscriber() {
		_eventListener = null;
	}

	/**
	 * Post in this SessionController 's handler the destruction of the 
	 * current user session
	 */
	private synchronized void postSessionDestroyed() {
		_fatal = true;
		if (_listener != null) {
			final SessionListener listener = _listener;
			_handler.post(new Runnable() {
				public void run() {
					listener.onDestroy();
				}
			});
		}
	}

	/**
	 * Execute the "getUserPlaces" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getUserPlaces(QueryListener listener,
			UserTypPrx usr) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getUserPlaces(listener, usr);
		return queryController;
	}

	/**
	 * Execute the "searchTrips" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController searchTrips(QueryListener listener,
			TripRequestTyp tRequest) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.searchTrips(listener, tRequest);
		return queryController;
	}

	/**
	 * Execute the "getUserTrips" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getUserTrips(QueryListener listener,
			UserTypPrx usr) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getUserTrips(listener, usr);
		return queryController;
	}

	/**
	 * Execute the "getPassengerTrips" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getPassengerTrips(
			QueryListener listener, UserTypPrx passenger) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getPassengerTrips(listener, passenger);
		return queryController;
	}

	/**
	 * Execute the "getMessageTalks" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getMessageTalks(
			QueryListener listener, UserTypPrx usr1, UserTypPrx usr2) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getMessageTalks(listener, usr1, usr2);
		return queryController;
	}

	/**
	 * Execute the "getMessageTalksSpeakers" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getMessageTalksSpeakers(
			QueryListener listener, UserTypPrx usr) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getMessageTalksSpeakers(listener, usr);
		return queryController;
	}

	/**
	 * Execute the "getUserActivity" query asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the query
	 * @return QueryController containing the context of this query
	 */
	public synchronized DBQueryController getUserActivity(
			QueryListener listener, UserTypPrx usr) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		queryController.getUserActivity(listener, usr);
		return queryController;
	}

	/**
	 * Execute the "getUserPlaces" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<PlaceTypPrx> getUserPlaces(UserTypPrx usr, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getUserPlaces(usr, n);
	}

	/**
	 * Execute the "searchTrips" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<TripTypPrx> searchTrips(TripRequestTyp tRequest,
			int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.searchTrips(tRequest, n);
	}

	/**
	 * Execute the "getUserTrips" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<TripOfferTypPrx> getUserTrips(UserTypPrx usr, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getUserTrips(usr, n);
	}

	/**
	 * Execute the "getPassengerTrips" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<TripOfferTypPrx> getPassengerTrips(
			UserTypPrx passenger, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getPassengerTrips(passenger, n);
	}

	/**
	 * Execute the "getMessageTalks" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<MessageTypPrx> getMessageTalks(UserTypPrx usr1,
			UserTypPrx usr2, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getMessageTalks(usr1, usr2, n);
	}

	/**
	 * Execute the "getMessageTalksSpeakers" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<UserTypPrx> getMessageTalksSpeakers(
			UserTypPrx usr, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getMessageTalksSpeakers(usr, n);
	}

	/**
	 * Execute the "getUserActivity" query synchronously
	 * @param n	Number of rows queried
	 * @return	List containing the result of this query
	 */
	public synchronized List<UserActivityTypPrx> getUserActivity(
			UserTypPrx usr, int n) {
		_queryController.destroy();
		DBQueryController queryController;
		_queryController = queryController = new DBQueryController(_handler,
				_session.getCardroidManager());
		return queryController.getUserActivity(usr, n);
	}

	/**
	 * Execute the "getTripFromId" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getTripFromId(
			QueryListener listener, int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getTripFromId(listener, tripId);
		return queryController;
	}

	/**
	 * Execute the "getTripOfferFromId" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getTripOfferFromId(
			QueryListener listener, int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getTripOfferFromId(listener, tripId);
		return queryController;
	}

	/**
	 * Execute the "getTripRequestFromId" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getTripRequestFromId(
			QueryListener listener, int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getTripRequestFromId(listener, tripId);
		return queryController;
	}

	/**
	 * Execute the "calculatePriceEstimation" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController calculatePriceEstimation(
			QueryListener listener, Fuel f, int distance) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.calculatePriceEstimation(listener, f, distance);
		return queryController;
	}

	/**
	 * Execute the "getUserFromEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getUserFromEmail(
			QueryListener listener, String email) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getUserFromEmail(listener, email);
		return queryController;
	}

	/**
	 * Execute the "getCarFromPlate" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getCarFromPlate(
			QueryListener listener, String plate, UserTypPrx owner) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getCarFromPlate(listener, plate, owner);
		return queryController;
	}

	/**
	 * Execute the "getCarFromPlateEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController getCarFromPlateEmail(
			QueryListener listener, String plate, String ownerEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.getCarFromPlateEmail(listener, plate, ownerEmail);
		return queryController;
	}

	/**
	 * Execute the "userTripRegistered" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController userTripRegistered(
			QueryListener listener, UserTypPrx usr, TripTypPrx trip) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.userTripRegistered(listener, usr, trip);
		return queryController;
	}

	/**
	 * Execute the "joinTrip" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController joinTrip(QueryListener listener,
			TripOfferTypPrx trip, UserTypPrx passenger, int nSeats) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.joinTrip(listener, trip, passenger, nSeats);
		return queryController;
	}

	/**
	 * Execute the "organizeTrip" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController organizeTrip(
			QueryListener listener, TripRequestTypPrx tripRequest,
			TripOffer tripOffer) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.organizeTrip(listener, tripRequest, tripOffer);
		return queryController;
	}

	/**
	 * Execute the "newTripOffer" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController newTripOffer(
			QueryListener listener, TripOffer tripOffer) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.newTripOffer(listener, tripOffer);
		return queryController;
	}

	/**
	 * Execute the "newTripRequest" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController newTripRequest(
			QueryListener listener, TripRequest tripRequest) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.newTripRequest(listener, tripRequest);
		return queryController;
	}

	/**
	 * Execute the "newMessage" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController newMessage(
			QueryListener listener, UserTypPrx user1, UserTypPrx user2,
			String message) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.newMessage(listener, user1, user2, message);
		return queryController;
	}

	/**
	 * Execute the "updateUserData" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController updateUserData(
			QueryListener listener, User user) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.updateUserData(listener, user);
		return queryController;
	}

	/**
	 * Execute the "updateCarData" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController updateCarData(
			QueryListener listener, Car car, User owner) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.updateCarData(listener, car, owner);
		return queryController;
	}

	/**
	 * Execute the "updateCarDataEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController updateCarDataEmail(
			QueryListener listener, Car car, String ownerEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.updateCarDataEmail(listener, car, ownerEmail);
		return queryController;
	}

	/**
	 * Execute the "addCar" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController addCar(QueryListener listener,
			Car car, UserTypPrx usr) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.addCar(listener, car, usr);
		return queryController;
	}

	/**
	 * Execute the "addCarEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController addCarEmail(
			QueryListener listener, Car car, String usrEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.addCarEmail(listener, car, usrEmail);
		return queryController;
	}

	/**
	 * Execute the "removeCar" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController removeCar(QueryListener listener,
			CarTypPrx car, UserTypPrx usr) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.removeCar(listener, car, usr);
		return queryController;
	}

	/**
	 * Execute the "removeCarPlateEmail" operation asynchronously
	 * @param listener	Implementation of the callback methods that will be 
	 * 					called upon the completion of the operation
	 * @return QueryController containing the context of this operation
	 */
	public synchronized DBProcedureController removeCarPlateEmail(
			QueryListener listener, String carPlate, String usrEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.removeCarPlateEmail(listener, carPlate, usrEmail);
		return queryController;
	}

	/**
	 * Execute the "getTripFromId" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripTypPrx getTripFromId(int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getTripFromId(tripId);
	}

	/**
	 * Execute the "getTripOfferFromId" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripOfferTypPrx getTripOfferFromId(int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getTripOfferFromId(tripId);
	}

	/**
	 * Execute the "getTripRequestFromId" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripRequestTypPrx getTripRequestFromId(int tripId) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getTripRequestFromId(tripId);
	}

	/**
	 * Execute the "calculatePriceEstimation" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized double calculatePriceEstimation(Fuel f, int distance) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.calculatePriceEstimation(f, distance);
	}

	/**
	 * Execute the "getUserFromEmail" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized UserTypPrx getUserFromEmail(String email) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getUserFromEmail(email);
	}

	/**
	 * Execute the "getCarFromPlate" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx getCarFromPlate(String plate, UserTypPrx owner) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getCarFromPlate(plate, owner);
	}

	/**
	 * Execute the "getCarFromPlateEmail" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx getCarFromPlateEmail(String plate,
			String ownerEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.getCarFromPlateEmail(plate, ownerEmail);
	}

	/**
	 * Execute the "userTripRegistered" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized boolean userTripRegistered(UserTypPrx usr,
			TripTypPrx trip) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.userTripRegistered(usr, trip);
	}

	/**
	 * Execute the "joinTrip" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripOfferTypPrx joinTrip(TripOfferTypPrx trip,
			UserTypPrx passenger, int nSeats) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.joinTrip(trip, passenger, nSeats);
	}

	/**
	 * Execute the "organizeTrip" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripOfferTypPrx organizeTrip(
			TripRequestTypPrx tripRequest, TripOffer tripOffer) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.organizeTrip(tripRequest, tripOffer);
	}

	/**
	 * Execute the "newTripOffer" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripOfferTypPrx newTripOffer(TripOffer tripOffer) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.newTripOffer(tripOffer);
	}

	/**
	 * Execute the "newTripRequest" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized TripRequestTypPrx newTripRequest(TripRequest tripRequest) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.newTripRequest(tripRequest);
	}

	/**
	 * Execute the "newMessage" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized MessageTypPrx newMessage(UserTypPrx user1,
			UserTypPrx user2, String message) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.newMessage(user1, user2, message);
	}

	/**
	 * Execute the "updateUserData" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized UserTypPrx updateUserData(User user) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.updateUserData(user);
	}

	/**
	 * Execute the "updateCarData" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx updateCarData(Car car, User owner) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.updateCarData(car, owner);
	}

	/**
	 * Execute the "updateCarDataEmail" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx updateCarDataEmail(Car car, String ownerEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.updateCarDataEmail(car, ownerEmail);
	}

	/**
	 * Execute the "addCar" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx addCar(Car car, UserTypPrx usr) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.addCar(car, usr);
	}

	/**
	 * Execute the "addCarEmail" operation synchronously
	 * @return The result of this operation
	 */
	public synchronized CarTypPrx addCarEmail(Car car, String usrEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		return queryController.addCarEmail(car, usrEmail);
	}

	/**
	 * Execute the "removeCar" operation synchronously
	 */
	public synchronized void removeCar(CarTypPrx car, UserTypPrx usr) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.removeCar(car, usr);
	}

	/**
	 * Execute the "removeCarPlateEmail" operation synchronously
	 */
	public synchronized void removeCarPlateEmail(String carPlate,
			String usrEmail) {
		_queryController.destroy();
		DBProcedureController queryController;
		_queryController = queryController = new DBProcedureController(
				_handler, _session.getCardroidManager());
		queryController.removeCarPlateEmail(carPlate, usrEmail);
	}

	
	/**
	 * \interface SessionListener
	 * Provides the implementation to whichever operation may need to be 
	 * performed upon the destruction of an user session. The relevant
	 * session operations left are handled on a different way
	 */
	public interface SessionListener {
		public void onDestroy();
	}

	/**
	 * \class SessionRefreshThread
	 * The Thread class responsible for refreshing the user session against 
	 * the server, in prevention of its closure and destruction due to its 
	 * lack of activity in the server while the client is still alive and 
	 * active
	 */
	private class SessionRefreshThread extends Thread {
		private final long _timeout;
		private boolean _terminated = false;

		public SessionRefreshThread(long timeout) {
			_timeout = timeout; // seconds
		}

		public synchronized void run() {
			while (!_terminated) {
				// Check idle
				try {
					wait(_timeout);
				} catch (InterruptedException e) {
				}
				if (!_terminated) {
					try {
						_session.refresh();
					} catch (LocalException ex) {
						postSessionDestroyed();
						_terminated = true;
					}
				}
			}
		}

		private synchronized void terminate() {
			_terminated = true;
			notify();
		}
	}

	/**
	 * \interface CardroidEventReplay
	 * Provide the implementation of the operation to be called whenever a
	 * notification event needs previously delivered does need to be replayed
	 */
	private interface CardroidEventReplay {
		public void replay(_CardroidEventStormOperationsNC event);
	}

	/**
	 * \class CardroidEventStormI
	 * The servant class for a monitor subscribed to a IceStorm topic 
	 * matching the CardroidEventStorm interface
	 */
	private class CardroidEventStormI extends _CardroidEventStormDisp {
		private static final long serialVersionUID = -6025384473477381467L;

		/**
		 * An UserActivity related to an event on the user trips has arrived
		 */
		public synchronized void _notify(final UserActivityTypPrx action,
				Current current) {
			_replay.add(new CardroidEventReplay() {
				public void replay(_CardroidEventStormOperationsNC event) {
					event._notify(action);
				}
			});
			if (_replay.size() > MAX_MESSAGES) {
				_replay.removeFirst();
			}

			if (_eventListener != null) {
				final _CardroidEventStormOperationsNC eventStorm = _eventListener;
				_handler.post(new Runnable() {
					public void run() {
						eventStorm._notify(action);
					}
				});
			}
		}

		/**
		 * The user has received a private message
		 */
		public synchronized void message(final MessageTypPrx msg,
				Current __current) {
			_replay.add(new CardroidEventReplay() {
				public void replay(_CardroidEventStormOperationsNC ccl) {
					ccl.message(msg);
				}
			});
			if (_replay.size() > MAX_MESSAGES) {
				_replay.removeFirst();
			}

			if (_eventListener != null) {
				final _CardroidEventStormOperationsNC listener = _eventListener;
				_handler.post(new Runnable() {
					public void run() {
						listener.message(msg);
					}
				});
			}
		}
	}

	/**
	 * \class LocatorI
	 * Instances of this class will be called by this client's ObjectAdapter 
	 * to locate a servant that is not found in its active servant map.
	 * This class is intended to be overridden by subclasses locating
	 * each one of the datatype categories this client's ObjectAdapter
	 * provides servant location to
	 * @see ServantLocator
	 */
	static class LocatorI implements ServantLocator {
		private Ice.Object _servant;

		private static final String[] CATEGORIES = { "place", "car", "user",
				"trip", "trip_offer", "trip_request", "user_activity",
				"message", "" };

		public LocatorI(Ice.Object servant) {
			// _servant = new DispatchInterceptorI(servant);
			_servant = servant;
		}

		public Ice.Object locate(Current c, LocalObjectHolder cookie) {
			assert Arrays.asList(CATEGORIES).contains(c.id.category);
			return _servant;
		}

		public void finished(Current c, Ice.Object servant, Object cookie) {
		}

		public void deactivate(String category) {
		}
	}
}
