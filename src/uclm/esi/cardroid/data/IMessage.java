package uclm.esi.cardroid.data;

import java.sql.Timestamp;

/**
 * \interface IMessage
 * Public operations interface for the implementation of a Message object
 */
public interface IMessage {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param messageObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
    public IMessage newInstance(IMessage messageObject);

    /**
     * @param user The sender of this IMessage
     */
    public void setFromUser(IUser user);

    /**
     * @return The sender of this IMessage
     */
    public IUser getFromUser();

    /**
     * @param user The receipt of this IMessage
     */
    public void setToUser(IUser user);

    /**
     * @return The receipt of this IMessage
     */
    public IUser getToUser();

    /**
     * @return The text of a this IMessage
     */
    public String getMessageText();

    /**
     * @param text The new text of this IMessage
     */
    public void setMessageText(String text);

    /**
     * @return The timestamp for this IMessage
     */
    public Timestamp getTimeStamp();

    /**
     * @param timeStamp The new timestamp for this IMessage
     */
    public void setTimeStamp(Timestamp timeStamp);
}
