package uclm.esi.cardroid.network.client;

import uclm.esi.cardroid.network.zerocice._CardroidEventStormOperationsNC;

/**
 * \interface CardroidEventStormListener
 * Specification of the operations which must be implemented by any class 
 * intended to provide an implementation of the actions to be taken upon 
 * the arrival of a CardroidEventStorm notification
 */
public interface CardroidEventStormListener extends
		_CardroidEventStormOperationsNC {
	/**
	 * 
	 * Called upon an error when communicating with the server. Once this
	 * method is called the user session has been destroyed.
	 * 
	 **/
	void error();

	/**
	 * 
	 * Called if the user session has been inactive for too long. Once this method
	 * is called the user session's context in the server has been destroyed.
	 */
	void inactivity();
}
