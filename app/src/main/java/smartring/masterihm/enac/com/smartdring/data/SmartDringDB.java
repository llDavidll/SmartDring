package smartring.masterihm.enac.com.smartdring.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import smartring.masterihm.enac.com.smartdring.R;

public class SmartDringDB {

    public final static int APP_DB = 0;
    public final static int SERVICE_DB = 1;
    // Data base definition
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "smartdring_database";
    private final static String DB_FILE_NAME = DB_NAME + ".db";

    private final static String DB_TABLE_PROFILES = "Profiles";
    String[] PROFILES_COLUMNS = {"profileId", "isDefault", "profileName", "profileColor", "profilePhoneLvl",
            "profileNotifLvl", "profileMediaLvl", "profileCallLvl", "profileAlarmLvl"};

    private final static String DB_TABLE_PLACES = "Places";
    String[] PLACES_COLUMNS = {"placeId", "isDefault", "placeName", "placeLatitude", "placeLongitude", "profileId"};

    // Singleton
    private static SmartDringDB singleton_app;
    private static SmartDringDB singleton_service;
    private final Context mContext;
    // Connection to data base
    private final DataBaseOpenHelper mOpenHelper;
    private final SQLiteDatabase mDatabase;

    private SmartDringDB(Context pContext, int db_type) {

        mContext = pContext.getApplicationContext();
        mOpenHelper = new DataBaseOpenHelper(mContext);
        switch (db_type) {
            case APP_DB:
                mDatabase = mOpenHelper.getWritableDatabase();
                break;

            case SERVICE_DB:
                mDatabase = mOpenHelper.getReadableDatabase();
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public static void initializeDB(Context pContext, int db_type) {
        switch (db_type) {
            case APP_DB:
                if (singleton_app == null) {
                    singleton_app = new SmartDringDB(pContext, db_type);
                }
                break;

            case SERVICE_DB:
                if (singleton_service == null) {
                    singleton_service = new SmartDringDB(pContext, db_type);
                }
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public static void closeDB(int db_type) {
        switch (db_type) {
            case APP_DB:
                if (singleton_app != null) {
                    singleton_app.mOpenHelper.close();
                    singleton_app = null;
                }
                break;

            case SERVICE_DB:
                if (singleton_service != null) {
                    singleton_service.mOpenHelper.close();
                    singleton_service = null;
                }
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public static SmartDringDB getDatabase(int db_type) {
        switch (db_type) {
            case APP_DB:
                return singleton_app;

            case SERVICE_DB:
                return singleton_service;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieve all the profiles from the database.
     *
     * @return a list of all the profiles.
     */
    public List<Profile> getProfiles() {

        // Get the profile names from the data base
        Cursor cursor = mDatabase.query(DB_TABLE_PROFILES, PROFILES_COLUMNS, null, null,
                null, null, null);

        // Store into a list
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        if (cursor.moveToFirst()) {

            Profile profile;
            do {

                profile = new Profile();
                profile.setId(cursor.getInt(0));
                profile.setDefault(cursor.getInt(1) > 0);
                profile.setName(cursor.getString(2));
                profile.setColor(Color.parseColor(cursor.getString(3)));
                profile.setmPhoneLvl(cursor.getInt(4));
                profile.setmNotifLvl(cursor.getInt(5));
                profile.setmMediaLvl(cursor.getInt(6));
                profile.setmCallLvl(cursor.getInt(7));
                profile.setmAlarmLvl(cursor.getInt(8));
                profiles.add(profile);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return profiles;
    }

    /**
     * Save a profile i the database.
     *
     * @param profile the profile to save.
     */
    public void saveProfile(Profile profile) {

        boolean saved = false;

        // Create the user data
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDefault", profile.isDefault());
        contentValues.put("profileName", profile.getName());
        contentValues.put("profileColor", profile.getColor());
        contentValues.put("profilePhoneLvl", profile.getmPhoneLvl());
        contentValues.put("profileNotifLvl", profile.getmNotifLvl());
        contentValues.put("profileMediaLvl", profile.getmMediaLvl());
        contentValues.put("profileCallLvl", profile.getmCallLvl());
        contentValues.put("profileAlarmLvl", profile.getmAlarmLvl());

        if (profile.getId() > -1) {
            try {
                saved = mDatabase.update(DB_TABLE_PROFILES, contentValues,
                        "profileId = ?",
                        new String[]{Integer.toString(profile.getId())}) > 0;
            } catch (SQLiteConstraintException ex) {
                return;
            }
        }

        if (!saved) {
            try {
                // Try to insert a new user in the database
                mDatabase.insert(DB_TABLE_PROFILES, null, contentValues);
            } catch (SQLiteConstraintException ignored) {
            }
        }
    }

    /**
     * Retrieve all the places from the database.
     *
     * @return a list of all the places.
     */
    public List<Place> getPlaces() {

        // Get the place names from the data base
        Cursor cursor = mDatabase.query(DB_TABLE_PLACES, PLACES_COLUMNS, null, null,
                null, null, null);

        // Store into a list
        ArrayList<Place> places = new ArrayList<Place>();

        if (cursor.moveToFirst()) {

            Place place;
            do {

                place = new Place();
                place.setId(cursor.getInt(0));
                place.setDefault(cursor.getInt(1) > 0);
                place.setName(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAssociatedProfile(cursor.getInt(5));

                places.add(place);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return places;
    }

    @Override
    protected void finalize() throws Throwable {
        mOpenHelper.close();
        super.finalize();
    }

    /**
     * This class creates the data base from the sql file.
     */
    private class DataBaseOpenHelper extends SQLiteOpenHelper {

        public DataBaseOpenHelper(Context context) {
            super(context, DB_FILE_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // Delete any existing database
            mContext.deleteDatabase(DB_NAME);

            Resources r = mContext.getResources();

            // Open the database file
            InputStream is = r
                    .openRawResource(R.raw.smartring_database);

            // Create the database
            try {

                String sql = convertStreamtoString(is);
                String[] sqlInstruction = sql.split(";");

                for (int i = 0; i < sqlInstruction.length - 1; i++)
                    db.execSQL(sqlInstruction[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            this.onCreate(db);
        }

        // Conversion from a stream to a string
        private String convertStreamtoString(InputStream pIS)
                throws IOException {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = pIS.read();
            while (i != -1) {

                baos.write(i);
                i = pIS.read();
            }

            return baos.toString();
        }
    }
}
