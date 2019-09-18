package com.app.medical.DB_Utilities;

public class DB_Utilities {

    // Collections name
    public static final String USERS = "users";

    // Document data
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_AGE = "age";
    public static final String USER_GENDER = "gender";
    public static final  String USER_PHONE = "phone";
    public static final  String USER_BLOOD_TYPE = "blood_type";
    public static final  String USER_ADDRESS = "address";
    public static final  String USER_EMERGENCY_CONTACT = "emergency_contact";
    public static final  String USER_NATIONALITY = "nationality";
    public static final String TABLE_USER= "users";
    public static final String COLUMN_ID = "user_id";
    public static final String CREATE_TABLE_USER = "CREATE TABLE "+TABLE_USER+" ("+COLUMN_ID+" TEXT)";
    public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+TABLE_USER;
}
