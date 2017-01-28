package in.co.viditkothari.storeinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

import static in.co.viditkothari.storeinventory.data.InventoryDBHelper.LOG_TAG;

/**
 * Created by viditkothari on 27-Jan-17.
 */

public class InventoryProvider extends ContentProvider {
    private InventoryDBHelper mDBHelper;

    private static final int INVENTORY = 100;
    private static final int INVENTORY_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", INVENTORY_ID);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(InventoryTable.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = InventoryTable._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(InventoryTable.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        try{
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e){
            e.printStackTrace();
        }


        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        // Validation Checks
        StringBuilder name = new StringBuilder(values.getAsString(InventoryTable.COL_PRODUCT_NAME));
        if (TextUtils.isEmpty(name.toString())) {
            throw new IllegalArgumentException("Inventory Product requires a name!");
        }

        name.replace(0,name.length(),values.getAsString(InventoryTable.COL_PRODUCT_DESC));
        if (TextUtils.isEmpty(name.toString())) {
            throw new IllegalArgumentException("Inventory Product requires a description!");
        }

        Integer qty = values.getAsInteger(InventoryTable.COL_PRODUCT_QUANTITY);
        if (qty == null || qty < 0) {
            throw new IllegalArgumentException("Inventory Product requires valid value for quantity!");
        }

        Double price = values.getAsDouble(InventoryTable.COL_PRODUCT_PRICE);
        if (price == null || price < 0.0) {
            throw new IllegalArgumentException("Inventory Product requires valid value for price!");
        }

        // Get writeable database
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(InventoryTable.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateInventory(uri, values, selection, selectionArgs);
            case INVENTORY_ID:
                selection = InventoryTable._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateInventory(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(InventoryTable.COL_PRODUCT_NAME)) {
            String product_name = values.getAsString(InventoryTable.COL_PRODUCT_NAME);
            if (product_name == null) {
                throw new IllegalArgumentException("Inventory requires a name");
            }
        }

        if (values.containsKey(InventoryTable.COL_PRODUCT_DESC)) {
            String product_desc = values.getAsString(InventoryTable.COL_PRODUCT_DESC);
            if (product_desc == null) {
                throw new IllegalArgumentException("Inventory requires a desc");
            }
        }

        if (values.containsKey(InventoryTable.COL_PRODUCT_QUANTITY)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer product_qty = values.getAsInteger(InventoryTable.COL_PRODUCT_QUANTITY);
            if (product_qty != null && product_qty < 0) {
                throw new IllegalArgumentException("Inventory requires a valid quantity");
            }
        }

        if (values.containsKey(InventoryTable.COL_PRODUCT_PRICE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer product_qty = values.getAsInteger(InventoryTable.COL_PRODUCT_PRICE);
            if (product_qty != null && product_qty < 0) {
                throw new IllegalArgumentException("Inventory requires a valid price");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(InventoryTable.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(InventoryTable.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                // Delete a single row given by the ID in the URI
                selection = InventoryTable._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(InventoryTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return InventoryTable.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
