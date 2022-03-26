package com.example.passwordmanager;

import android.provider.BaseColumns;

public class DataContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataContract() {}

    /* Inner class that defines the table contents */
    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_NAME_SERVICE = "service";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
}
