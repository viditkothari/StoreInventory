package in.co.viditkothari.storeinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by viditkothari on 27-Jan-17.
 */

public class InventoryContract {
    public static final String CONTENT_AUTHORITY = "in.co.viditkothari.storeinventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";


    public static final class InventoryTable implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;
        public final static String COL_PRODUCT_NAME = "productname";
        public final static String COL_PRODUCT_IMAGE_URI = "imageuri";
        public final static String COL_PRODUCT_DESC = "description";
        public final static String COL_PRODUCT_QUANTITY = "quantity";
        public final static String COL_PRODUCT_PRICE = "price";

    }
}
