package uclm.esi.cardroid.data.ice;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.util.SerialBlob;
import uclm.esi.cardroid.util.SerialException;

import Ice.Current;
import Ice.ObjectFactory;

import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IPassenger;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.PassengerTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;

/**
 * \class Passenger
 * Domain class implementing a Passenger for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Passenger extends PassengerTyp implements IPassenger,
		ObjectFactory {
	private static SessionController _sessionController;
	private static final long serialVersionUID = 4960964214323097918L;

	public Passenger(SessionController sessionController) {
		_sessionController = sessionController;
	}

	/**
	 * Default constructor
	 */
	public Passenger(UserTypPrx passengerUser, int nSeats) {
		super(passengerUser, nSeats);
	}

	public Passenger(PassengerTyp passenger) {
		this(passenger.passengerUser, passenger.nSeats);
	}

	/* IPassenger interface */

	public IUser newInstance(IUser userObject) {
		return new User().newInstance(userObject);
	}

	public void setName(String name) {
		passengerUser.setName(name);
	}

	public String getName() {
		return passengerUser.getName();
	}

	public void setSurname(String surname) {
		passengerUser.setSurname(surname);
	}

	public String getSurname() {
		return passengerUser.getSurname();
	}

	public void setAvatar(Blob avatar) {
		byte[] avatarBytes = null;
		try {
			avatarBytes = avatar.getBytes(1, (int) avatar.length());
		} catch (SerialException se) {
			System.err.println("SerialException: " + se.getMessage());
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
		passengerUser.setAvatarBytes(avatarBytes);
	}

	public boolean hasAvatar() {
		return passengerUser.hasAvatar();
	}

	public Blob getAvatar() {
		Blob avatar = null;
		if (passengerUser.hasAvatar()) {
			try {
				avatar = new SerialBlob(passengerUser.getAvatarBytes());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}
		return avatar;
	}

	public void setHome(IPlace home) {
		passengerUser.setUserHome(new Place().newInstance(home));
	}

	public Place getHome() {
		PlaceTyp home = passengerUser.getUserHome();
		return home instanceof Place ? (Place) home : new Place(home);
	}

	public void setTelephoneNumber(int telephone) {
		passengerUser.setTelephone(telephone);
	}

	public int getTelephoneNumber() {
		return passengerUser.getTelephone();
	}

	public void setEmail(String email) {
		passengerUser.setEmail(email);
	}

	public String getEmail() {
		return passengerUser.getEmail();
	}

	public void setReputation(int reputation) {
		passengerUser.setReputation(reputation);
	}

	public int getReputation() {
		return passengerUser.getReputation();
	}

	public void increaseReputation() {
		passengerUser.increaseReputation1();
	}

	public void increaseReputation(int increase) {
		passengerUser.increaseReputation(increase);
	}

	public void decreaseReputation() {
		passengerUser.decreaseReputation1();
	}

	public void decreaseReputation(int decrease) {
		passengerUser.decreaseReputation(decrease);
	}

	public boolean hasReputation() {
		return passengerUser.hasReputation();
	}

	public void setUserPassenger(IUser passengerUser) {
		this.passengerUser = _sessionController.getUserFromEmail(passengerUser
				.getEmail());
	}

	public User getUserPassenger() {
		return User.extractObject(passengerUser);
	}

	public void setCars(ICar[] cars) {
		ArrayList<CarTypPrx> c = new ArrayList<CarTypPrx>(cars.length);
		for (ICar ic : cars)
			c.add(_sessionController.getCarFromPlate(ic.getPlate(),
					passengerUser));
		passengerUser.setUserCars(c);
	}

	public Car[] getCars() {
		List<CarTypPrx> cars = passengerUser.getUserCars();
		ArrayList<Car> c = new ArrayList<Car>(cars.size());
		for (CarTypPrx cprx : cars)
			c.add(Car.extractObject(cprx));
		return c.toArray(new Car[0]);
	}

	public boolean addCar(ICar car) {
		return passengerUser.addCar(new Car().newInstance(car));
	}

	public boolean removeCar(ICar car) {
		return passengerUser.removeCar(new Car().newInstance(car));
	}

	public void clearCars() {
		passengerUser.clearCars();
	}

	public int getNCars() {
		return passengerUser.getNCars();
	}

	public Passenger newInstance(IPassenger passengerObject) {
		return new Passenger(
				_sessionController.getUserFromEmail(passengerObject.getEmail()),
				passengerObject.getNSeats());
	}

	/* Overriding superclass */

	@Override
	public UserTypPrx getPassengerUser(Current __current) {
		return passengerUser;
	}

	@Override
	public void setPassengerUser(UserTypPrx passengerUser, Current __current) {
		this.passengerUser = passengerUser;
	}

	@Override
	public int getNSeats(Current __current) {
		return nSeats;
	}

	@Override
	public void setNSeats(int nSeats, Current __current) {
		this.nSeats = nSeats;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(passengerUser._toString());
		builder.append(": " + nSeats + " asientos");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Passenger create(String type) {
		if (type.equals(ice_staticId())) {
			return new Passenger(_sessionController);
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
