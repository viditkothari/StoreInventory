package in.co.viditkothari.storeinventory;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if (item.getItemId() == R.id.orderSupplier) {
            Intent emailSupplierIntent = new Intent(Intent.ACTION_SEND);
            emailSupplierIntent.setData(Uri.parse("mailto:viditkothari@live.com"));
            emailSupplierIntent.setType("text/plain");
            emailSupplierIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Recipient"});
            emailSupplierIntent.putExtra(Intent.EXTRA_SUBJECT, "Product order request.");
            emailSupplierIntent.putExtra(Intent.EXTRA_TEXT   , "Greetings,\n I would like to order some more of the item.");
            try {
                startActivity(Intent.createChooser(emailSupplierIntent, "Send mail..."));
                Log.i("Sending Email:", "Some action was taken to send email!");
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(DetailActivity.this, "No email client installed!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

/*

android:id="@+id/activity_detail"
android:id="@+id/imgvout_product_image"
android:id="@+id/tv_product_name"
android:id="@+id/tv_product_quantity"
android:id="@+id/tv_product_price"
android:id="@+id/tv_product_description"
android:id="@+id/btn_product_sale"
android:id="@+id/et_sale_amount"
android:id="@+id/btn_product_shipment"
android:id="@+id/et_shipment_amount"
android:id="@+id/btn_product_delete"

*/
