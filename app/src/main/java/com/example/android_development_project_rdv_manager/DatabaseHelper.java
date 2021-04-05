package com.example.android_development_project_rdv_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private SQLiteDatabase database;

	public static final String TABLE_NAME = "RDVs";

	public static final String _ID = "_id";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String DATE = "date";
	public static final String DONE = "done";
	public static final String CONTACT = "contact";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";

	private static final String DB_NAME = "RDVManager.DB";

	private static final int DB_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String.format(
			"CREATE TABLE %s (" +
			"%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"%s TEXT NOT NULL, " +
			"%s TEXT NOT NULL, " +
			"%s TEXT NOT NULL, " +
			"%s INTEGER, " +
			"%s TEXT NOT NULL, " +
			"%s TEXT,  " +
			"%s TEXT" +
			");",
			TABLE_NAME,
			_ID,
			TITLE,
			DESCRIPTION,
			DATE,
			DONE,
			CONTACT,
			ADDRESS,
			PHONE
		));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void reset() {
		onUpgrade(database, 0, 0);
	}

	public void open() throws SQLException {
		this.database = this.getWritableDatabase();
	}

	public void close() {
		this.database.close();
	}

	private ContentValues contentFromRdv(Rdv rdv) {
		ContentValues content = new ContentValues();

		content.put(TITLE, rdv.getTitle());
		content.put(DESCRIPTION, rdv.getDescription());
		content.put(DATE, rdv.getDatetime());
		content.put(DONE, rdv.isDone());
		content.put(CONTACT, rdv.getContact());
		content.put(ADDRESS, rdv.getAddress());
		content.put(PHONE, rdv.getPhone());

		return content;
	}

	public Cursor getAllRdvs() {
		String[] projection = {_ID, TITLE, DESCRIPTION, DATE, DONE, CONTACT, ADDRESS, PHONE};

		Cursor cursor = database.query(
			TABLE_NAME,
			projection,
			null,
			null,
			null,
			null,
			DATE + " ASC",
			null
		);

		return cursor;
	}

	public void removeAllRdvs() {
		database.delete(TABLE_NAME, null, null);
	}

	public void addRdv(Rdv rdv) throws SQLException {
		ContentValues content = contentFromRdv(rdv);
		int id = (int)database.insert(TABLE_NAME, null, content);
		rdv.setId(id);
	}

	public void updateRdv(Rdv rdv) {
		ContentValues content = contentFromRdv(rdv);
		String[] args = {Integer.toString(rdv.getId())};
		database.update(TABLE_NAME, content, _ID+"=?", args);
	}

	public void removeRdv(long id) {
		String[] args = {Double.toString(id)};
		database.delete(TABLE_NAME, _ID+"=?", args);
	}

	public Rdv getRdv(int id) {
		String[] projection = {_ID, TITLE, DESCRIPTION, DATE, DONE, CONTACT, ADDRESS, PHONE};
		String[] selectionArgs = {Integer.toString(id)};

		Cursor cursor = database.query(
			TABLE_NAME,
			projection,
			_ID+"=?",
			selectionArgs,
			null,
			null,
			null,
			null
		);

		if(cursor == null) {
			return null;
		}

		cursor.moveToFirst();

		Rdv rdv = new Rdv(
			cursor.getInt(cursor.getColumnIndex(_ID)),
			cursor.getString(cursor.getColumnIndex(TITLE)),
			cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
			cursor.getString(cursor.getColumnIndex(DATE)),
			cursor.getInt(cursor.getColumnIndex(DONE)) != 0,
			cursor.getString(cursor.getColumnIndex(CONTACT)),
			cursor.getString(cursor.getColumnIndex(ADDRESS)),
			cursor.getString(cursor.getColumnIndex(PHONE))
			);

		return rdv;
	}
}
