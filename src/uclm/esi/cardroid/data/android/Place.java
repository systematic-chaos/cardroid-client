package uclm.esi.cardroid.data.android;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Blob;

import uclm.esi.cardroid.data.ILatLng;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.util.Utilities;

/**
 * \class Place
 * Persistence class that represents a place data
 */
public class Place implements Parcelable, IPlace {

	private String name;
	private LatLng coords;
	private String description;
	private Bitmap snapshot;

	/**
	 * Constructor that takes the place coordinates and its name
	 * 
	 * @param coords
	 *            Place coordinates
	 * @param name
	 *            Place name
	 */
	public Place(LatLng coords, String name) {
		this.coords = coords;
		this.name = name;
		description = "";
		snapshot = null;
	}

	/**
	 * Constructor that takes the place coordinates, its name and description
	 * 
	 * @param coords
	 *            Place coordinates
	 * @param name
	 *            Place name
	 * @param description
	 *            Place description
	 */
	public Place(LatLng coords, String name, String description) {
		this.coords = coords;
		this.name = name;
		this.description = description;
		snapshot = null;
	}

	/**
	 * Constructor that takes the place coordinates, its name and a snapshot of
	 * it
	 * 
	 * @param coords
	 *            Place coordinates
	 * @param name
	 *            Place name
	 * @param snapshot
	 *            Place snapshot (thumbnail image)
	 */
	public Place(LatLng coords, String name, Bitmap snapshot) {
		this.coords = coords;
		this.name = name;
		this.snapshot = snapshot;
		description = "";
	}

	/**
	 * Constructor that takes the place coordinates, its name, description, and
	 * a snapshot of it
	 * 
	 * @param coords
	 *            Place coordinates
	 * @param name
	 *            Place name
	 * @param description
	 *            Place description
	 * @param snapshot
	 *            Place snapshot (thumbnail image)
	 */
	public Place(LatLng coords, String name, String description, Bitmap snapshot) {
		this.coords = coords;
		this.name = name;
		this.description = description;
		this.snapshot = snapshot;
	}

	public Place() {
	}

	public Place newInstance(IPlace placeObject) {
		if (placeObject == null)
			return null;
		if (placeObject instanceof Place)
			return (Place) placeObject;

		Place place = null;
		LatLng coordinates = new LatLng(placeObject.getCoordinates()
				.getLatitude(), placeObject.getCoordinates().getLongitude());

		if (!(placeObject.hasDescription() ^ placeObject.hasSnapshot())) {
			place = new Place(coordinates, placeObject.getName());
			if (!placeObject.hasDescription() && !placeObject.hasSnapshot())
				return place;
		}
		if (!(placeObject.hasDescription() && placeObject.hasSnapshot())) {
			if (placeObject.hasDescription())
				place.setDescription(placeObject.getDescription());
			if (placeObject.hasSnapshot())
				place.setSnapshot(placeObject.getSnapshot());
		} else
			place = new Place(coordinates, placeObject.getName(),
					placeObject.getDescription(),
					Utilities.blobToBitmap(placeObject.getSnapshot()));

		return place;
	}

	/**
	 * @return The place's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The place's new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The place's coordinates
	 */
	public LatLng getCoords() {
		return coords;
	}

	/**
	 * @param coords
	 *            The place's new coordinates
	 */
	public void setCoords(LatLng coords) {
		this.coords = coords;
	}

	public Coordinates getCoordinates() {
		return new Coordinates(coords);
	}

	public void setCoordinates(ILatLng latLngObject) {
		coords = new Coordinates().newInstance(latLngObject).toLatLng();
	}

	/**
	 * @return The place's description, if any
	 */
	public String getDescription() {
		return hasDescription() ? description : null;
	}

	/**
	 * @param description
	 *            The place's new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasDescription() {
		return description != null && !"".equals(description);
	}

	/**
	 * @return The place's snapshot (thumbnail image), if any
	 */
	public Bitmap getSnapshotBitmap() {
		return hasSnapshot() ? snapshot : null;
	}

	/**
	 * @param snapshot
	 *            The place's new snapshot (thumbnail image)
	 */
	public void setSnapshot(Bitmap snapshot) {
		this.snapshot = snapshot;
	}

	public Blob getSnapshot() {
		return Utilities.bitmapToBlob(snapshot);

	}

	public void setSnapshot(Blob snapshot) {
		this.snapshot = Utilities.blobToBitmap(snapshot);
	}

	public boolean hasSnapshot() {
		return snapshot != null;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Place) {
			Place p = (Place) o;
			// / Two places are compared in terms of their name and coordinates
			eq = p.getName().equals(name) && p.getCoords().equals(coords);
		}
		return eq;
	}

	@Override
	public String toString() {
		return name + "\t(" + coords.latitude + ", " + coords.longitude + ")";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeParcelable(coords, flags);
		out.writeByte((byte) (hasDescription() ? 1 : 0));
		if (hasDescription())
			out.writeString(description);
		out.writeByte((byte) (hasSnapshot() ? 1 : 0));
		if (hasSnapshot())
			out.writeParcelable(snapshot, flags);
	}

	public static Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {

		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};

	private Place(Parcel in) {
		name = in.readString();
		coords = in.readParcelable(LatLng.class.getClassLoader());
		description = in.readByte() != 0 ? in.readString() : null;
		snapshot = in.readByte() != 0 ? (Bitmap) in.readParcelable(Bitmap.class
				.getClassLoader()) : null;
	}

	public static class Coordinates implements ILatLng, Parcelable {

		private double latitude, longitude;

		public Coordinates(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public Coordinates(LatLng coords) {
			this.latitude = coords.latitude;
			this.longitude = coords.longitude;
		}

		public Coordinates() {
		}

		@Override
		public Coordinates newInstance(ILatLng latLngObject) {
			if (latLngObject instanceof Coordinates) {
				return (Coordinates) latLngObject;
			}
			return new Coordinates(latLngObject.getLatitude(),
					latLngObject.getLongitude());
		}

		@Override
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		@Override
		public double getLatitude() {
			return latitude;
		}

		@Override
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		@Override
		public double getLongitude() {
			return longitude;
		}

		public LatLng toLatLng() {
			return new LatLng(latitude, longitude);
		}

		@Override
		public boolean equals(Object o) {
			boolean eq = false;
			if (o instanceof Coordinates) {
				Coordinates c = (Coordinates) o;
				eq = latitude == c.getLatitude()
						&& longitude == c.getLongitude();
			}
			return eq;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 89
					* hash
					+ (int) (Double.doubleToLongBits(this.latitude) ^ (Double
							.doubleToLongBits(this.latitude) >>> 32));
			hash = 89
					* hash
					+ (int) (Double.doubleToLongBits(this.longitude) ^ (Double
							.doubleToLongBits(this.longitude) >>> 32));
			return hash;
		}

		@Override
		public String toString() {
			return '[' + latitude + " , " + longitude + ']';
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeDouble(latitude);
			out.writeDouble(longitude);
		}

		public static final Parcelable.Creator<Coordinates> CREATOR = new Parcelable.Creator<Coordinates>() {
			public Coordinates createFromParcel(Parcel in) {
				return new Coordinates(in);
			}

			public Coordinates[] newArray(int size) {
				return new Coordinates[size];
			}
		};

		private Coordinates(Parcel in) {
			latitude = in.readDouble();
			longitude = in.readDouble();
		}
	}
}
