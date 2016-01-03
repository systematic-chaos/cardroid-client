package uclm.esi.cardroid.data.android;

import java.sql.Blob;

import android.os.Parcel;
import android.os.Parcelable;

import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IPassenger;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IUser;

/** \class Passenger
 * Persistence class that represents the data of a Passenger in a Trip
 */
public class Passenger implements Parcelable, IPassenger {
	private User userPassenger;
	private int nSeats;

	/**
	 * Default constructor
	 * @param passengerUser The User acting as a Passenger
	 * @param nSeats The number of seats reserved by passengerUser
	 */
	public Passenger(User passengerUser, int nSeats) {
		this.userPassenger = passengerUser;
		this.nSeats = nSeats;
	}

	public Passenger() {
	}

	public User newInstance(IUser userObject) {
		return new User().newInstance(userObject);
	}

	public void setName(String name) {
		userPassenger.setName(name);
	}

	public String getName() {
		return userPassenger.getName();
	}

	public void setSurname(String surname) {
		userPassenger.setSurname(surname);
	}

	public String getSurname() {
		return userPassenger.getSurname();
	}

	public void setAvatar(Blob avatar) {
		userPassenger.setAvatar(avatar);
	}

	public boolean hasAvatar() {
		return userPassenger.hasAvatar();
	}

	public Blob getAvatar() {
		return userPassenger.getAvatar();
	}

	public void setHome(IPlace home) {
		userPassenger.setHome(home);
	}

	public Place getHome() {
		return userPassenger.getHome();
	}

	public void setTelephoneNumber(int telephone) {
		userPassenger.setTelephoneNumber(telephone);
	}

	public int getTelephoneNumber() {
		return userPassenger.getTelephoneNumber();
	}

	public void setEmail(String email) {
		userPassenger.setEmail(email);
	}

	public String getEmail() {
		return userPassenger.getEmail();
	}

	public void setReputation(int reputation) {
		userPassenger.setReputation(reputation);
	}

	public int getReputation() {
		return userPassenger.getReputation();
	}

	public void increaseReputation() {
		userPassenger.increaseReputation();
	}

	public void increaseReputation(int increase) {
		userPassenger.increaseReputation(increase);
	}

	public void decreaseReputation() {
		userPassenger.decreaseReputation();
	}

	public void decreaseReputation(int decrease) {
		userPassenger.decreaseReputation(decrease);
	}

	public boolean hasReputation() {
		return userPassenger.hasReputation();
	}

	public void setCars(ICar[] cars) {
		userPassenger.setCars(cars);
	}

	public Car[] getCars() {
		return userPassenger.getCars();
	}

	public boolean addCar(ICar car) {
		return userPassenger.addCar(car);
	}

	public boolean removeCar(ICar car) {
		return userPassenger.removeCar(car);
	}

	public void clearCars() {
		userPassenger.clearCars();
	}

	public int getNCars() {
		return userPassenger.getNCars();
	}

	public Passenger newInstance(IPassenger passengerObject) {
		if (passengerObject == null)
			return null;
		if (passengerObject instanceof Passenger)
			return (Passenger) passengerObject;

		Passenger passenger = null;
		User userPassenger = new User().newInstance(passengerObject
				.getUserPassenger());
		passenger = new Passenger(userPassenger, passengerObject.getNSeats());
		return passenger;
	}

	public void setUserPassenger(IUser userPassenger) {
		this.userPassenger = new User().newInstance(userPassenger);
	}

	public User getUserPassenger() {
		return userPassenger;
	}

	public void setNSeats(int nSeats) {
		this.nSeats = nSeats;
	}

	public int getNSeats() {
		return nSeats;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof IUser)
			eq = userPassenger
					.equals(((IUser) o).getEmail().equals(getEmail()));
		return eq;
	}

	@Override
	public String toString() {
		return userPassenger.toString() + '\t' + nSeats;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(userPassenger, flags);
		out.writeInt(nSeats);
	}

	public static final Parcelable.Creator<Passenger> CREATOR = new Parcelable.Creator<Passenger>() {

		public Passenger createFromParcel(Parcel in) {
			return new Passenger(in);
		}

		public Passenger[] newArray(int size) {
			return new Passenger[size];
		}
	};

	private Passenger(Parcel in) {
		userPassenger = in.readParcelable(User.class.getClassLoader());
		nSeats = in.readInt();
	}
}
