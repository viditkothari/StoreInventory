package in.co.viditkothari.storeinventory;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int INTENT_RESULT = 1;
    private static final int INVENTORY_LOADER = 27;
    private Uri CurrentProductUri;

    ImageView imgV_product_image;
    TextView tv_product_name;
    TextView tv_product_quantity;
    TextView tv_product_price;
    TextView tv_product_description;

    TextView btn_product_sale;
    TextView btn_product_shipment;
    TextView btn_product_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgV_product_image = (ImageView) findViewById(R.id.imgvout_product_image);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_quantity = (TextView) findViewById(R.id.tv_product_quantity);
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        tv_product_description = (TextView) findViewById(R.id.tv_product_description);

        final EditText et_sale_amount = (EditText) findViewById(R.id.et_sale_amount);
        final EditText et_shipment_amount = (EditText) findViewById(R.id.et_shipment_amount);

        btn_product_sale = (TextView) findViewById(R.id.btn_product_sale);
        btn_product_shipment = (TextView) findViewById(R.id.btn_product_shipment);
        btn_product_delete = (TextView) findViewById(R.id.btn_product_delete);

        Intent intent = getIntent();
        CurrentProductUri = intent.getData();
        Log.v("CurrentProductUri", "" + CurrentProductUri);

        btn_product_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                int rowsAffected;

                if (btn_product_shipment.getVisibility() == View.GONE && et_shipment_amount.getVisibility() == View.GONE) {
                    int originalValue = Integer.parseInt(tv_product_quantity.getText().toString().trim());
                    int newValue = originalValue;
                    if (!TextUtils.isEmpty(et_sale_amount.getText().toString().trim()))
                        newValue = Integer.parseInt(tv_product_quantity.getText().toString().trim()) - Integer.parseInt(et_sale_amount.getText().toString().trim());

                    if (newValue != originalValue) {
                        if (newValue >= 0)
                            values.put(InventoryTable.COL_PRODUCT_QUANTITY, newValue);
                        else
                            values.put(InventoryTable.COL_PRODUCT_QUANTITY, 0);

                        rowsAffected = getContentResolver().update(CurrentProductUri, values, null, null);
                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            Toast.makeText(getBaseContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Update success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    et_sale_amount.setText("");
                    et_sale_amount.setVisibility(View.GONE);
                    btn_product_shipment.setVisibility(View.VISIBLE);
                    et_shipment_amount.setVisibility(View.GONE);
                } else {
                    et_sale_amount.setVisibility(View.VISIBLE);
                    btn_product_shipment.setVisibility(View.GONE);
                    et_shipment_amount.setVisibility(View.GONE);
                }
            }
        });

        btn_product_shipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                int rowsAffected;

                if (btn_product_sale.getVisibility() == View.GONE && et_sale_amount.getVisibility() == View.GONE) {
                    int originalValue = Integer.parseInt(tv_product_quantity.getText().toString().trim());
                    int newValue = originalValue;
                    if (!TextUtils.isEmpty(et_shipment_amount.getText().toString().trim()))
                        newValue = Integer.parseInt(tv_product_quantity.getText().toString().trim()) + Integer.parseInt(et_shipment_amount.getText().toString().trim());

                    if (newValue != originalValue) {
                        if (newValue >= 0)
                            values.put(InventoryTable.COL_PRODUCT_QUANTITY, newValue);
                        else
                            values.put(InventoryTable.COL_PRODUCT_QUANTITY, 0);

                        rowsAffected = getContentResolver().update(CurrentProductUri, values, null, null);
                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            Toast.makeText(getBaseContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Update success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    et_shipment_amount.setText("");
                    et_shipment_amount.setVisibility(View.GONE);
                    btn_product_sale.setVisibility(View.VISIBLE);
                    et_sale_amount.setVisibility(View.GONE);
                } else {
                    et_shipment_amount.setVisibility(View.VISIBLE);
                    btn_product_sale.setVisibility(View.GONE);
                    et_sale_amount.setVisibility(View.GONE);
                }
            }
        });

        btn_product_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
        getSupportLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        // Only perform the delete if this is an existing product.
        if (CurrentProductUri != null) {
            // Call the ContentResolver to delete the product at the given content URI.
            int rowsDeleted = getContentResolver().delete(CurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Deletion failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Record deleted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if (item.getItemId() == R.id.orderSupplier) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Product order request.");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Greetings,\n I would like to order " + tv_product_quantity.getText().toString() + " no. of " + tv_product_name.getText().toString());
            try {
                startActivityForResult(Intent.createChooser(emailIntent, "Send mail!"), INTENT_RESULT);
                Log.i("Sending Email:", "Some action was taken to send email!");
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(DetailActivity.this, "No email client installed!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryTable._ID,
                InventoryTable.COL_PRODUCT_NAME,
                InventoryTable.COL_PRODUCT_IMAGE_URI,
                InventoryTable.COL_PRODUCT_DESC,
                InventoryTable.COL_PRODUCT_QUANTITY,
                InventoryTable.COL_PRODUCT_PRICE};

        return new CursorLoader(this,   // Parent activity context
                CurrentProductUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1)
            return;
        else {
            DatabaseUtils.dumpCursor(cursor);
            Log.v("Logging cursor position", cursor.getCount() + "");
        }

        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int nameColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_NAME);
            int imgURIColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_IMAGE_URI);
            int descColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_DESC);
            int qtyColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_QUANTITY);
            int priceColIndex = cursor.getColumnIndex(InventoryTable.COL_PRODUCT_PRICE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColIndex);
            String imguri = cursor.getString(imgURIColIndex);
            String desc = cursor.getString(descColIndex);
            int qty = cursor.getInt(qtyColIndex);
            double price = cursor.getDouble(priceColIndex);

            // Update the views on the screen with the values from the database

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imguri));
                imgV_product_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tv_product_name.setText(name);
            tv_product_description.setText(desc);
            tv_product_quantity.setText(String.valueOf(qty));
            tv_product_price.setText(String.valueOf(price));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}