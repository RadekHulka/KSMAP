package com.hulkara1.ksmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_KSMAP = "KSMAP";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_MEDIA = "MEDIA";
    public static final String COLUMN_ALARM = "ALARM";
    public static final String COLUMN_SYSTEM = "SYSTEM";
    public static final String COLUMN_NOTIFICATION = "NOTIFICATION";
    public static final String COLUMN_CALL = "CALL";
    public static final String COLUMN_RINGING = "RINGING";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_RADIUS = "RADIUS";
    public static final String COLUMN_FROMMIN = "FROMMIN";
    public static final String COLUMN_TOMIN = "TOMIN";
    public static final String COLUMN_MONDAY = "MONDAY";
    public static final String COLUMN_TUESDAY = "TUESDAY";
    public static final String COLUMN_WEDNESDAY = "WEDNESDAY";
    public static final String COLUMN_THURSDAY = "THURSDAY";
    public static final String COLUMN_FRIDAY = "FRIDAY";
    public static final String COLUMN_SATURDAY = "SATURDAY";
    public static final String COLUMN_SUNDAY = "SUNDAY";
    public static final String COLUMN_INUSE = "INUSE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "ksmap.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_KSMAP + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_MEDIA + " INTEGER, "
                + COLUMN_ALARM + " INTEGER, "
                + COLUMN_SYSTEM + " INTEGER, "
                + COLUMN_NOTIFICATION + " INTEGER, "
                + COLUMN_CALL + " INTEGER, "
                + COLUMN_RINGING + " INTEGER, "
                + COLUMN_LATITUDE + " DECIMAL, "
                + COLUMN_LONGITUDE + " DECIMAL, "
                + COLUMN_RADIUS + " INTEGER, "
                + COLUMN_FROMMIN + " INTEGER, "
                + COLUMN_TOMIN + " INTEGER, "
                + COLUMN_MONDAY + " BOOL, "
                + COLUMN_TUESDAY + " BOOL, "
                + COLUMN_WEDNESDAY + " BOOL, "
                + COLUMN_THURSDAY + " BOOL, "
                + COLUMN_FRIDAY + " BOOL, "
                + COLUMN_SATURDAY + " BOOL, "
                + COLUMN_SUNDAY + " BOOL,"
                + COLUMN_INUSE + " BOOL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addRow(VolumeProfile volumeProfile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, volumeProfile.getName());
        cv.put(COLUMN_MEDIA, volumeProfile.getMedia());
        cv.put(COLUMN_ALARM, volumeProfile.getAlarm());
        cv.put(COLUMN_SYSTEM, volumeProfile.getSystem());
        cv.put(COLUMN_NOTIFICATION, volumeProfile.getNotification());
        cv.put(COLUMN_CALL, volumeProfile.getCall());
        cv.put(COLUMN_RINGING, volumeProfile.getRinging());
        cv.put(COLUMN_LATITUDE, volumeProfile.getLatitude());
        cv.put(COLUMN_LONGITUDE, volumeProfile.getLongitude());
        cv.put(COLUMN_RADIUS, volumeProfile.getRadius());
        cv.put(COLUMN_FROMMIN, volumeProfile.getFromMin());
        cv.put(COLUMN_TOMIN, volumeProfile.getToMin());
        cv.put(COLUMN_MONDAY, volumeProfile.isMonday());
        cv.put(COLUMN_TUESDAY, volumeProfile.isTuesday());
        cv.put(COLUMN_WEDNESDAY, volumeProfile.isWednesday());
        cv.put(COLUMN_THURSDAY, volumeProfile.isThursday());
        cv.put(COLUMN_FRIDAY, volumeProfile.isFriday());
        cv.put(COLUMN_SATURDAY, volumeProfile.isSaturday());
        cv.put(COLUMN_SUNDAY, volumeProfile.isSaturday());
        cv.put(COLUMN_INUSE, volumeProfile.isInUse());

        long insert = db.insert(TABLE_KSMAP, null, cv);

        db.close();

        return insert != -1;
    }

    public boolean updateRow(VolumeProfile volumeProfile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, volumeProfile.getName());
        cv.put(COLUMN_MEDIA, volumeProfile.getMedia());
        cv.put(COLUMN_ALARM, volumeProfile.getAlarm());
        cv.put(COLUMN_SYSTEM, volumeProfile.getSystem());
        cv.put(COLUMN_NOTIFICATION, volumeProfile.getNotification());
        cv.put(COLUMN_CALL, volumeProfile.getCall());
        cv.put(COLUMN_RINGING, volumeProfile.getRinging());
        cv.put(COLUMN_LATITUDE, volumeProfile.getLatitude());
        cv.put(COLUMN_LONGITUDE, volumeProfile.getLongitude());
        cv.put(COLUMN_RADIUS, volumeProfile.getRadius());
        cv.put(COLUMN_FROMMIN, volumeProfile.getFromMin());
        cv.put(COLUMN_TOMIN, volumeProfile.getToMin());
        cv.put(COLUMN_MONDAY, volumeProfile.isMonday());
        cv.put(COLUMN_TUESDAY, volumeProfile.isTuesday());
        cv.put(COLUMN_WEDNESDAY, volumeProfile.isWednesday());
        cv.put(COLUMN_THURSDAY, volumeProfile.isThursday());
        cv.put(COLUMN_FRIDAY, volumeProfile.isFriday());
        cv.put(COLUMN_SATURDAY, volumeProfile.isSaturday());
        cv.put(COLUMN_SUNDAY, volumeProfile.isSaturday());
        cv.put(COLUMN_INUSE, volumeProfile.isInUse());

        int update = db.update(TABLE_KSMAP, cv,  COLUMN_ID + "=" + volumeProfile.getId(), null);

        db.close();

        return update != -1;
    }

    public ArrayList<VolumeProfile> getUserDefinedVolumeProfiles() {
        ArrayList<VolumeProfile> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_KSMAP + " WHERE " + COLUMN_NAME + " IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int media = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEDIA));
                int alarm = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALARM));
                int system = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYSTEM));
                int notification = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION));
                int call = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALL));
                int ringing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RINGING));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                int radius = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RADIUS));
                int fromMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FROMMIN));
                int toMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOMIN));
                boolean monday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONDAY)) == 1;
                boolean tuesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUESDAY)) == 1;
                boolean wednesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEDNESDAY)) == 1;
                boolean thursday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_THURSDAY)) == 1;
                boolean friday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRIDAY)) == 1;
                boolean saturday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SATURDAY)) == 1;
                boolean sunday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUNDAY)) == 1;
                boolean inUse = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INUSE)) == 1;

                VolumeProfile volumeProfile = new VolumeProfile(id, name, media, alarm, system, notification, call, ringing, latitude, longitude, radius, fromMin, toMin, monday, tuesday, wednesday, thursday, friday, saturday, sunday, inUse);
                returnList.add(volumeProfile);

            } while (cursor.moveToNext());
        } else { }

        cursor.close();
        db.close();
        return returnList;
    }

    public VolumeProfile getVolumeProfileById(int volumeProfileId) {
        VolumeProfile returnVolumeProfile = null;

        String query = "SELECT * FROM " + TABLE_KSMAP + " WHERE " + COLUMN_ID + "=" + volumeProfileId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int media = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEDIA));
                int alarm = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALARM));
                int system = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYSTEM));
                int notification = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION));
                int call = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALL));
                int ringing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RINGING));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                int radius = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RADIUS));
                int fromMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FROMMIN));
                int toMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOMIN));
                boolean monday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONDAY)) == 1;
                boolean tuesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUESDAY)) == 1;
                boolean wednesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEDNESDAY)) == 1;
                boolean thursday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_THURSDAY)) == 1;
                boolean friday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRIDAY)) == 1;
                boolean saturday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SATURDAY)) == 1;
                boolean sunday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUNDAY)) == 1;
                boolean inUse = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INUSE)) == 1;

                returnVolumeProfile = new VolumeProfile(id, name, media, alarm, system, notification, call, ringing, latitude, longitude, radius, fromMin, toMin, monday, tuesday, wednesday, thursday, friday, saturday, sunday, inUse);
        }

        cursor.close();
        db.close();
        return returnVolumeProfile;
    }

    public int getActiveVolumeProfileId() {
        String query = "SELECT * FROM " + TABLE_KSMAP + " WHERE " + COLUMN_INUSE + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int activeVolumeProfileId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        cursor.close();
        db.close();
        return activeVolumeProfileId;
    }

    public VolumeProfile getActiveVolumeProfile() {
        return this.getVolumeProfileById(this.getActiveVolumeProfileId());
    }

    public VolumeProfile getVolumeProfileForLocationAndCurrentTime (double currentLatitude, double currentLongitude) {
        VolumeProfile returnVolumeProfile = null;

        String query = "SELECT * FROM " + TABLE_KSMAP + " WHERE ("
                + COLUMN_LATITUDE + " BETWEEN " + (currentLatitude - 0.001) + " AND " + (currentLatitude + 0.001) + ") AND ("
                + COLUMN_LONGITUDE + " BETWEEN " + (currentLongitude - 0.001) + " AND " + (currentLongitude + 0.001) + ") AND ("
                + COLUMN_FROMMIN + "<" + this.getMinuteOfDay() + ") AND ("
                + COLUMN_TOMIN + ">" + this.getMinuteOfDay() + ") AND ";

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                query = query.concat(COLUMN_MONDAY);
                break;
            case Calendar.TUESDAY:
                query = query.concat(COLUMN_TUESDAY);
                break;
            case Calendar.WEDNESDAY:
                query = query.concat(COLUMN_WEDNESDAY);
                break;
            case Calendar.THURSDAY:
                query = query.concat(COLUMN_THURSDAY);
                break;
            case Calendar.FRIDAY:
                query = query.concat(COLUMN_FRIDAY);
                break;
            case Calendar.SATURDAY:
                query = query.concat(COLUMN_SATURDAY);
                break;
            case Calendar.SUNDAY:
                query = query.concat(COLUMN_SUNDAY);
                break;
            default:
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            if (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)) != null) {
            } else {
                cursor.moveToNext();
            }
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            int media = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEDIA));
            int alarm = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALARM));
            int system = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYSTEM));
            int notification = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION));
            int call = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALL));
            int ringing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RINGING));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
            int radius = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RADIUS));
            int fromMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FROMMIN));
            int toMin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOMIN));
            boolean monday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONDAY)) == 1;
            boolean tuesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUESDAY)) == 1;
            boolean wednesday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEDNESDAY)) == 1;
            boolean thursday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_THURSDAY)) == 1;
            boolean friday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRIDAY)) == 1;
            boolean saturday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SATURDAY)) == 1;
            boolean sunday = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUNDAY)) == 1;
            boolean inUse = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INUSE)) == 1;

            returnVolumeProfile = new VolumeProfile(id, name, media, alarm, system, notification, call, ringing, latitude, longitude, radius, fromMin, toMin, monday, tuesday, wednesday, thursday, friday, saturday, sunday, inUse);
        }

        cursor.close();
        db.close();
        return returnVolumeProfile;
    }

    public int getDefaultVolumeProfileId() {
        return 1;
    }

    public VolumeProfile getDefaultVolumeProfile() {
        return this.getVolumeProfileById(this.getDefaultVolumeProfileId());
    }

    private int getMinuteOfDay() {
        return (60*Calendar.getInstance().getTime().getHours() + Calendar.getInstance().getTime().getMinutes());
    }

    public int getVolumeProfileId(VolumeProfile volumeProfile) {
        String query = "SELECT * FROM " + TABLE_KSMAP + " WHERE " + COLUMN_NAME + "=" + volumeProfile.getName() + " AND "
                + COLUMN_MONDAY + "=" + volumeProfile.isMonday() + " AND "
                + COLUMN_TUESDAY + "=" + volumeProfile.isTuesday() + " AND "
                + COLUMN_WEDNESDAY + "=" + volumeProfile.isWednesday() + " AND "
                + COLUMN_THURSDAY + "=" + volumeProfile.isThursday() + " AND "
                + COLUMN_FRIDAY + "=" + volumeProfile.isFriday() + " AND "
                + COLUMN_SATURDAY + "=" + volumeProfile.isSaturday() + " AND "
                + COLUMN_SUNDAY + "=" + volumeProfile.isSunday() + " AND "
                + COLUMN_MEDIA + "=" + volumeProfile.getMedia() + " AND "
                + COLUMN_ALARM + "=" + volumeProfile.getAlarm() + " AND "
                + COLUMN_SYSTEM + "=" + volumeProfile.getSystem() + " AND "
                + COLUMN_NOTIFICATION + "=" + volumeProfile.getNotification() + " AND "
                + COLUMN_CALL + "=" + volumeProfile.getCall() + " AND "
                + COLUMN_RINGING + "=" + volumeProfile.getRinging() + " AND "
                + COLUMN_LONGITUDE + "=" + volumeProfile.getLongitude() + " AND "
                + COLUMN_LATITUDE + "=" + volumeProfile.getLatitude() + " AND "
                + COLUMN_RADIUS + "=" + volumeProfile.getRadius() + " AND "
                + COLUMN_TOMIN + "=" + volumeProfile.getToMin() + " AND "
                + COLUMN_FROMMIN + "=" + volumeProfile.getFromMin();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int volumeProfileId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        cursor.close();
        db.close();
        return volumeProfileId;
    }

    public void saveActiveVolumeProfile(VolumeProfile volumeProfile) {
        VolumeProfile volumeProfile1 = getActiveVolumeProfile();
        volumeProfile1.setInUse(false);
        updateRow(volumeProfile1);
        updateRow(volumeProfile);
    }

    public boolean deleteRow(VolumeProfile volumeProfile) {
        if (getDefaultVolumeProfileId() != volumeProfile.getId()) {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_KSMAP + " WHERE " + COLUMN_ID + " = " + volumeProfile.getId();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                db.close();
                return true;
            }
            db.close();
        return false;
        } else {
            return false;
        }
    }
}
