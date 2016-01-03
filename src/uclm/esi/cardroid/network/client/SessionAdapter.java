package uclm.esi.cardroid.network.client;

/**
 * \interface SessionAdapter
 * Provides the operations that can be called upon an user session
 */
import IceStorm.AlreadySubscribed;
import uclm.esi.cardroid.network.zerocice.CardroidEventStormPrx;
import uclm.esi.cardroid.zerocice.CardroidManagerPrx;

public interface SessionAdapter {
	
	/** Destroy the current user session */
	public void destroy();

	/** Refresh the current user session, so it will not be deleted due to a
	 * period of inactivity while the client is still alive and connected	 */
	public void refresh();

	/**
	 * @return The CardroidManager remote object used by this user session
	 * to interact with its operational context in the platform's server   */
	public CardroidManagerPrx getCardroidManager();

	/**
	 * Subscribe the provided object to the topic responsible for delivering
	 * notifications to the user who owns the current session
	 * @param subscriber Proxy to the servant implementing the operations
	 * 						called upon the arrival of a notification
	 * @throws AlreadySubscribed Thrown if a suscription for the topic by the 
	 * 								given subscriber already exists
	 */
	public void subscribeTopic(CardroidEventStormPrx subscriber)
			throws AlreadySubscribed;

	/**
	 * Unsubscribe the provided object from the topic responsible for 
	 * delivering notifications to the user who owns the current session
	 * @param subscriber The subscriptor object to unsubscribe from the topic
	 */
	public void unsubscribeTopic(CardroidEventStormPrx subscriber);

	/**
	 * @return The login (id) of the user who owns the current session
	 */
	public String getUserLogin();
}
