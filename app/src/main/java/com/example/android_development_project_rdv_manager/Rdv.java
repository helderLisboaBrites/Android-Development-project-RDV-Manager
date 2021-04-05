package com.example.android_development_project_rdv_manager;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Rdv implements Parcelable {

	private int id;
	private String title;
	private String description;
	private String datetime;
	private boolean done;
	private String contact;
	private String address;
	private String phone;

	public Rdv(int id, String title, String description, String datetime, boolean done, String contact, String address, String phone) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.datetime = datetime;
		this.done = done;
		this.contact = contact;
		this.address = address;
		this.phone = phone;
	}

	public Rdv(String title, String description, String datetime, String contact) {
		this(-1, title, description, datetime, false, contact, "", "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(int year, int month, int day, int hour, int minute, int second) {
		this.datetime = String.format("%d-%d-%d %d:%d:%d", year, month, day, hour, minute, second);
	}
	
	public String getDate() {
		return datetime.substring(0, 10);
	}
	public int getDateYear() {
		return Integer.parseInt(datetime.substring(0, 4));
	}
	public int getDateMonth() {
		return Integer.parseInt(datetime.substring(5, 7));
	}
	public int getDateDay() {
		return Integer.parseInt(datetime.substring(8, 10));
	}


	public void setDate(int year, int month, int day) {
		this.datetime = String.format("%4d-%02d-%02d %s", year, month, day, getTime());
	}
	
	public String getTime() {
		Log.d("DEBUGTIME", datetime);
		return datetime.substring(11);
	}
	public int getTimeHour() {
		return Integer.parseInt(datetime.substring(11, 13));
	}
	public int getTimeMinute() {
		return Integer.parseInt(datetime.substring(14, 16));
	}

	public void setTime(int hour, int minute) {
		this.datetime = String.format("%s %02d:%02d:00", getDate(), hour, minute);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getContact() { return contact; }

	public void setContact(String contact) { this.contact = contact; }

	public String getAddress() { return address; }

	public void setAddress(String address) { this.address = address; }

	public String getPhone() { return phone; }

	public void setPhone(String phone) { this.phone = phone; }

	@SuppressLint("DefaultLocale") // ?
	@Override
	public String toString() {
		return String.format(
			"{%d} [%s] %s (%s) (%s) (%s) (%s) - %s",
			this.id,
			this.title,
			this.description,
			this.datetime,
			this.contact,
			this.address,
			this.phone,
			this.done ? "done" : "not yet"
		);
	}

	public Rdv(Parcel parcel) {
		this.id = parcel.readInt();
		this.title = parcel.readString();
		this.description = parcel.readString();
		this.datetime = parcel.readString();
		this.done = parcel.readInt() != 0;
		this.contact = parcel.readString();
		this.address = parcel.readString();
		this.phone = parcel.readString();
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.datetime);
		dest.writeInt(this.done ? 1 : 0);
		dest.writeString(this.contact);
		dest.writeString(this.address);
		dest.writeString(this.phone);
	}

	public static final Parcelable.Creator<Rdv> CREATOR = new Parcelable.Creator<Rdv>() {
		@Override
		public Rdv createFromParcel(Parcel source) {
			return new Rdv(source);
		}

		@Override
		public Rdv[] newArray(int size) {
			return new Rdv[size];
		}
	};
}
