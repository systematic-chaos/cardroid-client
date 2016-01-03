package uclm.esi.cardroid.data.android;

import java.sql.Blob;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.List;

import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.util.Utilities;

/**
 * \class User
 * Persistence class that represents an user data
 */
public class User implements Parcelable, IUser {

	private String name;
	private String surname;
	private Bitmap avatar;
	private Place home;
	private String telephone;
	private String email;
	private int reputation;
	private List<Car> cars;
	private static final int MAXCARS = 5;

	// / Constructor.
	public User(String name, String surname, Place home, String telephone,
			String email) {
		this.name = name;
		this.surname = surname;
		this.home = home;
		this.telephone = telephone;
		this.email = email;
		reputation = 0;
		cars = new ArrayList<Car>(MAXCARS);
	}

	public User(String name, String surname, Place home, String telephone,
			String email, Bitmap avatar, int reputation, List<Car> cars) {
		this(name, surname, home, telephone, email);
		this.avatar = avatar;
		this.reputation = reputation;
		this.cars = cars;
	}

	public User() {
	}

	public User newInstance(IUser userObject) {
		if (userObject == null)
			return null;
		if (userObject instanceof User)
			return (User) userObject;

		User user = null;
		Place homePlace = new Place().newInstance(userObject.getHome());

		if (!(userObject.hasAvatar() && userObject.hasReputation())) {
			user = new User(userObject.getName(), userObject.getSurname(),
					homePlace, String.valueOf(userObject.getTelephoneNumber()),
					userObject.getEmail());
			if (!userObject.hasAvatar() && !userObject.hasReputation())
				return user;
			if (userObject.hasAvatar())
				user.setAvatar(userObject.getAvatar());
			if (userObject.hasReputation())
				user.setReputation(userObject.getReputation());
			return user;
		}

		user = new User(userObject.getName(), userObject.getSurname(),
				homePlace, String.valueOf(userObject.getTelephoneNumber()),
				userObject.getEmail(), Utilities.blobToBitmap(userObject
						.getAvatar()), userObject.getReputation(),
				new ArrayList<Car>());
		return user;
	}

	/**
	 * @return The user's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The user's new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The user's surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname
	 *            The user's new surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return The user's avatar (thumbnail image)
	 */
	public Bitmap getAvatarBitmap() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            The user's new avatar (thumbnail image)
	 */
	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return The user's avatar (thumbnail image)
	 */
	public Blob getAvatar() {
		return hasAvatar() ? Utilities.bitmapToBlob(avatar) : null;
	}

	/**
	 * @param avatar
	 *            The user's new avatar (thumbnail image)
	 */
	public void setAvatar(Blob avatar) {
		this.avatar = Utilities.blobToBitmap(avatar);
	}

	public boolean hasAvatar() {
		return avatar != null;
	}

	/**
	 * @return The user's home place
	 */
	public Place getHome() {
		return home;
	}

	/**
	 * @param home
	 *            The user's new home place
	 */
	public void setHome(IPlace home) {
		this.home = new Place().newInstance(home);
	}

	/**
	 * @return The user's telephone number
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            The user's new telephone number
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return The user's telephone number
	 */
	public int getTelephoneNumber() {
		return Integer.parseInt(telephone);
	}

	/**
	 * @param telephone
	 *            The user's new telephone number
	 */
	public void setTelephoneNumber(int telephone) {
		this.telephone = String.valueOf(telephone);
	}

	/**
	 * @return The user's e-mail address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The user's new email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return The user's reputation score
	 */
	public int getReputation() {
		return reputation;
	}

	/**
	 * @param reputation
	 *            The user's updated reputation score
	 */
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	/**
	 * Increases the user's reputation in one unit.
	 */
	public void increaseReputation() {
		reputation++;
	}

	/**
	 * Increases the user's reputation.
	 * 
	 * @param increase
	 *            The number of units to increase the user's reputation
	 */
	public void increaseReputation(int increase) {
		reputation += increase;
	}

	/**
	 * Decreases the user's reputation in one unit.
	 */
	public void decreaseReputation() {
		reputation--;
	}

	/**
	 * Decreases the user's reputation.
	 * 
	 * @param decrease
	 *            The number of units to decrease the user's reputation
	 */
	public void decreaseReputation(int decrease) {
		reputation -= decrease;
	}

	public boolean hasReputation() {
		return reputation >= 0;
	}

	/**
	 * @return List of cars stored by the user as of her property
	 */
	public List<Car> getCarsList() {
		return cars;
	}

	/**
	 * @return Array of cars stored by the user as of her property
	 */
	public Car[] getCars() {
		return cars.toArray(new Car[0]);
	}

	/**
	 * @param cars
	 *            Array containing the user's cars
	 */
	public void setCars(ICar[] carObjects) {
		Car[] carsArray = new Car[carObjects.length];
		for (int n = 0; n < carsArray.length; n++) {
			carsArray[n] = new Car().newInstance(carObjects[n]);
		}
		this.cars = Arrays.asList(carsArray);
	}

	/**
	 * Add a new car to the list of cars stored by the user as of her property.
	 * 
	 * @param car
	 *            The new car to be stored in the user's car list
	 * @return true
	 */
	public boolean addCar(ICar car) {
		return cars.add(new Car().newInstance(car));
	}

	/**
	 * Remove an existing car from the list of cars stored by the user as of her
	 * property.
	 * 
	 * @param car
	 *            The car to be removed from the user's car list
	 * @return true if this list contained the specified element
	 */
	public boolean removeCar(ICar car) {
		return cars.remove(new Car().newInstance(car));
	}

	/**
	 * @return The user's number of cars
	 */
	public int getNCars() {
		return cars.size();
	}

	/**
	 * Remove every car from the list of cars stored by the user as of her
	 * property.
	 */
	public void clearCars() {
		cars.clear();
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof User) {
			User u = (User) o;
			/**
			 * Two users are compared in terms or their names, surnames, e-mail
			 * addresses and telephone numbers
			 */
			eq = name.equals(u.getName()) && surname.equals(u.getSurname())
					&& email.equals(u.getEmail())
					&& telephone.equals(u.getTelephone());
		}
		return eq;
	}

	@Override
	public String toString() {
		return name + " " + surname;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(surname);
		out.writeByte((byte) (hasAvatar() ? 1 : 0));
		if (hasAvatar())
			out.writeParcelable(avatar, flags);
		out.writeParcelable(home, flags);
		out.writeString(telephone);
		out.writeString(email);
		out.writeByte((byte) (hasReputation() ? 1 : 0));
		out.writeInt(reputation);
		out.writeTypedList(cars);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	private User(Parcel in) {
		name = in.readString();
		surname = in.readString();
		avatar = in.readByte() != 0 ? (Bitmap) in.readParcelable(Bitmap.class
				.getClassLoader()) : null;
		home = in.readParcelable(Place.class.getClassLoader());
		telephone = in.readString();
		email = in.readString();
		in.readByte();
		reputation = in.readInt();
		cars = in.createTypedArrayList(Car.CREATOR);
	}
}
