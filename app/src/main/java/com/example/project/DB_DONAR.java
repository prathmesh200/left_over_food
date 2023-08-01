package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import java.util.List;

public class DB_DONAR extends SQLiteOpenHelper {
    public static final String DB_NAME = "donar.db";
    public static final String DONAR_TABLE_NAME = "donarusers";
    public static final String VOLUNTEER_TABLE_NAME = "volunteerusers";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_RESTAURANT_NAME = "restaurantName";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";

    // table for donar and volunteer users
    public static final String DONAR_ACCOUNT_TABLE_NAME = "userAccount1";
    public static final String VOLUNTEER_ACCOUNT_TABLE_NAME = "userAccount2";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_PINCODE = "pincode";

    // table for donar added food
    public static final String FOOD_TABLE_NAME = "addedFood";
    public static final String COLUMN_FOOD_ID = "userid";
    public static final String COLUMN_FOOD_IMAGE = "foodimage";
    public static final String COLUMN_FOOD_TITLE = "foodtitle";
    public static final String COLUMN_FOOD_DESCRIPTION = "fooddescription";
    public static final String COLUMN_FEED_COUNT = "feedcount";

    public DB_DONAR(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        String createDonar = "CREATE TABLE " + DONAR_TABLE_NAME + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_PHONE_NUMBER + " TEXT NOT NULL,"
                + COLUMN_RESTAURANT_NAME + " TEXT NOT NULL" + ")";

        String createVolunteer = "CREATE TABLE " + VOLUNTEER_TABLE_NAME + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY, "
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_PHONE_NUMBER + " TEXT NOT NULL" + ")";

        String createAccountDonar = "CREATE TABLE " + DONAR_ACCOUNT_TABLE_NAME + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY, "
                + COLUMN_FIRSTNAME + " TEXT NOT NULL,"
                + COLUMN_LASTNAME + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_STATE + " TEXT NOT NULL,"
                + COLUMN_PINCODE + " TEXT NOT NULL,"
                + COLUMN_CITY + " TEXT NOT NULL" + ")";

        String createAccountVolunteer = "CREATE TABLE " + VOLUNTEER_ACCOUNT_TABLE_NAME + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY, "
                + COLUMN_FIRSTNAME + " TEXT NOT NULL,"
                + COLUMN_LASTNAME + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_STATE + " TEXT NOT NULL,"
                + COLUMN_PINCODE + " TEXT NOT NULL,"
                + COLUMN_CITY + " TEXT NOT NULL" + ")";

        String createFoodTable = "CREATE TABLE " + FOOD_TABLE_NAME + "("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RESTAURANT_NAME + " TEXT, "
                + COLUMN_FOOD_IMAGE + " BLOB, "
                + COLUMN_FOOD_TITLE + " TEXT NOT NULL,"
                + COLUMN_FOOD_DESCRIPTION + " TEXT NOT NULL,"
                + COLUMN_FEED_COUNT + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_STATE + " TEXT NOT NULL,"
                + COLUMN_PINCODE + " TEXT NOT NULL,"
                + COLUMN_CITY + " TEXT NOT NULL" + ")";

        MyDB.execSQL(createAccountDonar);
        MyDB.execSQL(createAccountVolunteer);
        MyDB.execSQL(createDonar);
        MyDB.execSQL(createVolunteer);
        MyDB.execSQL(createFoodTable);
    }

    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS " + DONAR_TABLE_NAME);
        MyDB.execSQL("DROP TABLE IF EXISTS " + VOLUNTEER_TABLE_NAME);
        MyDB.execSQL("DROP TABLE IF EXISTS " + DONAR_ACCOUNT_TABLE_NAME);
        MyDB.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE_NAME);
        onCreate(MyDB);
    }

    public boolean chechIfFoodTableisEmpty(){
        boolean empty = true;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + FOOD_TABLE_NAME, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();
        return empty;
    }
    public String getPhoneNumber(String username){
        String phone = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DONAR_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int phone_index = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);

        do {
            user = cursor.getString(userindex);
            phone = cursor.getString(phone_index);
            if(user.equals(username)){
                return phone;
            }
        }while (cursor.moveToNext());
        return phone;
    }

    public int getFeedCount(int id){
        int feed_count = 0;
        int food_id = 0;
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int food_id_index = cursor.getColumnIndex(COLUMN_FOOD_ID);
        int feed_index = cursor.getColumnIndex(COLUMN_FEED_COUNT);

        do {
            food_id = cursor.getInt(food_id_index);
            feed_count = cursor.getInt(feed_index);
            if(food_id == id){
                return feed_count;
            }
        }while (cursor.moveToNext());
        return feed_count;
    }

    public String getRestaurantNameFromDonar(String username){
        String restaurant_name = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DONAR_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int restaurantindex = cursor.getColumnIndex(COLUMN_RESTAURANT_NAME);
        do {
            user = cursor.getString(userindex);
            restaurant_name = cursor.getString(restaurantindex);
            if(user.equals(username)){
                return restaurant_name;
            }
        }while (cursor.moveToNext());
        return restaurant_name;
    }

    public String getStateFromDB(String username){
        String state_name = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int stateindex = cursor.getColumnIndex(COLUMN_STATE);
        do {
            user = cursor.getString(userindex);
            state_name = cursor.getString(stateindex);
            if(user.equals(username)){
                return state_name;
            }
        }while (cursor.moveToNext());
        return state_name;
    }

    public String getCityFromDB(String username){
        String city_name = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int stateindex = cursor.getColumnIndex(COLUMN_CITY);
        do {
            user = cursor.getString(userindex);
            city_name = cursor.getString(stateindex);
            if(user.equals(username)){
                return city_name;
            }
        }while (cursor.moveToNext());
        return city_name;
    }

    public String getPincodeFromAccount(String username,String type){
        String pincode = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        if(type.equals("Donar")) {
            cursor = db.rawQuery("SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME, new String[]{});
        }else{
            cursor = db.rawQuery("SELECT * FROM " + VOLUNTEER_ACCOUNT_TABLE_NAME, new String[]{});
        }

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int pincodeindex = cursor.getColumnIndex(COLUMN_PINCODE);
        do {
            user = cursor.getString(userindex);
            pincode = cursor.getString(pincodeindex);
            if(user.equals(username)){
                return pincode;
            }
        }while (cursor.moveToNext());
        return pincode;
    }

    public boolean checkIfPincodematches(String pincodeofVolunteer){
        String pincodeofDonar = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int pincodeindex = cursor.getColumnIndex(COLUMN_PINCODE);
        do {
            pincodeofDonar = cursor.getString(pincodeindex);
            if(pincodeofDonar.equals(pincodeofVolunteer)){
                return true;
            }
        }while (cursor.moveToNext());
        return false;
    }

    public String getUsernameFromDonar(String current_email){
        String user = "Not Found";
        String email = "Not Found";
        Cursor cursor;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }

        int emailindex = cursor.getColumnIndex(COLUMN_EMAIL);
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
//        List<String> l = new ArrayList<>();
        do {
            email = cursor.getString(emailindex);
            user = cursor.getString(userindex);
            if(email.equals(current_email)){
                return user;
            }
        }while (cursor.moveToNext());
        return user;
    }


    public boolean checkRestaurantexistsinFoodTable(String restaurantname){
        String query = "SELECT * FROM " + FOOD_TABLE_NAME + " WHERE " + COLUMN_RESTAURANT_NAME + " = ?";
        String[] whereArgs = {restaurantname};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public List<List<String>> getUsersFromFoodTable(String pincodeofVolunteer){
        String restaurant_name, food_title, food_description,feed_count,email,state,pincode,city;

        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }
        int restaurant_index = cursor.getColumnIndex(COLUMN_RESTAURANT_NAME);
        int foodtitle_index = cursor.getColumnIndex(COLUMN_FOOD_TITLE);
        int fooddescription_index = cursor.getColumnIndex(COLUMN_FOOD_DESCRIPTION);
        int feedcount_index = cursor.getColumnIndex(COLUMN_FEED_COUNT);
        int email_index = cursor.getColumnIndex(COLUMN_EMAIL);
        int state_index = cursor.getColumnIndex(COLUMN_STATE);
        int pincode_index = cursor.getColumnIndex(COLUMN_PINCODE);
        int city_index = cursor.getColumnIndex(COLUMN_CITY);
        List<List<String>> store = new ArrayList<>();
        do {
            List<String> list = new ArrayList<>();
            restaurant_name = cursor.getString(restaurant_index);
            food_title = cursor.getString(foodtitle_index);
            food_description = cursor.getString(fooddescription_index);
            feed_count = cursor.getString(feedcount_index);
            email = cursor.getString(email_index);
            state = cursor.getString(state_index);
            pincode = cursor.getString(pincode_index);
            city = cursor.getString(city_index);

            if(pincode.equals(pincodeofVolunteer)){
                list.add(restaurant_name);list.add(food_title);list.add(food_description);list.add(feed_count);list.add(email);list.add(state);
                list.add(pincode);list.add(city);
                store.add(list);
            }
        }while (cursor.moveToNext());
        return store;
    }

    public String getEmailFromDonar(String username,String type){
        String email = "Not Found";
        String user = "Not Found";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();
        if(type.equals("Donar")) {
            cursor = db.rawQuery("SELECT * FROM " + DONAR_TABLE_NAME, new String[]{});
        }else{
            cursor = db.rawQuery("SELECT * FROM " + VOLUNTEER_TABLE_NAME, new String[]{});
        }

        if(cursor != null){
            cursor.moveToFirst();
        }
        int userindex = cursor.getColumnIndex(COLUMN_USERNAME);
        int emailindex = cursor.getColumnIndex(COLUMN_EMAIL);
        do {
            user = cursor.getString(userindex);
            email = cursor.getString(emailindex);
            if(user.equals(username)){
                return email;
            }
        }while (cursor.moveToNext());
        return email;
    }

    public boolean addUsersInDonar(String username,String password,String email,String phoneNumber,String restaurantName){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_RESTAURANT_NAME, restaurantName);

        long result = MyDB.insert(DONAR_TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public void updateTheEmailInDonar(String updateEmail, String userName){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, updateEmail);
        MyDB.update(DONAR_TABLE_NAME, values, COLUMN_USERNAME + "= ?", new String[] {userName});
    }

    public void updateTheEmailInVolunteer(String updateEmail, String userName){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, updateEmail);
        MyDB.update(VOLUNTEER_TABLE_NAME, values, COLUMN_USERNAME + "= ?", new String[] {userName});
    }

    public boolean addUsersInVolunteer(String username,String password,String email,String phoneNumber){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);

        long result = MyDB.insert(VOLUNTEER_TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkUsernameInDonar(String username){
        String query = "SELECT * FROM " + DONAR_TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public boolean checkUsernameInVolunteer(String username) {
        String query = "SELECT * FROM " + VOLUNTEER_TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public boolean checkPasswordInDonar(String password){
        String query = "SELECT * FROM " + DONAR_TABLE_NAME + " WHERE " + COLUMN_PASSWORD + " = ?";
        String[] whereArgs = {password};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public boolean checkPasswordInVolunteer(String password){
        String query = "SELECT * FROM " + VOLUNTEER_TABLE_NAME + " WHERE " + COLUMN_PASSWORD + " = ?";
        String[] whereArgs = {password};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;

    }

    public boolean checkIfEmailExistsInDonar(String email){
        String query = "SELECT * FROM " + DONAR_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] whereArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public boolean checkIfEmailExistsInVolunteer(String email){
        String query = "SELECT * FROM " + VOLUNTEER_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] whereArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }
//    public boolean checkUsernamePassword(String username,String password){
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//
//        Cursor cursor = MyDB.rawQuery("Select * from DONAR_TABLE_NAME where username = ? and password = ?", new String[] {username,password});
//        if(cursor.getCount() > 0){
//            return true;
//        }else{
//            return false;
//        }
//    }

    public boolean checkDataAlreadyExistsInDonarAccount(String email){
        String query = "SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] whereArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    boolean checkDataAlreadyExistsInVolunteerAccount(String email){
        String query = "SELECT * FROM " + VOLUNTEER_ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] whereArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }

    public List getallUsersFromAccount(String currentEmail,String type){
        String firstname,lastname,state,city,pincode,email = "Not Found";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(type.equals("Donar")) {
            cursor = db.rawQuery("SELECT * FROM " + DONAR_ACCOUNT_TABLE_NAME, new String[]{});
        }else{
            cursor = db.rawQuery("SELECT * FROM " + VOLUNTEER_ACCOUNT_TABLE_NAME, new String[]{});
        }

        if(cursor != null){
            cursor.moveToFirst();
        }
        int email_index = cursor.getColumnIndex(COLUMN_EMAIL);
        int firstname_index = cursor.getColumnIndex(COLUMN_FIRSTNAME);
        int lastname_index = cursor.getColumnIndex(COLUMN_LASTNAME);
        int state_index = cursor.getColumnIndex(COLUMN_STATE);
        int city_index = cursor.getColumnIndex(COLUMN_CITY);
        int pincode_index = cursor.getColumnIndex(COLUMN_PINCODE);
        List<String> list = new ArrayList<>();
        do {
            email = cursor.getString(email_index);
            firstname = cursor.getString(firstname_index);
            lastname = cursor.getString(lastname_index);
            state = cursor.getString(state_index);
            city = cursor.getString(city_index);
            pincode = cursor.getString(pincode_index);
            if(email.equals(currentEmail)){
                list.add(firstname);list.add(lastname);list.add(state);list.add(city);list.add(pincode);
                return list;
            }
        }while (cursor.moveToNext());
        return list;
    }
    public boolean addUsersInDonarAccount(String username,String firstname,String lastname,String email,String state,String city,String pincode){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_STATE, state);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PINCODE, pincode);

        long result = MyDB.insert(DONAR_ACCOUNT_TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean addUsersInVolunteerAccount(String username,String firstname,String lastname,String email,String state,String city,String pincode){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_STATE, state);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PINCODE, pincode);

        long result = MyDB.insert(VOLUNTEER_ACCOUNT_TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public List<List<String>> makeItemsVisibleByDonar(String restaurantname){
        String restaurant_name, food_title, food_description,feed_count,email,state,pincode,city;
        int food_image;
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }

        int restaurant_index = cursor.getColumnIndex(COLUMN_RESTAURANT_NAME);
        int foodtitle_index = cursor.getColumnIndex(COLUMN_FOOD_TITLE);
        int fooddescription_index = cursor.getColumnIndex(COLUMN_FOOD_DESCRIPTION);
        int feedcount_index = cursor.getColumnIndex(COLUMN_FEED_COUNT);
        int email_index = cursor.getColumnIndex(COLUMN_EMAIL);
        int state_index = cursor.getColumnIndex(COLUMN_STATE);
        int pincode_index = cursor.getColumnIndex(COLUMN_PINCODE);
        int city_index = cursor.getColumnIndex(COLUMN_CITY);
        List<List<String>> storee = new ArrayList<>();
        do {
            List<String> list = new ArrayList<>();
            restaurant_name = cursor.getString(restaurant_index);
            food_title = cursor.getString(foodtitle_index);
            food_description = cursor.getString(fooddescription_index);
            feed_count = cursor.getString(feedcount_index);
            email = cursor.getString(email_index);
            state = cursor.getString(state_index);
            pincode = cursor.getString(pincode_index);
            city = cursor.getString(city_index);

            if(restaurant_name.equals(restaurantname)){
                list.add(restaurant_name);list.add(food_title);list.add(food_description);list.add(feed_count);list.add(email);list.add(state);
                list.add(pincode);list.add(city);
                storee.add(list);
            }
        }while (cursor.moveToNext());
        return storee;
    }
    public void deleteItemsFromRecyclerView(int food_id)
    {
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});



        db.delete(FOOD_TABLE_NAME, COLUMN_FOOD_ID + "=" + food_id, null);
        db.close();
    }

    public int getFoodId(int pos,String restaurant){
        int food_id = 0;
        String res = "Not found";
        int count = 1;
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, new String[]{});

        if(cursor != null){
            cursor.moveToFirst();
        }

        int food_index = cursor.getColumnIndex(COLUMN_FOOD_ID);
        int res_index = cursor.getColumnIndex(COLUMN_RESTAURANT_NAME);
        do{
            food_id = cursor.getInt(food_index);
            res = cursor.getString(res_index);
            if(count == pos){
                if(res.equals(restaurant)) {
                    return food_id;
                }else{
                    pos++;
                }
            }
            count++;
        }while(cursor.moveToNext());

        return food_id;
    }

    public Bitmap getImageFromDB(int id){
        Bitmap bitmap = null;
        int food_id = 0;
        byte[] img = new byte[0];
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        int food_id_index = cursor.getColumnIndex(COLUMN_FOOD_ID);
        int foodimage_index = cursor.getColumnIndex(COLUMN_FOOD_IMAGE);
        do{
            food_id = cursor.getInt(food_id_index);
            if(food_id == id) {
                img = cursor.getBlob(foodimage_index);
                bitmap = (BitmapFactory.decodeByteArray(img, 0, img.length));
                return bitmap;
            }
        }while(cursor.moveToNext());
        return bitmap;
    }

    public List<Bitmap> getImageFromDB(String restaurantname){
        List<Bitmap> bitmaps = new ArrayList<>();
        String restaurant_name = "Not found";
        byte[] img = new byte[0];
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        int restaurant_index = cursor.getColumnIndex(COLUMN_RESTAURANT_NAME);
        int foodimage_index = cursor.getColumnIndex(COLUMN_FOOD_IMAGE);
        do{
            restaurant_name = cursor.getString(restaurant_index);
            if(restaurant_name.equals(restaurantname)) {
                img = cursor.getBlob(foodimage_index);
                bitmaps.add(BitmapFactory.decodeByteArray(img, 0, img.length));
            }
        }while(cursor.moveToNext());
        return bitmaps;
    }

    public List<Bitmap> getImageForVolunteer(String vol_pincode){
        List<Bitmap> bitmaps = new ArrayList<>();
        String don_pincode = "Not found";
        byte[] img = new byte[0];
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        int pincode_index = cursor.getColumnIndex(COLUMN_PINCODE);
        int foodimage_index = cursor.getColumnIndex(COLUMN_FOOD_IMAGE);
        do{
            don_pincode = cursor.getString(pincode_index);
            if(don_pincode.equals(vol_pincode)) {
                img = cursor.getBlob(foodimage_index);
                bitmaps.add(BitmapFactory.decodeByteArray(img, 0, img.length));
            }
        }while(cursor.moveToNext());
        return bitmaps;
    }

    public boolean addDOonarFoodInFoodTable(byte[] img,String restaurant_name,String food_title,String food_description,String feed_count,
                                            String email,String state, String pincode,String city){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_RESTAURANT_NAME, restaurant_name);
        values.put(COLUMN_FOOD_IMAGE,img);
        values.put(COLUMN_FOOD_TITLE, food_title);
        values.put(COLUMN_FOOD_DESCRIPTION, food_description);
        values.put(COLUMN_FEED_COUNT, feed_count);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_STATE, state);
        values.put(COLUMN_PINCODE, pincode);
        values.put(COLUMN_CITY, city);

        long result = MyDB.insert(FOOD_TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
