package in.co.viditkothari.storeinventory;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

public class AddProductActivity extends AppCompatActivity {

    protected static final int IMAGE_PICK = 1;
    final ImageView imgView = (ImageView)findViewById(R.id.imgvin_product_image);
    final EditText etProductName = (EditText)findViewById(R.id.et_product_name);
    final EditText etProductQty = (EditText)findViewById(R.id.et_product_quantity);
    final EditText etProductPrice = (EditText)findViewById(R.id.et_product_price);
    final EditText etProductDesc = (EditText)findViewById(R.id.et_product_description);
    final TextView btnReset = (TextView)findViewById(R.id.btn_product_reset);
    final TextView btnAdd = (TextView)findViewById(R.id.btn_product_add);

    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(intent, "Select an image"), IMAGE_PICK);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.setImageDrawable(getDrawable(R.drawable.thumbnail));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etProductName.getText().toString()) ||
                   TextUtils.isEmpty(etProductQty.getText().toString()) ||
                   TextUtils.isEmpty(etProductPrice.getText().toString()) ||
                   TextUtils.isEmpty(etProductDesc.getText().toString()) || imageUri==null){
                    Toast.makeText(getBaseContext(),"Invalid Entry!",Toast.LENGTH_SHORT).show();
                }
                else{
                    insertProduct();
                    /*Intent intent=new Intent(AddProductActivity.this,DetailActivity.class);
                    startActivity(intent);*/
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK)
            if (RESULT_OK == resultCode) {
                imageUri = data.getData();
                Log.i("OnActivityResult","Image URI found: " + imageUri.toString());
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imgView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
    }

    private void insertProduct() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryTable.COL_PRODUCT_NAME, "Toto");
        values.put(InventoryTable.COL_PRODUCT_IMAGE_URI, "Terrier");
        values.put(InventoryTable.COL_PRODUCT_DESC, "Terrier");
        values.put(InventoryTable.COL_PRODUCT_QUANTITY, "Terrier");
        values.put(InventoryTable.COL_PRODUCT_PRICE, 7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(InventoryTable.CONTENT_URI, values);
        Log.i("New Row Added: ", "Row id: = " + newUri.toString());
    }

    /*
         COL_PRODUCT_NAME
         COL_PRODUCT_IMAGE_URI
         COL_PRODUCT_DESC
         COL_PRODUCT_QUANTITY
         COL_PRODUCT_PRICE

    imgvin_product_image
    et_product_name
    et_product_quantity
    et_product_price
    et_product_description
    btn_product_reset
    btn_product_add

    */
}