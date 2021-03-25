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
	// address;
	// phone;

	public Rdv(int id, String title, String description, String date, boolean done) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.done = done;
	}

	public Rdv(String title, String description, String date) {
		this(-1, title, description, date, false);
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

	@SuppressLint("DefaultLocale") // ?
	@Override
	public String toString() {
		return String.format(
			"{%d} [%s] %s (%s) - %s",
			this.id,
			this.title,
			this.description,
			this.date,
			this.done ? "done" : "not yet"
		);
	}

	public Rdv(Parcel parcel) {
		this.id = parcel.readInt();
		this.title = parcel.readString();
		this.description = parcel.readString();
		this.date = parcel.readString();
		this.done = parcel.readInt() != 0;
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
