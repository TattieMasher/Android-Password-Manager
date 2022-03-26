package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataInteractions extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PasswordStore.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DataContract.AccountEntry.TABLE_NAME + " (" +
                DataContract.AccountEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.AccountEntry.COLUMN_NAME_SERVICE + " TEXT, " +
                DataContract.AccountEntry.COLUMN_NAME_USERNAME + " TEXT, " +
                DataContract.AccountEntry.COLUMN_NAME_PASSWORD + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataContract.AccountEntry.TABLE_NAME;


    public DataInteractions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

                // CHANGE THIS PLEASEEEEEEEEEEEEEEEEEEEEEEEEEEEE            *** *** *** *** *** *** ***
    public boolean updateData(int id, String service, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("service", service);
        values.put("username", username);
        values.put("password", password);

        long newRowId = db.update(DataContract.AccountEntry.TABLE_NAME, values, "_id = " + Integer.toString(id), null);

        if(newRowId == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean debugUpgrade(int id, String service, String username, String password){
        return true;
    }



    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



                            //  ALL THE INTERESTING STUFF IS BELOW HERE     *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***


    public boolean insertData(String service, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("service", service);
        values.put("username", username);
        values.put("password", password);

        long newRowId = db.insert(DataContract.AccountEntry.TABLE_NAME, null, values);

        //  db.insert returns -1 in the case of an error, so we'll use this to detect if one has occurred
        if(newRowId == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateDataOLD(int id, String service, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        return true;
    }

    public Account readDetails(int key){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + DataContract.AccountEntry.TABLE_NAME + " WHERE _id = " + key;

        Cursor cursor = db.rawQuery(query, null);

        Account result = new Account();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < 4; i++) {
                switch(i){
                    case 0:
                        result.setId(cursor.getInt(i));
                        break;
                    case 1:
                        result.setService(cursor.getString(i));
                        break;
                    case 2:
                        result.setName(cursor.getString(i));
                        break;
                    case 3:
                        result.setPassword(cursor.getString(i));
                        break;
                }
            }
            cursor.close();
        }
        db.close();

        return result;
    }

}
