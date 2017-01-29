package in.co.viditkothari.storeinventory;

import android.content.ContentResolver;
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
    public void bindView(View view, Context context, Cursor cursor) {
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

        Log.v("VVVV Cursor Logging:",cursor.toString());
        String product_name = cursor.getString(product_name_ColIndex);
        String product_image = cursor.getString(product_image_ColIndex);
        String product_desc = cursor.getString(product_desc_ColIndex);
        int product_quantity = cursor.getInt(product_quantity_ColIndex);
        Double product_price = cursor.getDouble(product_price_ColIndex);
        Log.i("product_name: ",cursor.getColumnName(0) + " : " + cursor.getColumnCount());
        Log.i("product_image: ",cursor.getCount() + " : " + product_image);
        Log.i("product_description: ",product_desc);
        Log.i("product_quantity: ",String.valueOf(product_quantity));
        Log.i("product_price: ",String.valueOf(product_price));


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
    }
}
