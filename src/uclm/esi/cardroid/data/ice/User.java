package uclm.esi.cardroid.data.ice;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.util.SerialBlob;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.zerocice.CarTyp;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.UserTyp;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;

/**
 * \class User
 * Domain class implementing an User for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class User extends UserTyp implements IUser, ObjectFactory {
	private static SessionController _sessionController;
	private static final long serialVersionUID = -1954801766504512077L;

	public User() {
	}

	/**
	 * Short constructor
	 */
	public User(String name, String surname, PlaceTyp home, int telephone,
			String email, List<CarTypPrx> cars) {
		super(name, surname, home, telephone, email, cars);
	}

	/**
	 * Long constructor
	 */
	public User(String name, String surname, byte[] avatar, PlaceTyp home,
			int telephone, String email, int reputation, List<CarTypPrx> cars) {
		super(name, surname, avatar, home, telephone, email, reputation, cars);
	}

	public User(UserTyp user) {
		this(user.name, user.surname, user.home, user.telephone, user.email,
				user.cars);
		if (user.hasUserAvatarBytes())
			setUserAvatarBytes(user.getUserAvatarBytes());
		if (user.hasReputation())
			setUserReputation(user.getUserReputation());
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(String email) {
		Identity id = new Identity();
		id.category = "user";
		id.name = email;
		return id;
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static UserTypPrx createProxy(SessionController sessionController,
			String email) {
		return sessionController.getUserFromEmail(email);
	}

	/**
	 * @param proxy A proxy to a remote object implementing an User
	 * @return A local User object containing the data of the remote object
	 * 			referenced by proxy
	 */
	public static User extractObject(UserTypPrx proxy) {
		User user = null;

		if (!(proxy.hasAvatar() && proxy.hasReputation())) {
			user = new User(proxy.getName(), proxy.getSurname(),
					proxy.getUserHome(), proxy.getTelephone(),
					proxy.getEmail(), proxy.getUserCars());
			if (!proxy.hasAvatar() && !proxy.hasReputation())
				return user;
		}

		byte[] userAvatar = proxy.getAvatarBytes();
		int userReputation = proxy.getReputation();

		if (!(proxy.hasAvatar() && proxy.hasReputation())) {
			if (proxy.hasAvatar())
				user.setAvatarBytes(userAvatar);
			if (proxy.hasReputation())
				user.setReputation(userReputation);
		} else
			user = new User(proxy.getName(), proxy.getSurname(), userAvatar,
					proxy.getUserHome(), proxy.getTelephone(),
					proxy.getEmail(), userReputation, proxy.getUserCars());
		return user;
	}

	/* IUser interface */

	public User newInstance(IUser userObject) {
		User user = null;

		Place userHome = new Place().newInstance(userObject.getHome());
		ArrayList<CarTypPrx> userCars = new ArrayList<CarTypPrx>(
				userObject.getNCars());
		for (ICar c : userObject.getCars()) {
			userCars.add(_sessionController.getCarFromPlateEmail(c.getPlate(),
					userObject.getEmail()));
		}
		if (!(userObject.hasAvatar() && userObject.hasReputation())) {
			user = new User(userObject.getName(), userObject.getSurname(),
					userHome, userObject.getTelephoneNumber(),
					userObject.getEmail(), userCars);
			if (!userObject.hasAvatar() && !userObject.hasReputation())
				return user;
		}

		byte[] userAvatar = null;
		if (userObject.hasAvatar()) {
			try {
				userAvatar = userObject.getAvatar().getBytes(1,
						(int) userObject.getAvatar().length());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}
		int userReputation = userObject.getReputation();

		if (!(userObject.hasAvatar() && userObject.hasReputation())) {
			if (userObject.hasAvatar())
				user.setAvatar(userObject.getAvatar());
			if (userObject.hasReputation())
				user.setReputation(userReputation);
		} else
			user = new User(userObject.getName(), userObject.getSurname(),
					userAvatar, userHome, userObject.getTelephoneNumber(),
					userObject.getEmail(), userReputation, userCars);
		return user;
	}

	public void setAvatar(Blob avatar) {
		try {
			setAvatarBytes(avatar.getBytes(1, (int) avatar.length()));
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
	}

	public Blob getAvatar() {
		Blob avatarBlob = null;
		if (hasAvatar()) {
			try {
				avatarBlob = new SerialBlob(getAvatarBytes());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}
		return avatarBlob;
	}

	public void setHome(IPlace home) {
		this.home = new Place().newInstance(home);
	}

	public Place getHome() {
		return home instanceof Place ? (Place) home : new Place(home);
	}

	public void setTelephoneNumber(int telephone) {
		this.telephone = telephone;
	}

	public int getTelephoneNumber() {
		return telephone;
	}

	public void increaseReputation() {
		if (hasUserReputation())
			setUserReputation(getUserReputation() + 1);
	}

	public void decreaseReputation() {
		if (hasUserReputation())
			setUserReputation(getUserReputation() - 1);
	}

	public void setCars(ICar[] cars) {
		this.cars = new ArrayList<CarTypPrx>();
		for (ICar c : cars) {
			this.cars.add(_sessionController.getCarFromPlateEmail(c.getPlate(),
					getEmail()));
		}
	}

	public ICar[] getCars() {
		ArrayList<Car> cars = new ArrayList<Car>(getNCars());
		for (CarTypPrx c : this.cars) {
			cars.add(Car.extractObject(c));
		}
		return cars.toArray(new Car[0]);
	}

	public boolean addCar(ICar car) {
		CarTypPrx c = _sessionController.addCarEmail(
				new Car().newInstance(car), getEmail());
		int n;
		for (n = 0; n < cars.size(); n++)
			if (cars.get(n).getPlate().equals(car.getPlate()))
				break;
		if (n < cars.size())
			cars.set(n, c);
		else
			cars.add(c);
		return true;
	}

	public boolean removeCar(ICar car) {
		_sessionController.removeCarPlateEmail(car.getPlate(), getEmail());
		int n;
		for (n = 0; n < cars.size(); n++)
			if (cars.get(n).getPlate().equals(car.getPlate()))
				break;
		if (n < cars.size())
			cars.remove(n);
		return n < cars.size();
	}

	/* Overriding superclass */

	@Override
	public String getName(Current __current) {
		return name;
	}

	@Override
	public void setName(String name, Current __current) {
		this.name = name;
	}

	@Override
	public String getSurname(Current __current) {
		return surname;
	}

	@Override
	public void setSurname(String surname, Current __current) {
		this.surname = surname;
	}

	@Override
	public byte[] getAvatarBytes(Current __current) {
		return hasUserAvatarBytes() ? getUserAvatarBytes() : null;
	}

	@Override
	public void setAvatarBytes(byte[] avatarBytes, Current __current) {
		setUserAvatarBytes(avatarBytes);
	}

	@Override
	public boolean hasAvatar(Current __current) {
		return hasUserAvatarBytes() && getUserAvatarBytes().length > 0;
	}

	@Override
	public PlaceTyp getUserHome(Current __current) {
		return home;
	}

	@Override
	public void setUserHome(PlaceTyp home, Current __current) {
		this.home = home;
	}

	@Override
	public int getTelephone(Current __current) {
		return telephone;
	}

	@Override
	public void setTelephone(int telephone, Current __current) {
		this.telephone = telephone;
	}

	@Override
	public String getEmail(Current __current) {
		return email;
	}

	@Override
	public void setEmail(String email, Current __current) {
		this.email = email;
	}

	@Override
	public int getReputation(Current __current) {
		return hasUserReputation() ? getUserReputation() : 0;
	}

	@Override
	public void setReputation(int reputation, Current __current) {
		setUserReputation(reputation);
	}

	@Override
	public boolean hasReputation(Current __current) {
		return hasUserReputation();
	}

	@Override
	public List<CarTypPrx> getUserCars(Current __current) {
		return cars;
	}

	@Override
	public void setUserCars(List<CarTypPrx> cars, Current __current) {
		this.cars = cars;
	}

	@Override
	public void increaseReputation1(Current __current) {
		increaseReputation(1);
	}

	@Override
	public void increaseReputation(int increase, Current __current) {
		setUserReputation(hasUserReputation() ? getUserReputation() + increase
				: increase);
	}

	@Override
	public void decreaseReputation1(Current __current) {
		decreaseReputation(1);
	}

	@Override
	public void decreaseReputation(int decrease, Current __current) {
		setUserReputation(hasUserReputation() ? Math.max(getUserReputation()
				- decrease, 0) : 0);
	}

	@Override
	public boolean addCar(CarTyp c, Current __current) {
		CarTypPrx car = _sessionController.addCarEmail(new Car(c), getEmail());
		int n;
		for (n = 0; n < cars.size(); n++)
			if (cars.get(n).getPlate().equals(c.getPlate()))
				break;
		if (n <= cars.size())
			cars.set(n, car);
		else
			cars.add(car);
		return true;
	}

	@Override
	public boolean removeCar(CarTyp c, Current __current) {
		int n;
		for (n = 0; n < cars.size(); n++)
			if (cars.get(n).getPlate().equals(c.getPlate()))
				break;
		if (n < cars.size())
			cars.remove(n);
		return n < cars.size();
	}

	@Override
	public int getNCars(Current __current) {
		return cars.size();
	}

	@Override
	public void clearCars(Current __current) {
		cars.clear();
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(" " + surname);
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public User create(String type) {
		if (type.equals(ice_staticId())) {
			return new User();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
