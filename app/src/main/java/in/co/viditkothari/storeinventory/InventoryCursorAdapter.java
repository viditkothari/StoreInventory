package in.co.viditkothari.storeinventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

/**
 * Created by viditkothari on 28-Jan-17.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    Bitmap bitmap;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ImageView imgv_product_image = (ImageView) view.findViewById(R.id.product_image);
        TextView tv_product_name = (TextView) view.findViewById(R.id.product_name);
        TextView tv_product_quantity = (TextView) view.findViewById(R.id.product_quantity);
        TextView tv_product_price = (TextView) view.findViewById(R.id.product_price);
        TextView tv_product_description = (TextView) view.findViewById(R.id.product_description);
        TextView tv_product_sell = (TextView) view.findViewById(R.id.btn_product_sell);

        int product_name_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_NAME);
        int product_image_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_IMAGE_URI);
        int product_desc_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_DESC);
        int product_quantity_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_QUANTITY);
        int product_price_ColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_PRICE);

        final int _id = cursor.getInt(cursor.getColumnIndex(InventoryTable._ID));
        tv_product_quantity.setTag(_id);

        String product_name = cursor.getString(product_name_ColIndex);
        String product_image = cursor.getString(product_image_ColIndex);
        String product_desc = cursor.getString(product_desc_ColIndex);
        final int product_quantity = cursor.getInt(product_quantity_ColIndex);
        Double product_price = cursor.getDouble(product_price_ColIndex);

        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(product_image));
            imgv_product_image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv_product_name.setText(product_name);
        tv_product_description.setText(product_desc);
        tv_product_quantity.setText(String.valueOf(product_quantity));
        tv_product_price.setText(String.valueOf(product_price));

        // Implementing functionality for "sell button" on a list Item.
        tv_product_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_quantity > 0) {
                    Uri mCurrentCaseUri = ContentUris.withAppendedId(InventoryTable.CONTENT_URI, _id);
                    ContentValues values = new ContentValues();
                    values.put(InventoryTable.COL_PRODUCT_QUANTITY, (product_quantity - 1));
                    context.getContentResolver().update(mCurrentCaseUri, values, null, null);
                } else
                    Toast.makeText(context, context.getString(R.string.out_of_stock), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
