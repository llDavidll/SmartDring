package smartring.masterihm.enac.com.smartdring.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
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
    private final static String DB_TABLE_PLACES = "Places";
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
        String[] columns = {"profileId", "profileName", "profileColor"};

        // Get the profile names from the data base
        Cursor cursor = mDatabase.query(DB_TABLE_PROFILES, columns, null, null,
                null, null, null);

        // Store into a list
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        if (cursor.moveToFirst()) {

            Profile profile;
            do {

                profile = new Profile();
                profile.setId(cursor.getInt(0));
                profile.setName(cursor.getString(1));
                profile.setColor(Color.parseColor(cursor.getString(2)));

                profiles.add(profile);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return profiles;
    }

    /**
     * Retrieve all the places from the database.
     *
     * @return a list of all the places.
     */
    public List<Place> getPlaces() {
        String[] columns = {"placeId", "placeName", "profileId"};

        // Get the place names from the data base
        Cursor cursor = mDatabase.query(DB_TABLE_PLACES, columns, null, null,
                null, null, null);

        // Store into a list
        ArrayList<Place> places = new ArrayList<Place>();

        if (cursor.moveToFirst()) {

            Place place;
            do {

                place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setAssociatedProfile(cursor.getInt(2));

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
