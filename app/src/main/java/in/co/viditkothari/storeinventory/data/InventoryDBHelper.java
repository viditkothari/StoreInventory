package in.co.viditkothari.storeinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

/**
 * Created by viditkothari on 27-Jan-17.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDBHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "store_inventory.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link InventoryDBHelper}.
     * @param context of the app
     */
    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the 1st time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TABLE =  "CREATE TABLE " + InventoryTable.TABLE_NAME + " ("
                + InventoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryTable.COL_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryTable.COL_PRODUCT_IMAGE_URI + " TEXT, "
                + InventoryTable.COL_PRODUCT_DESC + " TEXT NOT NULL, "
                + InventoryTable.COL_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + InventoryTable.COL_PRODUCT_PRICE + " REAL NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}