package com.example.android_development_project_rdv_manager;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Rdv implements Parcelable {

	private int id;
	private String title;
	private String description;
	private LocalDateTime datetime;
	private boolean done;
	private String contact;
	private String address;
	private String phone;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public Rdv(int id, String title, String description, String datetime, boolean done, String contact, String address, String phone) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.datetime = LocalDateTime.parse(datetime, formatter);
		this.done = done;
		this.contact = contact;
		this.address = address;
		this.phone = phone;
	}

	public Rdv (){
		this(-1, "", "", LocalDateTime.now().format(formatter), false, "", "", "");

	};

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

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public String getDatetimeString() {
		return datetime.format(formatter);
	}

	public String getDateString() {
		return datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getTimeString() {
		return datetime.format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public void setDatetimeString(String datetime) {
		this.datetime = LocalDateTime.parse(datetime, formatter);
	}

	public void setDate(int year, int month, int day) {
		this.datetime = LocalDateTime.of(year, month, day, datetime.getHour(), datetime.getMinute());
	}

	public void setTime(int hour, int minute) {
		this.datetime = LocalDateTime.of(datetime.getYear(), datetime.getMonthValue(), datetime.getDayOfMonth(), hour, minute);
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
			"Titre :%s\nDescription : %s\nDate : %s\nContact : %s \nAddress : %s\nTel : %s\nEtat :%s",

			this.title,
			this.description,
			this.datetime.toString(),
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
		this.datetime = LocalDateTime.parse(parcel.readString());
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
		dest.writeString(this.datetime.toString());
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
