package uclm.esi.cardroid;

import uclm.esi.cardroid.network.client.LoginController;
import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.network.client.LoginController.LoginListener;
import android.app.Application;

/** \class CardroidApp
 * It maintains a global state of the application, being initialized when the 
 * process for the application is created. It holds those data the classes 
 * trying to establish a session or keeping one will need to reference to,
 * at the same time they update this data's status
 */
public class CardroidApp extends Application {
	/** The application's login controller. Used to start a new session */
	private LoginController _loginController;
	/** The application's session controller. Used to manage and finish an 
	 * existing session */
	private SessionController _sessionController;

	@Override
	public void onCreate() {
	}

	@Override
	public void onTerminate() {
		if (_loginController != null) {
			_loginController.destroy();
			_loginController = null;
		}

		if (_sessionController != null) {
			_sessionController.destroy();
			_sessionController = null;
		}
	}

	/**
	 * Called when an user session has been created. Destroy the 
	 * LoginController used to establish the session and keep the 
	 * SessionController that will manage it
	 * @param sessionController The SessionController holding the status of 
	 * 							the new user session and responsible for 
	 * 							managing it
	 */
	public void loggedIn(SessionController sessionController) {
		assert _sessionController == null && _loginController != null;
		_sessionController = sessionController;

		_loginController.destroy();
		_loginController = null;
	}
	
	/**
	 * Destroys the SessionController held by the Application, thus indirectly 
	 * finishing the existing user session
	 */
	public void logout() {
		if (_sessionController != null) {
			_sessionController.destroy();
			_sessionController = null;
		}
	}

	/**
	 * Initiates the process to establish a new user session
	 * @param userId	The user's login
	 * @param password	The user's password
	 * @param secure 	Whether the session should communicate over a secure 
	 * 					channel connection
	 * @param glacier2	Whether the session is enabled to be established via a 
	 * 					Glacier2 router
	 * @param listener	Specifies the callback methods implementations binded 
	 * 					to the various session establishment's events
	 * @return			The LoginController holding the session creation result
	 */
	public LoginController login(String userId, String password,
			boolean secure, boolean glacier2,
			LoginListener listener) {
		assert _loginController == null && _sessionController == null;
		_loginController = new LoginController(getResources(), userId,
				password, secure, glacier2, listener);
		return _loginController;
	}

	/**
	 * Called on failure of the login process
	 */
	public void loginFailure() {
		if (_loginController != null) {
			_loginController.destroy();
			_loginController = null;
		}
	}

	/**
	 * @return The SessionController held by this Application, used to manage 
	 * 			the existing user session, if such a session was previously 
	 * 			established and it is still active
	 */
	public SessionController getSessionController() {
		return _sessionController;
	}

	/**
	 * @return The LoginController held by this Application, used to start a 
	 * 			new user session, if no session exists nor it is currently 
	 * 			active
	 */
	public LoginController getLoginController() {
		return _loginController;
	}
}
