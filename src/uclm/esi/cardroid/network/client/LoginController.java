package uclm.esi.cardroid.network.client;

import java.io.InputStream;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.network.zerocice.CardroidEventStormPrx;
import uclm.esi.cardroid.network.zerocice.SessionFactoryPrx;
import uclm.esi.cardroid.network.zerocice.SessionFactoryPrxHelper;
import uclm.esi.cardroid.zerocice.CardroidManagerPrx;
import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;
import Glacier2.SessionNotExistException;
import Ice.Communicator;
import Ice.InitializationData;
import Ice.LocalException;
import Ice.ObjectPrx;
import Ice.Util;
import IceSSL.Plugin;
import IceStorm.AlreadySubscribed;
import IceStorm.BadQoS;
import IceStorm.TopicPrx;
import android.content.res.Resources;
import android.os.Handler;

/**
 * \class LoginController
 * Responsible for creating a new user session against the CarDroid server
 */
public class LoginController {

	private boolean _destroyed = false;
	private Handler _handler;
	private Communicator _communicator;
	private String _loginError;
	private LoginListener _listener;
	private SessionController _sessionController;

	/**
	 * Attempts to create a new user session from the parameters supplied
	 * @param resources Reference to the Android Application resources. Needed 
	 * 					to get the server host's IP address and ports
	 * @param userId	The user's login
	 * @param password	The user's password
	 * @param secure	Whether the session should communicate over a secure 
	 * 					channel connection
	 * @param glacier2	Whether the session is enabled to be established via a 
	 * 					Glacier2 router
	 * @param listener	Specifies the callback methods implementations binded
	 * 					to the various session establishment's events
	 */
	public LoginController(final Resources resources, final String userId,
			final String password, final boolean secure,
			final boolean glacier2, LoginListener listener) {
		_handler = new Handler();
		_listener = listener;
		_listener.onLoginInProgress();

		// Set the Ice configuration properties
		InitializationData initData = new InitializationData();

		initData.properties = Util.createProperties();
		initData.properties.setProperty("Ice.ACM.Client", "0");
		initData.properties.setProperty("Ice.RetryIntervals", "-1");
		initData.properties.setProperty("Ice.Trace.Network", "0");
		initData.properties.setProperty("Ice.Default.SlicedFormat", "1");
		initData.properties.setProperty("Ice.Package.cardroid", "uclm.esi");

		if (secure) {
			// If a secure connection is needed, initialize the IceSSL plugin
			initData.properties.setProperty("IceSSL.Trace.Security", "0");
			initData.properties.setProperty("IceSSL.TruststoreType", "BKS");
			initData.properties.setProperty("IceSSL.Password", "password");
			initData.properties.setProperty("Ice.InitPlugins", "0");
			initData.properties.setProperty("Ice.Plugin.IceSSL",
					"IceSSL.PluginFactory");
			_communicator = Util.initialize(initData);

			Plugin plugin = (Plugin) _communicator.getPluginManager()
					.getPlugin("IceSSL");
			InputStream certStream = resources.openRawResource(R.raw.client);
			plugin.setKeystoreStream(certStream);
			certStream = resources.openRawResource(R.raw.clienttruststore);
			plugin.setTruststoreStream(certStream);
			_communicator.getPluginManager().initializePlugins();
		} else {
			_communicator = Util.initialize(initData);
		}

		new Thread(new Runnable() {
			public void run() {
				try {
					long refreshTimeout;
					SessionAdapter session = null;

					if (glacier2) {
						// Create the session via the Glacier2 router
						StringBuilder s = new StringBuilder();
						s.append("CardroidGlacier2/router : ");
						s.append(secure ? "ssl" : "tcp");
						s.append(" -p ");
						s.append(resources
								.getString(secure ? R.string.loginPortSecure
										: R.string.loginPortNonSecure));
						s.append(" -h ");
						s.append(resources.getString(R.string.hostAddress));
						// s.append(" -t 10000");

						ObjectPrx proxy = _communicator.stringToProxy(s
								.toString());
						Ice.RouterPrx r = Ice.RouterPrxHelper
								.uncheckedCast(proxy);

						_communicator.setDefaultRouter(r);

						final Glacier2.RouterPrx router = Glacier2.RouterPrxHelper
								.checkedCast(r);
						if (router == null) {
							postLoginFailure("Glacier2 proxy is invalid.");
							return;
						}
						
						/* Create the session against the Glacier2 router *
						 * from the user's login credentials 			  */
						Glacier2.SessionPrx glacier2session = router
								.createSession(userId, password);

						final uclm.esi.cardroid.network.zerocice.Glacier2SessionPrx sess = uclm.esi.cardroid.network.zerocice.Glacier2SessionPrxHelper
								.uncheckedCast(glacier2session);
						
						/* Retrieve the session manager remote object for     *
						 * this session. It will be gotten by classes working *
						 * on an user session from its SessionAdapter         */
						final CardroidManagerPrx cardroidManager = sess
								.getCardroidManager();
						
						/* Retrieve the events remote object for the user. * 
						 * It will be gotten by classes working on an user * 	  
						 * session from the session's SessionAdapter       */
						final TopicPrx topic = sess.getTopic();
						
						refreshTimeout = router.getSessionTimeout() * 500;
						
						/* Initialize the SessionAdapter for the newly *
						 * created user session						    */
						session = new SessionAdapter() {
							public void destroy() {
								try {
									router.destroySession();
								} catch (SessionNotExistException e) {
								}
							}

							public void refresh() {
								sess.refresh();
							}

							public CardroidManagerPrx getCardroidManager() {
								return cardroidManager;
							}

							public void subscribeTopic(
									CardroidEventStormPrx subscriber)
									throws AlreadySubscribed {
								try {
									topic.subscribeAndGetPublisher(null,
											subscriber);
								} catch (BadQoS ex) {
								}
							}

							public void unsubscribeTopic(
									CardroidEventStormPrx subscriber) {
								topic.unsubscribe(subscriber);
							}

							public String getUserLogin() {
								return userId;
							}
						};
					} else {
						// Create the session via the SessionFactory
						StringBuilder s = new StringBuilder();
						s.append("SessionFactory : ");
						s.append(secure ? "ssl" : "tcp");
						s.append(" -p ");
						s.append(resources
								.getString(secure ? R.string.loginPortSecure
										: R.string.loginPortNonSecure));
						s.append(" -h ");
						s.append(resources.getString(R.string.hostAddress));
						s.append(" -t 10000");
						ObjectPrx proxy = _communicator.stringToProxy(s
								.toString());
						SessionFactoryPrx factory = SessionFactoryPrxHelper
								.checkedCast(proxy);
						if (factory == null) {
							postLoginFailure("SessionFactory proxy is invalid");
							return;
						}

						/* Create the session against a session factory *
						 * from the user's login credentials 			*/
						final uclm.esi.cardroid.network.zerocice.SessionPrx sess = factory
								.create();
						
						/* Retrieve the session manager remote object for     *
						 * this session. It will be gotten by classes working *
						 * on an user session from its SessionAdapter         */
						final CardroidManagerPrx cardroidManager = sess
								.getCardroidManager();
						
						/* Retrieve the events remote object for the user. * 
						 * It will be gotten by classes working on an user * 	  
						 * session from the session's SessionAdapter       */
						final TopicPrx topic = sess.getTopic();
						
						refreshTimeout = factory.getSessionTimeout() * 500;
						
						/* Initialize the SessionAdapter for the newly *
						 * created user session						   */
						session = new SessionAdapter() {
							public void destroy() {
								sess.destroy();
							}

							public void refresh() {
								sess.refresh();
							}

							public CardroidManagerPrx getCardroidManager() {
								return cardroidManager;
							}

							public void subscribeTopic(
									CardroidEventStormPrx subscriber)
									throws AlreadySubscribed {
								try {
									topic.subscribeAndGetPublisher(null,
											subscriber);
								} catch (BadQoS ex) {
								}
							}

							public void unsubscribeTopic(
									CardroidEventStormPrx subscriber) {
								topic.unsubscribe(subscriber);
							}

							public String getUserLogin() {
								return userId;
							}
						};
					}

					synchronized (LoginController.this) {
						if (_destroyed) {
							try {
								/*
								 * Here the app was terminated while session
								 * establishment was in progress
								 */
								session.destroy();
							} catch (Exception e) {
							}

							try {
								_communicator.destroy();
							} catch (Exception e) {
							}
							return;
						}

						/* If the session creation was successfully           *
						 * completed, initialize a SessionController with the * 
						 * new session's data								  */
						_sessionController = new SessionController(_handler,
								_communicator, session, refreshTimeout);
						if (_listener != null) {
							final LoginListener listener = _listener;
							_handler.post(new Runnable() {
								public void run() {
									listener.onLogin(_sessionController);
								}
							});
						}
					}
				} catch (final CannotCreateSessionException ex) {
					ex.printStackTrace();
					postLoginFailure(String.format(
							"Session creation failed: %s", ex.reason));
				} catch (PermissionDeniedException ex) {
					ex.printStackTrace();
					postLoginFailure(String.format("Login failed: %s",
							ex.reason));
				} catch (LocalException ex) {
					ex.printStackTrace();
					postLoginFailure(String.format("Login failed: %s",
							ex.toString()));
				}
			}
		}).start();
	}

