package com.example.android_development_project_rdv_manager;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Rdv implements Parcelable {

	private int id;
	private String title;
	private String description;
	private String date;
	private boolean done;
	private String contact;
	private String address;
	private String phone;

	public Rdv(int id, String title, String description, String date, boolean done, String contact, String address, String phone) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.done = done;
		this.contact = contact;
		this.address = address;
		this.phone = phone;
	}

	public Rdv(String title, String description, String date, String contact) {
		this(-1, title, description, date, false, contact, "", "");
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
			this.date,
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
		this.date = parcel.readString();
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
		dest.writeString(this.date);
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
