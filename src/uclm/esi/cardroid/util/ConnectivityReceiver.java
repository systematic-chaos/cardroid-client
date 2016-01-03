package uclm.esi.cardroid.util;

/**
 * \interface ConnectivityReceiver
 * Force a class to implement awareness of
 * changes in its connectivity state.
 */
public interface ConnectivityReceiver {
	/** To be executed when the device becomes connected to the Internet. */
	public void onConnected();

	/**
	 * To be disconnected when the device becomes disconnected from the
	 * Internet.
	 */
	public void onDisconnected();
}
