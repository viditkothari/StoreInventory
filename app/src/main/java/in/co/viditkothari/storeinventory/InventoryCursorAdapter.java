package in.co.viditkothari.storeinventory;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

/**
 * Created by viditkothari on 28-Jan-17.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /*
    product_name
    product_quantity
    product_price
    product_description
    product_sell

     _ID
     COL_PRODUCT_NAME
     COL_PRODUCT_IMAGE_URI
     COL_PRODUCT_DESC
     COL_PRODUCT_QUANTITY
     COL_PRODUCT_PRICE

     String SQL_CREATE_TABLE =  "CREATE TABLE " + InventoryTable.TABLE_NAME + " ("
                + InventoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryTable.COL_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryTable.COL_PRODUCT_IMAGE_URI + " TEXT, "
                + InventoryTable.COL_PRODUCT_DESC + " TEXT NOT NULL, "
                + InventoryTable.COL_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + InventoryTable.COL_PRODUCT_PRICE + " REAL NOT NULL DEFAULT 0);";

    */

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        ImageView imgv_product_image = (ImageView) view.findViewById(R.id.product_image);
        TextView tv_product_name = (TextView) view.findViewById(R.id.product_name);
        TextView tv_product_quantity = (TextView) view.findViewById(R.id.product_quantity);
        TextView tv_product_price = (TextView) view.findViewById(R.id.product_price);
        TextView tv_product_description = (TextView) view.findViewById(R.id.product_description);
        TextView tv_product_sell = (TextView) view.findViewById(R.id.btn_product_sell);

        // Find the columns of pet attributes that we're interested in
        int product_name_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_NAME);
        int product_image_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_IMAGE_URI);
        int product_description_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_DESC);
        int product_quantity_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_QUANTITY);
        int product_price_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_PRICE);

        // Read the pet attributes from the Cursor for the current pet
        String product_name = cursor.getString(product_name_ColIndex);
        String product_image = cursor.getString(product_image_ColIndex);
        String product_description = cursor.getString(product_description_ColIndex);
        int product_quantity = cursor.getInt(product_quantity_ColIndex);
        Double product_price = cursor.getDouble(product_price_ColIndex);


        // Update the TextViews with the attributes for the current pet
        // imgv_product_image.setImageBitmap();
        tv_product_name.setText(product_name);
        tv_product_quantity.setText(String.valueOf(product_quantity));
        tv_product_price.setText(String.valueOf(product_price));
        tv_product_description.setText(product_description);

        tv_product_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Onclickevent"," Yay! hurray!!!!!");
            }
        });
    }
}
