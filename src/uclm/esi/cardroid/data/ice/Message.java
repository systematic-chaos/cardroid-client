package uclm.esi.cardroid.data.ice;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;

import uclm.esi.cardroid.data.IMessage;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.zerocice.MessageTyp;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.network.client.SessionController;

/**
 * \class Message
 * Domain class implementing a Message for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Message extends MessageTyp implements IMessage, ObjectFactory {
	private static SessionController _sessionController;
	private static final long serialVersionUID = 5764230398370402663L;

	public Message(SessionController sessionController) {
		_sessionController = sessionController;
	}

	/**
	 * Default constructor
	 */
	public Message(UserTypPrx user1, UserTypPrx user2, String msg,
			long timeStamp) {
		super(user1, user2, msg, timeStamp);
	}

	public Message(MessageTyp message) {
		this(message.user1, message.user2, message.msg, message.timeStamp);
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(String emailUser1, String emailUser2,
			long timeStamp) {
		Identity id = new Identity();
		id.category = "message";
		id.name = emailUser1 + " - " + emailUser2 + " [" + timeStamp + "]";
		return id;
	}

	/**
	 * @param proxy A proxy to a remote object implementing a Message
	 * @return A local Message object containing the data of the remote object
	 * 			referenced by proxy
	 */
	public static Message extractObject(MessageTypPrx proxy) {
		return new Message(proxy.getUser1(), proxy.getUser2(),
				proxy.getMessageText(), proxy.getTimeStampInMillis());
	}

	/* IMessage interface */

	public Message newInstance(IMessage messageObject) {
		if (messageObject == null)
			return null;
		if (messageObject instanceof Message)
			return (Message) messageObject;
		UserTypPrx user1 = _sessionController.getUserFromEmail(messageObject
				.getFromUser().getEmail());
		UserTypPrx user2 = _sessionController.getUserFromEmail(messageObject
				.getToUser().getEmail());
		String msg = messageObject.getMessageText();
		long timeStamp = messageObject.getTimeStamp().getTime();
		return new Message(user1, user2, msg, timeStamp);
	}

	public void setFromUser(IUser user) {
		setUser1(_sessionController.getUserFromEmail(user.getEmail()));

	}

	public User getFromUser() {
		return User.extractObject(user1);
	}

	public void setToUser(IUser user) {
		setUser2(_sessionController.getUserFromEmail(user.getEmail()));
	}

	public IUser getToUser() {
		return User.extractObject(user2);
	}

	public Timestamp getTimeStamp() {
		return new Timestamp(timeStamp);
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp.getTime();
	}

	/* Overriding superclass */

	@Override
	public UserTypPrx getUser1(Current __current) {
		return user1;
	}

	@Override
	public void setUser1(UserTypPrx user1, Current __current) {
		this.user1 = user1;
	}

	@Override
	public UserTypPrx getUser2(Current __current) {
		return user2;
	}

	@Override
	public void setUser2(UserTypPrx user2, Current __current) {
		this.user2 = user2;
	}

	@Override
	public String getMessageText(Current __current) {
		return msg;
	}

	@Override
	public void setMessageText(String msg, Current __current) {
		this.msg = msg;
	}

	@Override
	public long getTimeStampInMillis(Current __current) {
		return timeStamp;
	}

	@Override
	public void setTimeStampInMillis(long timeStampMillis, Current __current) {
		this.timeStamp = timeStampMillis;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(user1._toString() + " - " + user2._toString());
		builder.append(": " + msg);
		builder.append(" ["
				+ SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,
						SimpleDateFormat.SHORT).format(
						new java.util.Date(timeStamp)) + "]");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Message create(String type) {
		if (type.equals(ice_staticId())) {
			return new Message(_sessionController);
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