	/**
	 * Destroy the user session held by this LoginController
	 * 
	 * There are three cases:
	 * 
	 * 1. A session has been created. In this case the communicator is owned
	 * by the session controller.
	 * 
	 * 2. The session creation failed. In this case _loginError is non-null.
	 * Destroy the communicator.
	 * 
	 * 3. The session creation is in progress. In which case, things will be
	 * cleaned up once the session establishment is complete. 
	 */
	public synchronized void destroy() {
		if (_destroyed) {
			return;
		}
		_destroyed = true;
		
		if (_sessionController == null && _loginError != null) {
			try {
				_communicator.destroy();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @return The error message raised on the attempt of establishing a new 
	 * 		   user session, if such an error happened (otherwise, return null)
	 */
	public synchronized String getLoginError() {
		return _loginError;
	}

	/**
	 * Set the LoginListener specifying the callback methods implementations
	 * binded to the login process events
	 * @param listener The new LoginListener for this LoginController
	 */
	public synchronized void setLoginListener(LoginListener listener) {
		_listener = listener;
		if (listener != null) {
			if (_loginError != null) {
				listener.onLoginError(_loginError);
			} else if (_sessionController == null) {
				listener.onLoginInProgress();
			} else {
				listener.onLogin(_sessionController);
			}
		}
	}

	/**
	 * When a login failure raises, post the associated message in _handler
	 * @param loginError The message provided at the raise of the login error
	 */
	private synchronized void postLoginFailure(final String loginError) {
		_loginError = loginError;
		if (_listener != null) {
			final LoginListener listener = _listener;
			_handler.post(new Runnable() {
				public void run() {
					listener.onLoginError(loginError);
				}
			});
		}
	}

	/**
	 * \interface LoginListener
	 * Implemented by those classes which wish to do something at the
	 * occurrence of the events derived from this class operation
	 */
	public interface LoginListener {
		/** Called when the session establishment is in progress */
		public void onLoginInProgress();

		/** Called when the login process has been successfully completed, 
		 * this driving to a new session creation
		 * @param controller The controller for the new user session
		 */
		public void onLogin(SessionController controller);

		/** Called when an error was raised when attempting to establish an
		 * user session, this avoiding the session creation
		 * @param loginError The message detailing the error raised
		 */
		public void onLoginError(String loginError);
	}
}
