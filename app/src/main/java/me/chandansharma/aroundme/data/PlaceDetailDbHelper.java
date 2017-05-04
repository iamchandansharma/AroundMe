package me.chandansharma.aroundme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.chandansharma.aroundme.data.PlaceDetailContract.PlaceDetailEntry;

/**
 * Created by iamcs on 2017-05-04.
 */

public class PlaceDetailDbHelper extends SQLiteOpenHelper {

    //TAG for the class
    public static final String TAG = PlaceDetailDbHelper.class.getSimpleName();

    //Place Database Name and Version
    public static final String DATABASE_NAME = "place_detail.db";
    public static final int DATABASE_VERSION = 1;

    public PlaceDetailDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Query for Place Detail
        String SQL_CREATE_PLACE_DETAIL_QUERY =
                "CREATE TABLE " + PlaceDetailEntry.PLACE_TABLE_NAME + "(" +
                        PlaceDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PlaceDetailEntry.COLUMN_PLACE_ID + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_LATITUDE + " REAL, " +
                        PlaceDetailEntry.COLUMN_PLACE_LONGITUDE + " REAL, " +
                        PlaceDetailEntry.COLUMN_PLACE_NAME + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING + " REAL ," +
                        PlaceDetailEntry.COLUMN_PLACE_ADDRESS + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_WEBSITE + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK + " TEXT" + ")";

        /**Create Query for Rating Detail
        String SQL_CREATE_RATING_DETAIL_QUERY =
                "CREATE TABLE " + PlaceDetailEntry.PLACE_RATING_TABLE_NAME + "(" +
                        PlaceDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING_AUTHOR_NAME + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING_AUTHOR_PROFILE_PICTURE_URL + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING_AUTHOR_RATING + " REAL, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING_RELATIVE_TIME_DESCRIPTION + " TEXT, " +
                        PlaceDetailEntry.COLUMN_PLACE_RATING_AUTHOR_REVIEW_TEXT + " TEXT" + ")";
         **/
        db.execSQL(SQL_CREATE_PLACE_DETAIL_QUERY);
        //db.execSQL(SQL_CREATE_RATING_DETAIL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_PLACE_DETAIL_QUERY =
                "DELETE FROM " + PlaceDetailEntry.PLACE_TABLE_NAME;
        db.execSQL(SQL_DELETE_PLACE_DETAIL_QUERY);
        /*
        String SQL_DELETE_REVIEW_DETAIL_QUERY =
                "DELETE FROM " + PlaceDetailEntry.PLACE_RATING_TABLE_NAME;


        db.execSQL(SQL_DELETE_REVIEW_DETAIL_QUERY);*/
    }
}
