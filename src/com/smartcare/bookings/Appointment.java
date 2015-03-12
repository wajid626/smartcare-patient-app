package com.smartcare.bookings;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.smartcare.bookings.enums.Physician;
import com.smartcare.bookings.enums.Location;

/**
 * Sources
 * http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
 * http://blog.cluepusher.dk/2009/10/28/writing-parcelable-classes-for-android/
 * http://prasanta-paul.blogspot.com.ar/2010/06/android-parcelable-example.html
 * http://techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html
 * 
 * @author lautaro.dragan
 *
 */

public class Appointment implements Parcelable {
//	@SerializedName("location")
	@Expose
	public Location location;
	
//	@SerializedName("benefit")
	@Expose
	public Physician physician;
	
//	@SerializedName("date")
	@Expose
	public Date date;
	
	public Appointment(Location location, Physician physician, Date date) {
		this.location = location;
		this.physician = physician;
		this.date = date;
	}
	
	public Appointment(Parcel source) {
		location = Location.valueOf(source.readString());
		physician = Physician.valueOf(source.readString());
		date = new Date(source.readLong());
	}
	
	@Override
	public String toString() {
		return DateFormat.format("EEEE, MMMM dd, h:mmaa", this.date).toString();
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(location.name());
		dest.writeString(physician.name());
		dest.writeLong(date.getTime());
	}
	
	public static final Parcelable.Creator<Appointment> CREATOR = new Parcelable.Creator<Appointment>() {
		public Appointment createFromParcel(Parcel in) {
			return new Appointment(in);
		}

		public Appointment[] newArray(int size) {
			return new Appointment[size];
		}
	};
	
	
	 
	
}
