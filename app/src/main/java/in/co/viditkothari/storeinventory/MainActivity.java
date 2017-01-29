package in.co.viditkothari.storeinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 26;
    InventoryCursorAdapter mCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCursorAdapter = new InventoryCursorAdapter(this, null);

        listView = (ListView)findViewById(R.id.products_listView);
        listView.setAdapter(mCursorAdapter);
        listView.setEmptyView(findViewById(R.id.emptyView));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                final Uri currentUri = ContentUris.withAppendedId(InventoryTable.CONTENT_URI, id);

                // Implementing functionality for "sell button" on a list Item.
                listView.findViewById(R.id.btn_product_sell).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] projection = {
                                InventoryTable._ID,
                                InventoryTable.COL_PRODUCT_QUANTITY};
                        Cursor c=getContentResolver().query(currentUri,projection,null,null,null);
                        int product_quantity_ColIndex = c.getColumnIndex(InventoryTable.COL_PRODUCT_QUANTITY);
                        int product_quantity = c.getInt(product_quantity_ColIndex);
                        ContentValues values = new ContentValues();
                        if(product_quantity>0) {
                            values.put(InventoryTable.COL_PRODUCT_QUANTITY, product_quantity - 1);
                            int a = getContentResolver().update(currentUri,values,null,null);
                        }
                        // ((TextView)listView.findViewById(R.id.product_quantity)).setText(String.valueOf(product_quantity));
                        //
                       /* int originalValue = Integer.parseInt(tv_product_quantity.getText().toString().trim());
                                values.put(InventoryTable.COL_PRODUCT_QUANTITY, newValue);

                            rowsAffected = getContentResolver().update(currentUri, values, null, null);*/
                    }
                });

                // Set the URI on the data field of the intent
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        // Kick off the loader
        getSupportLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryTable._ID,
                InventoryTable.COL_PRODUCT_NAME,
                InventoryTable.COL_PRODUCT_IMAGE_URI,
                InventoryTable.COL_PRODUCT_DESC,
                InventoryTable.COL_PRODUCT_QUANTITY,
                InventoryTable.COL_PRODUCT_PRICE};

        return new CursorLoader(this,   // Parent activity context
                InventoryTable.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        DatabaseUtils.dumpCursor(data);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}